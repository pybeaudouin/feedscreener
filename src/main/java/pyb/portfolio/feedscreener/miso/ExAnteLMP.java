package pyb.portfolio.feedscreener.miso;

import java.util.List;

import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class ExAnteLMP {
	@NotNull
	List<Hub> hubs;
}
