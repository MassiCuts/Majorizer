package framework;

import java.util.ArrayList;
import java.util.Objects;

public class Course {
	private int courseID;
	private final String courseCode;
	private final String courseName;
	private ArrayList<String> timesOffered;
	private RequiredCourses preRecCourses;
	
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
	
	public void setCourseID(int courseID) {
		this.courseID = courseID;
	}
	
	public void setPreRecCourses(RequiredCourses preRecCourses) {
		this.preRecCourses = preRecCourses;
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
	
	public boolean isOfferedSemester(int whichSemester) {
		String r;
		if(whichSemester == 0) {
			r = "Fall.*[02468]";
		}else if(whichSemester == 1){
			r = "Spring.*[02468]";
		}else if(whichSemester == 2){
			r = "Fall.*[13579]";
		}else {
			r = "Spring.*[13579]";
		}
		for(String s : getTimesOffered()) {
			if(s.matches(r))
				return true;
		}
		return false;
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
