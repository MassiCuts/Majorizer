package framework;

import java.net.URI;
import java.net.URISyntaxException;

import database.DatabaseColumn;
import database.DatabaseColumn.ColumnType;
import database.DatabaseInterface;
import database.DatabaseTable;
import database.JSONDatabase;
import database.JSONDatabase.JSON_DatabaseConnectionException;

public class DatabaseManager {
	
	public static final String DATABASE_LOCATOR = "file:/C:/Users/Massimiliano%20Cutugno/Desktop/database";
	
	private static final DatabaseInterface DATABASE = new JSONDatabase();
	
	public static final DatabaseTable USERS_TABLE    = new DatabaseTable("Users");
	public static final DatabaseTable ADVISORS_TABLE = new DatabaseTable("Users", "Advisors");
	public static final DatabaseTable STUDENTS_TABLE = new DatabaseTable("Users", "Students");
	
	public static final DatabaseTable COURSES_TABLE 		= new DatabaseTable("Courses");
	public static final DatabaseTable REQUESTS_TABLE 		= new DatabaseTable("Requests");
	public static final DatabaseTable MAJORS_TABLE 			= new DatabaseTable("Majors");
	public static final DatabaseTable MINORS_TABLE 			= new DatabaseTable("Minors");
	
	public static void connect() {
		URI uri;
		try {
			uri = new URI(DATABASE_LOCATOR);
			DATABASE.connect(uri);
		} catch (URISyntaxException | JSON_DatabaseConnectionException e) {
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
				new DatabaseColumn("isNewStudent", ColumnType.BOOLEAN));
		
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
				new DatabaseColumn("courseName", 	ColumnType.STRING),
				new DatabaseColumn("requestID", 	ColumnType.INT));
		
		DATABASE.createTable(MAJORS_TABLE);
		DATABASE.addColumns(MAJORS_TABLE, 
				new DatabaseColumn("majorID", 		ColumnType.INT),
				new DatabaseColumn("name", 		ColumnType.STRING));
		
		DATABASE.createTable(MINORS_TABLE);
		DATABASE.addColumns(MINORS_TABLE, 
				new DatabaseColumn("minorID", 		ColumnType.INT),
				new DatabaseColumn("name", 		ColumnType.STRING));
	}
	
	public static void printTable(DatabaseTable table) {
		System.out.println(DATABASE.tableToString(table));
	}
	
	public void makeAccount(String name, String username, String password, int ID, boolean userIsStudent) {
		
	}
}
