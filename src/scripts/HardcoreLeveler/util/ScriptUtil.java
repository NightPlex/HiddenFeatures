package scripts.HardcoreLeveler.util;

import org.tribot.api.Clicking;
import org.tribot.api.General;
import org.tribot.api.Timing;
import org.tribot.api.types.generic.Condition;
import org.tribot.api2007.Banking;
import org.tribot.api2007.Camera;
import org.tribot.api2007.Equipment;
import org.tribot.api2007.Game;
import org.tribot.api2007.GroundItems;
import org.tribot.api2007.Interfaces;
import org.tribot.api2007.Inventory;
import org.tribot.api2007.NPCs;
import org.tribot.api2007.Objects;
import org.tribot.api2007.Player;
import org.tribot.api2007.Walking;
import org.tribot.api2007.WebWalking;
import org.tribot.api2007.Equipment.SLOTS;
import org.tribot.api2007.types.RSArea;
import org.tribot.api2007.types.RSGroundItem;
import org.tribot.api2007.types.RSInterface;
import org.tribot.api2007.types.RSNPC;
import org.tribot.api2007.types.RSObject;

import scripts.HardcoreLeveler.EquipmentManager;
import scripts.HardcoreLeveler.data.Vars;
/**
 * 
 * ScriptUtil contains reusable code, used by tasks.
 *         
 * @author Einstein
 * 
 */
public class ScriptUtil {

	/**
	 * Checks if bank still contains specified resource.
	 * 
	 * @param resourceID to check
	 * @return true if resource exists, false otherwise
	 */
	public static boolean isResourceStillAvailable(int... resourceID) {
		if (Banking.isBankScreenOpen())
			for (int i = 0; i < resourceID.length; i++)
				if (Banking.find(resourceID[i]).length == 0 && Inventory.getCount(resourceID[i]) == 0)
					return false;
		return true;
	}

	/**
	 * Fetches specified resources without depositing specified tool.
	 * 
	 * @param toolID to avoid depositing
	 * @param resourceID to withdraw from the bank
	 */
	public static boolean fetchResources(int toolID, int resourceID) {
		if (Inventory.getCount(resourceID) > 0)
			return true;
		int[] toolAndResources = { toolID, resourceID };
		if (goToBank())
			if (openBank()) {
				if (Inventory.getAll().length - (Inventory.find(toolAndResources).length) > 0)
					Banking.depositAllExcept(toolAndResources);
				else if (Banking.withdraw(0, resourceID))
					return true;
			}
		return Inventory.getCount(resourceID) > 0;
	}

	/**
	 * Fetches 2 types of resources (equal amounts) without depositing specified tool.
	 * 
	 * @param toolID to avoid depositing
	 * @param firstResourceID to withdraw from the bank
	 * @param secondResourceID to withdraw from the bank
	 */
	public static boolean fetchResources(int toolID, int firstResourceID, int secondResourceID) {
		if (Inventory.getCount(firstResourceID) > 0 && Inventory.getCount(secondResourceID) > 0)
			return true;
		int[] toolAndResources = { toolID, firstResourceID, secondResourceID };
		if (goToBank())
			if (openBank())
				if (Inventory.getAll().length - (Inventory.find(toolAndResources).length) > 0) {
					Banking.depositAllExcept(toolAndResources);
				} else {
					int requiredOfEachType = (28 - Inventory.find(toolID).length) / 2;
					if (Inventory.getCount(firstResourceID) < requiredOfEachType)
						if (Banking.withdraw(requiredOfEachType - Inventory.getCount(firstResourceID), firstResourceID))
							waitItemAppears(firstResourceID);
					if (Inventory.getCount(secondResourceID) < requiredOfEachType)
						if (Banking.withdraw(requiredOfEachType - Inventory.getCount(secondResourceID),
								secondResourceID))
							waitItemAppears(secondResourceID);
				}
		return (Inventory.getCount(firstResourceID) > 0 && Inventory.getCount(secondResourceID) > 0);
	}

	/**
	 * Walks to the nearest bank. Will be interrupted by task switch
	 * 
	 * @return true if in bank, false otherwise
	 */
	public static boolean goToBank() {
		if (!Banking.isInBank())
			WebWalking.walkToBank(new Condition() {
				public boolean active() {
					return Vars.get().shouldSwitchTask;
				}
			}, 100);
		return Banking.isInBank();
	}

	/**
	 * Opens bank
	 * 
	 * @return true if bank open, false otherwise
	 */
	public static boolean openBank() {
		if (!Banking.isBankScreenOpen())
			Banking.openBank();
		return Banking.isBankScreenOpen();
	}

	/**
	 * Will fetch the first available tool from a given array.
	 * 
	 * @param tool array
	 * @return true if got tool, false otherwise
	 */
	public static boolean fetchTool(int... tool) {
		if (Inventory.getCount(tool) == 0 && !Equipment.isEquipped(tool))
			if (goToBank())
				if (openBank()) {
					if (Banking.find(tool).length == 0)
						EquipmentManager.cycle();
					else {
						if (Equipment.getItem(SLOTS.WEAPON) != null)
							Banking.depositEquipment();
						if (Inventory.getAll().length > 0)
							Banking.depositAll();
						if (Banking.withdraw(1, tool))
							return true;
					}
				}
		return Inventory.getCount(tool) > 0 || Equipment.isEquipped(tool);
	}

	/**
	 * Walk to specified area. Will be interrupted by task switch.
	 * 
	 * @param area to walk into
	 * @return true if in area, false otherwise
	 */
	public static boolean getInArea(RSArea area) {
		if (!area.contains(Player.getPosition()))
			WebWalking.walkTo(area.getRandomTile(), new Condition() {
				public boolean active() {
					return Vars.get().shouldSwitchTask;
				}
			}, 100);
		return area.contains(Player.getPosition());
	}

	/**
	 * Banks all except tools if inventory is full. Used in gathering tasks.
	 * 
	 * @param tool not to deposit
	 * @return true if inventory is not full, false otherwise
	 */
	public static boolean handleFullInventory(int... tool) {
		if (Inventory.isFull())
			if (goToBank())
				if (openBank())
					Banking.depositAllExcept(tool);
		return !Inventory.isFull();
	}

	/**
	 * Interacts with an object.
	 * 
	 * @param actionName click option name
	 * @param objectID to interact with
	 * @return true if successfully clicked object, false otherwise
	 */
	public static boolean interactObject(String actionName, int... objectID) {
		// Only attempt to interact if player is not busy
		if (Player.getAnimation() != -1 || Player.isMoving())
			return false;
		RSObject[] object = Objects.findNearest(10, objectID);
		if (object.length == 0)
			return false;
		if (!object[0].isOnScreen()) {
			Walking.blindWalkTo(object[0].getPosition());
			Camera.turnToTile(object[0].getPosition());
		} else {
			if (Clicking.click(actionName, object[0]))
				;
			return true;
		}
		return false;
	}

	/**
	 * Interacts with a NPC.
	 * 
	 * @param actionName option
	 * @param npcID to interact with
	 * @return true if successfully clicked NPC, false otherwise
	 */
	public static boolean interactNPC(String actionName, int... npcID) {
		// Only attempt to interact if player is not busy
		if (Player.getAnimation() != -1 || Player.isMoving())
			return false;
		RSNPC[] npc = NPCs.findNearest(npcID);
		if (npc.length == 0)
			return false;
		if (!npc[0].isOnScreen()) {
			Walking.blindWalkTo(npc[0].getPosition());
			Camera.turnToTile(npc[0].getPosition());
		} else {
			if (!npc[0].isInCombat()) // for the combat classes
				if (Clicking.click(actionName, npc[0]))
					return true;
		}
		return false;
	}

	/**
	 * Waits for the player to stop walking and start animating.
	 */
	public static void waitToAnimate() {
		// Wait to finish walking towards the entity we are attempting to interact with
		Timing.waitCondition(new Condition() {
			@Override
			public boolean active() {
				General.sleep(300);
				return !Player.isMoving();
			}
		}, General.random(5000, 7000));
		
		// Wait to start animating
		Timing.waitCondition(new Condition() {
			@Override
			public boolean active() {
				General.sleep(300);
				return Player.getAnimation() != -1;
			}
		}, General.random(2000, 4000));
	}

	/**
	 * Waits for the player to stop walking, start animating and finish animating.
	 */
	public static void waitToFishishAnimation() {
		// Wait to finish walking towards the entity we are attempting to interact with
		Timing.waitCondition(new Condition() {
			@Override
			public boolean active() {
				General.sleep(300);
				return !Player.isMoving();
			}
		}, General.random(5000, 7000));

		// Wait to start animating
		Timing.waitCondition(new Condition() {
			@Override
			public boolean active() {
				General.sleep(300);
				return Player.getAnimation() != -1;
			}
		}, General.random(2000, 4000));

		// Wait to finish animation
		Timing.waitCondition(new Condition() {
			@Override
			public boolean active() {
				General.sleep(300);
				return Player.getAnimation() == -1;
			}
		}, General.random(2000, 4000));
	}

	/**
	 * Waits for a interface to be valid.
	 * 
	 * @param interfaceID the script is waiting for
	 */
	public static void waitForInterface(int interfaceID) {
		Timing.waitCondition(new Condition() {
			@Override
			public boolean active() {
				General.sleep(300);
				return Interfaces.isInterfaceValid(interfaceID);
			}
		}, General.random(5000, 7000));
	}

	/**
	 * Waits for an item to appear in the inventory.
	 * 
	 * @param itemID -ID of the item the script is waiting for
	 */
	public static void waitItemAppears(int itemID) {
		int i = Inventory.getCount(itemID);
		Timing.waitCondition(new Condition() {
			@Override
			public boolean active() {
				General.sleep(300);
				return Inventory.getCount(itemID) > i;
			}
		}, General.random(2000, 4000));
	}
	
	/**
	 * Waits for an item to disappear from the inventory.
	 * 
	 * @param itemID
	 */
	public static void waitItemDisappears(int itemID) {
		int i = Inventory.getCount(itemID);
		Timing.waitCondition(new Condition() {
			@Override
			public boolean active() {
				General.sleep(300);
				return Inventory.getCount(itemID) < i;
			}
		}, General.random(2000, 4000));
	}

	/**
	 * Picks up ground item.
	 * 
	 * @param itemID to be picked up
	 * @return true if clicked item, false otherwise
	 */
	public static boolean takeGroundItem(String name) {
		if (Player.getAnimation() != -1 || Player.isMoving())
			return false;
		RSGroundItem[] groundItem = GroundItems.findNearest(name);
		if (groundItem.length == 0)
			return false;
		if (Clicking.click("Take " + name, groundItem[0]))
			return true;
		return false;
	}

	/**
	 * Equips tool if not already equipped.
	 * 
	 * @param tool to equip
	 */
	public static void equipTool(int... tool) {
		// Don't click tool if the bank is open
		if (Banking.isBankScreenOpen())
			return;
		if (!Equipment.isEquipped(tool))
			Clicking.click(Inventory.find(tool));
	}

	/**
	 * Clicks inventory item. It cannot be used for items that will disappear
	 * upon clicking such as food / weapons.
	 * 
	 * @param id to click
	 * @return true if clicked any item; false otherwise.
	 */
	public static boolean selectInventoryItem(int... id) {
		if (Game.getItemSelectionState() != 0)
			return true;
		if (Clicking.click(Inventory.find(id)))
			Timing.waitCondition(new Condition() {
				@Override
				public boolean active() {
					General.sleep(300);
					return Game.getItemSelectionState() != 0;
				}
			}, General.random(1500, 3000));
		return Game.getItemSelectionState() != 0;
	}

	/**
	 * Waits until there is a certain amount of the specified resource left.
	 * Breaks out of the sleep if level up interfaces appear.
	 * 
	 * Note: the only reason why there is an amount specified (instead of 0) is because 
	 *       smithing task sometimes requires more than one bar to continue the work.
	 * 
	 * @param amount to stop sleeping at
	 * @param id of the resource
	 */
	public static void waitUntilAmountOfResourcesLeft(int amount, int... id) {
		Timing.waitCondition(new Condition() {
			public boolean active() {
				General.sleep(300);
				return Inventory.getCount(id) <= amount || Interfaces.isInterfaceValid(233)
						|| Interfaces.isInterfaceValid(11) || Interfaces.isInterfaceValid(229);
			}
		}, General.random(70 * 1000, 80 * 1000));
	}

	/**
	 * Waits until there is a certain amount left of any of the two resources.
	 * Breaks out of the sleep if level up interfaces appear.
	 * 
	 * Note: the only reason why there is an amount specified (instead of 0) is because 
	 *       smithing task sometimes requires more than one bar to continue the work.
	 * 
	 * @param amount to stop sleeping at
	 * @param idOne of the first resource
	 * @param idTwo of the second resource
	 */
	public static void waitUntilAmountOfResourcesLeft(int amount, int idOne, int idTwo) {
		Timing.waitCondition(new Condition() {
			public boolean active() {
				General.sleep(300);
				return Inventory.getCount(idOne) <= amount || Inventory.getCount(idTwo) <= amount
						|| Interfaces.isInterfaceValid(233) || Interfaces.isInterfaceValid(11)
						|| Interfaces.isInterfaceValid(229);
			}
		}, General.random(70 * 1000, 80 * 1000));
	}

	/**
	 * Finds and clicks an interface with the specified action.
	 * 
	 * @param parent id of the interface
	 * @param child id of the interface
	 * @param option name
	 * @return true if clicked, false otherwise
	 */
	public static boolean handleInterface(int parent, int child, String option) {
		RSInterface cookOption = Interfaces.get(parent, child);
		if (cookOption != null)
			if (cookOption.click(option))
				return true;
		return false;
	}

}