package pyb.portfolio.jaxb.adapter;

import java.sql.Timestamp;
import java.time.LocalDateTime;

import org.junit.Assert;
import org.junit.Test;


public class TimestampAdapterTest {

	private final TimestampAdapter adapter = new TimestampAdapter();

	@Test
	public void testUnmarshalString() throws Exception {
		final String input = "14-Aug-2017 00:30";
		final LocalDateTime expectedLDT = LocalDateTime.of(2017, 8, 14, 0, 30);
		final Timestamp expected = Timestamp.valueOf(expectedLDT);
		Assert.assertEquals(expected, adapter.unmarshal(input));
	}

	@Test
	public void testMarshalTimestamp() throws Exception {
		final String expected = "14-Aug-2017 00:30";
		final LocalDateTime inputLDT = LocalDateTime.of(2017, 8, 14, 0, 30);
		final Timestamp input = Timestamp.valueOf(inputLDT);
		Assert.assertEquals(expected, adapter.marshal(input));
	}

}
