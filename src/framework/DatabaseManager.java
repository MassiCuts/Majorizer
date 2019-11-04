package framework;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;

import database.DatabaseColumn;
import database.DatabaseColumn.ColumnType;
import database.DatabaseInterface;
import database.DatabaseTable;
import database.JSONDatabase;
import database.JSONDatabase.JSONDatabaseConnectionException;

public class DatabaseManager {
	
	private static final DatabaseInterface DATABASE = new JSONDatabase();
	
	public static final DatabaseTable USERS_TABLE    		= new DatabaseTable("Users");
	public static final DatabaseTable ADVISORS_TABLE 		= USERS_TABLE.subTable("Advisors");
	public static final DatabaseTable STUDENTS_TABLE 		= USERS_TABLE.subTable("Students");

	public static final DatabaseTable ADVISORS_PROFILES 	= ADVISORS_TABLE.subTable("Profiles");
	public static final DatabaseTable STUDENTS_PROFILES 	= STUDENTS_TABLE.subTable("Profiles");
	
	public static final DatabaseTable COURSES_TABLE 		= new DatabaseTable("Courses");
	public static final DatabaseTable COURSES_PREREC_TABLE 	= COURSES_TABLE.subTable("PreRecs");
	public static final DatabaseTable REQUESTS_TABLE 		= new DatabaseTable("Requests");
	public static final DatabaseTable MAJORS_TABLE 			= new DatabaseTable("Majors");
	public static final DatabaseTable MINORS_TABLE 			= new DatabaseTable("Minors");
	
	public static void connect(String database_locator) {
		URI uri;
		try {
			uri = new URI(database_locator);
			DATABASE.connect(uri);
		} catch (URISyntaxException | JSONDatabaseConnectionException e) {
			System.err.println("Could not connect to database!");
			e.printStackTrace();
			System.exit(0);
		}
	}

	public static void disconnect() {
		DATABASE.disconnect();
	}

	public static void initializeDatabase() {
		DATABASE.createTable(USERS_TABLE);
		DATABASE.addColumns(USERS_TABLE, 
				new DatabaseColumn("name", 			ColumnType.STRING), 
				new DatabaseColumn("username", 		ColumnType.STRING), 
				new DatabaseColumn("password", 		ColumnType.STRING), 
				new DatabaseColumn("id", 			ColumnType.INT), 
				new DatabaseColumn("userIsStudent", ColumnType.BOOLEAN));
		
		DATABASE.createTable(ADVISORS_TABLE);
		DATABASE.addColumns(ADVISORS_TABLE, 
				new DatabaseColumn("id", 			ColumnType.INT));  // to link to user
		
		DATABASE.createTable(STUDENTS_TABLE);
		DATABASE.addColumns(STUDENTS_TABLE,
				new DatabaseColumn("id", 			ColumnType.INT), // to link to user
				new DatabaseColumn("isNewStudent", 	ColumnType.BOOLEAN));
		
		DATABASE.createTable(REQUESTS_TABLE);
		DATABASE.addColumns(REQUESTS_TABLE, 
				new DatabaseColumn("description", 	ColumnType.STRING),
				new DatabaseColumn("studentID", 	ColumnType.INT),
				new DatabaseColumn("requestID", 	ColumnType.INT),
				new DatabaseColumn("advisorID", 	ColumnType.INT),
				new DatabaseColumn("majorID", 		ColumnType.INT),
				new DatabaseColumn("minorID", 		ColumnType.INT),
				new DatabaseColumn("isAdd", 		ColumnType.BOOLEAN));
		
		DATABASE.createTable(COURSES_TABLE);
		DATABASE.addColumns(COURSES_TABLE, 
				new DatabaseColumn("courseID", 		ColumnType.STRING),
				new DatabaseColumn("courseName", 	ColumnType.STRING));
		
		DATABASE.createTable(MAJORS_TABLE);
		DATABASE.addColumns(MAJORS_TABLE, 
				new DatabaseColumn("majorID", 		ColumnType.INT),
				new DatabaseColumn("name", 			ColumnType.STRING));
		
		DATABASE.createTable(MINORS_TABLE);
		DATABASE.addColumns(MINORS_TABLE, 
				new DatabaseColumn("minorID", 		ColumnType.INT),
				new DatabaseColumn("name", 			ColumnType.STRING));
		
		DATABASE.createTable(COURSES_PREREC_TABLE);
		DATABASE.addColumns(COURSES_PREREC_TABLE,
				new DatabaseColumn("courseID", 			ColumnType.STRING),
				new DatabaseColumn("preRecCourseID", 	ColumnType.STRING));
		
	}
	
	public static DatabaseTable[] listTables() {
		return DATABASE.listTables();
	}
	
	public static void printTable(DatabaseTable table) {
		System.out.println(DATABASE.tableToString(table));
	}
	
	public static void makeAdvisorAccount(String name, String username, String password, int id) {
		DATABASE.queryEntry(USERS_TABLE, (m) -> {
			if(m.get("username").equals(username))
				throw new UsernameNotUniqueException(username);
			if((int) m.get("id") == id)
				throw new EntryNotUniqueException("The user id \"" + id + "\" is already taken");
			return false;
		});
		
		HashMap<String, Object> map = new HashMap<>();
		map.put("name", name);
		map.put("username", username);
		map.put("password", password);
		map.put("id", id);
		map.put("userIsStudent", false);
		DATABASE.addEntry(USERS_TABLE, 0, map);
		
		map.clear();
		map.put("id", id);
		DATABASE.addEntry(ADVISORS_TABLE, 0, map);
		
		DatabaseTable advisorProfile = ADVISORS_PROFILES.subTable(Integer.toHexString(id));
		
		DATABASE.createTable(advisorProfile);
	}
	
	public static void makeStudentAccount(String name, String username, String password, int id, boolean studentIsNew) {
		DATABASE.queryEntry(USERS_TABLE, (m) -> {
			if(m.get("username").equals(username))
				throw new UsernameNotUniqueException(username);
			if((int) m.get("id") == id)
				throw new EntryNotUniqueException("The user id \"" + id + "\" is already taken");
			return false;
		});
		
		HashMap<String, Object> map = new HashMap<>();
		map.put("name", name);
		map.put("username", username);
		map.put("password", password);
		map.put("id", id);
		map.put("userIsStudent", true);
		DATABASE.addEntry(USERS_TABLE, 0, map);
		
		map.clear();
		map.put("id", id);
		map.put("isNewStudent", studentIsNew);
		DATABASE.addEntry(STUDENTS_TABLE, 0, map);
		
		DatabaseTable studentProfile = STUDENTS_PROFILES.subTable(Integer.toHexString(id));
		
		DATABASE.createTable(studentProfile);
	}
	
	
	@SuppressWarnings("serial")
	public static class EntryNotUniqueException extends RuntimeException {
		private EntryNotUniqueException(String message) {
			super(message);
		}
	}
	
	@SuppressWarnings("serial")
	public static class UsernameNotUniqueException extends EntryNotUniqueException {
		private UsernameNotUniqueException(String username) {
			super("Username \"" + username + "\" is already taken.");
		}
	}
}
