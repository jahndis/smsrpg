package sqlitedb;

import java.util.ArrayList;


public class Column {
	private String name;
	private ArrayList<Object> values;
	
	public Column(String name, ArrayList<Object> values) {
		this.name = name;
		this.values = values;
	}
	
	public Column(String name) {
		this(name, new ArrayList<Object>());
	}
	
	public int size() {
		return values.size();
	}
	
	public boolean isEmpty() {
		return values.isEmpty();
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
	
	public String toString() {
		return super.toString() + " name:" + name + " values:" + values.toString();
	}
}
