package sqlitedb;

import java.util.ArrayList;


public final class TableDefinition {
	
	private String name;
	private ArrayList<ColumnDefinition> columnDefinitions;
	
	public TableDefinition (String name, ColumnDefinition[] columnDefinitions) throws IllegalArgumentException {
		if (columnDefinitions.length == 0) {
			throw new IllegalArgumentException("No columns were defined for the table");
		}
		
		this.name = name;
		
		this.columnDefinitions = new ArrayList<ColumnDefinition>();
		for (int i = 0; i < columnDefinitions.length; i++) {
			this.columnDefinitions.add(columnDefinitions[i]);
		}
	}
	
	public String getName() {
		return name;
	}
	
	public ArrayList<ColumnDefinition> getColumnDefinitions() {
		return columnDefinitions;
	}

}
