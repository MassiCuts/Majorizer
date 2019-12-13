package framework;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.yaml.snakeyaml.Yaml;

import framework.Curriculum.CurriculumType;
import framework.RequiredCourses.RequiredCourse;
import framework.RequiredCourses.RequiredCourseGroup;
import framework.RequiredCourses.RequiredCourseNode;

public class DatabaseLoader {
	
	@SuppressWarnings("unchecked")
	public static void loadAdvisors(File yamlFile) throws IOException {
		Yaml yaml = new Yaml();
		try (
			InputStream is = new FileInputStream(yamlFile);	
		) {
			ArrayList<Object> rootLink = yaml.load(is);
			for(Object child : rootLink)
				loadAdvisor((Map<String, Object>) child);
		}
	}
	
	@SuppressWarnings("unchecked")
	private static void loadAdvisor(Map<String, Object> map) {
		String universityID = (String) map.get("universityID");
		String firstName 	= (String) map.get("firstName");
		String lastName 	= (String) map.get("lastName");
		String username 	= (String) map.get("username");
		String password		= (String) map.get("password");
		
		
		ArrayList<String> adviseeUsernames = (ArrayList<String>) map.get("advisees");
		ArrayList<Integer> adviseeIDs = new ArrayList<>();
		
		for(String adviseeUsername : adviseeUsernames) {
			Student student = DatabaseManager.getStudent(adviseeUsername);
			int studentID;
			if(student != null)
				studentID = student.getUserID();
			else 
				studentID = mkStudentGetID(adviseeUsername);
			adviseeIDs.add(studentID);
		}
		Advisor advisor = new Advisor(universityID, firstName, lastName, username, password, adviseeIDs);
		DatabaseManager.saveAdvisor(advisor);
	}
	
	@SuppressWarnings("unchecked")
	public static void loadStudents(File yamlFile) throws FileNotFoundException, IOException {
		Yaml yaml = new Yaml();
		try (
			InputStream is = new FileInputStream(yamlFile);	
		) {
			ArrayList<Object> rootLink = yaml.load(is);
			for(Object child : rootLink)
				loadStudent((Map<String, Object>) child);
		}
	}
	
	@SuppressWarnings("unchecked")
	private static void loadStudent(Map<String, Object> map) {
		String universityID 	= (String) map.get("universityID");
		String firstName 		= (String) map.get("firstName");
		String lastName 		= (String) map.get("lastName");
		String username 		= (String) map.get("username");
		String password			= (String) map.get("password");
		Boolean isNewStudent	= (boolean) map.get("isNewStudent");
		String  startSemester	= (String) map.get("startSemester");
		
		
		ArrayList<Map<String, String>> curriculumMaps  = (ArrayList<Map<String, String>>) map.get("curriculums");
		ArrayList<Map<String, Object>> takenCourseMaps = (ArrayList<Map<String, Object>>) map.get("takenCourses");
		
		ArrayList<Integer> degreeIDs = new ArrayList<>();
		for(Map<String, String> curriculumMap : curriculumMaps) {
			String name = (String) curriculumMap.get("name");
			String type = (String) curriculumMap.get("type");
			CurriculumType curriculumType = CurriculumType.getByName(type);
			
			Curriculum curriculum = DatabaseManager.getCurriculum(name);
			int curriculumID;
			if(curriculum != null)
				curriculumID = curriculum.getCurriculumID();
			else
				curriculumID = mkCurriculumGetID(name, curriculumType);
			
			degreeIDs.add(curriculumID);
		}
		
		Map<String, ArrayList<Integer>> selectedCoursesIDs = new HashMap<>();
		for(Map<String, Object> takenCourse : takenCourseMaps) {
			String semester = (String) takenCourse.get("semester");
			ArrayList<String> courseCodes = (ArrayList<String>) takenCourse.get("courses");
			
			ArrayList<Integer> courseIDs = new ArrayList<>();
			
			for(String courseCode : courseCodes) {
				Course course = DatabaseManager.getCourse(courseCode);
				int courseID;
				if(course != null)
					courseID = course.getCourseID();
				else
					courseID = mkCourseGetID(courseCode);
				
				courseIDs.add(courseID);
			}
			selectedCoursesIDs.put(semester, courseIDs);
		}
		
		
		AcademicPlan plan = new AcademicPlan(startSemester, degreeIDs, selectedCoursesIDs, null);
		
		Student student = new Student(universityID, firstName, lastName, username, password, isNewStudent, plan);
		DatabaseManager.saveStudent(student);
	}
	
	@SuppressWarnings("unchecked")
	public static void loadCourses(File yamlFile) throws FileNotFoundException, IOException {
		Yaml yaml = new Yaml();
		try (
			InputStream is = new FileInputStream(yamlFile);	
		) {
			ArrayList<Object> rootLink = yaml.load(is);
			for(Object child : rootLink)
				loadCourse((Map<String, Object>) child);
		}
	}
	
	@SuppressWarnings("unchecked")
	public static void loadCourse(Map<String, Object> map)  {
		String courseCode = (String) map.get("courseCode");
		String courseName = (String) map.get("courseName");
		ArrayList<String> timesOffered = (ArrayList<String>) map.get("timesOffered");
		RequiredCourseNode root = loadPreRecs(map);
		RequiredCourses requiredCourses = new RequiredCourses(root);

		int id = DatabaseManager.REQUEST_NEW_ID;
		Course storedCourse = DatabaseManager.getCourse(courseCode);
		if(storedCourse != null)
			id = storedCourse.getCourseID();
		
		Course course = new Course(id, courseCode, courseName, timesOffered, requiredCourses);
		DatabaseManager.saveCourse(course);
	}
	
	@SuppressWarnings("unchecked")
	public static RequiredCourseNode loadPreRecs(Map<String, Object> node) {
		ArrayList<Object> preRecCourses = (ArrayList<Object>) node.get("requirements");
		int amtReq = (int) node.get("amtReq");
		
		ArrayList<RequiredCourseNode> childNodes = new ArrayList<>();
		for(Object object : preRecCourses) {
			RequiredCourseNode child;
			if(object instanceof String) {
				String courseCode = (String) object;
				Course course = DatabaseManager.getCourse(courseCode);
				int courseID;
				if(course != null)
					courseID = course.getCourseID();
				else
					courseID = mkCourseGetID(courseCode);
				child = new RequiredCourse(courseID);
			} else {
				Map<String, Object> childNode = (Map<String, Object>) object;
				child = loadPreRecs(childNode);
			}
			childNodes.add(child);
		}
		return new RequiredCourseGroup(amtReq, childNodes);
	}
	
	
	@SuppressWarnings("unchecked")
	public static void loadCurriculums(File yamlFile) throws FileNotFoundException, IOException {
		Yaml yaml = new Yaml();
		try (
			InputStream is = new FileInputStream(yamlFile);	
		) {
			ArrayList<Object> rootLink = yaml.load(is);
			for(Object child : rootLink)
				loadCurriculum((Map<String, Object>) child);
		}
	}
	
	private static void loadCurriculum(Map<String, Object> map) {
		String name = (String) map.get("name");
		String typeName = (String) map.get("type");
		CurriculumType curriculumType = CurriculumType.getByName(typeName);
		RequiredCourseNode root = loadPreRecs(map);
		RequiredCourses requiredCourses = new RequiredCourses(root);
		
		Curriculum curriculum = new Curriculum(name, curriculumType, requiredCourses);
		DatabaseManager.saveCurriculum(curriculum);
	}
	
	
	private static int mkStudentGetID(String username) {
		Student student = new Student(DatabaseManager.NULL_ENTRY_STRING, DatabaseManager.NULL_ENTRY_STRING, DatabaseManager.NULL_ENTRY_STRING, username, DatabaseManager.NULL_ENTRY_STRING, DatabaseManager.NULL_ENTRY_STRING);
		DatabaseManager.saveStudent(student);
		return student.getUserID();
	}
	
	private static int mkCurriculumGetID(String name, CurriculumType type) {
		Curriculum curriculum = new Curriculum(name, type, new RequiredCourses());
		DatabaseManager.saveCurriculum(curriculum);
		return curriculum.getCurriculumID();
	}
	
	private static int mkCourseGetID(String courseCode) {
		Course course = new Course(courseCode, DatabaseManager.NULL_ENTRY_STRING, new ArrayList<>(), new RequiredCourses());
		DatabaseManager.saveCourse(course);
		return course.getCourseID();
	}
}
