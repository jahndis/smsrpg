package test.smsrpg;

import static org.junit.Assert.*;
import log.Log;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.techventus.server.voice.datatypes.Contact;

import smsrpg.Player;
import smsrpg.PlayerState;
import sqlitedb.DatabaseResult;
import sqlitedb.SQLiteDatabase;
import test.sqlitedb.TestUtils;

public class PlayerTest {
	
	private static final String DB_NAME = "test.db";
	private static final String DB_LOCATION = "db";
	
	@BeforeClass
	public static void setUp() {
		Log.showAll(true);
		
		TestUtils.deleteFile(DB_LOCATION + "/" + DB_NAME);
		
		SQLiteDatabase.init(DB_NAME, DB_LOCATION);
	}
	
	@Before
	public void before() {
		SQLiteDatabase.openConnection();
		SQLiteDatabase.dropAllTables();
		SQLiteDatabase.closeConnection();
	}
	
	@After
	public void after() {
		SQLiteDatabase.closeConnection();
	}
	
	@AfterClass
	public static void tearDown() {
		TestUtils.deleteFile(DB_LOCATION + "/" + DB_NAME);
	}

	@Test
	public void testPlayer() {
		SQLiteDatabase.openConnection();
		assertFalse("Players table should not exist", SQLiteDatabase.hasTable("players"));
		SQLiteDatabase.closeConnection();
		
		String playerNumber = "+15555555555";
		Contact contact = new Contact("name", "id", playerNumber, null);
		Player player = new Player(contact);

		SQLiteDatabase.openConnection();
		assertEquals("Player should have number '" + playerNumber + "'", player.getNumber(), playerNumber);
		assertNotNull("Player's user name should not be null", player.getUserName());
		assertEquals("Players current state should be NOT REGISTERED", player.getState().getCurrent(), PlayerState.NOT_REGISTERED);
		assertNull("Player's location should be null", player.getLocation());
		assertTrue("Players table should be created", SQLiteDatabase.hasTable("players"));
		SQLiteDatabase.closeConnection();
	}

	@Test
	public void testRegister() {
		String playerNumber = "+15555555555";
		Contact contact = new Contact("name", "id", playerNumber, null);
		Player player = new Player(contact);
		
		SQLiteDatabase.openConnection();
		DatabaseResult result = SQLiteDatabase.executeQuery("SELECT * FROM players");
		SQLiteDatabase.closeConnection();
		
		assertTrue("Players table should be empty", result.isEmpty());
		
		player.register();
		
		SQLiteDatabase.openConnection();
		result = SQLiteDatabase.executeQuery("SELECT * FROM players");
		SQLiteDatabase.closeConnection();
		
		assertFalse("Players table should not be empty", result.isEmpty());
		assertEquals("Players table should have one entry", result.numRows(), 1);
		assertEquals("Player in database should have correct number", result.getRow(0).getValue("phone_number"), player.getNumber());
	}

	@Test
	public void testUnregister() {
		String playerNumber = "+15555555555";
		Contact contact = new Contact("name", "id", playerNumber, null);
		Player player = new Player(contact);
		
		player.register();
		player.unregister();
		
		SQLiteDatabase.openConnection();
		DatabaseResult result = SQLiteDatabase.executeQuery("SELECT * FROM players");
		SQLiteDatabase.closeConnection();
		
		assertTrue("Players table should be empty", result.isEmpty());
	}

	@Test
	public void testUserNameIsTaken() {
		String userName = "user";
		Contact contact = new Contact("name", "id", "+15555555555", null);
		Player player = new Player(contact);
		
		assertFalse("User name 'user' should not be taken", Player.userNameIsTaken(userName));
		
		player.setUserName(userName);
		player.register();
		
		SQLiteDatabase.openConnection();
		DatabaseResult result = SQLiteDatabase.executeQuery("SELECT * FROM players");
		SQLiteDatabase.closeConnection();
		
		assertEquals("Player should have user name 'user'", result.getRow(0).getValue("name"), userName);
		assertTrue("User name 'user' should be taken", Player.userNameIsTaken(userName));
	}

}
