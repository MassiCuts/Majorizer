package database;

public class DatabaseTable {
	
	private final String rootTableName;
	private final String[] subTableNames;
	
	public DatabaseTable(String rootTableName, String ... subTableNames) {
		this.rootTableName = rootTableName;
		this.subTableNames = subTableNames;
	}
	
	public String getRootTableName() {
		return rootTableName;
	}
	
	public String[] getSubTableNames() {
		return subTableNames;
	}
	
	public String getFullString(String separator) {
		String full = rootTableName;
		for(int i = 0; i < subTableNames.length; i++)
			full += separator + subTableNames[i];
		return full;
	}
}
