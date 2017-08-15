package pyb.portfolio.feedscreener.miso;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import lombok.extern.slf4j.Slf4j;

@Configuration
@EnableBatchProcessing
@EnableScheduling
@Slf4j
public class BatchConfiguration {

	@Autowired
	private DataSource dataSource;

	@Autowired
	private JobBuilderFactory jobBuilderFactory;

	@Autowired
	private StepBuilderFactory stepBuilderFactory;

	@Autowired
	private JobLauncher jobLauncher;

	@Autowired
	private JobCompletionNotificationListener listener;

	// tag::readerwriterprocessor[]
	@Bean
	public LMPDataItemProcessor processor() {
		return new LMPDataItemProcessor();
	}

	@StepScope
	public class ItemListWriter<T> implements ItemWriter<List<T>> {
		private ItemWriter<T> wrapped;

		public ItemListWriter(ItemWriter<T> wrapped) {
			this.wrapped = wrapped;
		}

		@Override
		public void write(List<? extends List<T>> items) throws Exception {
			for (final List<T> subList : items) {
				log.debug("Writing item {}", subList);
				wrapped.write(subList);
			}
		}
	}

	@Bean
	public JdbcBatchItemWriter<MisoMarketPrice> itemWriter() {
		final JdbcBatchItemWriter<MisoMarketPrice> itemWriter = new JdbcBatchItemWriter<>();
		itemWriter.setItemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<MisoMarketPrice>());

		// MERGE = UPSERT. http://h2database.com/html/grammar.html#merge
		itemWriter.setSql("MERGE INTO miso_market_price_five_minutes(originaldatetime, hubname, lmp, loss, congestion)"
				+ " VALUES(:originaldatetime, :hubname, :lmp, :loss, :congestion)");
		itemWriter.setDataSource(dataSource);
		return itemWriter;
	}

	@Bean
	public ItemWriter<List<MisoMarketPrice>> writer() {
		return new ItemListWriter<>(itemWriter());
	}
	// end::readerwriterprocessor[]

	// FIXME: test Daylight Saving Time sensitivity
	@Scheduled(cron = "* 0/5 * * * *")
	public void runJob() throws Exception {
		//@formatter:off
		final Job importLMPDataJob = jobBuilderFactory.get("importLMPDataJob")
				.incrementer(new RunIdIncrementer())
				.listener(listener)
				.flow(step1())
				.end()
				.build();
		//@formatter:on

		final Map<String, JobParameter> jobParams = new HashMap<>(1);
		jobParams.put("startDate", new JobParameter(new Date(), true));
		jobLauncher.run(importLMPDataJob, new JobParameters(jobParams));
	}

	// tag::jobstep[]
	// don't use @Bean as this keeps the same step with the same reader
	public Step step1() {
		log.info("Building step1...");
		//@formatter:off
		return stepBuilderFactory.get("step1")
				.<LMPData, List<MisoMarketPrice>>chunk(10)
				// the reader blows itself after one usage so we build a new one at every step
				.reader(new LMPDataItemReader())
				.processor(processor())
				.writer(writer())
				.allowStartIfComplete(true)
				.build();
		//@formatter:on
	}
	// end::jobstep[]
}