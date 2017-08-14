package pyb.portfolio.feedscreener.miso;

import java.math.BigDecimal;
import java.sql.Timestamp;

import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MisoMarketPrice {
	@NotNull
	private Timestamp originaldatetime;
	@NotNull
	private String hubname;
	@NotNull
	private BigDecimal lmp;
	@NotNull
	private BigDecimal loss;
	@NotNull
	private BigDecimal congestion;
}
