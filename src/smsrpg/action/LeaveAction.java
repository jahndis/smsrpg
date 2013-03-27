package smsrpg.action;

import smsrpg.Player;
import smsrpg.PlayerState;
import smsrpg.World;

public class LeaveAction extends PlayerAction {

	@Override
	public void execute(Player player, World world) {
		player.getState().push(PlayerState.LEAVING);
	}

}
