package smsrpg;

import java.io.IOException;
import java.util.Collection;

import log.Log;

import com.techventus.server.voice.Voice;
import com.techventus.server.voice.datatypes.records.SMSThread;

public class SMSListener {
	
	private final static int MIN_SLEEP_TIME = 5;
	private final static int MAX_SLEEP_TIME = 25;

	public static Collection<SMSThread> listen(Voice voice) {
		boolean listening = true;
		int sleepTime = MIN_SLEEP_TIME;
		
		while (listening) { 				
			try {
				//Need to check if there are messages in inbox otherwise getInbox might throw an error
				if (thereAreMessagesInInbox(voice)) {
					sleepTime = MIN_SLEEP_TIME;
				
					return voice.getSMSThreads(voice.getInbox());
					
				} else {
					Log.log("No New Messages");
					
					if (sleepTime < MAX_SLEEP_TIME) {
						sleepTime++;
					}
				}
			} catch (IOException e) {
				Log.error("There was a problem accessing the inbox.");
				listening = false;
			}
			
			//Sleep for an amount of time
			try {
				Thread.sleep(sleepTime * 1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
		}
		
		return null;
	}
	
	
	public static boolean thereAreMessagesInInbox(Voice voice) throws IOException {
		return voice.getInbox().contains("gc-message-sms-text");
	}

}
