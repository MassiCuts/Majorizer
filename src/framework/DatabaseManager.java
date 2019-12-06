package framework;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Map;

import database.DatabaseColumn;
import database.DatabaseColumn.ColumnType;
import database.DatabaseInterface;
import database.DatabaseTable;
import database.JSONDatabase;
import database.JSONDatabase.JSONDatabaseConnectionException;
import framework.Curriculum.CurriculumType;
import framework.RequiredCourses.RequiredCourse;
import framework.RequiredCourses.RequiredCourseGroup;
import framework.RequiredCourses.RequiredCourseNode;
import utils.Two;

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
	
	public static final String PRE_REC_TYPE_LIST = "PRE_REC_LIST";
	public static final String PRE_REC_TYPE_COURSE = "COURSE";

	public static final String CURRICULUM_TYPE_MAJOR = "MAJOR";
	public static final String CURRICULUM_TYPE_MINOR = "MINOR";
	public static final String CURRICULUM_TYPE_LIST = "SUB_CURRICULUM";
	public static final String CURRICULUM_TYPE_COURSE = PRE_REC_TYPE_COURSE;
	
	public static final int REQUEST_NEW_ID = -1;
	
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
	
	
	// database get functions:
	
	public static Advisor getAdvisor(String username) {
		ArrayList<Map<String, Object>> results = DATABASE.queryEntry(USERS_TABLE, (m) -> {
			if(m.get("username").equals(username))
				return true;
			return false;
		});
		
		if(results.size() == 0)
			return null;
		
		Map<String, Object> m = results.get(0);
		
		return mapToAdvisor(m);
	}
	
	public static Advisor getAdvisor(int userID) {
		ArrayList<Map<String, Object>> results = DATABASE.queryEntry(USERS_TABLE, (m) -> {
			if(m.get("userID").equals(userID))
				return true;
			return false;
		});
		
		if(results.size() == 0)
			return null;
		
		Map<String, Object> m = results.get(0);
		
		return mapToAdvisor(m);
	}
	
	
	public static User authenticate(String username, String password) {	
		Advisor advisor = getAdvisor(username);
		if(advisor != null) return advisor;
		return getStudent(username);
	}
	
	public static ArrayList<Course> searchCourse(String searchString, String ... dates) {
		ArrayList<Course> courses = new ArrayList<>();
		ArrayList<Map<String, Object>> courseResults = new ArrayList<>();
		courseResults = DATABASE.queryEntry(COURSES_TABLE, (m) -> {
			String retrievedCourse 		= (String) m.get("curriculumName");
			String retrievedCourseCode 	= (String) m.get("courseCode");
			if(retrievedCourse.contains(searchString) || retrievedCourseCode.contains(searchString)) 
				return true;
			return false;
		});
		
		for(Map<String, Object> result : courseResults) {
			Course course = mapToCourse(result);
			courses.add(course);
		}
		
		if(dates.length == 0)
			return courses;
		
		
		ArrayList<Course> courseWithDates = new ArrayList<>();
		for(Course course : courses) {
			ArrayList<String> courseDates = course.getTimesOffered();
			courseLoop : for(String courseDate : courseDates) {
				for(String date : dates) {
					if(date.equals(courseDate)) {
						courseWithDates.add(course);
						continue courseLoop;
					}
				}
			}
		}
		
		return courseWithDates;
	}

	public static ArrayList<Curriculum> searchCurriculum(String searchString) {
		ArrayList<Curriculum> curriculums = new ArrayList<>();
		ArrayList<Map<String, Object>> curriculumResults = new ArrayList<>();
		curriculumResults = DATABASE.queryEntry(CURRICULUMS_TABLE, (m) -> {
			if(m.get("type").equals(CURRICULUM_TYPE_LIST)) 
				return false;
			String retrievedCurriculums = (String) m.get("curriculumName");
			if(retrievedCurriculums.contains(searchString)) 
				return true;
			return false;
		});
		
		for(Map<String, Object> result : curriculumResults) {
			Curriculum curriculum = mapToCurriculum(result);
			curriculums.add(curriculum);
		}
		
		return curriculums;
	}
	
	public static Student getStudent(String username) {
		ArrayList<Map<String, Object>> userResults = DATABASE.queryEntry(USERS_TABLE, (m) -> {
			if(m.get("username").equals(username))
				return true;
			return false;
		});
		
		if(userResults.size() == 0)
			return null;
		
		Map<String, Object> userMap = userResults.get(0);
		return mapToStudent(userMap);
	}
	
	public static Student getStudent(int userID) {
		ArrayList<Map<String, Object>> userResults = DATABASE.queryEntry(USERS_TABLE, (m) -> {
			if(m.get("userID").equals(userID))
				return true;
			return false;
		});
		
		if(userResults.size() == 0)
			return null;
		
		Map<String, Object> userMap = userResults.get(0);
		return mapToStudent(userMap);
	}
	
	private static AcademicPlan getAcademicPlan(int studentID, String startSemester) {
		ArrayList<Map<String, Object>> curriculumResults;
		curriculumResults = DATABASE.queryEntry(STUDENTS_CURRICULUMS_TABLE, (m) -> {
			if(m.get("studentID").equals(studentID))
				return true;
			return false;
		});
		
		ArrayList<Integer> degreeIDs = new ArrayList<>();
		for(Map<String, Object> entry : curriculumResults) {
			int curriculumID = (int) entry.get("curriculumID");
			degreeIDs.add(curriculumID);
		}
		
		ArrayList<Map<String, Object>> selectedCourseResults;
		selectedCourseResults = DATABASE.queryEntry(STUDENT_TAKEN_COURSES_TABLE, (m) -> {
			if(m.get("studentID").equals(studentID))
				return true;
			return false;
		});
		
		Map<String, ArrayList<Integer>> selectedCourseIDs = new Hashtable<>();
		for(Map<String, Object> entry : selectedCourseResults) {
			int courseID = (int) entry.get("courseID");
			String semester = (String) entry.get("semester");
			
			ArrayList<Integer> courseList;
			if(!selectedCourseIDs.containsKey(semester)) {
				courseList = new ArrayList<>();
				selectedCourseIDs.put(semester, courseList);
			} else {
				courseList = selectedCourseIDs.get(semester);
			}
			
			courseList.add(courseID);
		}
		
		
		return new AcademicPlan(startSemester, degreeIDs, selectedCourseIDs, null);
	}
	
	public static Request getRequest(int requestID) {
		ArrayList<Map<String, Object>> requestResults;
		requestResults = DATABASE.queryEntry(REQUESTS_TABLE, (m) -> {
			if(m.get("requestID").equals(requestID)) return true;
			return false;
		});
		
		if(requestResults.isEmpty())
			return null;
		
		return mapToRequest(requestResults.get(0));
	}
	
	public static Curriculum getCurriculum(int curriculumID) {
		ArrayList<Map<String, Object>> curriculumResults;
		curriculumResults = DATABASE.queryEntry(CURRICULUMS_TABLE, (m) -> {
			if(m.get("curriculumID").equals(curriculumID)) return true;
			return false;
		});
		
		if(curriculumResults.size() == 0)
			return null;
		
		Curriculum curriculum = mapToCurriculum(curriculumResults.get(0));
		
		return curriculum;
	}
	
	public static Curriculum getCurriculum(String curriculumName) {
		ArrayList<Map<String, Object>> curriculumResults;
		curriculumResults = DATABASE.queryEntry(CURRICULUMS_TABLE, (m) -> {
			if(m.get("curriculumName").equals(curriculumName)) return true;
			return false;
		});
		
		if(curriculumResults.size() == 0)
			return null;
		
		Curriculum curriculum = mapToCurriculum(curriculumResults.get(0));
		
		return curriculum;
	}
	
	public static RequiredCourses getCurriculumCourses(int curriculumID) {
		ArrayList<Map<String, Object>> curriculumResults = DATABASE.queryEntry(CURRICULUMS_TABLE, (m) -> {
			if(m.get("curriculumID").equals(curriculumID)) return true;
			return false;
		});
		
		if(curriculumResults.isEmpty())
			return new RequiredCourses();
		
		int amtReq = (int) curriculumResults.get(0).get("amtReq");
		
		RequiredCourseNode root = getCurriculumNode(curriculumID, amtReq);
		return new RequiredCourses(root);
	}
	
	public static RequiredCourses getCurriculumCourses(String curriculumName) {
		
		ArrayList<Map<String, Object>> curriculumResults = DATABASE.queryEntry(COURSES_TABLE, (m) -> {
			if(m.get("curriculumName").equals(curriculumName)) return true;
			return false;
		});
		
		if(curriculumResults.isEmpty())
			return new RequiredCourses();
		
		int curriculumID = (int) curriculumResults.get(0).get("curriculumID");
		int amtReq = (int) curriculumResults.get(0).get("amtReq");
		
		RequiredCourseNode root = getCurriculumNode(curriculumID, amtReq);
		return new RequiredCourses(root);
	}
	
	private static RequiredCourseNode getCurriculumNode(int curriculumID, int amtReq) {
		ArrayList<Map<String, Object>> curriculumChildResults = DATABASE.queryEntry(CURRICULUM_COURSE_SELECTION_TABLE, (m) -> {
			if(m.get("curriculumID").equals(curriculumID)) return true;
			return false;
		});
		
		ArrayList<RequiredCourseNode> nodes = new ArrayList<>();
		
		for(Map<String, Object> childMap : curriculumChildResults) {
			String type = (String) childMap.get("type");
			int curriculumCourseID = (int) childMap.get("curriculumCourseID");
			
			if(type.equals(CURRICULUM_TYPE_LIST)) {
				RequiredCourseNode child = getCurriculumNode(curriculumCourseID);
				nodes.add(child);
			} else if (type.equals(CURRICULUM_TYPE_COURSE)) {
				RequiredCourse rc = new RequiredCourse(curriculumCourseID);
				nodes.add(rc);
			} else {
				throw new DatabaseFormatException(CURRICULUM_COURSE_SELECTION_TABLE, "type", type, CURRICULUM_TYPE_LIST, CURRICULUM_TYPE_COURSE);
			}
		}
		
		return new RequiredCourseGroup(amtReq, nodes);
	}
	
	private static RequiredCourseNode getCurriculumNode(int curriculumID) {
		
		ArrayList<Map<String, Object>> curriculumResults = DATABASE.queryEntry(CURRICULUMS_TABLE, (m) -> {
			if(m.get("curriculumID").equals(curriculumID)) return true;
			return false;
		});
		
		if(curriculumResults.isEmpty())
			return null;
		
		int amtReq = (int) curriculumResults.get(0).get("amtReq");
		
		
		return getCurriculumNode(curriculumID, amtReq);
	}
	
	public static Course getCourse(int courseID) {
		ArrayList<Map<String, Object>> courseResults;
		courseResults = DATABASE.queryEntry(COURSES_TABLE, (m) -> {
			if(m.get("courseID").equals(courseID)) return true;
			return false;
		});
		
		if(courseResults.size() == 0)
			return null;
		
		Course c = mapToCourse(courseResults.get(0));
		return c;
	}
	
	public static Course getCourse(String courseCode) {
		ArrayList<Map<String, Object>> courseResults;
		courseResults = DATABASE.queryEntry(COURSES_TABLE, (m) -> {
			if(m.get("courseCode").equals(courseCode)) return true;
			return false;
		});
		
		if(courseResults.size() == 0)
			return null;
		
		Course c = mapToCourse(courseResults.get(0));
		return c;
	}
	
	public static RequiredCourses getCoursePreRecs(int courseID) {
		ArrayList<Map<String, Object>> preReqResults = DATABASE.queryEntry(COURSES_PREREC_TABLE, (m) -> {
			if(m.get("courseID").equals(courseID)) return true;
			return false;
		});
		
		if(preReqResults.isEmpty())
			return new RequiredCourses();
		
		int preRecID = (int) preReqResults.get(0).get("preRecID");
		RequiredCourseNode root = getPreRecCourseNode(preRecID);
		return new RequiredCourses(root);
	}
	
	public static RequiredCourses getCoursePreRecs(String courseCode) {
		
		ArrayList<Map<String, Object>> courseResults = DATABASE.queryEntry(COURSES_TABLE, (m) -> {
			if(m.get("courseCode").equals(courseCode)) return true;
			return false;
		});
		
		if(courseResults.isEmpty())
			return new RequiredCourses();
		
		int courseID = (int) courseResults.get(0).get("courseID");
		
		return getCoursePreRecs(courseID);
	}
	
	private static RequiredCourseNode getPreRecCourseNode(int preRecID) {
		
		ArrayList<Map<String, Object>> preReqResults = DATABASE.queryEntry(PREREC_TABLE, (m) -> {
			if(m.get("preRecID").equals(preRecID)) return true;
			return false;
		});
		
		if(preReqResults.isEmpty())
			return null;
		
		int amtReq = (int) preReqResults.get(0).get("amtReq");
		
		
		
		ArrayList<Map<String, Object>> preReqChildResults = DATABASE.queryEntry(PREREC_COURSE_SELECTION_TABLE, (m) -> {
			if(m.get("preRecID").equals(preRecID)) return true;
			return false;
		});
		
		
		ArrayList<RequiredCourseNode> nodes = new ArrayList<>();
		
		for(Map<String, Object> childMap : preReqChildResults) {
			String type = (String) childMap.get("type");
			int preRecCourseID = (int) childMap.get("preRecCourseID");
			
			if(type.equals(PRE_REC_TYPE_LIST)) {
				RequiredCourseNode child = getPreRecCourseNode(preRecCourseID);
				nodes.add(child);
			} else if (type.equals(PRE_REC_TYPE_COURSE)) {
				RequiredCourse rc = new RequiredCourse(preRecCourseID);
				nodes.add(rc);
			} else {
				throw new DatabaseFormatException(PREREC_COURSE_SELECTION_TABLE, "type", type, PRE_REC_TYPE_LIST, PRE_REC_TYPE_COURSE);
			}
		}
		
		return new RequiredCourseGroup(amtReq, nodes);
	}
	
	
	
	// database save functions:
	
	public static void saveStudent(Student student) {
		if(student.getUserID() == REQUEST_NEW_ID) {
			
		} else {
			
		}
	}
	
	public static void saveAdvisor(Advisor advisor) {
		
	}
	
	public static void saveCurriculum(Curriculum curriculum) {
		
	}
	
	public static void saveCourse(Student student) {
		
	}
	
	public static void saveRequest(Request request) {
		
	}
	
//	public static void saveStudent(Student student, boolean isNew) {
//	final int userID = student.getUserID();
//	String username = student.getUsername();
//	
//	if(isNew) {
//		DATABASE.queryEntry(USERS_TABLE, (m) -> {
//			if(m.get("username").equals(username))
//				throw new UsernameNotUniqueException(username);
//			return false;
//		});
//		
//		int newUserID = findUniqueID(USERS_TABLE, "userID");
//		student.setUserID(newUserID);
//		
//		Map<String, Object> map = userToMap(student);
//		
//		DATABASE.addEntry(USERS_TABLE, 0, map);
//		
//		map.clear();
//		map.put("userID", newUserID);
//		map.put("isNewStudent", student.isStudentNew());
//		DATABASE.addEntry(STUDENTS_TABLE, 0, map);
//	} else {
//		Map<String, Object> map = userToMap(student);
//		
//		DATABASE.setEntry(USERS_TABLE, (m) -> {
//			if((int) m.get("userID") == userID)
//				return map;
//			return null;
//		});
//		
//		map.clear();
//		map.put("userID", userID);
//		map.put("isNewStudent", student.isStudentNew());
//		
//		DATABASE.setEntry(STUDENTS_TABLE, (m) -> {
//			if((int) m.get("userID") == userID)
//				return map;
//			return null;
//		});
//	}
//}

//	public static void saveAdvisor(Advisor advisor, boolean isNew) {
//	final int userID = advisor.getUserID();
//	String username = advisor.getUsername();
//	
//	if(isNew) {
//		DATABASE.queryEntry(USERS_TABLE, (m) -> {
//			if(m.get("username").equals(username))
//				throw new UsernameNotUniqueException(username);
//			return false;
//		});
//		
//		int newUserID = findUniqueID(USERS_TABLE, "userID");
//		advisor.setUserID(newUserID);
//		
//		Map<String, Object> map = userToMap(advisor);
//		
//		DATABASE.addEntry(USERS_TABLE, 0, map);
//		
//		map.clear();
//		map.put("userID", newUserID);
//		DATABASE.addEntry(ADVISORS_TABLE, 0, map);
//	} else {
//		Map<String, Object> map = userToMap(advisor);
//		
//		DATABASE.setEntry(USERS_TABLE, (m)->{
//			if((int) m.get("userID") == userID)
//				return map;
//			return null;
//		});
//		
//		map.clear();
//		map.put("userID", userID);
//		
//		DATABASE.setEntry(ADVISORS_TABLE, (m)-> {
//			if((int) m.get("userID") == userID)
//				return map;
//			return null;
//		});
//	}
//}
	
	// database remove functions:

	public static void removeStudent(Student student) {
		
	}
	
	public static void removeAdvisor(Advisor advisor) {
		
	}
	
	public static void removeCurriculum(Curriculum curriculum) {
		
	}
	
	public static void removeCourse(Student student) {
		
	}
	
	public static void removeRequest(Request request) {
		
	}
	
	
	
	
	
	
	// maps to object functions:
	
	
	private static Advisor mapToAdvisor(Map<String, Object> userMap) {
		int userID = (int) userMap.get("userID");
		String universityID = (String) userMap.get("universityID");
		String firstName = (String) userMap.get("firstName");
		String lastName = (String) userMap.get("lastName");
		String username = (String) userMap.get("username");
		String password = (String) userMap.get("password");
		
		ArrayList<Integer> adviseeIDs = new ArrayList<>();
		ArrayList<Map<String, Object>> adivsorResults = DATABASE.queryEntry(ADVISOR_STUDENTS_TABLE, (m) -> {
			if(m.get("advisorID").equals(userID))
				return true;
			return false;
		});
		for(Map<String, Object> entry : adivsorResults) {
			int studentID = (int) entry.get("studentID");
			adviseeIDs.add(studentID);
		}
		
		
		ArrayList<Integer> requestIDs = new ArrayList<>();
		ArrayList<Map<String, Object>> requestResults = DATABASE.queryEntry(REQUESTS_TABLE, (m) -> {
			for(int i = 0; i < adviseeIDs.size(); i++) {
				int studentID = adviseeIDs.get(i);
				if(m.get("studentID").equals(studentID))
					return true;
			}
			return false;
		});
		
		for(Map<String, Object> entry : requestResults) {
			int requestID = (int) entry.get("requestID");
			requestIDs.add(requestID);
		}
		
		return new Advisor(userID, universityID, firstName, lastName, username, password, adviseeIDs, requestIDs);
	}
	
	private static Student mapToStudent(Map<String, Object> userMap) {
		int userID = (int) userMap.get("userID");
		String universityID = (String) userMap.get("universityID");
		String firstName = (String) userMap.get("firstName");
		String lastName = (String) userMap.get("lastName");
		String username = (String) userMap.get("username");
		String password = (String) userMap.get("password");
		
		ArrayList<Map<String, Object>> studentResults = DATABASE.queryEntry(STUDENTS_TABLE, (m) -> {
			if(m.get("userID").equals(userID))
				return true;
			return false;
		});
		
		if(studentResults.size() == 0)
			return null;
		
		Map<String, Object> studentMap = studentResults.get(0);
		
		boolean isNewStudent = (Boolean) studentMap.get("isNewStudent");
		String startSemester = (String) studentMap.get("startSemester");
		AcademicPlan academicPlan = getAcademicPlan(userID, startSemester);
		
		return new Student(userID, universityID, firstName, lastName, username, password, isNewStudent, academicPlan);
	}
	
	private static Request mapToRequest(Map<String, Object> requestMap) {
		int requestID = (int) requestMap.get("requestID");
		int studentID = (int) requestMap.get("studentID");
		int curriculumID = (int) requestMap.get("curriculumID");
		boolean isAdding = (boolean) requestMap.get("isAdding");
		
		return new Request(requestID, studentID, isAdding, curriculumID);
	}
	
	private static Curriculum mapToCurriculum(Map<String, Object> curriculumMap) {
		int curriculumID = (int) curriculumMap.get("curriculumID");
		String curriculumName = (String) curriculumMap.get("curriculumName");
		String type = (String) curriculumMap.get("type");
		int amtReq = (int) curriculumMap.get("amtReq");
		
		CurriculumType curriculumType;
		
		if(type.equals(CURRICULUM_TYPE_MAJOR)) {
			curriculumType = CurriculumType.MAJOR;
		} else if (type.equals(CURRICULUM_TYPE_MINOR)) {
			curriculumType = CurriculumType.MINOR;
		} else {
			throw new DatabaseFormatException(CURRICULUMS_TABLE, "type", type, CURRICULUM_TYPE_MAJOR, CURRICULUM_TYPE_MINOR);
		}
		
		RequiredCourseNode root = getCurriculumNode(curriculumID, amtReq);
		RequiredCourses requiredCourses = new RequiredCourses(root);
		
		return new Curriculum(curriculumID, curriculumName, curriculumType, requiredCourses);
	}
	
	private static Course mapToCourse(Map<String, Object> courseMap) {
		int courseID = (int) courseMap.get("courseID");
		String courseCode = (String) courseMap.get("courseCode");
		String courseName = (String) courseMap.get("courseName");
		
		ArrayList<Map<String, Object>> availabilityResults;
		availabilityResults = DATABASE.queryEntry(COURSE_AVAILABLITY_TABLE, (m) -> {
			if(m.get("courseID").equals(courseID)) return true;
			return false;
		});
		
		ArrayList<String> timesOffered = new ArrayList<>();
		for(Map<String, Object> availabilityMap : availabilityResults) {
			String timeOffered = (String) availabilityMap.get("timeOffered");
			timesOffered.add(timeOffered);
		}
		
		RequiredCourses requiredCourses = getCoursePreRecs(courseID);
		
		return new Course(courseID, courseCode, courseName, timesOffered, requiredCourses);
	}
	
	
	
	// object to maps functions:
	
	
	private static Map<String, Object> userToMap(User user) {
		int userID = user.getUserID();
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
	
	private static Map<String, Object> studentToMap(Student user) {
		int userID = user.getUserID();
		boolean isNewStudent = user.isStudentNew();
		String startSemester = user.getAcademicPlan().getStartSemester();
		
		HashMap<String, Object> map = new HashMap<>();
		map.put("userID", userID); 
		map.put("isNewStudent", isNewStudent);
		map.put("startSemester", startSemester);
		
		return map;
	}
	
	private static Map<String, Object> advisorToMap(Advisor advisor) {
		int userID = advisor.getUserID();
		
		HashMap<String, Object> map = new HashMap<>();
		map.put("userID", userID); 
		
		return map;
	}
	
	private static ArrayList<Map<String, Object>> advisorStudentsToMaps(Advisor advisor) {
		int advisorID = advisor.getUserID();
		
		ArrayList<Map<String, Object>> maps = new ArrayList<>();
		for(int studentID : advisor.getAdviseeIDs()) {
			HashMap<String, Object> map = new HashMap<>();
			map.put("advisorID", advisorID); 
			map.put("studentID", studentID); 
			maps.add(map);
		}
		
		return maps;
	}
	
	private static Map<String, Object> requestToMap(Request request) {
		int requestID = request.getRequestID();
		int studentID = request.getStudentID();
		int curriculumID = request.getCurriculumID();
		boolean isAdding = request.isAdding();
		
		HashMap<String, Object> map = new HashMap<>();
		map.put("requestID", requestID); 
		map.put("studentID", studentID);
		map.put("curriculumID", curriculumID);
		map.put("isAdding", isAdding);
		
		return map;
	}
	
	private static ArrayList<Map<String, Object>> studentCurriculumsToMaps(Student student) {
		int studentID = student.getUserID();
		
		ArrayList<Map<String, Object>> maps = new ArrayList<>();
		for(int curriculumID : student.getAcademicPlan().getDegreeIDs()) {
			HashMap<String, Object> map = new HashMap<>();
			map.put("studentID", studentID); 
			map.put("curriculumID", curriculumID); 
			maps.add(map);
		}
		
		return maps;
	}
	
	private static Two<ArrayList<Map<String, Object>>> curriculumsToMaps(Curriculum curriculum) {
		int curriculumID = curriculum.getCurriculumID();
		String curriculumName = curriculum.getName();
		CurriculumType curriculumType = curriculum.getCurriculumType();
		String type = curriculumType == CurriculumType.MAJOR ? CURRICULUM_TYPE_MAJOR : CURRICULUM_TYPE_MINOR;
		RequiredCourses rc = curriculum.getRequiredCourses();
		
		int amtReq = -1;
		ArrayList<Map<String, Object>> curriculumMaps = new ArrayList<>();
		ArrayList<Map<String, Object>> curriculumCourseMaps = new ArrayList<>();
		
//		if(rc.hasRequirements()) {
//			HashMap<String, Object> map = new HashMap<>();
//			map.put("", );
//			
//		} else {
//			
//		}
		
//		ArrayList<Map<String, Object>> maps = new ArrayList<>();
//		for(int curriculumID : student.getAcademicPlan().getDegreeIDs()) {
//			HashMap<String, Object> map = new HashMap<>();
//			map.put("studentID", studentID); 
//			map.put("curriculumID", curriculumID); 
//			map.containsKey("curriculumID");
//			maps.add(map);
//		}
		
		return new Two<>(curriculumMaps, curriculumCourseMaps);
	}
	
	
	// other utility database functions:
	
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
		
		if(id == REQUEST_NEW_ID)
			throw new RuntimeException("DATABASE FULL");
		
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
	
	@SuppressWarnings("serial")
	public static class DatabaseFormatException extends RuntimeException {
		private static final String MSG_FORMAT = "The table \"%s\" at column \"%s\" is given the type %s, but must";
		
		private DatabaseFormatException(String msg) {
			super(msg);
		}
		
		private DatabaseFormatException(DatabaseTable table, String column, String givenArg, String ... availableArgs) {
			super(genString(table, column, givenArg, availableArgs));
		}
		
		private static String genString(DatabaseTable table, String column, String givenArg, String ... availableArgs) {
			String tableName = table.getFullString(JSONDatabase.TABLE_SEPARATOR);
			String msg = String.format(MSG_FORMAT, tableName, column, givenArg);
			
			if(availableArgs.length > 1) {
				int argLength = availableArgs.length;
				msg += " either be " + availableArgs[0];
				for(int i = 1; i < argLength - 1; i++)
					msg += ", " + availableArgs[i];
				msg += ", or " + availableArgs[argLength - 1];
			} else if (availableArgs.length == 1) {
				msg += " be " + availableArgs[0];
			} else {
				return "Error";
			}
			return msg;
		}
	}
}
