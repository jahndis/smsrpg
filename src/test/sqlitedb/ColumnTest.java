package test.sqlitedb;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Test;

import sqlitedb.Column;


public class ColumnTest {

	@Test
	public void testValidColumn() {
		Column column = new Column("test");
		
		assertTrue("Column should be empty", column.isEmpty());	
		
		String name = "test";
		ArrayList<Object> values = new ArrayList<Object>();
		column = new Column(name, values);
		
		assertTrue("Column initialized with empty list should be empty", column.isEmpty());
		
		name = "test";
		values.add(1);
		values.add("test");
		column = new Column(name, values);
		
		assertFalse("Column should not be empty", column.isEmpty());
		assertEquals("Column should have a size of 2", column.size(), 2);
		assertEquals("Column should have correct data at index 0 => 1", column.getValue(0), 1);
		assertEquals("Column should have correct data at index 1 => 'test'", column.getValue(1), "test");
	}
	
	@Test(expected=IndexOutOfBoundsException.class)
	public void testInvalidIndex() {
		Column column = new Column("test");
		column.getValue(0);
	}

	@Test
	public void testAddValue() {
		Column column = new Column("test");
		
		assertTrue("Column should be empty", column.isEmpty());
		
		column.addValue(1);
		column.addValue("test");
		
		assertFalse("Column should not be empty", column.isEmpty());
		assertEquals("Column should have size of 2", column.size(), 2);
		assertEquals("Column should have correct data at index 0 => 1", column.getValue(0), 1);
		assertEquals("Column should have correct data at index 1 => 'test'", column.getValue(1), "test");
	}

}
