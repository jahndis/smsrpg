package sqlitedb;

public final class ColumnDefinition {
	
	private String name;
	private DataType type;
	private ColumnRule[] rules;
	
	public ColumnDefinition(String name, DataType type, ColumnRule[] rules) {
		this.name = name;
		this.type = type;
		this.rules = rules;
	}
	
	public ColumnDefinition(String name, DataType type) {
		this(name, type, new ColumnRule[] { });
	}
	
	public String getName() {
		return name;
	}
	
	public String getTypeAsString() {
		return type.toString();
	}
	
	public String getRulesAsString() {
		StringBuilder builder = new StringBuilder();
		
		if (rules.length > 0) {
			for (ColumnRule rule : rules) {
				builder.append(rule.toString());
				builder.append(" ");
			}
			//Get rid of the extra space
			builder.deleteCharAt(builder.length() - 1);
		}
		
		return builder.toString();
	}

}
