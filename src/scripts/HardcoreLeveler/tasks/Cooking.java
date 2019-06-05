package scripts.HardcoreLeveler.tasks;

import static scripts.HardcoreLeveler.data.Constants.*;

import scripts.HardcoreLeveler.data.Vars;
import scripts.HardcoreLeveler.framework.AbstractTask;
import scripts.HardcoreLeveler.util.ScriptUtil;
/**
 * 
 * @author Einstein
 *
 *
 */
public class Cooking implements AbstractTask {

	@Override
	public String info() {
		return "Cooking";
	}

	@Override
	public boolean shouldExecute() {
		return !Vars.get().doneCooking && Vars.get().doneFishing;
	}

	@Override
	public void execute() {
		// Resource availability check
		if (!ScriptUtil.isResourceStillAvailable(RAW_SHRIMPS) && !ScriptUtil.isResourceStillAvailable(RAW_ANCHOVIES))
			Vars.get().doneCooking = true;

		// Gets shrimps; if fails, gets anchovies.
		// (I took advantage of the way logical OR works: if the first expression evaluates true, the second one is not even checked)
		if (ScriptUtil.fetchResources(-1, RAW_SHRIMPS) || ScriptUtil.fetchResources(-1, RAW_ANCHOVIES))
			if (ScriptUtil.getInArea(COOKING_AREA))
				if (ScriptUtil.selectInventoryItem(RAW_FOOD))
					if (ScriptUtil.interactObject("Use", RANGE)) {
						ScriptUtil.waitForInterface(270);
						if (ScriptUtil.handleInterface(270, 14, "Cook"))
							ScriptUtil.waitUntilAmountOfResourcesLeft(0, RAW_FOOD);
					}
	}

}