package scripts.HardcoreLeveler.tasks;

import static scripts.HardcoreLeveler.data.Constants.DUMMY;
import static scripts.HardcoreLeveler.data.Constants.VARROCK_GYM_AREA;

import scripts.HardcoreLeveler.data.Vars;
import scripts.HardcoreLeveler.framework.AbstractTask;
import scripts.HardcoreLeveler.util.ScriptUtil;
/**
 * 
 * @author Einstein
 *
 *
 */
public class CombatDummy implements AbstractTask {
	
	@Override
	public String info() {
		return "Pump iron at the Varrock gym";
	}

	@Override
	public boolean shouldExecute() {
		 return !Vars.get().doneAttack;
	}

	@Override
	public void execute() {
		if(ScriptUtil.getInArea(VARROCK_GYM_AREA))
			if(ScriptUtil.interactObject("Attack", DUMMY))
				ScriptUtil.waitToFishishAnimation();
	}

}