package framework;

import java.net.URI;
import java.net.URISyntaxException;

import database.DatabaseTable;
import database.JSONDatabase;
import database.JSONDatabase.JSON_DatabaseConnectionException;

public class MajorizerDatabaseManager {
	
	public static final String DATABASE_LOCATOR = "file:/C:/Users/Massimiliano%20Cutugno/Desktop/database";
	
	private static final JSONDatabase JSON_DATABASE = new JSONDatabase();
	
	private static final DatabaseTable USERS_TABLE    = new DatabaseTable("Users");
	private static final DatabaseTable ADVISORS_TABLE = new DatabaseTable("Users", "Advisors");
	private static final DatabaseTable STUDENTS_TABLE = new DatabaseTable("Users", "Students");
	
	private static final DatabaseTable CURRICULUMS_TABLE 		= new DatabaseTable("Users", "Students", "Curriculums");
	private static final DatabaseTable TAKEN_SEMESTERS_TABLE 	= new DatabaseTable("Users", "Students", "Curriculums", "TakenSemesters");
	private static final DatabaseTable REQUIRED_SEMESTERS_TABLE = new DatabaseTable("RequiredSemesters");
	private static final DatabaseTable COURSES_TABLE 			= new DatabaseTable("Courses");
	private static final DatabaseTable REQUESTS_TABLE 			= new DatabaseTable("Requests");
	private static final DatabaseTable MAJORS_TABLE 			= new DatabaseTable("Majors");
	private static final DatabaseTable MINORS_TABLE 			= new DatabaseTable("Minors");
	
	public static void connect() {
		URI uri;
		try {
			uri = new URI(DATABASE_LOCATOR);
			JSON_DATABASE.connect(uri);
		} catch (URISyntaxException | JSON_DatabaseConnectionException e) {
			System.err.println("Could not connect to database!");
			e.printStackTrace();
			System.exit(0);
		}
	}
	
	public static void setupDatabase() {
		
	}
	
	public static void disconnect() {
		JSON_DATABASE.disconnect();
	}
	
	public static void initializeDatabase() {
		
	}
}
