package smsrpg;

import java.util.UUID;

import smsrpg.action.ArbitraryAction;
import smsrpg.action.JoinAction;
import smsrpg.action.LeaveAction;
import smsrpg.action.NoAction;
import smsrpg.action.YesAction;
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
		
		// Need to process arbitrary input first to make sure the arbitrary input
		// does not coincide with any of the key words (i.e. the user chooses their
		// name to be a keyword)
		ArbitraryAction arbitraryAction = new ArbitraryAction(message);
		arbitraryAction.execute(this, world);
		
		//Process normal key words
		if (!arbitraryAction.isInputArbitrary()) {
			switch (message.toUpperCase()) {
			case "JOIN":
				new JoinAction().execute(this, world);
				break;
			case "LEAVE":
				new LeaveAction().execute(this, world);
				break;
			case "YES":
				new YesAction().execute(this, world);
				break;
			case "NO":
				new NoAction().execute(this, world);
			default:
				Log.log("Invalid user input. Just leave state same and resend.");
				break;
			}
		}
		
		// Save registered players
		if (isRegistered()) {
			saveInfo();
		}
	}
	
	public void register() {
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
	
	public void unregister() {
		Log.log("Unregistering player " + userName);
		
		SQLiteDatabase.openConnection();
		String statement = "DELETE FROM players WHERE phone_number = ?";
		Object[] values = { getNumber() };
		SQLiteDatabase.executeUpdate(statement, values);
		SQLiteDatabase.closeConnection();
	}
	
	public static boolean userNameIsTaken(String name) {
		SQLiteDatabase.openConnection();
		String statement = "SELECT name FROM players WHERE name = ?";
		Object[] values = { name };
		DatabaseResult result = SQLiteDatabase.executeQuery(statement, values);
		SQLiteDatabase.closeConnection();

		return !result.isEmpty();
	}
	
	public String getNumber() {
		return contact.getNumber();
	}
	
	public String getUserName() {
		return userName;
	}
	
	public void setUserName(String userName) {
		this.userName = userName;
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
	
	private boolean isRegistered() {
		SQLiteDatabase.openConnection();
		String statement = "SELECT phone_number FROM players WHERE phone_number = ?";
		Object[] values = { getNumber() };
		DatabaseResult result = SQLiteDatabase.executeQuery(statement, values);
		SQLiteDatabase.closeConnection();

		return !result.isEmpty();
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
		SQLiteDatabase.closeConnection();
		
		if (!result.isEmpty()) {
			userName = (String) result.getRow(0).getValue("name");
			state = new State<PlayerState>();
			state.parseString((String) result.getRow(0).getValue("state"), PlayerState.class);
		}	
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

}
