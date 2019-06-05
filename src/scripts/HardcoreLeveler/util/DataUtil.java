package scripts.HardcoreLeveler.util;


import java.awt.Image;
import java.awt.Point;
import java.io.IOException;
import java.net.URL;
import javax.imageio.ImageIO;
import org.tribot.api.General;
import org.tribot.api2007.Skills;

import scripts.HardcoreLeveler.data.Vars;
/**
 * 
 * @author Einstein
 *
 *
 */
public class DataUtil {
	
	/**
	 * Gets image from the web.
	 * 
	 * @param url of the image
	 * @return Image or null if failed.
	 */
	public static Image getImage(String url) {
		try {
			return ImageIO.read(new URL(url));
		} catch (IOException e) {
			General.println("Unable to load image!");
		}
		return null;
	}

	/**
	 * Decides what happened based on received string and updates variables accordingly.
	 * 
	 * @param string to interpret
	 */
	public static void interpretString(String string) {
		if (string.contains("advanced")) {
			Vars.get().levelsGained++;
			updateLevels();
			compareLevels();
		}
		if (string.contains("you are dead"))
			Vars.get().info = ("You died noob!");
	}
	
	/**
	 * Decides if the user clicked the Task Switch button.
	 * 
	 * @param mousePoint point clicked
	 * @param botClick source of the click
	 */
	public static void interpretClick(Point mousePoint, boolean botClick) {
		if (botClick)
			return;
		if (mousePoint.x >= 714)
			if (mousePoint.y <= 50)
				switchTaskButtonPressed();
	}
	
	/**
	 * Called when the Task Switch button is pressed.
	 * Decides if the script should switch tasks.
	 * 
	 * Note: Task switching is only allowed after the bot gathered all the required tools
	 */
	private static void switchTaskButtonPressed() {
		if (!Vars.get().hasTools)
			return;
		Vars.get().info = "Attempting to switch task...";
		Vars.get().shouldSwitchTask = true;
	}

	/**
	 * Updates all levels.
	 * 
	 * Called upon leveling up.
	 */
	public static void updateLevels() {
		Vars.get().attackLevel      = Skills.getActualLevel(Skills.SKILLS.ATTACK);
		Vars.get().strengthLevel    = Skills.getActualLevel(Skills.SKILLS.STRENGTH);
		Vars.get().defenceLevel     = Skills.getActualLevel(Skills.SKILLS.DEFENCE);
		Vars.get().prayerLevel      = Skills.getActualLevel(Skills.SKILLS.PRAYER);
		Vars.get().fishingLevel     = Skills.getActualLevel(Skills.SKILLS.FISHING);
		Vars.get().cookingLevel     = Skills.getActualLevel(Skills.SKILLS.COOKING);
		Vars.get().miningLevel      = Skills.getActualLevel(Skills.SKILLS.MINING);
		Vars.get().smithingLevel    = Skills.getActualLevel(Skills.SKILLS.SMITHING);
		Vars.get().woodcuttingLevel = Skills.getActualLevel(Skills.SKILLS.WOODCUTTING);
		Vars.get().firemakingLevel  = Skills.getActualLevel(Skills.SKILLS.FIREMAKING);
	}
	
	/**
	 * Compares current levels with target levels.
	 * 
	 * If target level was achieved, the corresponding task is set to 'done' (true).
	 */
	public static void compareLevels() {
		if(Vars.get().attackLevel      >= Vars.get().targetAttackLevel)      Vars.get().doneAttack = true;
		if(Vars.get().strengthLevel    >= Vars.get().targetStrengthLevel)    Vars.get().doneStrength = true;
		if(Vars.get().defenceLevel     >= Vars.get().targetDefenceLevel)    {Vars.get().doneDefence = true; Vars.get().donePrayer = true; }
		if(Vars.get().fishingLevel     >= Vars.get().targetFishingLevel)     Vars.get().doneFishing = true;
		if(Vars.get().miningLevel      >= Vars.get().targetMiningLevel)      Vars.get().doneMining = true;
		if(Vars.get().woodcuttingLevel >= Vars.get().targetWoodcuttingLevel) Vars.get().doneWoodcutting = true;
	}

}