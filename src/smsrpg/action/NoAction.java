package smsrpg.action;

import log.Log;
import smsrpg.Player;
import smsrpg.World;

public class NoAction extends PlayerAction {

	@Override
	public void execute(Player player, World world) {
		switch (player.getState().getCurrent()) {
		case LEAVING:
			player.getState().pop();
			break;
		default:
			Log.log("User responded 'Yes' to nothing. Just leave state same and resend.");
			break;
		}
	}

}
