package smsrpg;

import java.io.IOException;

import log.Log;

import com.techventus.server.voice.Voice;

public class SMSResponder {
	
	public static void send(Voice voice, String response, Player player) {
		try {
			Log.log("RESPONDING: " + response);
			
			voice.sendSMS(player.getContact().getNumber(), response);
			
		} catch (IOException e) {
			Log.error("Error sending response to player");
		}
	}

}
