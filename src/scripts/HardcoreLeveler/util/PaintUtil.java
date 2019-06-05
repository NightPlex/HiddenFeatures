package scripts.HardcoreLeveler.util;

import org.tribot.api2007.Skills;

import scripts.HardcoreLeveler.data.Vars;

/**
 * 
 * @author Einstein
 *
 *
 */
public class PaintUtil {

	public static boolean isTaskDone(String task) {
		switch (task) {
		case "attack":     return Vars.get().doneAttack;
		case "strength":   return Vars.get().doneStrength;
		case "defence":    return Vars.get().doneDefence;
		case "prayer":     return Vars.get().donePrayer;
		case "fishing":    return Vars.get().doneFishing;
		case "cooking":    return Vars.get().doneCooking;
		case "mining":     return Vars.get().doneMining;
		case "smelting":   return Vars.get().doneSmelting;
		case "smithing":   return Vars.get().doneSmithing;
		case "woodcutting":return Vars.get().doneWoodcutting;
		case "firemaking": return Vars.get().doneFiremaking;
		default:           return false;
		}
	}

	public static int getLevel(String task) {
		if (!Vars.get().hasTools)
			return 0;
		switch (task) {
		case "attack":     return Skills.getActualLevel(Skills.SKILLS.ATTACK);
		case "strength":   return Skills.getActualLevel(Skills.SKILLS.STRENGTH);
		case "defence":    return Skills.getActualLevel(Skills.SKILLS.DEFENCE);
		case "prayer":     return Skills.getActualLevel(Skills.SKILLS.PRAYER);
		case "fishing":    return Skills.getActualLevel(Skills.SKILLS.FISHING);
		case "cooking":    return Skills.getActualLevel(Skills.SKILLS.COOKING);
		case "mining":     return Skills.getActualLevel(Skills.SKILLS.MINING);
		case "smelting":   return Skills.getActualLevel(Skills.SKILLS.SMITHING);
		case "smithing":   return Vars.get().doneSmelting ? 2 : 1;
		case "woodcutting":return Skills.getActualLevel(Skills.SKILLS.WOODCUTTING);
		case "firemaking": return Skills.getActualLevel(Skills.SKILLS.FIREMAKING);
		default:           return -1;
		}
	}

}