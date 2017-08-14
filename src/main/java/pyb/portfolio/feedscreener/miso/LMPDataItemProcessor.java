package pyb.portfolio.feedscreener.miso;

import java.util.ArrayList;
import java.util.List;

import org.springframework.batch.item.ItemProcessor;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class LMPDataItemProcessor implements ItemProcessor<LMPData, List<MisoMarketPrice>> {

	@Override
	public List<MisoMarketPrice> process(LMPData lmp) throws Exception {
		log.trace("Converting LMPData {} ...", lmp);
		final List<MisoMarketPrice> mmps = new ArrayList<>();

		for (final Hub hub : lmp.getExAnteLMP().getHubs()) {
			final MisoMarketPrice mmp = new MisoMarketPrice();
			mmp.setOriginaldatetime(lmp.getRefId());
			mmp.setHubname(hub.getName());
			mmp.setLmp(hub.getLmp());
			mmp.setLoss(hub.getLoss());
			mmp.setCongestion(hub.getCongestion());

			mmps.add(mmp);
		}

		log.trace("Converted LMPData {} to {} ...", lmp, mmps);
		return mmps;
	}

}
