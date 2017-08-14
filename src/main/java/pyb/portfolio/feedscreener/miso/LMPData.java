package pyb.portfolio.feedscreener.miso;

import java.sql.Timestamp;

import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class LMPData {
	@NotNull
	private Timestamp refId;
	@NotNull
	private ExAnteLMP exAnteLMP;
}
