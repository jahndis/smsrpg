package sqlitedb;

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
}
