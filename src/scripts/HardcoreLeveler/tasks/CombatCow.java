package scripts.HardcoreLeveler.tasks;

import static scripts.HardcoreLeveler.data.Constants.*;

import org.tribot.api.Clicking;
import org.tribot.api2007.Combat;
import org.tribot.api2007.Equipment;
import org.tribot.api2007.Equipment.SLOTS;
import org.tribot.api2007.GroundItems;
import org.tribot.api2007.Inventory;
import org.tribot.api2007.types.RSGroundItem;

import scripts.HardcoreLeveler.data.Vars;
import scripts.HardcoreLeveler.framework.AbstractTask;
import scripts.HardcoreLeveler.util.ScriptUtil;
/**
 * 
 * @author Einstein
 *
 *
 */
public class CombatCow implements AbstractTask {

	private boolean pickedEnoughBones = false;

	@Override
	public String info() {
		return "Animal abuse";
	}

	@Override
	public boolean shouldExecute() {
		return Vars.get().doneAttack && (!Vars.get().doneDefence || !Vars.get().doneStrength);
	}

	@Override
	public void execute() {
		if (ScriptUtil.fetchTool(WEAPONS))
			if (ScriptUtil.getInArea(COMBAT_AREA))
				combatCycle();
	}

	private void combatCycle() {
		// Equip weapon
		ScriptUtil.equipTool(WEAPONS);

		// Select style; prioritize strength xp
		if (!Vars.get().doneStrength)
			Combat.selectIndex(1); // strength xp
		else if (Equipment.getItem(SLOTS.WEAPON) != null)
			Combat.selectIndex(3); // defence xp

		// Combat
		if (!Combat.isUnderAttack())
			if (!pickedEnoughBones)
				pickUpBones();
			else if (burryBones())
				abuseCow();
	}

	/** Takes up to 4 bones */
	private void pickUpBones() {
		RSGroundItem[] bones = GroundItems.findNearest(BONES);
		if (bones.length > 0)
			if (bones[0].isOnScreen())
				ScriptUtil.takeGroundItem("Bones");
			else
				pickedEnoughBones = true;
		if (Inventory.getCount(BONES) >= 4)
			pickedEnoughBones = true;
	}

	private boolean burryBones() {
		Inventory.drop(COWHIDE, RAW_BEEF);
		Clicking.click("Bury Bones", Inventory.find(BONES));
		return Inventory.getCount(BONES) == 0;
	}

	private void abuseCow() {
		if (ScriptUtil.interactNPC("Attack", COW)) {
			ScriptUtil.waitToAnimate();
			pickedEnoughBones = false;
		}
	}

}