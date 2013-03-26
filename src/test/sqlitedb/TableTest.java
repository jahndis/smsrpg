package test.sqlitedb;

import static org.junit.Assert.*;

import java.util.ArrayList;

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
import sqlitedb.Row;
import sqlitedb.SQLiteDatabase;
import sqlitedb.Table;
import sqlitedb.TableDefinition;

public class TableTest {
	
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
	public void testCreateAndDrop() {
		assertFalse("Database should not have test table before create", SQLiteDatabase.hasTable("test"));
		
		Table testTable = new Table(TEST_TABLE);
		testTable.create();
		
		assertTrue("Database should have test table after create", SQLiteDatabase.hasTable("test"));
		
		testTable.drop();
		
		assertFalse("Database should not have test table after drop", SQLiteDatabase.hasTable("test"));
	}
	
	@Test
	public void testInsert() {
		Table testTable = new Table(TEST_TABLE);
		testTable.create();
		
		DatabaseResult result = SQLiteDatabase.executeQuery("SELECT * FROM test");
		
		assertTrue("Table should be empty", result.isEmpty());
		
		Row row = new Row();
		row.addValue("id", 1);
		row.addValue("name", "Test");
		
		testTable.insert(row);
		
		result = SQLiteDatabase.executeQuery("SELECT * FROM test");
		
		assertFalse("Table should not be empty", result.isEmpty());
		assertEquals("Table should have one row", result.numRows(), 1);
		assertEquals("Table should have an id column in the first row with value of 1", result.getRow(0).getValue("id"), 1);
		assertEquals("Table should have a row in the name column with value of 'Test'", result.getColumn("name").getValue(0), "Test");
	}
	
	@Test
	public void testInsertWithManyRows() {
		Table testTable = new Table(TEST_TABLE);
		testTable.create();
		
		ArrayList<Row> rows = new ArrayList<Row>();
		Row row = new Row();
		row.addValue("id", 1);
		row.addValue("name", "Test1");
		rows.add(row);
		row = new Row();
		row.addValue("id", 2);
		row.addValue("name", "Test2");
		rows.add(row);
		
		testTable.insert(rows);
		
		DatabaseResult result = SQLiteDatabase.executeQuery("SELECT * FROM test");
		
		assertFalse("Table should not be empty", result.isEmpty());
		assertEquals("Table should have an id column in the first row with value of 1", result.getRow(0).getValue("id"), 1);
		assertEquals("Table should have a row in the name column with value of 'Test1'", result.getColumn("name").getValue(0), "Test1");
		assertEquals("Table should have an id column in the second row with value of 2", result.getRow(1).getValue("id"), 2);
		assertEquals("Table should have a row in the name column with value of 'Test2'", result.getColumn("name").getValue(1), "Test2");
		assertEquals("Table should have two rows", result.numRows(), 2);
	}

}
