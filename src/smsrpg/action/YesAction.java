package smsrpg.action;

import log.Log;
import smsrpg.Player;
import smsrpg.PlayerState;
import smsrpg.World;

public class YesAction extends PlayerAction {

	@Override
	public void execute(Player player, World world) {
		switch (player.getState().getCurrent()) {
		case LEAVING:
			player.getState().change(PlayerState.LEFT);
			player.unregister();
			break;
		default:
			Log.log("User responded 'Yes' to nothing. Just leave state same and resend.");
			break;
		}
	}
	
}
