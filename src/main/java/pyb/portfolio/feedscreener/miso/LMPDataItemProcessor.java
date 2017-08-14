package pyb.portfolio.feedscreener.miso;

import java.util.ArrayList;
import java.util.List;

import org.springframework.batch.item.ItemProcessor;

import lombok.extern.slf4j.Slf4j;
import pyb.portfolio.feedscreener.miso.LMPData.ExAnteLMP.Hub;

@Slf4j
public class LMPDataItemProcessor implements ItemProcessor<LMPData, List<MisoMarketPrice>> {

	@Override
	public List<MisoMarketPrice> process(LMPData lmp) throws Exception {
		log.trace("Converting LMPData {} ...", lmp);
		final List<MisoMarketPrice> mmps = new ArrayList<>();

		for (final Hub hub : lmp.exAnteLMP.hub) {
			final MisoMarketPrice mmp = new MisoMarketPrice();
			mmp.setOriginaldatetime(lmp.refId);
			mmp.setHubname(hub.name);
			mmp.setLmp(hub.lmp);
			mmp.setLoss(hub.loss);
			mmp.setCongestion(hub.congestion);

			mmps.add(mmp);
		}

		log.trace("Converted LMPData {} to {} ...", lmp, mmps);
		return mmps;
	}

}
