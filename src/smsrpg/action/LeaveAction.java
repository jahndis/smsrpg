package smsrpg.action;

import smsrpg.Player;
import smsrpg.PlayerState;

public class LeaveAction extends PlayerAction {

	@Override
	public void execute(Player player) {
		player.getState().push(PlayerState.LEAVING);
	}

}
