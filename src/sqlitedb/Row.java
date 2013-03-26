package sqlitedb;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class Row {
	private HashMap<String, Object> values;
	
	public Row(List<String> columnNames, List<Object> values) throws IllegalArgumentException {
		if (columnNames.size() != values.size()) {
			throw new IllegalArgumentException("Column names and values sizes do not match");
		}
		
		this.values = new HashMap<String, Object>();
		
		for (int i = 0; i < columnNames.size(); i++) {
			this.values.put(columnNames.get(i), values.get(i));
		}
	}
	
	public Row() {
		this(new ArrayList<String>(), new ArrayList<Object>());
	}
	
	public int size() {
		return values.size();
	}
	
	public boolean isEmpty() {
		return values.isEmpty();
	}
	
	public ArrayList<String> getColumnNames() {
		return new ArrayList<String>(values.keySet());
	}
	
	public void addValue(String columnName, Object value) {
		values.put(columnName, value);
	}
	
	public Object getValue(String columnName) throws IndexOutOfBoundsException {
		Object value = values.get(columnName);

		if (value == null) {
			throw new IndexOutOfBoundsException("Invalid column name: " + columnName);
		}
		
		return value;
	}
	
	public HashMap<String, Object> getValues() {
		return values;
	}
	
	public String toString() {
		return super.toString() + " values:" + values.toString();
	}
}
