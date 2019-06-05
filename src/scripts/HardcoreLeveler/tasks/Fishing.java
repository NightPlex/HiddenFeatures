package scripts.HardcoreLeveler.tasks;

import static scripts.HardcoreLeveler.data.Constants.FISHING_AREA;
import static scripts.HardcoreLeveler.data.Constants.FISHING_SPOT;
import static scripts.HardcoreLeveler.data.Constants.SMALL_FISHING_NET;

import scripts.HardcoreLeveler.data.Vars;
import scripts.HardcoreLeveler.framework.AbstractTask;
import scripts.HardcoreLeveler.util.ScriptUtil;
/**
 * 
 * @author Einstein
 *
 *
 */
public class Fishing implements AbstractTask {

	@Override
	public String info() {
		return "Fishing";
	}

	@Override
	public boolean shouldExecute() {
		return !Vars.get().doneFishing;
	}

	@Override
	public void execute() {
		if (ScriptUtil.fetchTool(SMALL_FISHING_NET))
			if (ScriptUtil.handleFullInventory(SMALL_FISHING_NET))
				if (ScriptUtil.getInArea(FISHING_AREA))
					if (ScriptUtil.interactNPC("Net", FISHING_SPOT))
						ScriptUtil.waitToAnimate();
	}
	
}