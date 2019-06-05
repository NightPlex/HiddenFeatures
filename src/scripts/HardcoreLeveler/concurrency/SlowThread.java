package scripts.HardcoreLeveler.concurrency;

import org.tribot.api.General;
import org.tribot.api.Timing;
import org.tribot.api2007.Interfaces;
import org.tribot.api2007.Skills;
import org.tribot.script.Script;

import scripts.HardcoreLeveler.data.Vars;

/**
 * Updates certain variables and selectively performs fail-safes
 * at as slower rate,
 * in an attempt to reduce the number of computations per second.
 * 
 * @author Einstein
 * 
 */

public class SlowThread extends Thread {

	Script script;

	public SlowThread(Script script) {
		this.script = script;
	}

	private long currentTime = System.currentTimeMillis();
	private long taskEndTime = currentTime + General.randomLong(5 * 60 * 1000, 15 * 60 * 1000);

	@Override
	public void run() {
		while (true) {
			General.sleep(1000);

			// Updates script running time; Used in paint
			updateTime();

			// Displayed when the player dies
			pwnedNoob();

			// Sends task switch command if current task timed out
			taskSwitch();

			// Closes the deposit box interface if the bot gets stuck
			depositBoxFailsafe();
		}
	}

	private void updateTime() {
		Vars.get().runningTime = Timing.msToString(script.getRunningTime());
	}

	private void pwnedNoob() {
		if (Skills.getCurrentLevel(Skills.SKILLS.HITPOINTS) == 0)
			Vars.get().shouldDisplayMessage = true;
		else
			Vars.get().shouldDisplayMessage = false;
	}

	private void taskSwitch() {
		currentTime = System.currentTimeMillis();
		// Task switching is only allowed if the script has finished gathering all tools
		if (!Vars.get().hasTools)
			return;
		if (currentTime > taskEndTime) {
			Vars.get().info = "Attempting to switch task...";
			Vars.get().shouldSwitchTask = true;
			// Note: this will occur roughly every 10 minutes.
			taskEndTime = currentTime + General.randomLong(5 * 60 * 1000, 15 * 60 * 1000);
		}
	}

	private void depositBoxFailsafe() {
		if (Interfaces.isInterfaceValid(192))
			Interfaces.closeAll();
	}

}