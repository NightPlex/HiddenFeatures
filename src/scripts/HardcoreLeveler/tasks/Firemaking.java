package scripts.HardcoreLeveler.tasks;

import static scripts.HardcoreLeveler.data.Constants.FIREMAKING_AREA;
import static scripts.HardcoreLeveler.data.Constants.FIREMAKING_START_AREA;
import static scripts.HardcoreLeveler.data.Constants.NORMAL_LOGS;
import static scripts.HardcoreLeveler.data.Constants.OAK_LOGS;
import static scripts.HardcoreLeveler.data.Constants.TINDERBOX;

import org.tribot.api.General;
import org.tribot.api2007.Objects;
import org.tribot.api2007.Player;
import org.tribot.api2007.WebWalking;
import org.tribot.api2007.types.RSObject;

import scripts.HardcoreLeveler.data.Vars;
import scripts.HardcoreLeveler.framework.AbstractTask;
import scripts.HardcoreLeveler.util.ScriptUtil;
/**
 * 
 * @author Einstein
 *
 *
 */
public class Firemaking implements AbstractTask {

	private int logs;

	@Override
	public String info() {
		return "Firemaking";
	}

	@Override
	public boolean shouldExecute() {
		return !Vars.get().doneFiremaking && Vars.get().doneWoodcutting;
	}

	@Override
	public void execute() {
		// Decide what logs to burn
		if (Vars.get().firemakingLevel < 15)
			logs = NORMAL_LOGS;
		else
			logs = OAK_LOGS;

		// Resource availability check
		if (ScriptUtil.isResourceStillAvailable(logs))
			Vars.get().doneFiremaking = true;

		// Firemaking
		if (ScriptUtil.fetchTool(TINDERBOX))
			if (ScriptUtil.fetchResources(TINDERBOX, logs)) {
				if (!FIREMAKING_AREA.contains(Player.getPosition()) || currentTileContainsAnotherObject())
					walkToStartArea();
				else if (Player.getAnimation() == -1)
					if (ScriptUtil.selectInventoryItem(logs))
						if (ScriptUtil.selectInventoryItem(TINDERBOX))
							ScriptUtil.waitToAnimate();
			}
	}
	
	private void walkToStartArea() {
			WebWalking.walkTo(FIREMAKING_START_AREA.getRandomTile());
	}

	private boolean currentTileContainsAnotherObject() {
		RSObject [] fire = Objects.getAt(Player.getPosition());
		if(fire.length > 0) General.println("Tile already contains an object");
		return fire.length > 0;
	}
	
}