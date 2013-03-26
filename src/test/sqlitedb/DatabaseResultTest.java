package test.sqlitedb;

import static org.junit.Assert.*;

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

public class DatabaseResultTest {
	
	private static final String DB_NAME = "test.db";
	private static final String DB_LOCATION = "db";
	
	private static final TableDefinition TEST_TABLE = new TableDefinition(
			"test",
			new ColumnDefinition[] {
					new ColumnDefinition("id", DataType.INTEGER, new ColumnRule[] { ColumnRule.PRIMARY_KEY, ColumnRule.NOT_NULL, ColumnRule.UNIQUE }),
					new ColumnDefinition("name", DataType.BLOB, new ColumnRule[] { ColumnRule.NOT_NULL }),
					new ColumnDefinition("weight", DataType.REAL, new ColumnRule[] { ColumnRule.NOT_NULL }),
					new ColumnDefinition("about", DataType.TEXT, new ColumnRule[] { })
			});
	
	@BeforeClass
	public static void setUp() {
		TestUtils.deleteFile(DB_LOCATION + "/" + DB_NAME);
		SQLiteDatabase.init(DB_NAME, DB_LOCATION);
		populateDatabase();
	}
	
	@Before
	public void before() {
		SQLiteDatabase.openConnection();
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
	public void testDatabaseResult() {
		DatabaseResult result = SQLiteDatabase.executeQuery("SELECT * FROM test");
		
		assertFalse("Result should not be empty", result.isEmpty());
		assertEquals("Result should have 4 columns", result.numColumns(), 4);
		assertEquals("Result should have 10 rows", result.numRows(), 10);
	}

	@Test
	public void testGetColumn() {
		DatabaseResult result = SQLiteDatabase.executeQuery("SELECT * FROM test");
		
		assertEquals("First value in id column should be 0", result.getColumn("id").getValue(0), 0);
		assertEquals("First value in name column should be 'Test0'", result.getColumn("name").getValue(0), "Test0");
		assertEquals("First value in weight column should be 0.0", result.getColumn("weight").getValue(0), 0.0);
		assertEquals("First value in about column should be 'About Test0'", result.getColumn("about").getValue(0), "About Test0");
		
		assertEquals("Tenth value in id column should be 9", result.getColumn("id").getValue(9), 9);
		assertEquals("Tenth value in name column should be 'Test9'", result.getColumn("name").getValue(9), "Test9");
		assertEquals("Tenth value in weight column should be 9.9", result.getColumn("weight").getValue(9), 9.9);
		assertEquals("Tenth value in about column should be 'About Test9'", result.getColumn("about").getValue(9), "About Test9");
	}

	@Test
	public void testGetRow() {
		DatabaseResult result = SQLiteDatabase.executeQuery("SELECT * FROM test");
		
		assertEquals("First row in result should have id => 0", result.getRow(0).getValue("id"), 0);
		assertEquals("First row in result should have name => 'Test0'", result.getRow(0).getValue("name"), "Test0");
		assertEquals("First row in result should have weight => 0", result.getRow(0).getValue("weight"), 0.0);
		assertEquals("First row in result should have about => 'About Test0'", result.getRow(0).getValue("about"), "About Test0");
		
		assertEquals("Tenth row in result should have id => 9", result.getRow(9).getValue("id"), 9);
		assertEquals("Tenth row in result should have name => 'Test9'", result.getRow(9).getValue("name"), "Test9");
		assertEquals("Tenth row in result should have weight => 9.9", result.getRow(9).getValue("weight"), 9.9);
		assertEquals("Tenth row in result should have about => 'About Test9'", result.getRow(9).getValue("about"), "About Test9");
	}
	
	
	/* Helpers */
	
	private static void populateDatabase() {
		SQLiteDatabase.openConnection();
		
		SQLiteDatabase.createTable(TEST_TABLE);
		
		String statement;
		Object[] values;
		for (int i = 0; i < 10; i++) {
			statement = "INSERT INTO test (id, name, weight, about) VALUES (?, ?, ?, ?)";
			values = new Object[] { i, "Test" + String.valueOf(i), 1.1 * i, "About Test" + String.valueOf(i) };
			SQLiteDatabase.executeUpdate(statement, values);
		}
		
		SQLiteDatabase.closeConnection();
	}

}
