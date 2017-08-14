package pyb.portfolio.feedscreener.miso;

import java.util.List;

import javax.sql.DataSource;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableBatchProcessing
public class BatchConfiguration {

	@Autowired
	public JobBuilderFactory jobBuilderFactory;

	@Autowired
	public StepBuilderFactory stepBuilderFactory;

	@Autowired
	public DataSource dataSource;

	@Autowired
	private LMPDataItemReader lmpDataItemReader;

	// tag::readerwriterprocessor[]
	@Bean
	public FlatFileItemReader<LMPData> reader() {
		return lmpDataItemReader;
	}

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
				wrapped.write(subList);
			}
		}
	}

	@Bean
	public JdbcBatchItemWriter<MisoMarketPrice> itemWriter() {
		final JdbcBatchItemWriter<MisoMarketPrice> itemWriter = new JdbcBatchItemWriter<>();
		itemWriter.setItemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<MisoMarketPrice>());

		itemWriter.setSql("INSERT INTO miso_market_price_five_minutes(originaldatetime, hubname, lmp, loss, congestion)"
				+ " VALUES(:originaldatetime, :hubname, :lmp, :loss, :congestion)");
		itemWriter.setDataSource(dataSource);
		return itemWriter;
	}

	@Bean
	public ItemWriter<List<MisoMarketPrice>> writer() {
		return new ItemListWriter<>(itemWriter());
	}
	// end::readerwriterprocessor[]

	// tag::jobstep[]
	@Bean
	public Job importUserJob(JobCompletionNotificationListener listener) {
		return jobBuilderFactory.get("importUserJob").incrementer(new RunIdIncrementer()).listener(listener)
				.flow(step1()).end().build();
	}

	@Bean
	public Step step1() {
		return stepBuilderFactory.get("step1").<LMPData, List<MisoMarketPrice>>chunk(10).reader(reader())
				.processor(processor()).writer(writer()).build();
	}
	// end::jobstep[]
}