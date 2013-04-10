package smsrpg.action;

import smsrpg.Player;
import smsrpg.PlayerState;

public class ArbitraryAction extends PlayerAction {
	
	private String message;
	private boolean inputIsArbitrary;
	
	public ArbitraryAction(String message) {
		super();
		this.message = message;
		this.inputIsArbitrary = true;
	}

	@Override
	public void execute(Player player) {
		switch (player.getState().getCurrent()) {
		case JOINING:
			//Player is entering their user name
			if (!Player.userNameIsTaken(message)){
				player.setUserName(message);
				player.getState().change(PlayerState.NEW);
			} else {
				player.getState().change(PlayerState.NAME_TAKEN);
			}
			break;
		case NAME_TAKEN:
			//Player is entering a different user name
			if (!Player.userNameIsTaken(message)){
				player.setUserName(message);
				player.getState().change(PlayerState.NEW);
			}
			break;
		default:
			inputIsArbitrary = false;			
			break;
		}
	}
	
	public boolean isInputArbitrary() {
		return inputIsArbitrary;
	}

}
