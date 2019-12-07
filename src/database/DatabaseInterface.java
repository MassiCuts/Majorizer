package database;

import java.net.URI;
import java.util.ArrayList;
import java.util.Map;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;

public interface DatabaseInterface {
	public void connect(URI uri, Object ... args);
	public void disconnect();
	
	public void createTable(DatabaseTable table);
	public void removeTable(DatabaseTable table);
	public DatabaseTable[] listTables();
	
	public void addColumns(DatabaseTable table, DatabaseColumn ... columns);
	public void removeColumns(DatabaseTable table, String ... names);
	public DatabaseColumn[] listColumns(DatabaseTable table);
	
	public void addEntry(DatabaseTable table, int entryIndex, Map<String, Object> cells);
	

	public default void addEntry(DatabaseTable table, Map<String, Object> cells) {
		addEntry(table, 0, cells);
	}
	
	public void setEntry(DatabaseTable table, int entryIndex, Map<String, Object> cells);
	public void setEntry(DatabaseTable table, UnaryOperator<Map<String, Object>> operator);
	public void removeEntry(DatabaseTable table, int entryIndex);
	public void removeEntries(DatabaseTable table, Predicate<Map<String, Object>> predicate);
	
	public void setCell(DatabaseTable table, int entryIndex, String column, Object element);
	public Object getCell(DatabaseTable table, int entryIndex, String column);
	
	public int getNumberOfEntries(DatabaseTable table);
	
	public ArrayList<Map<String, Object>> queryEntry(DatabaseTable table, Predicate<Map<String, Object>> predicate);
	
	public Object[] queryColumn(DatabaseTable table, String column, Predicate<Object> predicate);
	
	public default void printTable(DatabaseTable table) {
		System.out.println(tableToString(table));
	}
	
	public default String tableToString(DatabaseTable table) {
		DatabaseColumn[] columns = listColumns(table);
		int[] stringLengths = new int[columns.length];
		for(int i = 0; i < columns.length; i++)
			stringLengths[i] = columns[i].getName().length();
		
		ArrayList<Map<String, Object>> entries = queryEntry(table, a -> true);
		for(int i = 0; i < entries.size(); i++) {
			Map<String, Object> entry = entries.get(i);
			for(int j = 0; j < columns.length; j++) {
				String columnName = columns[j].getName();
				Object cell = entry.get(columnName);
				
				int stringLength = stringLengths[j];
				int cellStringLength = cell.toString().length();
				if(cellStringLength > stringLength)
					stringLengths[j] = cellStringLength;
			}
		}
		

		String tableString = "[[" + table.getFullString(".") + "]]";
		
		for(int i = 0; i < columns.length; i++) {
			if(i == 0) tableString += "\n|";
			String columnName = columns[i].getName();
			int cellLength = stringLengths[i];
			int columnSpaceLength = cellLength - columnName.length();
			tableString += columnName;
			
			for(int j = 0; j < columnSpaceLength; j++)
				tableString += " ";
			tableString += "|";
		}
		
		for(int i = 0; i < columns.length; i++) {
			if(i == 0) tableString += "\n-";
			int cellLength = stringLengths[i];
			for(int j = 0; j < cellLength; j++)
				tableString += "-";
			tableString += "-";
		}
		
		for(int i = 0; i < entries.size(); i++) {
			Map<String, Object> entry = entries.get(i);
			for(int j = 0; j < columns.length; j++) {
				if(j == 0) tableString += "\n|";
				String columnName = columns[j].getName();
				Object cell = entry.get(columnName);
				
				int stringLength = stringLengths[j];
				int cellStringLength = cell.toString().length();
				int columnSpaceLength = stringLength - cellStringLength;
				tableString += cell.toString();
				
				for(int k = 0; k < columnSpaceLength; k++)
					tableString += " ";
				tableString += "|";
			}
		}
		
		return tableString;
	}
}
