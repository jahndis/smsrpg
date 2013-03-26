package test.sqlitedb;

import static org.junit.Assert.*;

import java.io.File;

import log.Log;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import sqlitedb.SQLiteDatabase;

public class SQLiteDatabaseInitializationTest {
	
	private static final String DB_NAME = "test.db";
	private static final String DB_LOCATION = "db";
	
	@BeforeClass 
	public static void setUp() {
		Log.showAll(true);
	}
	
	@Before
	public void before() {
		TestUtils.deleteFile(DB_LOCATION + "/" + DB_NAME);
	}
	
	@After
	public void after() {
		TestUtils.deleteFile(DB_LOCATION + "/" + DB_NAME);
	}

	@Test
	public void testInit() {
		File file = new File(DB_LOCATION + "/" + DB_NAME);
		
		// Check that database file has not been created yet
		assertFalse("Database file should not exist", file.exists());
		
		SQLiteDatabase.init(DB_NAME, DB_LOCATION);
		
		// Check that database file has been created
		assertTrue("Database file should exist", file.exists());
	}

	@Test
	public void testConnecting() {
		SQLiteDatabase.init(DB_NAME, DB_LOCATION);	
		assertFalse("Database should not have connection after being initialized", SQLiteDatabase.isConnected());
		
		SQLiteDatabase.openConnection();
		assertTrue("Database should have a connection after opening a connection", SQLiteDatabase.isConnected());
		
		SQLiteDatabase.closeConnection();
		assertFalse("Database should not have a connection after closing a connection", SQLiteDatabase.isConnected());
	}

}
