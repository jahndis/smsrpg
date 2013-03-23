package sqlitedb;

public enum ColumnRule {
	PRIMARY_KEY("primary key"),
	NOT_NULL("not null"),
	UNIQUE("unique");
	
	private String stringValue;
	
	ColumnRule(String stringValue){
		this.stringValue = stringValue;
	}
	
	public String toString() {
		return stringValue;
	}
}