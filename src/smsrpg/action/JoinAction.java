package smsrpg.action;

import smsrpg.Player;
import smsrpg.PlayerState;

public class JoinAction extends PlayerAction {

	@Override
	public void execute(Player player) {
		switch (player.getState().getCurrent()) {
		case NOT_REGISTERED:
			player.getState().change(PlayerState.JOINING);
			player.register();
			break;
		default:
			break;
		}
	}
	
}
