package test;

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

}
