package pyb.portfolio.feedscreener.miso;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;

import java.util.ArrayList;

import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

@Component
public class LMPDataItemReader extends FlatFileItemReader<LMPData> {
	private boolean first = true;

	@Override
	public LMPData doRead() {
		// kill switch to only produce one LMPData instance
		if (first) {
			first = false;
		} else {
			return null;
		}
		final LMPData lmpd = new LMPData();
		// lmpd.setRefId(Timestamp.valueOf("2017-08-13 20:35:00"));
		lmpd.setRefId(Timestamp.valueOf(LocalDateTime.now()));
		final ExAnteLMP exAnteLMP = new ExAnteLMP();
		exAnteLMP.setHubs(new ArrayList<>());
		exAnteLMP.getHubs()
		.add(new Hub("ARKANSAS.HUB", new BigDecimal("26.13"), new BigDecimal("-0.45"), BigDecimal.ZERO));
		exAnteLMP.getHubs()
		.add(new Hub("ILLINOIS.HUB", new BigDecimal("26.11"), new BigDecimal("-0.47"), BigDecimal.ZERO));
		exAnteLMP.getHubs()
		.add(new Hub("INDIANA.HUB", new BigDecimal("26.93"), new BigDecimal("0.35"), BigDecimal.ZERO));
		lmpd.setExAnteLMP(exAnteLMP);
		return lmpd;
	}

	public LMPDataItemReader() {
		// as we're mocking data reading, the only restriction is that the filename
		// exists.
		setResource(new ClassPathResource("application.properties"));
		setLineMapper(new DefaultLineMapper<LMPData>() {
			{
				setLineTokenizer(new DelimitedLineTokenizer() {
					{
						setNames(new String[] { "originaldatetime", "hubname", "lmp", "loss", "congestion" });
					}
				});
				setFieldSetMapper(new BeanWrapperFieldSetMapper<LMPData>() {
					{
						setTargetType(LMPData.class);
					}
				});
			}
		});
	}
}
