package smsrpg;

import java.util.UUID;

import sqlitedb.ColumnDefinition;
import sqlitedb.ColumnRule;
import sqlitedb.DataType;
import sqlitedb.DatabaseResult;
import sqlitedb.TableDefinition;
import sqlitedb.SQLiteDatabase;

import log.Log;

import com.techventus.server.voice.datatypes.Contact;


public class Player {

	private static final TableDefinition PLAYERS_TABLE = new TableDefinition(
			"players", 
			new ColumnDefinition[] {
					new ColumnDefinition("id", DataType.BLOB, new ColumnRule[] { ColumnRule.PRIMARY_KEY, ColumnRule.NOT_NULL, ColumnRule.UNIQUE }),
					new ColumnDefinition("name", DataType.BLOB, new ColumnRule[] { ColumnRule.NOT_NULL, ColumnRule.UNIQUE }),
					new ColumnDefinition("phone_number", DataType.BLOB, new ColumnRule[] { ColumnRule.NOT_NULL, ColumnRule.UNIQUE }),
					new ColumnDefinition("state", DataType.BLOB, new ColumnRule[] { ColumnRule.NOT_NULL })
			});
	
	private Contact contact;
	private String userName;
	private State<PlayerState> state;
	private Location location;
	
	public Player(Contact contact) {
		this.contact = contact;
		
		if (!playersTableIsCreated()) {
			createPlayerTableInDatabase();
		}
		
		if (isRegistered()) {
			loadInfo();
		} else {
			this.userName = generateRandomUserName();
//			this.state = new Stack<PlayerState>();
			this.state = new State<PlayerState>();
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
			throw new IllegalStateException("The player's state stack is empty.");
		}
		
		//Need to process arbitrary input first to make sure the arbitrary input
		//does not coincide with any of the key words
		boolean arbitraryInputProcessed = true;
		switch (state.getCurrent()) {
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
				switch (state.getCurrent()) {
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
				switch (state.getCurrent()) {
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
				switch (state.getCurrent()) {
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
		
		if (state.getCurrent() != PlayerState.NOT_REGISTERED) {
			saveInfo();
		}
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
	
	public State<PlayerState> getState() {
		return state;
	}
	
	public Location getLocation() {
		return location;
	}
	
	public void setLocation(Location location) {
		this.location = location;
	}
	
	
	/* Private methods */
	
	private void register() {
		Log.log("Registering player");
		if (!playersTableIsCreated()) {
			createPlayerTableInDatabase();
		}
		
		SQLiteDatabase.openConnection();
		String statement = "INSERT INTO players (id, name, phone_number, state) VALUES (?, ?, ?, ?)";
		Object[] values = { generateRandomID(), userName, getNumber(), state.toString() };
		SQLiteDatabase.executeUpdate(statement, values);
		SQLiteDatabase.closeConnection();
	}
	
	private void unregister() {
		Log.log("Unregistering player " + userName);
		if (!playersTableIsCreated()) {
			createPlayerTableInDatabase();
		}
		
		SQLiteDatabase.openConnection();
		String statement = "DELETE FROM players WHERE phone_number = ?";
		Object[] values = { getNumber() };
		SQLiteDatabase.executeUpdate(statement, values);
		SQLiteDatabase.closeConnection();
	}
	
	private boolean isRegistered() {
		boolean registered = false;
		
		SQLiteDatabase.openConnection();
		String statement = "SELECT phone_number FROM players WHERE phone_number = ?";
		Object[] values = { getNumber() };
		DatabaseResult result = SQLiteDatabase.executeQuery(statement, values);
		
		if (!result.isEmpty()) {
			registered = true;
		}
		SQLiteDatabase.closeConnection();
		
		return registered;
	}
	
	private static boolean playersTableIsCreated() {
		SQLiteDatabase.openConnection();
		boolean tableCreated = SQLiteDatabase.hasTable("players");
		SQLiteDatabase.closeConnection();
		
		return tableCreated;
	}
	
	private static void createPlayerTableInDatabase() {
		SQLiteDatabase.openConnection();
		SQLiteDatabase.createTable(PLAYERS_TABLE);
		SQLiteDatabase.closeConnection();
	}
	
	private void loadInfo() {
		Log.log("Loading " + userName + "'s info");
		
		SQLiteDatabase.openConnection();	
		String statement = "SELECT name, state FROM players WHERE phone_number = ?";
		Object[] values = { getNumber() };
		DatabaseResult result = SQLiteDatabase.executeQuery(statement, values);
		
		if (!result.isEmpty()) {
			userName = (String) result.getRow(0).getValue("name");
			state = new State<PlayerState>();
			state.parseString((String) result.getRow(0).getValue("state"), PlayerState.class);
		}	
		SQLiteDatabase.closeConnection();
	}
	
	private void saveInfo() {
		Log.log("Saving player info: " + userName + " " + state.toString() + " " + getNumber());
		
		SQLiteDatabase.openConnection();
		String statement = "UPDATE players SET name = ?, state = ? WHERE phone_number = ?";
		Object[] values = { userName, state.toString(), getNumber() };
		SQLiteDatabase.executeUpdate(statement, values);
		SQLiteDatabase.closeConnection();
	}
	
	private static String generateRandomID() {
		return UUID.randomUUID().toString();
	}
	
	private static String generateRandomUserName() {
		return UUID.randomUUID().toString();
	}
	
	private static boolean userNameIsTaken(String name) {
		boolean nameTaken = false;
		
		SQLiteDatabase.openConnection();
		String statement = "SELECT name FROM players WHERE name = ?";
		Object[] values = { name };
		DatabaseResult result = SQLiteDatabase.executeQuery(statement, values);
		
		if (!result.isEmpty()) {
			nameTaken = true;
		}
		SQLiteDatabase.closeConnection();
		
		return nameTaken;
	}

}
