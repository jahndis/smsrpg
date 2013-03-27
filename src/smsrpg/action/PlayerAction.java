package smsrpg.action;

import smsrpg.Player;
import smsrpg.World;

public abstract class PlayerAction {
	
	public PlayerAction() {
		
	}
	
	public abstract void execute(Player player, World world);

}
