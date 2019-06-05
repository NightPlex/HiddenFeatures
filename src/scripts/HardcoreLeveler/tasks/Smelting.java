package scripts.HardcoreLeveler.tasks;

import static scripts.HardcoreLeveler.data.Constants.COPPER_ORE;
import static scripts.HardcoreLeveler.data.Constants.FURANCE;
import static scripts.HardcoreLeveler.data.Constants.SMELTING_AREA;
import static scripts.HardcoreLeveler.data.Constants.TIN_ORE;

import scripts.HardcoreLeveler.data.Vars;
import scripts.HardcoreLeveler.framework.AbstractTask;
import scripts.HardcoreLeveler.util.ScriptUtil;
/**
 * 
 * @author Einstein
 *
 *
 */
public class Smelting implements AbstractTask {

	@Override
	public String info() {
		return "Smelting";
	}

	@Override
	public boolean shouldExecute() {
		return !Vars.get().doneSmelting && Vars.get().doneMining;
	}

	@Override
	public void execute() {
		// Resource availability check
		if (!ScriptUtil.isResourceStillAvailable(TIN_ORE, COPPER_ORE))
			Vars.get().doneSmelting = true;

		// Smelting
		if (ScriptUtil.fetchResources(-1, TIN_ORE, COPPER_ORE))
			if (ScriptUtil.getInArea(SMELTING_AREA)) {
				if (ScriptUtil.interactObject("Smelt", FURANCE))
					ScriptUtil.waitForInterface(270);
				if (ScriptUtil.handleInterface(270, 14, "Smelt Bronze bar"))
					ScriptUtil.waitUntilAmountOfResourcesLeft(0, TIN_ORE, COPPER_ORE);
			}
	}

}