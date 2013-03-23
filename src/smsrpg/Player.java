package smsrpg;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Stack;
import java.util.UUID;

import log.Log;


import com.techventus.server.voice.datatypes.Contact;

import db.SQLiteDatabase;

public class Player {
	
	private Contact contact;
	
	private String userName;
	private Stack<PlayerState> state;
	private Location location;
	
	public Player(Contact contact) {
		this.contact = contact;
		
		if (!playersTableIsCreated()) {
			createPlayerTableInDatabase();
		}
		
		if (isRegistered()) {
			loadInfo();
		} else {
			this.userName = UUID.randomUUID().toString();
			this.state = new Stack<PlayerState>();
			this.state.push(PlayerState.NOT_REGISTERED);
			this.location = null;
		}
	}
	
	/*
	 * Figure out, based on the player's state, what the action specified does.
	 * Then, change the player's state to reflect the changes that were made.
	 */
	public void performAction(String message, World world) {
		if (state.isEmpty()) {
			Log.error("The player stack is empty.");
			throw new IllegalStateException();
		}
		
		//Need to process arbitrary input first to make sure the arbitrary input
		//does not coincide with any of the key words
		boolean arbitraryInputProcessed = true;
		switch (state.peek()) {
		case JOINING:
			//Player is entering their user name
			if (!userNameIsTaken(message)){
				userName = message;
				state.pop();
				state.push(PlayerState.NEW);
			} else {
				state.pop();
				state.push(PlayerState.NAME_TAKEN);
			}
			break;
		case NAME_TAKEN:
			//Player is entering a different user name
			if (!userNameIsTaken(message)){
				userName = message;
				state.pop();
				state.push(PlayerState.NEW);
			}
			break;
		default:
			arbitraryInputProcessed = false;
			break;
		}
		
		//Process normal key words
		if (!arbitraryInputProcessed) {
			switch (message.toUpperCase()) {
			case "JOIN":
				switch (state.peek()) {
					case NOT_REGISTERED:
						state.pop();
						state.push(PlayerState.JOINING);
						register();
						break;
					default:
						break;
				}
				break;
			case "LEAVE":
				state.push(PlayerState.LEAVING);
				break;
			case "YES":
				switch (state.peek()) {
				case LEAVING:
					state.pop();
					state.push(PlayerState.LEFT);
					unregister();
					break;
				default:
					Log.log("User responded 'Yes' to nothing. Just leave state same and resend.");
					break;
				}
				break;
			case "NO":
				switch (state.peek()) {
				case LEAVING:
					state.pop();
					break;
				default:
					Log.log("User responded 'Yes' to nothing. Just leave state same and resend.");
					break;
				}
			default:
				Log.log("Invalid user input. Just leave state same and resend.");
				break;
			}
		}
		
		if (state.peek() != PlayerState.NOT_REGISTERED) {
			saveInfo();
		}
	}
	
	public void register() {
		Log.log("Registering player");
		if (!playersTableIsCreated()) {
			createPlayerTableInDatabase();
		}
		
		SQLiteDatabase.openConnection();
		SQLiteDatabase.executeUpdate("insert into players (id, name, phone_number, state) values ('" + UUID.randomUUID().toString() + "','" + userName + "','" + getNumber() + "','" + stateStackToStateText(state) + "');");
		SQLiteDatabase.closeConnection();
	}
	
	public void unregister() {
		Log.log("Unregistering player " + userName);
		if (!playersTableIsCreated()) {
			createPlayerTableInDatabase();
		}
		
		SQLiteDatabase.openConnection();
		SQLiteDatabase.executeUpdate("delete from players where phone_number = '" + getNumber() + "';");
		SQLiteDatabase.closeConnection();
	}
	
	public static boolean playersTableIsCreated() {
		boolean tableCreated = false;
		
		SQLiteDatabase.openConnection();
		tableCreated = SQLiteDatabase.hasTable("players");
		SQLiteDatabase.closeConnection();
		
		return tableCreated;
	}
	
	public static void createPlayerTableInDatabase() {
		String[] columnNames = {"id", "name", "phone_number", "state"};
		String[] columnTypes = {"blob", "blob", "blob", "blob"};
		String[] columnRules = {"primary key not null unique", "not null unique", "not null unique", "not null"};
		
		SQLiteDatabase.openConnection();
		SQLiteDatabase.createTable("players", columnNames, columnTypes, columnRules);
		SQLiteDatabase.closeConnection();
	}
	
	public void loadInfo() {
		Log.log("Loading " + userName + "'s info");
		
		SQLiteDatabase.openConnection();
		
		ResultSet rs = SQLiteDatabase.executeQuery("select name, state from players where phone_number = '" + getNumber() + "'");
		try {
			if (rs.next()) {
				this.userName = rs.getString("name");
				this.state = stateTextToStateStack(rs.getString("state"));
			}
			rs.close();
		} catch (SQLException e) {
			Log.error("Error loading player information.");
			e.printStackTrace();
		}
		
		SQLiteDatabase.closeConnection();
	}
	
	public void saveInfo() {
		Log.log("Saving player info: " + userName + " " + stateStackToStateText(state) + " " + getNumber());
		
		SQLiteDatabase.openConnection();
		SQLiteDatabase.executeUpdate("update players set name='" + userName + "', state='" + stateStackToStateText(state) + "' where phone_number='" + getNumber() + "';");
		SQLiteDatabase.closeConnection();
	}
	
	public boolean isRegistered() {
		boolean registered = false;
		
		SQLiteDatabase.openConnection();
		
		ResultSet rs = SQLiteDatabase.executeQuery("select phone_number from players where phone_number = '" + getNumber() + "'");
		try {
			if (rs.next()) {
				registered = true;
			}
			rs.close();
		} catch (SQLException e) {
			Log.error("Error checking player registration.");
			e.printStackTrace();
		}
		
		SQLiteDatabase.closeConnection();
		
		return registered;
	}
	
	public static boolean userNameIsTaken(String name) {
		boolean nameTaken = false;
		
		SQLiteDatabase.openConnection();
		
		ResultSet rs = SQLiteDatabase.executeQuery("select name from players where name = '" + name + "'");
		try {
			if (rs.next()) {
				nameTaken = true;
			}
			rs.close();
		} catch (SQLException e) {
			Log.error("Error checking if user name is taken.");
			e.printStackTrace();
		}
		
		SQLiteDatabase.closeConnection();
		
		return nameTaken;
	}
	
	public Stack<PlayerState> stateTextToStateStack(String stateText) {
		Stack<PlayerState> stateStack = new Stack<PlayerState>();
		
		String[] states = stateText.split(">");
		
		for (String state : states) {
			stateStack.push(PlayerState.valueOf(state));
		}
		
		return stateStack;
	}
	
	public String stateStackToStateText(Stack<PlayerState> inputStack) {
		@SuppressWarnings("unchecked")
		Stack<PlayerState> stateStack = (Stack<PlayerState>) inputStack.clone();
		StringBuilder stateText = new StringBuilder();
		
		while (!stateStack.isEmpty()) {
			//Insert the delimiter only if this is not the first state entered
			if (stateText.length() > 0) {
				stateText.insert(0, ">");
			}
			
			stateText.insert(0, stateStack.pop().toString());
		}

		return stateText.toString();
	}
	
	public Contact getContact() {
		return contact;
	}
	
	public String getNumber() {
		return contact.getNumber();
	}
	
	public String getOptionsString() {
		return "Choose an action:\n";
	}
	
	public String getUserName() {
		return userName;
	}
	
	public Stack<PlayerState> getState() {
		return state;
	}
	
	public Location getLocation() {
		return location;
	}
	
	public void setLocation(Location location) {
		this.location = location;
	}

}
