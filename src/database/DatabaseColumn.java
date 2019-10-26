package database;

public final class DatabaseColumn {
	
	public enum ColumnType {
		INT,BOOLEAN,STRING;
		
		public static boolean checkType(String type, Object object) {
			if(INT.name().equals(type)) {
				return object instanceof Integer;
			} else if (BOOLEAN.name().equals(type)) {
				return object instanceof Boolean;
			} else if (STRING.name().equals(type)) {
				return object instanceof String;
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
