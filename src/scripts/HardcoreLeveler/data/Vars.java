package scripts.HardcoreLeveler.data;

import org.tribot.api.General;

public class Vars {

	// Instance manipulation
	private Vars() {}
	private final static Vars VARS = new Vars();
	public static Vars get() { return VARS; }

	// Script settings
	public boolean shouldSwitchTask;
	public boolean hasTools;
	public int levelsGained;

	// Target levels
	public int targetAttackLevel = General.random(5, 8);
	public int targetStrengthLevel = General.random(5, 8);
	public int targetDefenceLevel = General.random(5, 8);
	public int targetFishingLevel = General.random(15, 20);
	public int targetMiningLevel = General.random(15, 20);
	public int targetWoodcuttingLevel = General.random(20, 25);

	// Current levels
	public int attackLevel;
	public int strengthLevel;
	public int defenceLevel;
	public int prayerLevel;
	public int fishingLevel;
	public int cookingLevel;
	public int miningLevel;
	public int smithingLevel;
	public int woodcuttingLevel;
	public int firemakingLevel;

	// Task status
	public boolean doneAttack = false;
	public boolean doneStrength = false;
	public boolean doneDefence = false;
	public boolean donePrayer = false;
	public boolean doneFishing = false;
	public boolean doneCooking = false;
	public boolean doneMining = false;
	public boolean doneSmelting = false;
	public boolean doneSmithing = false;
	public boolean doneWoodcutting = false;
	public boolean doneFiremaking = false;

	// Paint
	public String runningTime = "";
	public String info = "";
	public boolean shouldDisplayMessage = false;

}