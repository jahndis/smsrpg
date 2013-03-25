package sqlitedb;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import log.Log;

public class DatabaseResult {
	
	private HashMap<String, Column> columns;
	private ArrayList<Row> rows;
	
	public DatabaseResult(ResultSet rs) {
		columns = new HashMap<String, Column>();
		rows = new ArrayList<Row>();
		
		try {
		  ResultSetMetaData rsmd = rs.getMetaData();
		
			boolean columnsInitialized = false;
			while (rs.next()) {
				// Fill in the column names for the columns hash the first time
				if (!columnsInitialized) {
					for (int i = 0; i < rsmd.getColumnCount(); i++) {
						columns.put(rsmd.getColumnName(i+1), new Column(rsmd.getColumnName(i+1)));
					}
				}
				
				// Add the values to rows and columns
				ArrayList<String> columnNames = new ArrayList<String>();
				ArrayList<Object> values = new ArrayList<Object>();
				for (int i = 0; i < rsmd.getColumnCount(); i++) {
					// Fill in value for column
					columns.get(rsmd.getColumnName(i+1)).addValue(rs.getObject(i+1));
					
					// Fill in value for row
					columnNames.add(rsmd.getColumnName(i+1));
					values.add(rs.getObject(i+1));
				}
				
				// Create the row
				rows.add(new Row(columnNames, values));
			}
		} catch (SQLException e) {
			Log.error("Error parsing result set");
			e.printStackTrace();
		} finally {
      if (rs != null) {
      	try {
					rs.close();
				} catch (SQLException e) {
					Log.error("Error closing ResultSet");
					e.printStackTrace();
				}
      }
    }
	}
	
	public boolean isEmpty() {
		return columns.isEmpty() || rows.isEmpty();
	}
	
	public int size() {
		return rows.size();
	}
	
	public Column getColumn(String columnName) {
		if (!columns.keySet().contains(columnName)) {
			throw new IllegalArgumentException("Invalid column name");
		}
		
		return columns.get(columnName);
	}
	
	public Row getRow(int index) {
		if (index >= rows.size() || index < 0) {
			throw new IllegalArgumentException("Invalid index");
		}
		
		return rows.get(index);
	}
	
	public HashMap<String, Column> getColumns() {
		return columns;
	}
	
	public ArrayList<Row> getRows() {
		return rows;
	}

}
