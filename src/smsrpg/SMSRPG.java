package smsrpg;

import java.io.IOException;
import java.util.Collection;

import sqlitedb.SQLiteDatabase;

import log.Log;


import com.techventus.server.voice.Voice;
import com.techventus.server.voice.datatypes.records.SMS;
import com.techventus.server.voice.datatypes.records.SMSThread;


public class SMSRPG {

	public static void main(String[] args) {
		// Set up logger
		Log.showAll(true);
		
		// Take out reset database
		SQLiteDatabase.init("smsrpg.db", "db");
		SQLiteDatabase.openConnection();
		SQLiteDatabase.dropAllTables();
		SQLiteDatabase.closeConnection();
		
		// Get the user name and password
		String username = "";
		String password = "";

		if (args.length == 2) {
			username = args[0];
			password = args[1];
		} else {
			// username = JOptionPane.showInputDialog(null, "Please enter your username:");
			username = "ravensilentstar";
			// password = JOptionPane.showInputDialog(null, "Please enter your password:");
			password = "Sup3rN0vA";
		}
		
		Voice voice = loginToVoice(username, password);
			
		Collection<SMSThread> threads;
		Player player;
		
		boolean running = true;
		
		while (running) {
			// Wait for some messages
			threads = SMSListener.listen(voice);
			
			// Process the messages
			for (SMSThread thread : threads) {
				player = new Player(thread.getContact());
				
				Collection<SMS> smses = thread.getAllSMS();
				for (SMS sms : smses) {
					String response = SMSProcessor.processMessage(player, sms.getContent());
					SMSResponder.send(voice, response, player);
				}
				
				// Delete the messages
				try {
					voice.deleteMessage(thread.getId());
				} catch (IOException e) {
					Log.error("There was a problem while deleting the messages. SMSRPG is stopping...");
					running = false;
				}
			}
		}

		System.exit(0);
	}
	
	public static Voice loginToVoice(String username, String password) {
		Voice voice = null;
		
		try {
			voice = new Voice(username, password);
		} catch (IOException e) {
			Log.log("Invalid Login.");
		}
		
		if (voice == null) {
			Log.error("Voice was unable to be created. Exiting...");
			System.exit(1);
		}
		
		return voice;
	}

}
