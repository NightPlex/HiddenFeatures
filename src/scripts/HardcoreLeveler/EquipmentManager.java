package scripts.HardcoreLeveler;

import static scripts.HardcoreLeveler.data.Constants.*;

import org.tribot.api.Clicking;
import org.tribot.api.General;
import org.tribot.api.Timing;
import org.tribot.api.input.Keyboard;
import org.tribot.api.types.generic.Condition;
import org.tribot.api2007.Banking;
import org.tribot.api2007.Equipment;
import org.tribot.api2007.Equipment.SLOTS;
import org.tribot.api2007.Interfaces;
import org.tribot.api2007.Inventory;
import org.tribot.api2007.Player;
import org.tribot.api2007.Walking;
import org.tribot.api2007.WebWalking;
import org.tribot.api2007.types.RSArea;

import scripts.HardcoreLeveler.data.Vars;
import scripts.HardcoreLeveler.util.ScriptUtil;

/**
 * All code responsible for gathering the required tools is contained within this class.
 * 
 * Not the cleanest way to handle things, but it does the job done very well.
 * 
 * 
 * 
 * A complete cycle is defined as follows:
 * 
 *   1. Scans bank, inventory, worn items
 *   2. Determines if there are any tools missing 
 *   3. Gathers the missing tools if any. 
 *   4. Banks all the gathered tools. 
 *   5. Updates variables.
 * 
 * 
 * 
 * Notes:
 *   - control flow will exit this class ONLY after the script successfully managed to gather all the missing tools. 
 *   - the methods contained within this class will attempt to gather a tool until they succeed
 *   - cycle() is called once, upon starting the script
 *   - cycle() may be called at any time by any other class if the player lost tools
 * 
 * 
 * @author Einstein
 *
 */
public class EquipmentManager {
	
	private static boolean bankAnalyzed;
	private static boolean hasPickaxe;
	private static boolean hasAxe;
	private static boolean hasWeapon;
	private static boolean hasSmallFishingNet;
	private static boolean hasTinderbox;
	private static boolean hasHammer;
	
	public static void cycle() {
		
		// Sets all booleans to false
		Vars.get().info = "Resetting tool data...";
		resetData();

		// Scans the bank, inventory, and worn equipment
		Vars.get().info = "Analyzing bank...";
		while (!bankAnalyzed) // Note: No sleep required
			scan();
		
		// Attempts to gather the missing tools if any
		Vars.get().info  = "Attempting to gather missing tools...";
		while (!hasAllTools()) // Note: No sleep required
			getMissingTools();
		
		// Deposit all tools before resuming the actual script
		Vars.get().info  = "Banking gathered tools...";
		depositAllTools();
		
		// Script successfully managed to gather all tools; Control flow will now exit.
		Vars.get().info  = "Tool cycle completed succesfully. Exiting...";
		Vars.get().hasTools = true;
	}
	
	/**
	 * Sets hasTools to false; this is very important.
	 * 
	 * Sets all tool booleans to false.
	 */
	private static void resetData() {
		// Important script setting
		Vars.get().hasTools = false;
		// Class variables
		bankAnalyzed       = false;
		hasPickaxe         = false;
		hasAxe             = false;
		hasWeapon          = false;
		hasSmallFishingNet = false;
		hasTinderbox       = false;
		hasHammer          = false;
	}
	
	/**
	 * Goes to bank, opens it, performs the scan.
	 */
	private static void scan() {
		if (goToBankUninterrupted())
			if (ScriptUtil.openBank())
				if (Inventory.getAll().length > 0)
					Banking.depositAll();
				else if (Equipment.find(SLOTS.WEAPON).length > 0)
					Banking.depositEquipment();
				else {
					General.sleep(1000); // required
					hasPickaxe = checkBankFor(PICKAXES);
					hasAxe = checkBankFor(AXES);
					hasWeapon = checkBankFor(WEAPONS);
					hasSmallFishingNet = checkBankFor(SMALL_FISHING_NET);
					hasTinderbox = checkBankFor(TINDERBOX);
					hasHammer = checkBankFor(HAMMER);
					bankAnalyzed = true;
				}
	}
	
	/**
	 * @return true if player has all tools, false if any of them is missing
	 */
	private static boolean hasAllTools() {
		return (hasPickaxe && hasAxe && hasWeapon && hasSmallFishingNet && hasTinderbox && hasHammer);
	}

	/**
	 * Gathers all the missing tools.
	 * 
	 * WHILE statements are used so the script doesn't jump from getting one tool to another:
	 * It will attempt to get the tool until it succeeds. Only then breaks the loop.
	 */
	private static void getMissingTools() {
		
		while (!hasWeapon) {
			getTool(COMBAT_TUTOR_AREA, COMBAT_TUTOR_ID, TRAINING_SWORD);
			if (Inventory.getCount(TRAINING_SWORD) > 0)
				hasWeapon = true;
		}

		while (!hasAxe) {
			getTool(WOODCUTTING_TUTOR_AREA, WOODCUTTING_TUTOR_ID, BRONZE_AXE);
			if (Inventory.getCount(BRONZE_AXE) > 0)
				hasAxe = true;
		}

		while (!hasTinderbox) {
			getTool(WOODCUTTING_TUTOR_AREA, WOODCUTTING_TUTOR_ID, TINDERBOX);
			if (Inventory.getCount(TINDERBOX) > 0)
				hasTinderbox = true;
		}

		while (!hasPickaxe) {
			getTool(MINING_TUTOR_AREA, MINING_TUTOR_ID, BRONZE_PICKAXE);
			if (Inventory.getCount(BRONZE_PICKAXE) > 0)
				hasPickaxe = true;
		}

		while (!hasSmallFishingNet) {
			getTool(FISHING_TUTOR_AREA, FISHING_TUTOR_ID, SMALL_FISHING_NET);
			if (Inventory.getCount(SMALL_FISHING_NET) > 0)
				hasSmallFishingNet = true;
		}

		while (!hasHammer) {
			getHammer();
			if (Inventory.getCount(HAMMER) > 0)
				if (getOutOfHammerRoom()) 		
					// Script is done with the hammer only after it escapes the 'hammer room'
					// Reason: 'hammer room' is not supported by the walking system & the bot will otherwise get stuck in the room
					hasHammer = true; 
		}
	}
	
	private static void getHammer() {
		General.sleep(300);
		if (Inventory.getCount(HAMMER) == 0)
			if (getNearHammer())
				ScriptUtil.takeGroundItem("Hammer");
	}
	
	/**
	 * Gets near the hammer.
	 * Note: annoying logic because location is not supported by the walking system.
	 * 
	 * @return true if near hammer, false otherwise.
	 */
	private static boolean getNearHammer() {
		if(!HAMMER_GROUND_LEVEL_AREA.contains(Player.getPosition()) && !HAMMER_SAME_LEVEL_AREA.contains(Player.getPosition())) // player not in the building
			WebWalking.walkTo(HAMMER_GROUND_LEVEL_AREA.getRandomTile()); // Get inside 'hammer building'
		else if (!HAMMER_SAME_LEVEL_AREA.contains(Player.getPosition())) //Player is in building, but not at correct floor
			ScriptUtil.interactObject("Climb-up", 24079); // climb up to 'hammer level'
		else if (!HAMMER_PROXIMITY_AREA.contains(Player.getPosition())) //Player is on 'hammer level' but not near hammer
			Walking.blindWalkTo(HAMMER_PROXIMITY_AREA.getRandomTile()); // Go near hammer
		return HAMMER_PROXIMITY_AREA.contains(Player.getPosition()); // return: true if player is near the hammer; false otherwise
	}

	/**
	 * Escapes the hammer room.
	 * Required because location is not supported by the current walking system.
	 * 
	 * @return true if escaped the room, false if still trapped inside
	 */
	private static boolean getOutOfHammerRoom() {
	    if(HAMMER_SAME_LEVEL_AREA.contains(Player.getPosition())) { // if player on the same level as hammer - 'hammer room'
	    	if(ScriptUtil.interactObject("Climb-down", 24080)) // if clicked climb down stairs
					Timing.waitCondition(new Condition() { // wait until we are on ground level
						@Override
						public boolean active() { 
							General.sleep(300);
							return (HAMMER_GROUND_LEVEL_AREA.contains(Player.getPosition()));
						}
					}, General.random(7000, 10000));
	    }
		return !HAMMER_SAME_LEVEL_AREA.contains(Player.getPosition()); //true if escaped the room, false if still trapped inside
	}
	
	/**
	 * Gathers specified tool from tutors.
	 * 
	 * Will attempt to get it until it succeeds.
	 * 
	 * @param tutorArea where the tool is located
	 * @param tutorID bot needs to interact with
	 * @param toolID bot attempts to get
	 */
	private static void getTool(RSArea tutorArea, int tutorID, int toolID) {
		General.sleep(300); // sleep added because we are in a while loop
		General.println("Script attempts to get the following tool: " + toolID);
		if (getInAreaUninterrupted(tutorArea)) 
			persuadeTutor(tutorID, toolID);
	}
	
	/**
	 * Forces the tutor to hand over the tool.
	 * 
	 * @param tutorID to abuse
	 * @param toolID the bot is expecting to get
	 */
	private static void persuadeTutor(int tutorID, int toolID) {
		if (!areDialogueInterfacesValid())
			ScriptUtil.interactNPC("Talk-to", tutorID);
		 else 
			handleInterfaces();
	}

	/**
	 * @return true of any dialogue interfaces are open, false if none are open
	 */
	private static boolean areDialogueInterfacesValid() {
		for (int interfaceID : DIALOGUE_INTERFACES) {
			if (Interfaces.isInterfaceValid(interfaceID))
				return true;
		}
		return false;
	}
	
	/**
	 * Handles the chat by spamming spacebar.
	 */
	@SuppressWarnings("deprecation")
	private static void handleInterfaces() {
		if (Interfaces.isInterfaceValid(219))
			Clicking.click(Interfaces.get(219, 0).getChild(1));
		else
			Keyboard.pressKey(' ');
	}
	
	/**
	 * Checks bank for specified tool. (could be array or single tool)
	 * 
	 * @param ids of the tool(s)
	 * @return true if found (if array: true if any was found), false otherwise
	 */
	private static boolean checkBankFor(int...ids) {
		return Banking.find(ids).length > 0;
	}
	
	
	/**
	 * Deposits all tools before the actual script resumes execution.
	 * 
	 * Prevents the bot from losing the gathered tools by dying.
	 */
	private static void depositAllTools() {
		while (Inventory.getAll().length > 0 || Equipment.find(SLOTS.WEAPON).length > 0) {
			General.sleep(300);
			if (goToBankUninterrupted())
				if (ScriptUtil.openBank())
					if (Inventory.getAll().length > 0)
						Banking.depositAll();
					else if (Equipment.find(SLOTS.WEAPON).length > 0)
						Banking.depositEquipment();
		}
	}

	/**
	 * Walks to the bank uninterrupted.
	 * Note: the walking methods in ScriptUtil are all interrupted by task switching.
	 * 
	 * @return true if is in bank, false otherwise
	 */
	private static boolean goToBankUninterrupted() {
		if(!Banking.isInBank())
			WebWalking.walkToBank();
		return Banking.isInBank();
	}
	
	/**
	 * Walks to the specified area uninterrupted.
	 * Note: the walking methods in ScriptUtil are all interrupted by task switching.
	 * 
	 * @param area to walk in
	 * @return true if in area, false otherwise
	 */
	private static boolean getInAreaUninterrupted(RSArea area) {
		  if (!area.contains(Player.getPosition()))
		      WebWalking.walkTo(area.getRandomTile());
		  return area.contains(Player.getPosition());
	}
	
}