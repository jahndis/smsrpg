package smsrpg;

import java.io.IOException;
import java.util.Collection;

import log.Log;


import com.techventus.server.voice.Voice;
import com.techventus.server.voice.datatypes.records.SMS;
import com.techventus.server.voice.datatypes.records.SMSThread;

import db.SQLiteDatabase;

public class SMSRPG {

	public static void main(String[] args) {
		//Set up logger
		Log.showLog(true);
		Log.showDebug(true);
		Log.showError(true);
		
		//Take out reset database
		SQLiteDatabase.init("smsrpg.db", "db");
		SQLiteDatabase.openConnection();
		SQLiteDatabase.dropAllTables();
		SQLiteDatabase.closeConnection();
		
		//System.exit(0);
		
		//Get the user name and password
		String username = "";
		String password = "";

		if (args.length == 2) {
			username = args[0];
			password = args[1];
		} else {
			//username = JOptionPane.showInputDialog(null, "Please enter your username:");
			username = "ravensilentstar";
			//password = JOptionPane.showInputDialog(null, "Please enter your password:");
			password = "Sup3rN0vA";
		}
		
		Voice voice = loginToVoice(username, password);
			
		Collection<SMSThread> threads;
		Player player;
		World world = createWorld();
		
		boolean running = true;
		
		while (running) {
			//Wait for some messages
			threads = SMSListener.listen(voice);
			
			//Process the messages
			for (SMSThread thread : threads) {
				player = new Player(thread.getContact());
				
				Collection<SMS> smses = thread.getAllSMS();
				for (SMS sms : smses) {
					String response = SMSProcessor.processMessage(player, world, sms.getContent());
					SMSResponder.send(voice, response, player);
				}
				
				//Delete the messages
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
	
	public static World createWorld() {
		World world = new World();
		
		//Create main locations
		world.addLocation("A");
		world.addLocation("B");
		world.addLocation("C");
		world.addLocation("D");
		world.addLocation("E");
		
		//Create path locations
		world.addLocation("a-b");
		world.addLocation("b-c");
		world.addLocation("c-d");
		world.addLocation("d-e");
		world.addLocation("e-a");
		world.addLocation("c-e");
		
		//Connect the locations
		world.connectLocationsWithLocation("a-b", "A", "B");
		world.connectLocationsWithLocation("b-c", "B", "C");
		world.connectLocationsWithLocation("c-d", "C", "D");
		world.connectLocationsWithLocation("d-e", "D", "E");
		world.connectLocationsWithLocation("e-a", "E", "A");
		world.connectLocationsWithLocation("c-e", "C", "E");
		
		return world;
	}

}
