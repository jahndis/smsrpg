package sqlitedb;

import java.util.ArrayList;
import java.util.List;


public final class Table {
	private TableDefinition tableDefinition;
	
	public Table(TableDefinition tableDefinition) {
		this.tableDefinition = tableDefinition;
	}
	
	public void create() {
		StringBuilder statement = new StringBuilder();
		
		statement.append("CREATE TABLE " + tableDefinition.getName());
		
		if (tableDefinition.getColumnDefinitions().size() > 0) {
			statement.append(" (");
			for (ColumnDefinition column :tableDefinition.getColumnDefinitions()) {
				statement.append(column.getName() + " " + column.getTypeAsString() + " " + column.getRulesAsString() + ",");
			}
			// Get rid of the extra comma
			statement.deleteCharAt(statement.length() - 1);
			statement.append(")");
		}

		SQLiteDatabase.executeUpdate(statement.toString());
	}
	
	public void drop() {
		String statement = "DROP TABLE IF EXISTS " + tableDefinition.getName();
		SQLiteDatabase.executeUpdate(statement);
	}
	
	public void insert(Row row) {
		ArrayList<String> columnNames = row.getColumnNames();
		
		StringBuilder statement = new StringBuilder();
		Object[] values = new Object[row.size()];
		
		statement.append("INSERT INTO " + tableDefinition.getName());
		statement.append(" (");
		
		// Specify column names
		for (String columnName : columnNames) {
			statement.append(columnName + ",");
		}
		statement.deleteCharAt(statement.length() - 1); // Get rid of the extra comma
		
		statement.append(") ");
		statement.append("VALUES");
		statement.append("(");
		
		// Specify the values
		for (int i = 0; i < row.size(); i++) {
			statement.append("?,");
			values[i] = row.getValue(columnNames.get(i));
		}
		statement.deleteCharAt(statement.length() - 1); // Get rid of the extra comma
		
		statement.append(")");
		
		SQLiteDatabase.executeUpdate(statement.toString(), values);
	}
	
	public void insert(List<Row> rows) {
		for (Row row : rows) {
			insert(row);
		}
	}
	
}
