package test.sqlitedb;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Test;

import sqlitedb.Row;

public class RowTest {

	@Test
	public void testValidRow() {
		Row row = new Row();
		
		assertTrue("Row should be empty", row.isEmpty());	
		
		ArrayList<String> columnNames = new ArrayList<String>();
		ArrayList<Object> values = new ArrayList<Object>();
		row = new Row(columnNames, values);
		
		assertTrue("Row initialized with empty lists should be empty", row.isEmpty());
		
		columnNames.add("id");
		columnNames.add("name");
		values.add(1);
		values.add("test");
		row = new Row(columnNames, values);
		
		assertFalse("Row should not be empty", row.isEmpty());
		assertEquals("Row should have a size of 2", row.size(), 2);
		assertEquals("Row should have correct data id => 1", row.getValue("id"), 1);
		assertEquals("Row should have correct data name => 'test'", row.getValue("name"), "test");
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testInvalidRow() {
		ArrayList<String> columnNames = new ArrayList<String>();
		ArrayList<Object> values = new ArrayList<Object>();
		columnNames.add("id");
		columnNames.add("name");
		values.add("1");
		
		new Row(columnNames, values);
	}
	
	@Test(expected=IndexOutOfBoundsException.class)
	public void testInvalidIndex() {
		Row row = new Row();
		row.getValue("invalid");
	}

	@Test
	public void testAddValue() {
		Row row = new Row();
		
		assertTrue("Row should be empty", row.isEmpty());
		
		row.addValue("id", 1);
		
		assertFalse("Row should not be empty", row.isEmpty());
		assertEquals("Row should have size of 1", row.size(), 1);
		assertEquals("Row should have correct data", row.getValue("id"), 1);
	}

}
