package framework;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.regex.PatternSyntaxException;

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
import utils.MapArrayMap;
import utils.MapMap;
import utils.ReferenceHashMap;
import utils.Two;

public class DatabaseManager {
	
	private static final DatabaseInterface DATABASE = new JSONDatabase();
	
	public static final DatabaseTable USERS_TABLE    					= new DatabaseTable("Users");
	
	public static final DatabaseTable STUDENTS_TABLE 					= USERS_TABLE.subTable("Students");
	public static final DatabaseTable STUDENT_CURRICULUMS_TABLE 		= STUDENTS_TABLE.subTable("Student_Curriculums");
	public static final DatabaseTable STUDENT_TAKEN_COURSES_TABLE 		= STUDENTS_TABLE.subTable("Student_Taken_Courses");
	
	public static final DatabaseTable ADVISORS_TABLE 					= USERS_TABLE.subTable("Advisors");
	public static final DatabaseTable ADVISOR_STUDENTS_TABLE 			= ADVISORS_TABLE.subTable("Advisor_Students");
	public static final DatabaseTable REQUESTS_TABLE 					= ADVISORS_TABLE.subTable("Requests");
	
	
	public static final DatabaseTable CURRICULUMS_TABLE 				= new DatabaseTable("Curriculums");
	public static final DatabaseTable CURRICULUM_COURSE_SELECTION_TABLE = CURRICULUMS_TABLE.subTable("Curriculum_Course_Selection");
	
	
	public static final DatabaseTable COURSES_TABLE 					= new DatabaseTable("Courses");
	public static final DatabaseTable COURSE_AVAILABLITY_TABLE			= COURSES_TABLE.subTable("Course_Availablity");
	public static final DatabaseTable COURSE_PREREC_TABLE 				= COURSES_TABLE.subTable("Courses_PreRecs");
	public static final DatabaseTable PREREC_TABLE 						= COURSE_PREREC_TABLE.subTable("PreRecs");
	public static final DatabaseTable PREREC_COURSE_SELECTION_TABLE 	= COURSE_PREREC_TABLE.subTable("PreRecs_Course_Selection");
	
	public static final String PRE_REC_TYPE_LIST = "PRE_REC_LIST";
	public static final String PRE_REC_TYPE_COURSE = "COURSE";

	public static final String CURRICULUM_TYPE_MAJOR = "MAJOR";
	public static final String CURRICULUM_TYPE_MINOR = "MINOR";
	public static final String CURRICULUM_TYPE_LIST = "SUB_CURRICULUM";
	public static final String CURRICULUM_TYPE_COURSE = PRE_REC_TYPE_COURSE;
	
//	private static final String COURSE_NAME = "courseName";
//	private static final String PREREC_NAME = "preRecCourses";
//	private static final String TIMES_OFFERED = "timesOffered";
//	private static final String NUM_REQUIRED = "numRequired";
//	private static final int DEFAULT_ID = 0;
	
	public static final int REQUEST_NEW_ID = -1;
	public static final String NULL_ENTRY_STRING = "!<NULL>!";
	
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
		
		DATABASE.createTable(STUDENT_CURRICULUMS_TABLE);
		DATABASE.addColumns(STUDENT_CURRICULUMS_TABLE,
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
		
		DATABASE.createTable(COURSE_PREREC_TABLE);
		DATABASE.addColumns(COURSE_PREREC_TABLE,
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
	
	
	
	
//	public static void loadStudents(File yamlFile) {
//		Yaml yaml = new Yaml(); // Create the parser
//		try (
//			InputStream is = new FileInputStream(yamlFile);	
//		) {
//			LinkedHashMap<String, LinkedHashMap> specification = yaml.load(is); // Parse the YAML representation
//			System.out.println(specification);
//			System.out.println(specification.keySet());
//			LinkedHashMap courseInfo;
//			ArrayList<String> timesOffered; 
//			ArrayList<String> preRecCourses;
//			String courseName;
//			Course newCourse;
//			RequiredCourses requiredCourses;
//			
//			for(String courseCode : specification.keySet()) {// You can probably just iterate through all of the values rather than doing this crap
//				courseInfo = specification.get(courseCode);
//				if((int)courseInfo.get(NUM_REQUIRED) == 0) { // This implies that it is a course, not a group
//				    System.out.println(courseInfo);
//				    timesOffered = (ArrayList<String>) courseInfo.get(TIMES_OFFERED);
//				    preRecCourses = (ArrayList<String>) courseInfo.get(PREREC_NAME);
//				    courseName = (String) courseInfo.get(COURSE_NAME);
//				    requiredCourses = new RequiredCourses(parseRequiredCourses(specification, courseCode));
//				    newCourse = new Course(DEFAULT_ID, courseCode, courseName, timesOffered, requiredCourses);//TODO turn this null into a RequiredCourseObject
//				    saveCourse(newCourse);
//				}
//			}
//		} catch (FileNotFoundException e) {
//			e.printStackTrace();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//	}
//	
//	private static RequiredCourseNode parseRequiredCourses(LinkedHashMap<String, LinkedHashMap> description, String tag) {
//		if(description.containsKey(tag)) {
//			LinkedHashMap newNode = (LinkedHashMap) description.get(tag);// Get the new node from the description
//			int numRequired = (int) newNode.get(NUM_REQUIRED);
//			ArrayList<RequiredCourseNode> createdRequirements = new ArrayList<RequiredCourseNode>();
//			System.out.println(newNode);
//			ArrayList<String> requirementNames = (ArrayList<String>) newNode.get(PREREC_NAME);// Get all the names of things it depends on
//			System.out.println(requirementNames);
//			for (String rec : requirementNames) {
//				createdRequirements.add(parseRequiredCourses(description, rec));// Add all of the requirements recursively
//			}
//			return new RequiredCourseGroup(numRequired, createdRequirements);
//		}else {
//			System.out.println("This is the tag which wasn't found: " + tag);
//			return null;
//		}
//	}
	
	
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
		ArrayList<Map<String, Object>> userResults;
		userResults = DATABASE.queryEntry(USERS_TABLE, (m) -> {
			if(m.get("username").equals(username))
				return true;
			return false;
		});
		
		
		if(userResults.size() == 0)
			return null;
		
		int id = (int) userResults.get(0).get("userID");
		
		ArrayList<Map<String, Object>> advisorResults;
		advisorResults = DATABASE.queryEntry(ADVISORS_TABLE, (m) -> {
			if(m.get("userID").equals(id))
				return true;
			return false;
		});
		
		boolean isStudent = advisorResults.isEmpty();
		
		if(isStudent) {
			Student student = getStudent(username);
			if(student != null && student.getPassword().equals(password))
				return getStudent(username);
		} else {
			Advisor advisor = getAdvisor(username);
			if(advisor != null && advisor.getPassword().equals(password))
				return getAdvisor(username);
		}
		return null;
	}
	
	
	
	
	public static ArrayList<Course> searchCourse(String searchString, String ... dates) {
		final String searchStringMod = makeIgnoreCaseRegex(searchString);
		if(searchStringMod == null)
			return new ArrayList<>();
		
		ArrayList<Course> courses = new ArrayList<>();
		ArrayList<Map<String, Object>> courseResults = new ArrayList<>();
		courseResults = DATABASE.queryEntry(COURSES_TABLE, (m) -> {
			String retrievedCourse 		= (String) m.get("courseName");
			String retrievedCourseCode 	= (String) m.get("courseCode");
			boolean matchedName = false;
			boolean matchedCode = false;
			try {
				matchedName = retrievedCourse.matches(searchStringMod);
				matchedCode = retrievedCourseCode.matches(searchStringMod);
			} catch (PatternSyntaxException e) {}
			return matchedName || matchedCode;
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
		final String searchStringMod = makeIgnoreCaseRegex(searchString);
		if(searchStringMod == null)
			return new ArrayList<>();
		
		ArrayList<Curriculum> curriculums = new ArrayList<>();
		ArrayList<Map<String, Object>> curriculumResults = new ArrayList<>();
		curriculumResults = DATABASE.queryEntry(CURRICULUMS_TABLE, (m) -> {
			if(m.get("type").equals(CURRICULUM_TYPE_LIST)) 
				return false;
			String retrievedCurriculums = (String) m.get("curriculumName");
			boolean nameMatches = false;
			try {
				nameMatches = retrievedCurriculums.matches(searchStringMod);
			} catch (PatternSyntaxException e) {}
			return nameMatches;
		});
		for(Map<String, Object> result : curriculumResults) {
			Curriculum curriculum = mapToCurriculum(result);
			curriculums.add(curriculum);
		}
		return curriculums;
	}
	
	private static String makeIgnoreCaseRegex(String comparingString) {
		if (comparingString == null || comparingString.equals(""))
			return null;
		
		int stringLength = comparingString.length();
		int alphabeticalChars = 0;
		for(int i = 0; i < stringLength; i++) {
			char c = comparingString.charAt(i);
			if(Character.isAlphabetic(c))
				alphabeticalChars++;
		}
		int length = alphabeticalChars * 4 + stringLength - alphabeticalChars + 4;
		char[] regexChars = new char[length];
		
		
		int regexIndex = 0;
		int stringIndex = 0;
		regexChars[regexIndex++] = '.';
		regexChars[regexIndex++] = '*';

		while(regexIndex < length - 2) {
			char c = comparingString.charAt(stringIndex++);
			if(Character.isAlphabetic(c)) {
				char lower = Character.toLowerCase(c);
				char upper = Character.toUpperCase(c);
				regexChars[regexIndex++] = '[';
				regexChars[regexIndex++] = lower;
				regexChars[regexIndex++] = upper;
				regexChars[regexIndex++] = ']';
			} else {
				regexChars[regexIndex++] = c;
			}
		}
		regexChars[regexIndex++] = '.';
		regexChars[regexIndex++] = '*';
		
		return new String(regexChars);
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
		curriculumResults = DATABASE.queryEntry(STUDENT_CURRICULUMS_TABLE, (m) -> {
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
		return new RequiredCourses(curriculumID, root);
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
		return new RequiredCourses(curriculumID, root);
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
		
		return new RequiredCourseGroup(curriculumID, amtReq, nodes);
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
		ArrayList<Map<String, Object>> preReqResults = DATABASE.queryEntry(COURSE_PREREC_TABLE, (m) -> {
			if(m.get("courseID").equals(courseID)) return true;
			return false;
		});
		
		if(preReqResults.isEmpty())
			return new RequiredCourses();
		
		int preRecID = (int) preReqResults.get(0).get("preRecID");
		RequiredCourseNode root = getPreRecCourseNode(preRecID);
		return new RequiredCourses(preRecID, root);
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
		
		return new RequiredCourseGroup(preRecID, amtReq, nodes);
	}
	
	
	
	
	
	// database save functions:
	
	public static boolean saveStudent(Student student) {
		Map<String, Object> userMap = userToMap(student);
		boolean isUnique = isUniqueMap(USERS_TABLE, userMap, "userID", "username", "universityID");
		if(!isUnique)
			return false;
		ArrayList<Map<String, Object>> storedUserMaps = loadMaps(USERS_TABLE, "userID", student.getUserID());
		saveUniqueMap(USERS_TABLE, userMap, storedUserMaps, "userID");
		student.setUserID((int) userMap.get("userID"));
		
		Map<String, Object> studentMap = studentToMap(student);
		ArrayList<Map<String, Object>> storedStudentMaps = loadMaps(STUDENTS_TABLE, "userID", student.getUserID());
		saveUniqueMap(STUDENTS_TABLE, studentMap, storedStudentMaps, "userID");
		
		ArrayList<Map<String, Object>> studentCurriculumsMaps = studentCurriculumsToMaps(student);
		ArrayList<Map<String, Object>> storedStudentCurriculumsMaps = loadMaps(STUDENT_CURRICULUMS_TABLE, "studentID", student.getUserID());
		saveIdenticalMaps(STUDENT_CURRICULUMS_TABLE, studentCurriculumsMaps , storedStudentCurriculumsMaps, "studentID", "curriculumID");
		
		ArrayList<Map<String, Object>> studentTakenCoursesMaps = studentTakenCoursesToMap(student);
		ArrayList<Map<String, Object>> storedStudentTakenCoursesMaps = loadMaps(STUDENT_TAKEN_COURSES_TABLE, "studentID", student.getUserID());
		saveIdenticalMaps(STUDENT_TAKEN_COURSES_TABLE, studentTakenCoursesMaps, storedStudentTakenCoursesMaps, "studentID", "courseID", "semester");
		
		return true;
	}	
	
	
	public static boolean saveAdvisor(Advisor advisor) {
		Map<String, Object> userMap = userToMap(advisor);
		boolean isUnique = isUniqueMap(USERS_TABLE, userMap, "userID", "username", "universityID");
		if(!isUnique)
			return false;
		ArrayList<Map<String, Object>> storedUserMaps = loadMaps(USERS_TABLE, "userID", advisor.getUserID());
		saveUniqueMap(USERS_TABLE, userMap, storedUserMaps, "userID");
		advisor.setUserID((int) userMap.get("userID"));
		
		Map<String, Object> advisorMap = advisorToMap(advisor);
		ArrayList<Map<String, Object>> storedAdvisorMaps = loadMaps(ADVISORS_TABLE, "userID", advisor.getUserID());
		saveUniqueMap(ADVISORS_TABLE, advisorMap, storedAdvisorMaps, "userID");
		
		ArrayList<Map<String, Object>> advisorStudentsMaps = advisorStudentsToMaps(advisor);
		ArrayList<Map<String, Object>> storedadvisorStudentsMapsMaps = loadMaps(ADVISOR_STUDENTS_TABLE, "advisorID", advisor.getUserID());
		saveIdenticalMaps(ADVISOR_STUDENTS_TABLE, advisorStudentsMaps, storedadvisorStudentsMapsMaps, "advisorID", "studentID");
		
		
		
		return true;
	}
	
	public static boolean saveCurriculum(Curriculum curriculum) {
		MapMap<String, Object> nodeDependacies = new MapMap<>();
		MapArrayMap<String, Object> branchDependacies = new MapArrayMap<>();
		Two<ArrayList<Map<String, Object>>> curriculumMap = curriculumToMaps(curriculum, nodeDependacies, branchDependacies);
		Two<ArrayList<Map<String, Object>>> storedCurriculumMaps = loadTreeMap(CURRICULUMS_TABLE, "curriculumID", CURRICULUM_COURSE_SELECTION_TABLE, "curriculumCourseID", CURRICULUM_TYPE_LIST, curriculum.getCurriculumID());
		
		saveTreeMapNodes(CURRICULUMS_TABLE, new Two<>(curriculumMap.first, storedCurriculumMaps.first), nodeDependacies, branchDependacies, "curriculumID", "curriculumCourseID");
		saveIdenticalMaps(CURRICULUM_COURSE_SELECTION_TABLE, curriculumMap.second, storedCurriculumMaps.second, "curriculumID", "type", "curriculumCourseID");
		
		int curriculumID = (int) curriculumMap.first.get(0).get("curriculumID");
		curriculum.setCurriculumID(curriculumID);
		
		RequiredCourses requiredCourses = getCurriculumCourses(curriculumID);
		curriculum.setRequiredCourses(requiredCourses);
		
		return true;
	}
	
	public static boolean saveCourse(Course course) {
		Map<String, Object> courseMap = courseToMap(course);
		boolean isUnique = isUniqueMap(COURSES_TABLE, courseMap, "courseID", "courseCode");
		if(!isUnique)
			return false;
		
		ArrayList<Map<String, Object>> storedCourseMaps = loadMaps(COURSES_TABLE, "courseID", course.getCourseID());
		saveUniqueMap(COURSES_TABLE, courseMap, storedCourseMaps, "courseID");
		course.setCourseID((int) courseMap.get("courseID"));
		
		ArrayList<Map<String, Object>> courseTimeMaps = courseTimesToMap(course);
		ArrayList<Map<String, Object>> storedCourseTimesMaps = loadMaps(COURSE_AVAILABLITY_TABLE, "courseID", course.getCourseID());
		saveIdenticalMaps(COURSE_AVAILABLITY_TABLE, courseTimeMaps, storedCourseTimesMaps, "courseID", "timeOffered");
		
		Map<String, Object> coursePreRec = coursePreReqToMap(course);
		ArrayList<Map<String, Object>> storedCourseRecsMaps = loadMaps(COURSE_PREREC_TABLE, "preRecID", course.getRequiredCourses().getRequiredCourseID());
		saveUniqueMap(COURSE_PREREC_TABLE, coursePreRec, storedCourseRecsMaps, "preRecID");
		int preRecID = (int) coursePreRec.get("preRecID");
		course.getRequiredCourses().setRequiredCoursesID(preRecID);

		MapMap<String, Object> nodeDependacies = new MapMap<>();
		MapArrayMap<String, Object> branchDependacies = new MapArrayMap<>();
		Two<ArrayList<Map<String, Object>>> coursePreRecs = coursePreReqToMaps(course, nodeDependacies, branchDependacies);
		Two<ArrayList<Map<String, Object>>> storedPreRecs = loadTreeMap(PREREC_TABLE, "preRecID", PREREC_COURSE_SELECTION_TABLE, "preRecCourseID", PRE_REC_TYPE_LIST, preRecID);
		saveTreeMapNodes(PREREC_TABLE, new Two<>(coursePreRecs.first, storedPreRecs.first), nodeDependacies, branchDependacies, "preRecID", "preRecCourseID");
		saveIdenticalMaps(PREREC_COURSE_SELECTION_TABLE, coursePreRecs.second, storedPreRecs.second, "preRecID", "type", "preRecCourseID");
		RequiredCourses rc = getCoursePreRecs(course.getCourseID());
		course.setPreRecCourses(rc);
		
		return true;
	}
	
	public static boolean saveRequest(Request request) {
		Map<String, Object> map = requestToMap(request);
		ArrayList<Map<String, Object>> storedMaps = loadMaps(REQUESTS_TABLE, "requestID", request.getRequestID());
		saveUniqueMap(REQUESTS_TABLE, map, storedMaps, "requestID");
		request.setRequestID((int) map.get("requestID"));
		
		return true;
	}
	
	
	
	
	
	// database remove functions:

	public static void removeStudent(Student student) {
		int studentID = student.getUserID();
		removeUniqueEntries(USERS_TABLE, "userID", studentID);
		removeUniqueEntries(STUDENTS_TABLE, "userID", studentID);
		removeUniqueEntries(ADVISOR_STUDENTS_TABLE, "studentID", studentID);
		removeUniqueEntries(STUDENT_CURRICULUMS_TABLE, "studentID", studentID);
		removeUniqueEntries(STUDENT_TAKEN_COURSES_TABLE, "studentID", studentID);
		removeUniqueEntries(REQUESTS_TABLE, "studentID", studentID);
		student.setUserID(REQUEST_NEW_ID);
	}
	
	public static void removeAdvisor(Advisor advisor) {
		int advisorID = advisor.getUserID();
		removeUniqueEntries(USERS_TABLE, "userID", advisorID);
		removeUniqueEntries(ADVISORS_TABLE, "userID", advisorID);
		removeUniqueEntries(ADVISOR_STUDENTS_TABLE, "advisorID", advisorID);
		advisor.setUserID(REQUEST_NEW_ID);
	}
	
	public static void removeCurriculum(Curriculum curriculum) {
		int curriculumID = curriculum.getCurriculumID();
		removeTreeEntries(CURRICULUMS_TABLE, "curriculumID", CURRICULUM_COURSE_SELECTION_TABLE, "curriculumCourseID", CURRICULUM_TYPE_LIST, curriculumID);
		removeUniqueEntries(STUDENT_CURRICULUMS_TABLE, "curriculumID", curriculumID);
		removeUniqueEntries(REQUESTS_TABLE, "curriculumID", curriculumID);
		curriculum.setCurriculumID(REQUEST_NEW_ID);
		curriculum.getRequiredCourses().traverseTree((id) -> REQUEST_NEW_ID);
	}
	
	public static void removeCourse(Course course) {
		int courseID = course.getCourseID();
		int preRecID = course.getRequiredCourses().getRequiredCourseID();
		removeUniqueEntries(COURSES_TABLE, "courseID", courseID);
		removeUniqueEntries(COURSE_AVAILABLITY_TABLE, "courseID", courseID);
		removeUniqueEntries(COURSE_PREREC_TABLE, "courseID", courseID);
		removeUniqueEntries(STUDENT_TAKEN_COURSES_TABLE, "courseID", courseID);
		removeTreeEntries(PREREC_TABLE, "preRecID", PREREC_COURSE_SELECTION_TABLE, "preRecCourseID", PRE_REC_TYPE_LIST, preRecID);
		
		removeUniqueEntriesByType(PREREC_COURSE_SELECTION_TABLE, "preRecCourseID", courseID, PRE_REC_TYPE_COURSE);
		removeUniqueEntriesByType(CURRICULUM_COURSE_SELECTION_TABLE, "curriculumCourseID", courseID, CURRICULUM_TYPE_COURSE);
		
		course.setCourseID(REQUEST_NEW_ID);
		course.getRequiredCourses().traverseTree((id) -> REQUEST_NEW_ID);
	}
	
	public static void removeRequest(Request request) {
		removeUniqueEntries(REQUESTS_TABLE, "requestID", request.getRequestID());
		request.setRequestID(REQUEST_NEW_ID);
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
		RequiredCourses requiredCourses = new RequiredCourses(curriculumID, root);
		
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
	
	private static Map<String, Object> studentToMap(Student student) {
		int userID = student.getUserID();
		boolean isNewStudent = student.isStudentNew();
		String startSemester = student.getAcademicPlan().getStartSemester();
		
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
	
	private static Two<ArrayList<Map<String, Object>>> curriculumToMaps(Curriculum curriculum, MapMap<String, Object> nodeDependancies, MapArrayMap<String, Object> branchDependancies) {
		int curriculumID = curriculum.getCurriculumID();
		String curriculumName = curriculum.getName();
		CurriculumType curriculumType = curriculum.getCurriculumType();
		String type = curriculumType == CurriculumType.MAJOR ? CURRICULUM_TYPE_MAJOR : CURRICULUM_TYPE_MINOR;
		RequiredCourses rc = curriculum.getRequiredCourses();
		
		ArrayList<Map<String, Object>> curriculumMaps = new ArrayList<>();
		ArrayList<Map<String, Object>> curriculumCourseMaps = new ArrayList<>();
		Two<ArrayList<Map<String, Object>>> maps = new Two<>(curriculumMaps, curriculumCourseMaps);
		
		if(rc.hasRequirements()) {
			RequiredCourseNode root = rc.getRootCourseNode();
			curriculumToMaps(maps, nodeDependancies, branchDependancies, null, root);
		}
		
		Map<String, Object> map;
		if(maps.first.isEmpty()) {
			map = new ReferenceHashMap<>();
			boolean hasReq = !maps.second.isEmpty();
			map.put("amtReq", hasReq? 1 : 0);
			maps.first.add(map);
		} else {
			map = maps.first.get(0);
		}
		 
		map.put("curriculumID", curriculumID);
		map.put("curriculumName", curriculumName);
		map.put("type", type);
		
		return maps;
	}
	
	private static boolean curriculumToMaps(Two<ArrayList<Map<String, Object>>> maps, MapMap<String, Object> nodeDependancies, MapArrayMap<String, Object> branchDependancies, Map<String, Object> parentMap, RequiredCourseNode requiredCourseNode) {
		if(requiredCourseNode instanceof RequiredCourseGroup) {
			RequiredCourseGroup group = (RequiredCourseGroup) requiredCourseNode;
			int curriculumID = group.getNodeID();
			int amtReq = group.getAmtMustChoose();
			
			Map<String, Object> map = new ReferenceHashMap<>();
			map.put("curriculumID", curriculumID);
			map.put("curriculumName", "");
			map.put("type", CURRICULUM_TYPE_LIST);
			map.put("amtReq", amtReq);
			maps.first.add(map);
			nodeDependancies.put(map, parentMap);
			ArrayList<Map<String, Object>> branchMaps = new ArrayList<>();
			branchDependancies.put(map, branchMaps);
			
			for(RequiredCourseNode childNode : group) {
				Map<String, Object> branchMap = new ReferenceHashMap<>();
				boolean isCourse = curriculumToMaps(maps, nodeDependancies, branchDependancies, branchMap, childNode);
				String type = isCourse? CURRICULUM_TYPE_COURSE : CURRICULUM_TYPE_LIST;
				
				branchMap.put("curriculumID", curriculumID);
				branchMap.put("type", type);
				branchMap.put("curriculumCourseID", childNode.getNodeID());
				maps.second.add(branchMap);
				branchMaps.add(branchMap);
			}
			return false;
		} else {
			return true;
		}
	}
	
	private static Map<String, Object> courseToMap(Course course) {
		int courseID = course.getCourseID();
		String courseCode = course.getCourseCode();
		String courseName = course.getCourseName();
		
		Map<String, Object> map = new HashMap<>();
		
		map.put("courseID", courseID);
		map.put("courseCode", courseCode);
		map.put("courseName", courseName);
		
		return map;
	}
	
	private static ArrayList<Map<String, Object>> courseTimesToMap(Course course) {
		int courseID = course.getCourseID();
		
		ArrayList<Map<String, Object>> maps = new ArrayList<>();
		for(String time : course.getTimesOffered()) {
			
			Map<String, Object> map = new HashMap<>();
			
			map.put("courseID", courseID);
			map.put("timeOffered", time);
			maps.add(map);
		}	
		return maps;
	}
	
	private static ArrayList<Map<String, Object>> studentTakenCoursesToMap(Student student) {
		int studentID = student.getUserID();
		
		Map<String, ArrayList<Integer>> selectedCourses = student.getAcademicPlan().getSelectedCourseIDs();
		Set<String> semesters = selectedCourses.keySet();
		
		ArrayList<Map<String, Object>> takenCoursesMaps = new ArrayList<>();
		
		for(String semester : semesters) {
			ArrayList<Integer> courses = selectedCourses.get(semester);
			for(int courseID : courses) {
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("studentID", studentID);
				map.put("courseID", courseID);
				map.put("semester", semester);
				takenCoursesMaps.add(map);
			}
		}
		return takenCoursesMaps;
	}
	
	private static Map<String, Object> coursePreReqToMap (Course course) {
		int courseID = course.getCourseID();
		int preRecID;
		
		RequiredCourses rc = course.getRequiredCourses();
		preRecID = rc.getRequiredCourseID();
		
		Map<String, Object> map = new HashMap<>();
		map.put("courseID", courseID);
		map.put("preRecID", preRecID);
		
		return map;
	}
	
	private static Two<ArrayList<Map<String, Object>>> coursePreReqToMaps (Course course, MapMap<String, Object> nodeDependacies, MapArrayMap<String, Object> branchDependacies) {
		RequiredCourses requiredCourses = course.getRequiredCourses();
		
		ArrayList<Map<String, Object>> preRecNodes = new ArrayList<>();
		ArrayList<Map<String, Object>> preRecBranches = new ArrayList<>();
		Two<ArrayList<Map<String, Object>>> maps = new Two<ArrayList<Map<String,Object>>>(preRecNodes, preRecBranches);
		
		if(requiredCourses.hasRequirements()) {
			coursePreReqToMaps(maps, nodeDependacies, branchDependacies, null, requiredCourses.getRootCourseNode());
		} else {
			Map<String, Object> map = new ReferenceHashMap<>();
			map.put("preRecID", requiredCourses.getRequiredCourseID());
			map.put("amtReq", 0);
			maps.first.add(map);
		}
		return maps;
	}
	
	private static boolean coursePreReqToMaps (Two<ArrayList<Map<String, Object>>> maps, MapMap<String, Object> nodeDependacies, MapArrayMap<String, Object> branchDependacies, Map<String, Object> parentMap, RequiredCourseNode requiredCourseNode) {
		if(requiredCourseNode instanceof RequiredCourseGroup) {
			RequiredCourseGroup group = (RequiredCourseGroup) requiredCourseNode;
			
			int preRecID = group.getNodeID();
			int amtReq = group.getAmtMustChoose();
			
			Map<String, Object> map = new ReferenceHashMap<>();
			map.put("preRecID", preRecID);
			map.put("amtReq", amtReq);
			maps.first.add(map);
			nodeDependacies.put(map, parentMap);
			ArrayList<Map<String, Object>> branchMaps = new ArrayList<>();
			branchDependacies.put(map, branchMaps);
			
			for(RequiredCourseNode node : group) {
				Map<String, Object> branchMap = new ReferenceHashMap<>();
				branchMaps.add(branchMap);
				boolean isCourse = coursePreReqToMaps (maps, nodeDependacies, branchDependacies, branchMap, node);
				
				String type = isCourse ? PRE_REC_TYPE_COURSE : PRE_REC_TYPE_LIST;
				int preRecCourseID = node.getNodeID();
				
				branchMap.put("preRecID", preRecID);
				branchMap.put("type", type);
				branchMap.put("preRecCourseID", preRecCourseID);
				maps.second.add(branchMap);
			}
			
			return false;
		} else {
			return true;
		}
	}
	
	
	
	
	
	
	// other utility database functions:
	
	private static void removeTreeEntries(DatabaseTable nodeTable, String columnNodeIDName, DatabaseTable branchTable, String columnBranchChildIDName, String typeNode, int rootID) {
		Two<ArrayList<Map<String, Object>>> maps = loadTreeMap(nodeTable, columnNodeIDName, branchTable, columnBranchChildIDName, typeNode, rootID);
		
		HashSet<Integer> nodeIDs = new HashSet<>();
		for(Map<String, Object> map : maps.first)
			nodeIDs.add((int) map.get(columnNodeIDName));
		
		DATABASE.removeEntries(nodeTable, (m) -> {
			if(nodeIDs.contains(m.get(columnNodeIDName)))
				return true;
			return false;
		});
		
		DATABASE.removeEntries(nodeTable, (m) -> {
			if(nodeIDs.contains(m.get(columnNodeIDName)))
				return true;
			return false;
		});
	}
	
	
	private static void removeUniqueEntries(DatabaseTable table, String columnIDName, int id) {
		DATABASE.removeEntries(table, (m) -> {
			if(m.get(columnIDName).equals(id))
				return true;
			return false;
		});
	}
	
	private static void removeUniqueEntriesByType(DatabaseTable table, String columnIDName, int id, String typeValue) {
		DATABASE.removeEntries(table, (m) -> {
			if(m.get(columnIDName).equals(id) && m.get("type").equals(typeValue))
				return true;
			return false;
		});
	}
	
	
	private static boolean isUniqueMap(DatabaseTable table, Map<String, Object> map, String columIDName, String ... columnNames) {
		int id = (int) map.get(columIDName);
		ArrayList<String> columnNamesToCheck = new ArrayList<>(); 
		for (String columnName : columnNames)
			if(!map.get(columnName).equals(NULL_ENTRY_STRING))
				columnNamesToCheck.add(columnName);
		
		
		if(id == REQUEST_NEW_ID) {
			ArrayList<Map<String, Object>> results = DATABASE.queryEntry(table, (m)-> {
				for(String columnName : columnNamesToCheck)
					if(m.get(columnName).equals(map.get(columnName)))
						return true;
				return false;
			});
			if(results.isEmpty())
				return true;
		} else {
			ArrayList<Map<String, Object>> results = DATABASE.queryEntry(table, (m)-> {
				if(m.get(columIDName).equals(id))
					return false;
				for(String columnName : columnNamesToCheck)
					if(m.get(columnName).equals(map.get(columnName)))
						return true;
				return false;
			});
			if(results.isEmpty())
				return true;
		}
		return false;
	}
	
	private static ArrayList<Map<String, Object>> loadMaps(DatabaseTable table, String columnIDName, int id) {
		if(id == REQUEST_NEW_ID)
			return new ArrayList<>();
		
		ArrayList<Map<String, Object>> maps;
		maps = DATABASE.queryEntry(table, (e) -> {
			if(e.get(columnIDName).equals(id))
				return true;
			return false;
		});
		return maps;
	}
	
	private static Two<ArrayList<Map<String, Object>>> loadTreeMap(DatabaseTable nodeTable, String columnNodeIDName, DatabaseTable branchTable, String columnBranchChildIDName, String typeNode, int rootID) {
		ArrayList<Map<String, Object>> nodeMaps;
		ArrayList<Map<String, Object>> branchMaps;
		
		nodeMaps = DATABASE.queryEntry(nodeTable, (e) -> {
			if(e.get(columnNodeIDName).equals(rootID))
				return true;
			return false;
		});
		
		if(nodeMaps.isEmpty())
			return new Two<ArrayList<Map<String,Object>>>(nodeMaps, new ArrayList<>());
		
		Map<String, Object> root = nodeMaps.get(0);
		nodeMaps.clear();
		nodeMaps.add(root);
		
		branchMaps = DATABASE.queryEntry(branchTable, (e) -> {
			if(e.get(columnNodeIDName).equals(rootID))
				return true;
			return false;
		});
		
		ArrayList<Map<String, Object>> childBranchMaps = new ArrayList<>();
		
		for(Map<String, Object> branchMap : branchMaps) {
			String type = (String) branchMap.get("type");
			if(type.equals(typeNode)) {
				int childNodeID = (int) branchMap.get(columnBranchChildIDName);
				Two<ArrayList<Map<String, Object>>> childMaps = loadTreeMap(nodeTable, columnNodeIDName, branchTable, columnBranchChildIDName, typeNode, childNodeID);
				nodeMaps.addAll(childMaps.first);
				childBranchMaps.addAll(childMaps.second);
			}
		}
		
		branchMaps.addAll(childBranchMaps);
		
		Two<ArrayList<Map<String, Object>>> maps = new Two<>(nodeMaps, branchMaps);
		return maps;
	}
	
	private static void saveIdenticalMaps(DatabaseTable table, ArrayList<Map<String, Object>> maps, ArrayList<Map<String, Object>> storedMaps, String ... keys) {
		ArrayList<Map<String, Object>> elementsToCreate = new ArrayList<>();		
		ArrayList<Map<String, Object>> elementsToRemove = new ArrayList<>();
		
		elementsToCreate.addAll(maps);
		
		for(Map<String, Object> storedMap : storedMaps) {
			boolean containsMap = false;
			for(Map<String, Object> element : elementsToRemove) {
				boolean keysMatch = true;
				for(String key : keys) {
					if(!storedMap.get(key).equals(element.get(key))) {
						keysMatch = false;
						break;
					}
				}
				if(keysMatch) {
					elementsToCreate.remove(element);
					containsMap = true;
					break;
				}
			}
			if(!containsMap)
				elementsToRemove.add(storedMap);
		}
		
		DATABASE.removeEntries(table, (m)-> {
			for(Map<String, Object> element : elementsToRemove) {
				for(String key : keys) {
					if(!element.get(key).equals(m.get(key)))
						break;
					return true;
				}
			}
			return false;
		});
		
		for(Map<String, Object> map : elementsToCreate)
			DATABASE.addEntry(table, map);
	}
	
	private static void saveUniqueMap(DatabaseTable table, Map<String, Object> map, ArrayList<Map<String, Object>> storedMaps, String uniqueIDColumn) {
		ArrayList<Map<String, Object>> maps = new ArrayList<>();
		maps.add(map);
		saveUniqueMaps(table, maps, storedMaps, uniqueIDColumn);
	}
	
	private static void saveUniqueMaps(DatabaseTable table, ArrayList<Map<String, Object>> maps, ArrayList<Map<String, Object>> storedMaps, String uniqueIDColumn) {
		saveTreeMapNodes(table, new Two<>(maps, storedMaps), null, null, uniqueIDColumn, null);
	}
	
	private static void saveTreeMapNodes(DatabaseTable table, Two<ArrayList<Map<String, Object>>> nodeMaps, MapMap<String, Object> mapDependancies, MapArrayMap<String, Object> branchDependancies, String uniqueIDColumn, String columnBranchChildName) {
		ArrayList<Map<String, Object>> elementsToCreate = new ArrayList<>();
		ArrayList<Map<String, Object>> elementsToUpdate = new ArrayList<>();
		
		for(Map<String, Object> map : nodeMaps.first) {
			int id = (int) map.get(uniqueIDColumn);
			if (id == REQUEST_NEW_ID)
				elementsToCreate.add(map);
			else
				elementsToUpdate.add(map);
		} 
		
		ArrayList<Map<String, Object>> elementsToRemove = new ArrayList<>();
		for(Map<String, Object> storedMap : nodeMaps.second) {
			boolean containsMap = false;
			for(Map<String, Object> updatingMap : elementsToUpdate) {
				if(storedMap.get(uniqueIDColumn).equals(updatingMap.get(uniqueIDColumn))) {
					containsMap = true;
					break;
				}
			}
			if(!containsMap)
				elementsToRemove.add(storedMap);
		}
		
		DATABASE.removeEntries(table, (m)-> {
			for(Map<String, Object> map : elementsToRemove)
				if(m.get(uniqueIDColumn).equals(map.get(uniqueIDColumn)))
					return true;
			return false;
		});
		
		
		DATABASE.setEntry(table, (m) -> {
			Iterator<Map<String, Object>> iterator = elementsToUpdate.iterator();
			while(iterator.hasNext()) {
				Map<String, Object> map = iterator.next();
				int currentID = (int) map.get(uniqueIDColumn);
				if(m.get(uniqueIDColumn).equals(currentID)) {
					iterator.remove();
					return map;
				}
			}
			return null;
		});
		
		int[] uniqueIDs = findUniqueIDs(table, uniqueIDColumn, elementsToCreate.size());
		
		int idIndex = 0;
		if(mapDependancies != null && branchDependancies != null) { 
			for(Map<String, Object> map : elementsToCreate) {
				int id = uniqueIDs[idIndex++];
				map.put(uniqueIDColumn, id);
				Map<String, Object> mapDependancy = mapDependancies.get(map);
				if(mapDependancy != null)
					mapDependancy.put(columnBranchChildName, id);
				ArrayList<Map<String, Object>> branchDependancyMaps = branchDependancies.get(map);
				if(branchDependancyMaps != null)
					for(Map<String, Object> branch : branchDependancyMaps)
						branch.put(uniqueIDColumn, id);
			}
		} else {
			for(Map<String, Object> map : elementsToCreate)
				map.put(uniqueIDColumn, uniqueIDs[idIndex++]);
		}
			
		
		elementsToCreate.addAll(elementsToUpdate);
		for(Map<String, Object> map : elementsToCreate)
			DATABASE.addEntry(table, map);
	}
	
	private static int[] findUniqueIDs(DatabaseTable table, String column, int amt) {
		int[] ids = new int[amt];
		
		if(amt <= 0) return ids;
		
		HashSet<Integer> retrievedIDs = new HashSet<>();
		DATABASE.queryColumn(table, column, (e)-> {
			retrievedIDs.add((int) e);
			return false;
		});
		
		int idsFound = 0;
		int currentID = 0;
		while (idsFound < amt) {
			if(!retrievedIDs.contains(currentID))
				ids[idsFound++] = currentID;
			currentID++;
		}
		
		
		if(ids[amt-1] == REQUEST_NEW_ID)
			throw new RuntimeException("DATABASE FULL");
		
		return ids;
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
