package scripts.HardcoreLeveler.tasks;

import static scripts.HardcoreLeveler.data.Constants.*;

import org.tribot.api2007.types.RSArea;

import scripts.HardcoreLeveler.data.Vars;
import scripts.HardcoreLeveler.framework.AbstractTask;
import scripts.HardcoreLeveler.util.ScriptUtil;
/**
 * 
 * @author Einstein
 *
 *
 */
public class Woodcutting implements AbstractTask {

	private RSArea area;
	private int[] trees;

	@Override
	public String info() {
		return "Woodcutting";
	}

	@Override
	public boolean shouldExecute() {
		return !Vars.get().doneWoodcutting;
	}

	@Override
	public void execute() {
		// Equip axe if required
		ScriptUtil.equipTool(BRONZE_AXE);

		// Decide what trees to chop down
		if (Vars.get().woodcuttingLevel < 15) {
			area = NORMAL_TREE_AREA;
			trees = REGULAR_TREES;
		} else {
			area = OAK_AREA;
			trees = OAK_TREE;
		}

		// Woodcutting
		if (ScriptUtil.fetchTool(AXES))
			if (ScriptUtil.handleFullInventory(AXES))
				if (ScriptUtil.getInArea(area))
					if (ScriptUtil.interactObject("Chop down", trees))
						ScriptUtil.waitToAnimate();
	}

}