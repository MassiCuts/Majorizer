package database;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.net.URI;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import database.DatabaseColumn.ColumnType;

public class JSONDatabase implements DatabaseInterface {
	
	private boolean connected = false;
	private File databaseRoot = null;
	

	private static final String JSON_EXT = "JSON";
	private static final String CONTENTS_FILENAME = "tableContents." + JSON_EXT;
	private static final String COLUMN_DATA = "columnData";
	private static final String ENTRY_DATA = "entryData";
	private static final int INDENT_FACTOR = 1;
	
	@Override
	public void connect(URI uri, Object ... args) throws JSONDatabaseConnectionException {
		File f = new File(uri);
		if(f.exists() && f.isDirectory() && f.canRead() && f.canWrite()) {
			databaseRoot = f;
			connected = true;
		} else {
			throw new JSONDatabaseConnectionException(uri);
		}
	}

	public void disconnect() {
		connected = false;
		databaseRoot = null;
	}
	
	private File getTableDirectory(DatabaseTable table) {
		if(!connected)
			throw new JSONDatabaseException("Cannot perform task; Not connected to a database");
		return getTableDirectory(table, databaseRoot, 0);
	}
	
	private JSONObject getJSONTable(DatabaseTable table) {
		File contentsFile = getTableContentsFile(table);
		try {
			Reader reader = new FileReader(contentsFile);
			JSONTokener tokener = new JSONTokener(reader);
			JSONObject jsonTable = new JSONObject(tokener);
			return jsonTable;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			throw new JSONDatabaseException("Could not retreive JSON object at " + table.getFullString("."));
		}
	}
	
	private void saveJSONTable(DatabaseTable table, JSONObject jsonTable) {
		File contentsFile = getTableContentsFile(table);
		try {
			Writer writer = new FileWriter(contentsFile);
			writer.write(jsonTable.toString(INDENT_FACTOR));
			writer.flush();
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
			throw new JSONDatabaseException("Could not save JSON object at " + table.getFullString("."));
		}
	}
	
	private File getTableContentsFile(DatabaseTable table) {
		if(!connected)
			throw new JSONDatabaseException("Cannot perform task; Not connected to a database");
		File directory = getTableDirectory(table);
		
		File[] files = directory.listFiles((f) -> {
			return f.isFile() && f.getName().equals(CONTENTS_FILENAME);
		});
		
		if(files.length == 0)
			throw new JSONDatabaseException("Cannot find \"" + CONTENTS_FILENAME +"\" for " + table.getFullString("."));
		return files[0];
	}
	
	private File getTableDirectory(DatabaseTable table, File parentDirectory, int subTableIndex) {
		String tableName;
		String[] subTableNames = table.getSubTableNames();
		int totalNumNames = subTableNames.length + 1;
		
		if(subTableIndex == 0)
			tableName = table.getRootTableName();
		else
			tableName = subTableNames[subTableIndex - 1];
		
		if(tableName.endsWith("." + JSON_EXT))
			throw new JSONDatabaseException("Cannot have table name end with \"." + JSON_EXT + "\" for " + table.getFullString("."));
		
		File[] files = parentDirectory.listFiles((f) -> {
			return f.isDirectory() && f.getName().equals(tableName);
		});
		
		File tableFile;
		if(files.length == 0)
			throw new JSONDatabaseException("No table exists at " + table.getFullString("."));
		else
			tableFile = files[0];
		
		if(subTableIndex + 1 < totalNumNames)
			return getTableDirectory(table, tableFile, subTableIndex + 1);
		else 
			return tableFile;
	}
	
	private File createTableFile(DatabaseTable table, File parentDirectory, int subTableIndex) {
		String tableName;
		String[] subTableNames = table.getSubTableNames();
		int totalNumNames = subTableNames.length + 1;
		if(subTableIndex >= totalNumNames)
			return parentDirectory;
		
		
		if(subTableIndex == 0)
			tableName = table.getRootTableName();
		else
			tableName = subTableNames[subTableIndex - 1];
		
		if(tableName.endsWith("." + JSON_EXT))
			throw new JSONDatabaseException("Cannot have table name end with \"." + JSON_EXT + "\" for " + table.getFullString("."));
		
		
		File[] files = parentDirectory.listFiles((f) -> {
			return f.isDirectory() && f.getName().equals(tableName);
		});
		
		File tableFolder;
		if(files.length == 0) {
			tableFolder = new File(parentDirectory, tableName);
			if(tableFolder.isFile())
				tableFolder.delete();
			tableFolder.mkdir();
		} else {
			tableFolder = files[0];
		}
		
		files = tableFolder.listFiles((f) -> {
			return f.isFile() && f.getName().equals(CONTENTS_FILENAME);
		});
		
		
		if(files.length == 0) {
			File contentFile = new File(tableFolder, CONTENTS_FILENAME);
			if(contentFile.isDirectory())
				contentFile.delete();
			JSONObject obj = new JSONObject();
			obj.put(COLUMN_DATA, new JSONObject());
			obj.put(ENTRY_DATA, new JSONArray());
			
			try {
				FileWriter fw = new FileWriter(contentFile);
				fw.write(obj.toString());
				fw.flush();
				fw.close();
			} catch (IOException e) {
				e.printStackTrace();
				throw new JSONDatabaseException("Not able to create \"" + CONTENTS_FILENAME + "\" for "  + table.getFullString("."));
			}
		}
		
		return createTableFile(table, tableFolder, subTableIndex + 1);
	}
	
	@Override
	public void createTable(DatabaseTable table) {
		if(!connected)
			throw new JSONDatabaseException("Cannot perform task; Not connected to a database");
		createTableFile(table, databaseRoot, 0);
	}
	
	
	

	@Override
	public void removeTable(DatabaseTable table) {
		getTableDirectory(table).delete();
	}

	@Override
	public DatabaseTable[] listTables() {
		if(!connected)
			throw new JSONDatabaseException("Cannot perform task; Not connected to a database");

		LinkedList<DatabaseTable> tables = new LinkedList<>();
		listTables(databaseRoot, null, tables);
		
		DatabaseTable[] list = new DatabaseTable[tables.size()];
		tables.toArray(list);
		return list;
	}
	
	private void listTables(File parentFile, DatabaseTable parent, LinkedList<DatabaseTable> tables) {
		parentFile.listFiles((f) -> {
			if(f.isFile() && f.getName().equals(CONTENTS_FILENAME)) {
				if(parent != null)
					tables.add(parent);
			} else if (f.isDirectory()){
				String fileName = f.getName();
				DatabaseTable child = parent == null? new DatabaseTable(fileName) : parent.subTable(fileName);
				listTables(f, child, tables);
			}
			return false;
		});
	}
	
	@Override
	public void addColumns(DatabaseTable table, DatabaseColumn... columns) {
		JSONObject jsonTable = getJSONTable(table);
		
		JSONObject columnData;
		if(jsonTable.has(COLUMN_DATA)) {
			columnData = jsonTable.getJSONObject(COLUMN_DATA);
		} else {
			columnData = new JSONObject();
			jsonTable.put(COLUMN_DATA, columnData);
		}
		
		// check if columns already exist
		for(DatabaseColumn column : columns)
			if(columnData.has(column.getName()))
				throw new JSONDatabaseException("Column \"" + column.getName() + "\" already exists");
		
		JSONArray entryData;
		if(jsonTable.has(ENTRY_DATA)) {
			entryData = jsonTable.getJSONArray(ENTRY_DATA);
		} else {
			entryData = new JSONArray();
			jsonTable.put(ENTRY_DATA, entryData);
		}
		
		for(DatabaseColumn column : columns) {
			columnData.put(column.getName(), column.getType().name());
			entryData.forEach((o) -> {
				JSONObject jsonEntry = (JSONObject) o;
				switch(column.getType()) {
				case BOOLEAN:
					jsonEntry.put(column.getName(), false);
					break;
				case INT:
					jsonEntry.put(column.getName(), 0);
					break;
				case STRING:
					jsonEntry.put(column.getName(), "");
					break;
				case CHAR:
					jsonEntry.put(column.getName(), '\0');
					break;
				case FLOAT:
					jsonEntry.put(column.getName(), 0f);
					break;
				default:
					break;
				}
			});
		}
		
		saveJSONTable(table, jsonTable);
	}

	@Override
	public void removeColumns(DatabaseTable table, String ... names) {
		JSONObject jsonTable = getJSONTable(table);
		
		JSONObject columnData;
		if(jsonTable.has(COLUMN_DATA)) {
			columnData = jsonTable.getJSONObject(COLUMN_DATA);
		} else {
			columnData = new JSONObject();
			jsonTable.put(COLUMN_DATA, columnData);
		}
		
		// check if columns already exist
		for(String columnName : names)
			if(!columnData.has(columnName))
				throw new JSONDatabaseException("Column \"" + columnName + "\" does not exists");
		
		JSONArray entryData;
		if(jsonTable.has(ENTRY_DATA)) {
			entryData = jsonTable.getJSONArray(ENTRY_DATA);
		} else {
			entryData = new JSONArray();
			jsonTable.put(ENTRY_DATA, entryData);
		}
		
		for(String columnName : names) {
			columnData.remove(columnName);
			entryData.forEach((o) -> {
				JSONObject jsonEntry = (JSONObject) o;
				jsonEntry.remove(columnName);
			});
		}
		
		saveJSONTable(table, jsonTable);
	}

	@Override
	public DatabaseColumn[] listColumns(DatabaseTable table) {
		JSONObject jsonTable = getJSONTable(table);
		JSONObject columnData = jsonTable.getJSONObject(COLUMN_DATA);
		String[] names = JSONObject.getNames(columnData);
		if(names == null)
			names = new String[0];
		
		DatabaseColumn[] databaseColumns = new DatabaseColumn[names.length];
		
		int i = 0;
		for(String name : names) {
			ColumnType type = ColumnType.valueOf(columnData.getString(name));
			databaseColumns[i++] = new DatabaseColumn(name, type);
		}
		
		return databaseColumns;
	}
	
	private JSONObject createEntry(JSONObject columnData, Map<String, Object> cells) {
		JSONObject entry = new JSONObject();
		
		// check types
		for(String name : cells.keySet()) {
			String typeName = columnData.getString(name);
			Object value = cells.get(name);
			
			if(!ColumnType.checkType(typeName, value))
				throw new JSONDatabaseException("The type " + typeName + " of " + name + " was not used");
		}
		
		Set<String> cellColumns = cells.keySet();
		
		for(String name : cellColumns) {
			String typeName = columnData.getString(name);
			Object value = cells.get(name);
			
			switch(ColumnType.valueOf(typeName)) {
			case BOOLEAN:
				entry.put(name, (boolean) value);
				break;
			case INT:
				entry.put(name, (int) value);
				break;
			case STRING:
				entry.put(name, (String) value);
				break;
			case CHAR:
				entry.put(name, (char) value);
				break;
			case FLOAT:
				entry.put(name, (float) value);
				break;
			default:
				break;
			}
		}
		columnData.toMap().forEach((n, t) -> {
			if(!cellColumns.contains(n)) {
				switch(ColumnType.valueOf((String)t)) {
				case BOOLEAN:
					entry.put(n, false);
					break;
				case INT:
					entry.put(n, 0);
					break;
				case STRING:
					entry.put(n, "");
					break;
				case CHAR:
					entry.put(n, '\0');
					break;
				case FLOAT:
					entry.put(n, 0f);
					break;
				default:
					break;
				}
			}
		});
		return entry;
	}
	
	
	@Override
	public void addEntry(DatabaseTable table, int entryIndex, Map<String, Object> cells) {
		JSONObject jsonTable = getJSONTable(table);
		JSONObject columnData = jsonTable.getJSONObject(COLUMN_DATA);
	
		JSONObject entry = createEntry(columnData, cells);
		
		JSONArray entryData = jsonTable.getJSONArray(ENTRY_DATA);
		entryData.put(entry);
		saveJSONTable(table, jsonTable);
	}

	@Override
	public void setEntry(DatabaseTable table, int entryIndex, Map<String, Object> cells) {
		JSONObject jsonTable = getJSONTable(table);
		JSONObject columnData = jsonTable.getJSONObject(COLUMN_DATA);
	
		JSONObject entry = createEntry(columnData, cells);
		
		JSONArray entryData = jsonTable.getJSONArray(ENTRY_DATA);
		if(entryIndex > entryData.length() || entryIndex < 0)
			throw new JSONDatabaseException("Entry index \"" + entryIndex + "\" is out of bounds");
		entryData.put(entryIndex, entry);
		saveJSONTable(table, jsonTable);
	}

	@Override
	public void removeEntry(DatabaseTable table, int entryIndex) {
		JSONObject jsonTable = getJSONTable(table);
		JSONArray entryData = jsonTable.getJSONArray(ENTRY_DATA);
		if(entryIndex >= entryData.length() || entryIndex < 0)
			throw new JSONDatabaseException("Entry index \"" + entryIndex + "\" is out of bounds");
		entryData.remove(entryIndex);
		saveJSONTable(table, jsonTable);
	}

	@Override
	public void setCell(DatabaseTable table, int entryIndex, String column, Object element) {
		JSONObject jsonTable = getJSONTable(table);
		
		JSONArray entryData = jsonTable.getJSONArray(ENTRY_DATA);
		if(entryIndex > entryData.length() || entryIndex < 0)
			throw new JSONDatabaseException("Entry index \"" + entryIndex + "\" is out of bounds");

		JSONObject entry = entryData.getJSONObject(entryIndex);
		if(!entry.has(column))
			throw new JSONDatabaseException("Column \"" + column + "\" does not exist for table " + table.getFullString("."));
		entry.put(column, element);
		entryData.put(entryIndex, entry);
		saveJSONTable(table, jsonTable);
	}

	@Override
	public Object getCell(DatabaseTable table, int entryIndex, String column) {
		JSONObject jsonTable = getJSONTable(table);
		
		JSONArray entryData = jsonTable.getJSONArray(ENTRY_DATA);
		if(entryIndex > entryData.length() || entryIndex < 0)
			throw new JSONDatabaseException("Entry index \"" + entryIndex + "\" is out of bounds");

		JSONObject entry = entryData.getJSONObject(entryIndex);
		if(!entry.has(column))
			throw new JSONDatabaseException("Column \"" + column + "\" does not exist for table " + table.getFullString("."));
		
		return entry.get(column);
	}

	@Override
	public int getNumberOfEntries(DatabaseTable table) {
		JSONObject jsonTable = getJSONTable(table);
		JSONArray entryData = jsonTable.getJSONArray(ENTRY_DATA);
		return entryData.length();
	}

	@Override
	public ArrayList<Map<String, Object>> queryEntry(DatabaseTable table, Predicate<Map<String, Object>> predicate) {
		JSONObject jsonTable = getJSONTable(table);
		JSONArray entryData = jsonTable.getJSONArray(ENTRY_DATA);
		ArrayList<Map<String, Object>> entriesArray = new ArrayList<>();
		
		int size = entryData.length();
		for(int i = 0; i < size; i++) {
			JSONObject entry = entryData.getJSONObject(i);
			Map<String, Object> map = entry.toMap();
			if(predicate.test(map))
				entriesArray.add(map);
		}
		
		return entriesArray;
	}

	@Override
	public Object[] queryColumn(DatabaseTable table, String column, Predicate<Object> predicate) {
		JSONObject jsonTable = getJSONTable(table);
		JSONArray entryData = jsonTable.getJSONArray(ENTRY_DATA);
		ArrayList<Object> entriesArray = new ArrayList<>();
		
		int size = entryData.length();
		for(int i = 0; i < size; i++) {
			JSONObject entry = entryData.getJSONObject(i);
			Object elem = entry.get(column);
			if(predicate.test(elem))
				entriesArray.add(elem);
		}
		
		Object[] elems = new Object[entriesArray.size()];
		entriesArray.toArray(elems);
		return elems;
	}
	

	@Override
	public void setEntry(DatabaseTable table, UnaryOperator<Map<String, Object>> operator) {
		JSONObject jsonTable = getJSONTable(table);
		JSONObject columnData = jsonTable.getJSONObject(COLUMN_DATA);
		JSONArray entryData = jsonTable.getJSONArray(ENTRY_DATA);
		
		int size = entryData.length();
		for(int i = 0; i < size; i++) {
			JSONObject entry = entryData.getJSONObject(i);
			Map<String, Object> map = entry.toMap();
			Map<String, Object> mapOut = operator.apply(map);
			if(map != null) {
				for(String name : mapOut.keySet()) {
					String typeName = columnData.getString(name);
					Object value = mapOut.get(name);
					
					switch(ColumnType.valueOf(typeName)) {
					case BOOLEAN:
						entry.put(name, (boolean) value);
						break;
					case INT:
						entry.put(name, (int) value);
						break;
					case STRING:
						entry.put(name, (String) value);
						break;
					case CHAR:
						entry.put(name, (char) value);
						break;
					case FLOAT:
						entry.put(name, (float) value);
						break;
					default:
						break;
					}
				}
				entryData.put(i, entry);
			}
		}
		saveJSONTable(table, jsonTable);
	}

	@Override
	public void removeEntries(DatabaseTable table, Predicate<Map<String, Object>> predicate) {
		JSONObject jsonTable = getJSONTable(table);
		JSONArray entryData = jsonTable.getJSONArray(ENTRY_DATA);
		
		int size = entryData.length();
		for(int i = 0; i < size; i++) {
			JSONObject entry = entryData.getJSONObject(i);
			Map<String, Object> map = entry.toMap();
			if(predicate.test(map))
				entryData.remove(i);
		}
		saveJSONTable(table, jsonTable);
	}
	
	@SuppressWarnings("serial")
	public class JSONDatabaseConnectionException extends RuntimeException {
		private JSONDatabaseConnectionException(URI uri) {
			super("Could not connect to: " + uri.toString());
		}
	}
	
	@SuppressWarnings("serial")
	public class JSONDatabaseException extends RuntimeException {
		private JSONDatabaseException(String message) {
			super(message);
		}
	}
	
}
