package test.sqlitedb;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import sqlitedb.ColumnDefinition;
import sqlitedb.ColumnRule;
import sqlitedb.DataType;
import sqlitedb.Row;
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
		fail("Not yet implemented");
	}

	@Test
	public void testGetColumn() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetRow() {
		fail("Not yet implemented");
	}
	
	
	/* Helpers */
	
	private static void populateDatabase() {
		SQLiteDatabase.openConnection();
		
		SQLiteDatabase.createTable(TEST_TABLE);
		
		ArrayList<Row> rows = new ArrayList<Row>();
		Row row;
		for (int i = 0; i < 10; i++) {
			row = new Row();
			row.addValue("id", i);
			row.addValue("name", "test" + String.valueOf(i));
			rows.add(row);
		}
		
		SQLiteDatabase.insertIntoTable(TEST_TABLE, rows);
		
		SQLiteDatabase.closeConnection();
	}

}
