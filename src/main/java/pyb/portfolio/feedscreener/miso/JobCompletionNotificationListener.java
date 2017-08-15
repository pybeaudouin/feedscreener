package pyb.portfolio.feedscreener.miso;

import java.util.List;

import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.listener.JobExecutionListenerSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class JobCompletionNotificationListener extends JobExecutionListenerSupport {
	private final JdbcTemplate jdbcTemplate;

	@Autowired
	public JobCompletionNotificationListener(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	@Override
	public void afterJob(JobExecution jobExecution) {
		if (jobExecution.getStatus() == BatchStatus.COMPLETED) {
			log.info("LMP Data import completed.");
			if (!log.isDebugEnabled()) {
				return;
			}
			final List<MisoMarketPrice> results = jdbcTemplate.query(
				"SELECT originaldatetime, hubname, lmp, loss, congestion FROM miso_market_price_five_minutes ORDER BY originaldatetime",
				//@formatter:off
				(RowMapper<MisoMarketPrice>) (rs, row) -> new MisoMarketPrice(
															rs.getTimestamp(1),
															rs.getString(2),
															rs.getBigDecimal("lmp"),
															rs.getBigDecimal("loss"),
															rs.getBigDecimal("congestion")
														)
				//@formatter:on
			);

			for (final MisoMarketPrice mmp : results) {
				log.info("Found <" + mmp + "> in the database.");
			}
		}
	}
}