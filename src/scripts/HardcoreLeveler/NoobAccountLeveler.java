package scripts.HardcoreLeveler;

import java.awt.Graphics;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.tribot.api.General;
import org.tribot.api2007.util.ThreadSettings;
import org.tribot.script.Script;
import org.tribot.script.ScriptManifest;
import org.tribot.script.interfaces.Ending;
import org.tribot.script.interfaces.MessageListening07;
import org.tribot.script.interfaces.MouseActions;
import org.tribot.script.interfaces.Painting;
import org.tribot.script.interfaces.Starting;

import scripts.HardcoreLeveler.concurrency.SlowThread;
import scripts.HardcoreLeveler.data.Vars;
import scripts.HardcoreLeveler.framework.AbstractTask;
import scripts.HardcoreLeveler.paint.Paint;
import scripts.HardcoreLeveler.tasks.CombatCow;
import scripts.HardcoreLeveler.tasks.CombatDummy;
import scripts.HardcoreLeveler.tasks.Cooking;
import scripts.HardcoreLeveler.tasks.Firemaking;
import scripts.HardcoreLeveler.tasks.Fishing;
import scripts.HardcoreLeveler.tasks.Mining;
import scripts.HardcoreLeveler.tasks.Smelting;
import scripts.HardcoreLeveler.tasks.Smithing;
import scripts.HardcoreLeveler.tasks.Woodcutting;
import scripts.HardcoreLeveler.util.DataUtil;

/**____________________________________________________________________________________*/
                             @ScriptManifest                                           (
         authors = "Einstein",
         category = "Tools",
         name = "Noob Account Leveler",
         gameMode = 1,
         version = 0, // This project is a work in progress
         description = "Trains 11 skills. Self sustaining. Randomly switches tasks."   )
/**____________________________________________________________________________________*/

public class NoobAccountLeveler extends Script implements Starting, Ending, MessageListening07, MouseActions, Painting {

	@Override
	public void onStart() {
		ThreadSettings.get().setClickingAPIUseDynamic(true);
	}
                            	 
	@Override
	public void run() {
		
		// Initializing task list
		List<AbstractTask> taskList = new ArrayList<AbstractTask>();
		Collections.addAll(taskList, new Fishing(), new Cooking(), new Mining(), new Smelting(), new Smithing(), new Woodcutting(), new Firemaking(), new CombatDummy(), new CombatCow());
		
		// Fire up the "slow thread"
		new SlowThread(this).start();
		
		// Do the initial tool management
		EquipmentManager.cycle();
			
		// Update levels
		// Note: When control flow reaches this point, the player is guaranteed to be in game
		DataUtil.updateLevels();
		DataUtil.compareLevels();
		
		// Main loop. Control flow returns at this label if script should switch task
		start: while (true) {
			
			// Randomize the order of tasks in the list
			Collections.shuffle(taskList);
			Vars.get().shouldSwitchTask = false;
			
			// Iterate through the tasks
			for (AbstractTask task : taskList)
				/**
				 * Since more tasks could be valid at the same time, will use WHILE statement
				 * in order to prevent the script from jumping from task to task.
				 * 
				 * Will do task until completing it or until the script is forced out of the task by the random task switcher/task switch button
				 */
				while (task.shouldExecute()) {
					// CPU pl0x
					General.sleep(300);
					
					// Switches to random task if required
					if (Vars.get().shouldSwitchTask) continue start;

					// A string displayed on the paint containing information about the current task
					Vars.get().info = task.info();

					// Execute the task
					task.execute();
				}		
		}
	}
	
	@Override
	public void serverMessageReceived(String message) {
		DataUtil.interpretString(message);
	}
	
	@Override
	public void mouseClicked(Point mousePoint, int arg1, boolean botClick) {
		DataUtil.interpretClick(mousePoint, botClick);
	}

	@Override
	public void onPaint(Graphics g) {
		Paint.get().paint(g);
	}

	@Override
	public void onEnd() {
		General.println("                                                              ");
		General.println("______________________________________________________________");
		General.println("Thank you for running Einstein's Noob Account Leveler         ");
		General.println("        Total running time: " + Vars.get().runningTime         );
		General.println("        Levels gained: "      + Vars.get().levelsGained        );
		General.println("______________________________________________________________");
	}
	
	// Not in use
	public void clanMessageReceived(String arg0, String arg1) {}
	public void duelRequestReceived(String arg0, String arg1) {}
	public void personalMessageReceived(String arg0, String arg1) {}
	public void playerMessageReceived(String arg0, String arg1) {}
	public void tradeRequestReceived(String arg0) {}
	public void mouseDragged(Point mousePoint, int arg1, boolean bot) {}
	public void mouseMoved(Point mousePoint, boolean bot) {}
	public void mouseReleased(Point arg0, int arg1, boolean arg2) {}

}