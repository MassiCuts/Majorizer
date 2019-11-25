package framework;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import database.DatabaseColumn;
import database.DatabaseColumn.ColumnType;
import database.DatabaseInterface;
import database.DatabaseTable;
import database.JSONDatabase;
import database.JSONDatabase.JSONDatabaseConnectionException;

public class DatabaseManager {
	
	private static final DatabaseInterface DATABASE = new JSONDatabase();
	
	public static final DatabaseTable USERS_TABLE    					= new DatabaseTable("Users");
	
	public static final DatabaseTable STUDENTS_TABLE 					= USERS_TABLE.subTable("Students");
	public static final DatabaseTable STUDENTS_CURRICULUMS_TABLE 		= STUDENTS_TABLE.subTable("Student_Curriculums");
	public static final DatabaseTable STUDENT_TAKEN_COURSES_TABLE 		= STUDENTS_TABLE.subTable("Student_Taken_Courses");
	
	public static final DatabaseTable ADVISORS_TABLE 					= USERS_TABLE.subTable("Advisors");
	public static final DatabaseTable ADVISOR_STUDENTS_TABLE 			= ADVISORS_TABLE.subTable("Advisor_Students");
	public static final DatabaseTable REQUESTS_TABLE 					= ADVISORS_TABLE.subTable("Requests");
	
	
	public static final DatabaseTable CURRICULUMS_TABLE 				= new DatabaseTable("Curriculums");
	public static final DatabaseTable CURRICULUM_COURSE_SELECTION_TABLE = CURRICULUMS_TABLE.subTable("Curriculum_Course_Selection");
	
	
	public static final DatabaseTable COURSES_TABLE 					= new DatabaseTable("Courses");
	public static final DatabaseTable COURSE_AVAILABLITY_TABLE			= COURSES_TABLE.subTable("Course_Availablity");
	public static final DatabaseTable COURSES_PREREC_TABLE 				= COURSES_TABLE.subTable("Courses_PreRecs");
	public static final DatabaseTable PREREC_TABLE 						= COURSES_PREREC_TABLE.subTable("PreRecs");
	public static final DatabaseTable PREREC_COURSE_SELECTION_TABLE 	= COURSES_PREREC_TABLE.subTable("PreRecs_Course_Selection");
	
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
		for(DatabaseTable table : DATABASE.listTables())
			DATABASE.removeTable(table);
		
		DATABASE.createTable(USERS_TABLE);
		DATABASE.addColumns(USERS_TABLE,
				new DatabaseColumn("userID", 		ColumnType.INT), 
				new DatabaseColumn("universityID", 	ColumnType.STRING), 
				new DatabaseColumn("firstName", 	ColumnType.STRING), 
				new DatabaseColumn("lastName", 		ColumnType.STRING), 
				new DatabaseColumn("username", 		ColumnType.STRING), 
				new DatabaseColumn("password", 		ColumnType.STRING));
		
		DATABASE.createTable(STUDENTS_TABLE);
		DATABASE.addColumns(STUDENTS_TABLE,
				new DatabaseColumn("userID", 		ColumnType.INT), 
				new DatabaseColumn("isNewStudent", 	ColumnType.BOOLEAN),
				new DatabaseColumn("startSemester", ColumnType.STRING));
		
		DATABASE.createTable(STUDENTS_CURRICULUMS_TABLE);
		DATABASE.addColumns(STUDENTS_CURRICULUMS_TABLE,
				new DatabaseColumn("studentID", 	ColumnType.INT), 
				new DatabaseColumn("curriculumID", 	ColumnType.INT));
		
		DATABASE.createTable(STUDENT_TAKEN_COURSES_TABLE);
		DATABASE.addColumns(STUDENT_TAKEN_COURSES_TABLE,
				new DatabaseColumn("studentID", 	ColumnType.INT), 
				new DatabaseColumn("courseID", 		ColumnType.INT), 
				new DatabaseColumn("semester", 		ColumnType.STRING));
		
		
		
		
		DATABASE.createTable(ADVISORS_TABLE);
		DATABASE.addColumns(ADVISORS_TABLE, 
				new DatabaseColumn("userID", 		ColumnType.INT));

		DATABASE.createTable(ADVISOR_STUDENTS_TABLE);
		DATABASE.addColumns(ADVISOR_STUDENTS_TABLE,
				new DatabaseColumn("advisorID", 	ColumnType.INT), 
				new DatabaseColumn("studentID", 	ColumnType.INT));
		
		DATABASE.createTable(REQUESTS_TABLE);
		DATABASE.addColumns(REQUESTS_TABLE, 
				new DatabaseColumn("requestID", 	ColumnType.INT),
				new DatabaseColumn("studentID", 	ColumnType.INT),
				new DatabaseColumn("advisorID", 	ColumnType.INT),
				new DatabaseColumn("curriculumID", 	ColumnType.INT),
				new DatabaseColumn("isAdding", 		ColumnType.BOOLEAN));
		
		
		

		DATABASE.createTable(CURRICULUMS_TABLE);
		DATABASE.addColumns(CURRICULUMS_TABLE,
				new DatabaseColumn("curriculumID", 		ColumnType.INT), 
				new DatabaseColumn("curriculumName", 	ColumnType.STRING), 
				new DatabaseColumn("type", 				ColumnType.STRING), 
				new DatabaseColumn("amtReq", 			ColumnType.INT));
		
		DATABASE.createTable(CURRICULUM_COURSE_SELECTION_TABLE);
		DATABASE.addColumns(CURRICULUM_COURSE_SELECTION_TABLE,
				new DatabaseColumn("curriculumID", 			ColumnType.INT), 
				new DatabaseColumn("type", 					ColumnType.STRING), 
				new DatabaseColumn("curriculumCourseID", 	ColumnType.INT));
		
		
		
		
		DATABASE.createTable(COURSES_TABLE);
		DATABASE.addColumns(COURSES_TABLE, 
				new DatabaseColumn("courseID", 		ColumnType.INT),
				new DatabaseColumn("courseCode", 	ColumnType.STRING),
				new DatabaseColumn("courseName", 	ColumnType.STRING));
		
		DATABASE.createTable(COURSE_AVAILABLITY_TABLE);
		DATABASE.addColumns(COURSE_AVAILABLITY_TABLE, 
				new DatabaseColumn("courseID", 		ColumnType.INT),
				new DatabaseColumn("timeOffered", 	ColumnType.STRING));
		
		DATABASE.createTable(COURSES_PREREC_TABLE);
		DATABASE.addColumns(COURSES_PREREC_TABLE,
				new DatabaseColumn("courseID", 		ColumnType.INT),
				new DatabaseColumn("preRecID", 		ColumnType.INT));
		
		DATABASE.createTable(PREREC_TABLE);
		DATABASE.addColumns(PREREC_TABLE,
				new DatabaseColumn("preRecID", 		ColumnType.INT),
				new DatabaseColumn("amtReq", 		ColumnType.INT));
		
		DATABASE.createTable(PREREC_COURSE_SELECTION_TABLE);
		DATABASE.addColumns(PREREC_COURSE_SELECTION_TABLE,
				new DatabaseColumn("preRecID", 			ColumnType.INT), 
				new DatabaseColumn("type", 				ColumnType.STRING), 
				new DatabaseColumn("preRecCourseID", 	ColumnType.INT));
		
	}
	
	public static void saveAdvisor(Advisor advisor, boolean isNew) {
		final int userID = advisor.getUserId();
		String username = advisor.getUsername();
		
		if(isNew) {
			DATABASE.queryEntry(USERS_TABLE, (m) -> {
				if(m.get("username").equals(username))
					throw new UsernameNotUniqueException(username);
				return false;
			});
			
			int newUserID = findUniqueID(USERS_TABLE, "userID");
			advisor.setUserID(newUserID);
			
			Map<String, Object> map = userToMap(advisor);
			
			DATABASE.addEntry(USERS_TABLE, 0, map);
			
			map.clear();
			map.put("userID", newUserID);
			DATABASE.addEntry(ADVISORS_TABLE, 0, map);
		} else {
			Map<String, Object> map = userToMap(advisor);
			
			DATABASE.setEntry(USERS_TABLE, (m)->{
				if((int) m.get("userID") == userID)
					return map;
				return null;
			});
			
			map.clear();
			map.put("userID", userID);
			
			DATABASE.setEntry(ADVISORS_TABLE, (m)-> {
				if((int) m.get("userID") == userID)
					return map;
				return null;
			});
		}
	}
	
	public static Advisor getAdvisor(String username) {
		ArrayList<Map<String, Object>> results = DATABASE.queryEntry(USERS_TABLE, (m) -> {
			if(m.get("username").equals(username))
				return true;
			return false;
		});
		
		if(results.size() == 0)
			return null;
		
		Map<String, Object> m = results.get(0);
		
		int userID = (int) m.get("userID");
		String universityID = (String) m.get("universityID");
		String firstName = (String) m.get("firstName");
		String lastName = (String) m.get("lastName");
		String password = (String) m.get("password");
		
		Advisor advisor = new Advisor(userID, universityID, firstName, lastName, username, password);
		return advisor;
	}
	
	public static Student getStudent(String username) {
		ArrayList<Map<String, Object>> userResults = DATABASE.queryEntry(USERS_TABLE, (m) -> {
			if(m.get("username").equals(username))
				return true;
			return false;
		});
		
		if(userResults.size() == 0)
			return null;
		
		Map<String, Object> userResultMap = userResults.get(0);
		int userID = (int) userResultMap.get("userID");
		
		ArrayList<Map<String, Object>> studentResults = DATABASE.queryEntry(STUDENTS_TABLE, (m) -> {
			if(m.get("userID").equals(userID))
				return true;
			return false;
		});
		
		if(studentResults.size() == 0)
			return null;
		
		Map<String, Object> studentResultMap = studentResults.get(0);
		
		String universityID = (String) userResultMap.get("universityID");
		String firstName = (String) userResultMap.get("firstName");
		String lastName = (String) userResultMap.get("lastName");
		String password = (String) userResultMap.get("password");
		
		boolean isNewStudent = (Boolean) studentResultMap.get("isNewStudent");
		String startSemester = (String) studentResultMap.get("startSemester");
		
		Student student = new Student(userID, universityID, firstName, lastName, username, password, startSemester, isNewStudent);
		return student;
	}
	
	public static void saveStudent(Student student, boolean isNew) {
		final int userID = student.getUserId();
		String username = student.getUsername();
		
		if(isNew) {
			DATABASE.queryEntry(USERS_TABLE, (m) -> {
				if(m.get("username").equals(username))
					throw new UsernameNotUniqueException(username);
				return false;
			});
			
			int newUserID = findUniqueID(USERS_TABLE, "userID");
			student.setUserID(newUserID);
			
			Map<String, Object> map = userToMap(student);
			
			DATABASE.addEntry(USERS_TABLE, 0, map);
			
			map.clear();
			map.put("userID", newUserID);
			map.put("isNewStudent", student.isStudentNew());
			DATABASE.addEntry(STUDENTS_TABLE, 0, map);
		} else {
			Map<String, Object> map = userToMap(student);
			
			DATABASE.setEntry(USERS_TABLE, (m)->{
				if((int) m.get("userID") == userID)
					return map;
				return null;
			});
			
			map.clear();
			map.put("userID", userID);
			map.put("isNewStudent", student.isStudentNew());
			
			DATABASE.setEntry(STUDENTS_TABLE, (m)-> {
				if((int) m.get("userID") == userID)
					return map;
				return null;
			});
		}
	}
	
	
//	
//	public static void makeStudentAccount(String name, String username, String password, int id, boolean studentIsNew) {
//		DATABASE.queryEntry(USERS_TABLE, (m) -> {
//			if(m.get("username").equals(username))
//				throw new UsernameNotUniqueException(username);
//			if((int) m.get("id") == id)
//				throw new EntryNotUniqueException("The user id \"" + id + "\" is already taken");
//			return false;
//		});
//		
//		HashMap<String, Object> map = new HashMap<>();
//		map.put("name", name);
//		map.put("username", username);
//		map.put("password", password);
//		map.put("id", id);
//		map.put("userIsStudent", true);
//		DATABASE.addEntry(USERS_TABLE, 0, map);
//		
//		map.clear();
//		map.put("id", id);
//		map.put("isNewStudent", studentIsNew);
//		DATABASE.addEntry(STUDENTS_TABLE, 0, map);
//		
//		DatabaseTable studentProfile = STUDENTS_PROFILES.subTable(Integer.toHexString(id));
//		
//		DATABASE.createTable(studentProfile);
//	}
	
	private static Map<String, Object> userToMap(User user) {
		int userID = user.getUserId();
		String universityID = user.getUniversityID();
		String firstName = user.getFirstName();
		String lastName = user.getLastName();
		String username = user.getUsername();
		String password = user.getPassword();
		
		HashMap<String, Object> map = new HashMap<>();
		map.put("userID", userID); 
		map.put("universityID", universityID);
		map.put("firstName", firstName);
		map.put("lastName", lastName);
		map.put("username", username);
		map.put("password", password);
		
		return map;
	}
	
	private static int findUniqueID(DatabaseTable table, String column) {
		int id = 0;
		HashSet<Integer> ids = new HashSet<>();
		DATABASE.queryColumn(table, column, (e)->{
			ids.add((int)e);
			return false;
		});
		
		for(int i = 0; i < ids.size(); i++) {
			if(!ids.contains(id))
				return id;
			id++;
		}
		return id;
	}
	
	public static DatabaseTable[] listTables() {
		return DATABASE.listTables();
	}
	
	public static void printTable(DatabaseTable table) {
		System.out.println(DATABASE.tableToString(table));
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
