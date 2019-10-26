package database;

public class DatabaseTable {
	
	private final String rootTableName;
	private final String[] subTableNames;
	
	public DatabaseTable(String rootTableName, String ... subTableNames) {
		this.rootTableName = rootTableName;
		this.subTableNames = subTableNames;
	}
	
	public DatabaseTable subTable(String subTableName, String ... subSubTableNames) {
		String[] newSubTableNames = new String[subSubTableNames.length + subTableNames.length + 1];
		for(int i = 0; i < subTableNames.length; i++)
			newSubTableNames[i] = subTableNames[i];
		newSubTableNames[subTableNames.length] = subTableName;
		for(int i = subTableNames.length + 1; i < newSubTableNames.length; i++)
			newSubTableNames[i] = subTableNames[i];
		return new DatabaseTable(rootTableName, newSubTableNames);
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
