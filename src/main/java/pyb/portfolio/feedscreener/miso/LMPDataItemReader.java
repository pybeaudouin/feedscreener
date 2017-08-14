package pyb.portfolio.feedscreener.miso;

import org.springframework.batch.item.ItemReader;
import org.springframework.http.converter.xml.Jaxb2RootElementHttpMessageConverter;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class LMPDataItemReader implements ItemReader<LMPData> {
	private static final String ENDPOINT = "https://www.misoenergy.org/ria/LMP.aspx?query=udstable&format=xml";
	private boolean first = true;

	@Override
	public LMPData read() {
		// kill switch to only produce one LMPData instance
		if (first) {
			first = false;
		} else {
			return null;
		}

		final RestTemplate restTemplate = new RestTemplate();
		restTemplate.getMessageConverters().add(new Jaxb2RootElementHttpMessageConverter());

		final LMPData lmpd = restTemplate.getForObject(ENDPOINT, LMPData.class);
		return lmpd;
	}
}
