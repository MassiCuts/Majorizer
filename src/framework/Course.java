package framework;

public class Course {
	private final int courseID;
	private final String courseCode;
	private final String courseName;
	private final RequiredCourses requiredCourses;
	
	public Course(int courseID, String courseCode, String courseName, RequiredCourses requiredCourses) {
		this.courseID = courseID;
		this.courseCode = courseCode;
		this.courseName = courseName;
		this.requiredCourses = requiredCourses;
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
		return requiredCourses;
	}
}
