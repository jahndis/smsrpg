package sqlitedb;

import java.util.HashMap;
import java.util.List;

public class Row {
	private HashMap<String, Object> values;
	
	public Row() {
		this.values = new HashMap<String, Object>();
	}
	
	public Row(List<String> columnNames, List<Object> values) {
		if (columnNames.size() != values.size()) {
			throw new IllegalArgumentException("Column names and values sizes do not match");
		}
		
		this.values = new HashMap<String, Object>();
		
		for (int i = 0; i < columnNames.size(); i++) {
			this.values.put(columnNames.get(i), values.get(i));
		}
	}
	
	public void addValue(String columnName, Object value) {
		values.put(columnName, value);
	}
	
	public Object getValue(String columnName) {
		return values.get(columnName);
	}
	
	public HashMap<String, Object> getValues() {
		return values;
	}
}
