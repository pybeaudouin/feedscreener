package pyb.portfolio.feedscreener.miso;

import org.springframework.batch.item.ItemReader;
import org.springframework.http.converter.xml.Jaxb2RootElementHttpMessageConverter;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class LMPDataItemReader implements ItemReader<LMPData> {
	private static final String ENDPOINT = "https://www.misoenergy.org/ria/LMP.aspx?query=udstable&format=xml";

	@Override
	public LMPData read() {
		final RestTemplate restTemplate = new RestTemplate();
		restTemplate.getMessageConverters().add(new Jaxb2RootElementHttpMessageConverter());

		final LMPData lmpd = restTemplate.getForObject(ENDPOINT, LMPData.class);
		return lmpd;
	}
}
