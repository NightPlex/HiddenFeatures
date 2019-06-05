package scripts.HardcoreLeveler.tasks;

import static scripts.HardcoreLeveler.data.Constants.*;

import org.tribot.api2007.Inventory;

import scripts.HardcoreLeveler.data.Vars;
import scripts.HardcoreLeveler.framework.AbstractTask;
import scripts.HardcoreLeveler.util.ScriptUtil;
/**
 * 
 * @author Einstein
 *
 *
 */
public class Mining implements AbstractTask {

	private int[] rocks;

	@Override
	public String info() {
		return "Mining";
	}

	@Override
	public boolean shouldExecute() {
		return !Vars.get().doneMining;
	}

	@Override
	public void execute() {
		// Equip pickaxe if required
		ScriptUtil.equipTool(PICKAXES);

		// Decide what rocks to mine
		if (Inventory.getCount(TIN_ORE) < 14)
			rocks = TIN_ROCK;
		else
			rocks = COPPER_ROCK;

		// Mining
		if (ScriptUtil.fetchTool(PICKAXES))
			if (ScriptUtil.handleFullInventory(PICKAXES))
				if (ScriptUtil.getInArea(MINING_AREA))
					if (ScriptUtil.interactObject("Mine", rocks))
						ScriptUtil.waitToAnimate();
	}

}