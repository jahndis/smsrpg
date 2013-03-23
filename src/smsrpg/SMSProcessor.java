package smsrpg;

import log.Log;

public class SMSProcessor {
	
	public static String processMessage(Player player, World world, String message) {
		Log.log("PROCESSING: " + message + " from " + player.getContact().getNumber());
		
		player.performAction(message, world);
		
		String response = buildResponse(player, world);
		
		return response;
	}
	
	private static String buildResponse(Player player, World world) {
		//Based on the player's state, determine what options to display to the player
		//and return them in a formatted string.
		String message = "";
		
		switch (player.getState().peek()) {
		case NOT_REGISTERED:
			message = "Text JOIN to join the game!";
			break;
		case JOINING:
			message = "Please enter your user name:";
			break;
		case NAME_TAKEN:
			message = "Name taken. Please enter a different name:";
			break;
		case NEW:
			message = "Welcome " + player.getUserName();
			break;
		case LEAVING:
			message = "This will erase your progress. Are you sure you want to leave? [Yes|No]";
			break;
		case LEFT:
			message = "Your profile has been erased.";
			break;
		default:
			message = "Player has reached an invalid state";	
		}
		
		return message;
	}

}
