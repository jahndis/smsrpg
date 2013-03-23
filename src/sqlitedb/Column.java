package sqlitedb;

public class Column {
	
	private String name;
	private DataType type;
	private ColumnRule[] rules;
	
	public Column(String name, DataType type, ColumnRule[] rules) {
		this.name = name;
		this.type = type;
		this.rules = rules;
	}
	
	public String getName() {
		return name;
	}
	
	public String getTypeAsString() {
		return type.toString();
	}
	
	public String getRulesAsString() {
		StringBuilder builder = new StringBuilder();
		
		for (ColumnRule rule : rules) {
			builder.append(rule.toString());
			builder.append(" ");
		}
		//Get rid of the extra space
		builder.deleteCharAt(builder.length() - 1);
		
		return builder.toString();
	}

}
