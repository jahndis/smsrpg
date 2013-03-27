package smsrpg.action;

import smsrpg.Player;
import smsrpg.PlayerState;
import smsrpg.World;

public class JoinAction extends PlayerAction {

	@Override
	public void execute(Player player, World world) {
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
