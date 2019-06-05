package scripts.HardcoreLeveler.paint;

import static scripts.HardcoreLeveler.data.Constants.*;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

import org.tribot.api.input.Mouse;
import org.tribot.api2007.Login;

import scripts.HardcoreLeveler.data.Vars;
import scripts.HardcoreLeveler.util.PaintUtil;
/**
 * 
 * @author Einstein
 *
 *
 */
public class Paint {
	
	// Instance manipulation
	private Paint() {}
	private static final Paint PAINT = new Paint();
	public static Paint get() { return PAINT; }
	
	
	/**
	 * As the script is still under development,
	 * these variables allow me to easily move the text
	 * if I decide to change the paint layout (again)
	 */
	int x = 5;
	int y = 17;
	int xlvl = x + 54;

	public void paint(Graphics g) {
		// Only paint if in game
		if (Login.getLoginState() != Login.STATE.INGAME)
			return;

		// Needs to be constantly reset to default
		y = 17;

		// Images
		g.drawImage(NOOB_ACCOUNT_LEVELER_PAINT, 7, 345, null);
		g.drawImage(TASK_LIST_PAINT, 0, 0, null);
			
		// Setting color based on status         Task name                           Task current level / Task target level (if applicable)
		g.setColor(setTaskColor("strength"));    g.drawString("Strength",x, y+=15);  g.drawString( "("+ Vars.get().strengthLevel + "/" + Vars.get().targetStrengthLevel + ")"      ,xlvl, y);
		g.setColor(setTaskColor("attack"));      g.drawString("Attack",x, y+=15);    g.drawString( "("+ Vars.get().attackLevel + "/" + Vars.get().targetAttackLevel + ")"          ,xlvl, y);
		g.setColor(setTaskColor("defence"));     g.drawString("Defance",x, y+=15);   g.drawString( "("+ Vars.get().defenceLevel + "/" + Vars.get().targetDefenceLevel + ")"        ,xlvl, y);
		g.setColor(setTaskColor("prayer"));      g.drawString("Prayer",x, y+=15); 
		g.setColor(setTaskColor("mining"));      g.drawString("Mining",x, y+=15);    g.drawString( "("+ Vars.get().miningLevel + "/" + Vars.get().targetMiningLevel + ")"          ,xlvl, y);
		g.setColor(setTaskColor("smelting"));    g.drawString("Smelting",x, y+=15); 
		g.setColor(setTaskColor("smithing"));    g.drawString("Smithing",x, y+=15); 
	    g.setColor(setTaskColor("fishing"));     g.drawString("Fishing",x, y+=15);   g.drawString( "("+ Vars.get().fishingLevel + "/" + Vars.get().targetFishingLevel + ")"         ,xlvl, y);
		g.setColor(setTaskColor("cooking"));     g.drawString("Cooking",x, y+=15); 
		g.setColor(setTaskColor("woodcutting")); g.drawString("Woodcut",x, y+=15);   g.drawString( "("+ Vars.get().woodcuttingLevel + "/" + Vars.get().targetWoodcuttingLevel + ")" ,xlvl, y);
		g.setColor(setTaskColor("firemaking"));	 g.drawString("Firemake",x, y+=15); 

		// "Task list:" title
		g.setColor(new Color(255, 152, 31));
		g.setFont(g.getFont().deriveFont(Font.BOLD));
		g.drawString("Task List:", 24, 16);

		// Task info and script running time
		g.setColor(Color.WHITE);
		g.setFont(g.getFont().deriveFont(Font.PLAIN));
		g.drawString(Vars.get().info, 15, 460);
		g.drawString(Vars.get().runningTime, 15, 440);

		// Switch task button
		// Task switching is only allowed after the bot gathered all the required tools
		if (Vars.get().hasTools) {
			g.drawImage(TASK_SWITCH_BUTTON, 714, 0, null);
			g.setColor(Color.WHITE);
			if (Mouse.getPos().x >= 714)
				if (Mouse.getPos().y <= 48) {
					g.drawString("New", 728, 13);
					g.drawString("Random", 716, 28);
					g.drawString("Task", 725, 43);
				}
		}

		// Very mature message displayed whenever you get PWNED NOOB
		if (Vars.get().shouldDisplayMessage) {
			g.setFont(new Font("Arial Black", Font.PLAIN, 200));
			g.setColor(Color.WHITE);
			g.drawString(DEATH_MESSAGE, 80, 220);
		}
	}

	private Color setTaskColor(String string) {
		if (PaintUtil.getLevel(string) == 0)
			return Color.BLACK; // task not initialized
		else if (PaintUtil.isTaskDone(string))
			return Color.GREEN; // task is complete
		else if (PaintUtil.getLevel(string) == 1)
			return Color.RED; // no progress was made
		return Color.YELLOW; // some progress was made
	}

}