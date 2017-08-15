package pyb.portfolio.feedscreener.miso;

import org.springframework.batch.item.ItemReader;
import org.springframework.http.converter.xml.Jaxb2RootElementHttpMessageConverter;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class LMPDataItemReader implements ItemReader<LMPData> {
	private static final String ENDPOINT = "https://www.misoenergy.org/ria/LMP.aspx?query=udstable&format=xml";
	private boolean first = true;
	private final RestTemplate restTemplate = new RestTemplate();

	public LMPDataItemReader() {
		restTemplate.getMessageConverters().add(new Jaxb2RootElementHttpMessageConverter());
	}

	@Override
	public LMPData read() {
		// kill switch to only produce one LMPData instance per reader
		if (first) {
			log.info("Reader running for the 1st time...");
			first = false;
		} else {
			log.info("Reader already called, signalling end of data.");
			return null;
		}

		final LMPData lmpd = restTemplate.getForObject(ENDPOINT, LMPData.class);
		log.info("Reader returning {}", lmpd);
		return lmpd;
	}
}
