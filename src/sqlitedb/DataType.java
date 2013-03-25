package sqlitedb;

public enum DataType {
	NULL("NULL"),
	INTEGER("INTEGER"),
	REAL("REAL"),
	TEXT("TEXT"),
	BLOB("BLOB");
	
	private String stringValue;
	
	DataType(String stringValue){
		this.stringValue = stringValue;
	}
	
	public String toString() {
		return stringValue;
	}
}
