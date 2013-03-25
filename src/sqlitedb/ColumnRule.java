package sqlitedb;

public enum ColumnRule {
	PRIMARY_KEY("PRIMARY KEY"),
	NOT_NULL("NOT NULL"),
	UNIQUE("UNIQUE");
	
	private String stringValue;
	
	ColumnRule(String stringValue){
		this.stringValue = stringValue;
	}
	
	public String toString() {
		return stringValue;
	}
}