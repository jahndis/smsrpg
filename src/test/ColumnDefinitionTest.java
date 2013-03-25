package test;

import static org.junit.Assert.*;

import org.junit.Test;

import sqlitedb.ColumnDefinition;
import sqlitedb.ColumnRule;
import sqlitedb.DataType;

public class ColumnDefinitionTest {

	@Test
	public void testGetTypeAsString() {
		ColumnDefinition colDef = new ColumnDefinition("id", DataType.INTEGER);
		
		assertEquals("Column definition type string should be 'INTEGER'", colDef.getTypeAsString(), DataType.INTEGER.toString());
	}

	@Test
	public void testGetRulesAsString() {
		ColumnDefinition colDef = new ColumnDefinition("id", DataType.INTEGER, new ColumnRule[] { ColumnRule.NOT_NULL, ColumnRule.PRIMARY_KEY, ColumnRule.UNIQUE });
		
		assertEquals("Column rules string should be 'NOT NULL PRIMARY KEY UNIQUE'", colDef.getRulesAsString(), "NOT NULL PRIMARY KEY UNIQUE");
	}

}
