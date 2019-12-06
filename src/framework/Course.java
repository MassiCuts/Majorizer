package framework;

import java.util.ArrayList;
import java.util.Objects;

public class Course {
	private final int courseID;
	private final String courseCode;
	private final String courseName;
	private ArrayList<String> timesOffered;
	private final RequiredCourses preRecCourses;
	
	public Course(String courseCode, String courseName, ArrayList<String> timesOffered, RequiredCourses requiredCourses) {
		this(DatabaseManager.REQUEST_NEW_ID, courseCode, courseName, timesOffered, requiredCourses);
	}
	
	public Course(int courseID, String courseCode, String courseName, ArrayList<String> timesOffered, RequiredCourses requiredCourses) {
		this.courseID = courseID;
		this.courseCode = courseCode;
		this.courseName = courseName;
		this.timesOffered = timesOffered;
		this.preRecCourses = requiredCourses;
	}
	
	public int getCourseID() {
		return courseID;
	}
	
	public String getCourseCode() {
		return courseCode;
	}
	
	public String getCourseName() {
		return courseName;
	}
	
	public RequiredCourses getRequiredCourses() {
		return preRecCourses;
	}
	
	public ArrayList<String> getTimesOffered() {
		return timesOffered;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof Course)
			return courseID == ((Course) obj).courseID;
		return false;
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(courseID);
	}
	
	@Override
	public String toString() {
		return "Name: " + this.courseName;
	}
}
