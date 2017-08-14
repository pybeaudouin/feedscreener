package pyb.portfolio.jaxb.adapter;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import java.util.Locale;

import javax.xml.bind.annotation.adapters.XmlAdapter;

public class TimestampAdapter extends XmlAdapter<String, Timestamp> {


	private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd-MMM-yyyy HH:mm")
			.withLocale(Locale.ENGLISH);
	// private static final DateTimeFormatter FORMATTER =
	// DateTimeFormatter.ofPattern("uuuu MMM dd")
	// .withLocale(Locale.ENGLISH);
	// DateTimeFormatterBuilder BUILDER = new DateTimeFormatterBuilder();
	// private static final DateTimeFormatter FORMATTER =
	// BUILDER.appendValue(TemporalField.)

	@Override
	public Timestamp unmarshal(String v) throws Exception {
		return Timestamp.valueOf(LocalDateTime.parse(v, FORMATTER));
	}

	@Override
	public String marshal(Timestamp v) throws Exception {
		return FORMATTER.format(v.toLocalDateTime());
	}

}
