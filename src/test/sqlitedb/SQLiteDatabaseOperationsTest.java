package test.sqlitedb;

import static org.junit.Assert.*;

import log.Log;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import sqlitedb.ColumnDefinition;
import sqlitedb.ColumnRule;
import sqlitedb.DataType;
import sqlitedb.DatabaseResult;
import sqlitedb.SQLiteDatabase;
import sqlitedb.TableDefinition;


public class SQLiteDatabaseOperationsTest {
	
	private static final String DB_NAME = "test.db";
	private static final String DB_LOCATION = "db";
	
	private static final TableDefinition TEST_TABLE = new TableDefinition(
			"test", 
			new ColumnDefinition[] {
					new ColumnDefinition("id", DataType.INTEGER, new ColumnRule[] { ColumnRule.PRIMARY_KEY, ColumnRule.NOT_NULL, ColumnRule.UNIQUE }),
					new ColumnDefinition("name", DataType.BLOB)
			});
	
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
	public void testHasTable() {
		assertFalse("Database should not have test table", SQLiteDatabase.hasTable("test"));
		
		SQLiteDatabase.executeUpdate("CREATE TABLE test (id INTEGER)");
		
		assertTrue("Database should have test table", SQLiteDatabase.hasTable("test"));
	}
	
	@Test
	public void testHasTableWithoutConnection() {
		SQLiteDatabase.closeConnection();
		
		assertFalse("Has connection should be false without connection", SQLiteDatabase.hasTable("test"));
	}

	@Test
	public void testDropAllTables() {
		SQLiteDatabase.executeUpdate("CREATE TABLE test (id INTEGER)");
		
		assertTrue("Database should have test table", SQLiteDatabase.hasTable("test"));
		
		SQLiteDatabase.dropAllTables();
		
		assertFalse("Database should not have test table", SQLiteDatabase.hasTable("test"));
	}
	
	@Test
	public void testDropAllTablesWithoutConnection() {
		SQLiteDatabase.executeUpdate("CREATE TABLE test (id INTEGER)");
		
		SQLiteDatabase.closeConnection();
		SQLiteDatabase.dropAllTables();
		SQLiteDatabase.openConnection();
		
		assertTrue("Database should still have test table", SQLiteDatabase.hasTable("test"));
	}
	
	@Test
	public void testCreateAndDropTable() {
		assertFalse("Database should not have test table", SQLiteDatabase.hasTable("test"));
		
		SQLiteDatabase.createTable(TEST_TABLE);
		
		assertTrue("Database should have test table", SQLiteDatabase.hasTable("test"));
		
		DatabaseResult result = SQLiteDatabase.executeQuery("SELECT * FROM test");
		
		assertTrue("Table should be empty", result.isEmpty());
		
		SQLiteDatabase.dropTable(TEST_TABLE);
		
		assertFalse("Database should have dropped test table", SQLiteDatabase.hasTable("test"));
	}

	@Test
	public void testExecuteUpdateAndQuery() {
		SQLiteDatabase.createTable(TEST_TABLE);
		
		DatabaseResult result = SQLiteDatabase.executeQuery("SELECT * FROM test");
		
		assertTrue("Table should not have data", result.isEmpty());
		
		SQLiteDatabase.executeUpdate("INSERT INTO test (id, name) VALUES(?, ?)", new Object[] { 1, "Test" });
		
		result = SQLiteDatabase.executeQuery("SELECT * FROM test");
		
		assertFalse("Table should have data", result.isEmpty());
		
		result = SQLiteDatabase.executeQuery("SELECT * FROM test WHERE id = ? AND name = ?", new Object[] { 1, "Test" });
		
		assertEquals("Table should have an id column in the first row with value of 1", result.getRow(0).getValue("id"), 1);
		assertEquals("Table should have a row in the name column with value of 'Test'", result.getColumn("name").getValue(0), "Test");
		assertEquals("Table should have only one row", result.numRows(), 1);
	}

}
