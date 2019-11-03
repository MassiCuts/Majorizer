package database;

public final class DatabaseColumn {
	
	public enum ColumnType {
		INT,FLOAT,BOOLEAN,STRING,CHAR;
		
		public static boolean checkType(String type, Object object) {
			if(INT.name().equals(type)) {
				return object instanceof Integer;
			} else if(FLOAT.name().equals(type)) {
				return object instanceof Float;
			} else if (BOOLEAN.name().equals(type)) {
				return object instanceof Boolean;
			} else if (STRING.name().equals(type)) {
				return object instanceof String;
			} else if(CHAR.name().equals(type)) {
				return object instanceof Character;
			} else {
				return false;
			}
		}
	}
	
	private final String name;
	private final ColumnType type;
	
	public DatabaseColumn(String name, ColumnType type) {
		this.name = name;
		this.type = type;
	}
	
	public boolean checkType(Object object) {
		switch(type) {
		case BOOLEAN:
			return object instanceof Boolean;
		case INT:
			return object instanceof Integer;
		case STRING:
			return object instanceof String;
		case CHAR:
			return object instanceof Character;
		case FLOAT:
			return object instanceof Float;
		default:
			break;
		}
		return false;
	}
	
	public String getName() {
		return name;
	}
	
	public ColumnType getType() {
		return type;
	}
}
