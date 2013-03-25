package test;

import static org.junit.Assert.*;

import org.junit.Test;

import sqlitedb.ColumnDefinition;
import sqlitedb.DataType;
import sqlitedb.TableDefinition;

public class TableDefinitionTest {

	@Test
	public void testValidTableDefinition() {
		TableDefinition tableDef = new TableDefinition(
				"test",
				new ColumnDefinition[] {
						new ColumnDefinition("id", DataType.INTEGER)
				});
		
		assertEquals("Table definition table name should be 'test'", tableDef.getName(), "test");
		assertEquals("Table definition should have one column definition", tableDef.getColumnDefinitions().size(), 1);
		assertEquals("Table definition should have a column definition with name 'id'", tableDef.getColumnDefinitions().get(0).getName(), "id");
		assertEquals("Table definition should have a column definition with type 'INTEGER'", tableDef.getColumnDefinitions().get(0).getTypeAsString(), DataType.INTEGER.toString());
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testInvalidTableDefinition() {
		new TableDefinition(
				"test",
				new ColumnDefinition[] { });
	}

}
