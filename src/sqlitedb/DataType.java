package sqlitedb;

public enum DataType {
	NULL("null"),
	INTEGER("integer"),
	REAL("real"),
	TEXT("text"),
	BLOB("blob");
	
	private String stringValue;
	
	DataType(String stringValue){
		this.stringValue = stringValue;
	}
	
	public String toString() {
		return stringValue;
	}
}
