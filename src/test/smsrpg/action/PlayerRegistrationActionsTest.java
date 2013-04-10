package test.smsrpg.action;

import static org.junit.Assert.*;
import log.Log;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import smsrpg.Player;
import smsrpg.PlayerState;
import sqlitedb.SQLiteDatabase;

import com.techventus.server.voice.datatypes.Contact;

public class PlayerRegistrationActionsTest {
	
	private Player player;
	
	@BeforeClass
	public static void setUp() {
		Log.showAll(true);
		
		SQLiteDatabase.init("test.db", "db");
	}
	
	@Before
	public void before() {
		player = new Player(new Contact("name", "id", "+15555555555", null));
	}
	
	@After
	public void tearDown() {
		SQLiteDatabase.openConnection();
		SQLiteDatabase.dropAllTables();
		SQLiteDatabase.closeConnection();
	}

	@Test
	public void testPerformJoinAction() {
		assertEquals("Player state should be NOT_REGISTERED", player.getState().getCurrent(), PlayerState.NOT_REGISTERED);
		
		player.performAction("JOIN");
		
		assertEquals("Player state should be JOINING", player.getState().getCurrent(), PlayerState.JOINING);
		
		player.performAction("New Name");
		
		assertEquals("Player's name should be 'New Name'", player.getUserName(), "New Name");
		assertEquals("Player state should be JOINING", player.getState().getCurrent(), PlayerState.NEW);
	}
	
	@Test
	public void testPerformLeaveAction() {
		player.performAction("JOIN");
		player.performAction("New Name");
		player.performAction("LEAVE");
		
		assertEquals("Player state should be LEAVING", player.getState().getCurrent(), PlayerState.LEAVING);
		
		player.performAction("NO");
		
		assertEquals("Player state should be NEW", player.getState().getCurrent(), PlayerState.NEW);
		
		player.performAction("LEAVE");
		player.performAction("YES");
		
		assertEquals("Player state should be LEFT", player.getState().getCurrent(), PlayerState.LEFT);
	}

}
