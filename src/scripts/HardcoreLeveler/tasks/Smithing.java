package scripts.HardcoreLeveler.tasks;

import static scripts.HardcoreLeveler.data.Constants.ANVIL;
import static scripts.HardcoreLeveler.data.Constants.BRONZE_BAR;
import static scripts.HardcoreLeveler.data.Constants.HAMMER;
import static scripts.HardcoreLeveler.data.Constants.SMITHING_AREA;

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
public class Smithing implements AbstractTask {

	int childIndex;

	@Override
	public String info() {
		return "Smithing";
	}

	@Override
	public boolean shouldExecute() {
		return !Vars.get().doneSmithing && Vars.get().doneSmelting;
	}

	@Override
	public void execute() {
		// Resource availability check
		if (!ScriptUtil.isResourceStillAvailable(BRONZE_BAR))
			Vars.get().doneSmithing = true;

		// Smithing
		if (ScriptUtil.fetchTool(HAMMER))
			if (ScriptUtil.fetchResources(HAMMER, BRONZE_BAR))
				if (ScriptUtil.getInArea(SMITHING_AREA))
					if (ScriptUtil.interactObject("Smith", ANVIL)) {
						ScriptUtil.waitForInterface(312);
						if (ScriptUtil.handleInterface(312, getChildIndex(), "Smith All"))
							ScriptUtil.waitUntilAmountOfResourcesLeft(getNotEnoughBarsNumber(getChildIndex()), BRONZE_BAR);
					}
	}

	private int getChildIndex() {
		if (Vars.get().smithingLevel < 5 || Inventory.getCount(BRONZE_BAR) == 1)
			return 2; // use 1 bar
		else if (Vars.get().smithingLevel < 9 || Inventory.getCount(BRONZE_BAR) == 2)
			return 4; // use 2 bars
		else if (Vars.get().smithingLevel < 18 || Inventory.getCount(BRONZE_BAR) < 5)
			return 9; // use 3 bars
		else
			return 15; // use 5 bars
	}

	private int getNotEnoughBarsNumber(int index) {
		switch (index) {
		case 4:
			return 1;
		case 9:
			return 2;
		case 15:
			return 4;
		default:
			return 0;
		}
	}

}