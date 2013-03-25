package sqlitedb;

import java.util.ArrayList;

public class Column {
	private String name;
	private ArrayList<Object> values;
	
	public Column(String name) {
		this.name = name;
		values = new ArrayList<Object>();
	}
	
	public Column(String name, ArrayList<Object> values) {
		this.name = name;
		this.values = values;
	}
	
	public void addValue(Object value) {
		values.add(value);
	}
	
	public Object getValue(int index) {
		return values.get(index);
	}
	
	public String getName() {
		return name;
	}
	
	public ArrayList<Object> getValues() {
		return values;
	}
}
