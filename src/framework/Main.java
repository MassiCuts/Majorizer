package framework;

import java.net.URI;
import java.net.URISyntaxException;

import database.DatabaseColumn;
import database.DatabaseColumn.ColumnType;
import database.DatabaseTable;
import database.JSONDatabase;
import database.JSONDatabase.JSON_DatabaseConnectionException;

public class Main {
//	public static final String DATABASE_LOCATOR = "file:/<path to database>/<database directory>"; // TODO: put database location here
	public static final String DATABASE_LOCATOR = "file:/C:/Users/Massimiliano%20Cutugno/Desktop/database";
	
	public static void main(String args[]) {
		JSONDatabase database = new JSONDatabase();
		URI uri;
		try {
			uri = new URI(DATABASE_LOCATOR);
			database.connect(uri);
		} catch (URISyntaxException | JSON_DatabaseConnectionException e) {
			System.err.println("Could not connect to database!");
			e.printStackTrace();
			System.exit(0);
		}
		
		DatabaseTable users_table = new DatabaseTable("Users");
		DatabaseTable advisors_table = new DatabaseTable("Users", "Advisors");
		DatabaseTable students_table = new DatabaseTable("Users", "Students");
		
//		database.removeColumns(users_table, "rich");
		
//		ArrayList<Map<String, Object>> elements = database.queryEntry(users_table, (entry) -> {
//			return entry.get("password").toString().equals("hickey1");
//		});
//		
//		for(Map<String, Object> map : elements) {
//			map.forEach((t, v) -> {
//				System.out.print(t + ":" + v + "|");
//			});
//			System.out.println();
//		}
		
//		database.createTable(users_table);
//		database.createTable(advisors_table);
//		database.createTable(students_table);
//		
//		database.addColumns(users_table,
//				new DatabaseColumn("ID", ColumnType.INT),
//				new DatabaseColumn("username", ColumnType.STRING),
//				new DatabaseColumn("name", ColumnType.STRING),
//				new DatabaseColumn("password", ColumnType.STRING));
//		
//		HashMap<String, Object> cells = new HashMap<>();
//		
//		cells.put("ID", 122);
//		cells.put("username", "puppy");
//		cells.put("name", "jickey");
//		cells.put("password", "hickey");
//		database.addEntry(users_table, 0, cells);
//
//		cells.put("ID", 123);
//		cells.put("username", "bob saget");
//		cells.put("name", "sam");
//		cells.put("password", "blah blah");
//		database.addEntry(users_table, 0, cells);
//
//		cells.put("ID", 124);
//		cells.put("username", "joey");
//		cells.put("name", "evan");
//		cells.put("password", "temp3");
//		database.addEntry(users_table, 0, cells);
//		
//		
//		database.addColumns(advisors_table,
//				new DatabaseColumn("power", ColumnType.INT),
//				new DatabaseColumn("ID", ColumnType.INT));
//		database.addColumns(students_table,
//				new DatabaseColumn("power", ColumnType.INT),
//				new DatabaseColumn("ID", ColumnType.INT));
		
		database.printTable(users_table);
		database.printTable(advisors_table);
		database.printTable(students_table);
	}
}
