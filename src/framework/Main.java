package framework;

public class Main {
	
	public static void main(String args[]) {
		DatabaseManager.connect();
//		MajorizerDatabaseManager.initializeDatabase();
		DatabaseManager.printTable(DatabaseManager.ADVISORS_TABLE);
		DatabaseManager.printTable(DatabaseManager.COURSES_TABLE);
		DatabaseManager.printTable(DatabaseManager.MAJORS_TABLE);
		DatabaseManager.printTable(DatabaseManager.MINORS_TABLE);
		DatabaseManager.printTable(DatabaseManager.REQUESTS_TABLE);
		DatabaseManager.printTable(DatabaseManager.STUDENTS_TABLE);
		DatabaseManager.printTable(DatabaseManager.USERS_TABLE);
	}
}
