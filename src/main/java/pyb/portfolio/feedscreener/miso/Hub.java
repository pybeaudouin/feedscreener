package pyb.portfolio.feedscreener.miso;

import java.math.BigDecimal;

import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Hub {
	@NotNull
	private String name;
	@NotNull
	private BigDecimal lmp;
	@NotNull
	private BigDecimal loss;
	@NotNull
	private BigDecimal congestion;
}
