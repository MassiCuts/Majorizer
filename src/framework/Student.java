package framework;

public class Student extends User {
	
	private String startSemesterDate;
	private boolean isStudentNew;
	
	public Student(int userID, String universityID, String firstName, String lastName, String username, String password, String startSemesterDate, boolean isStudentNew) {
		super(userID, universityID, firstName, lastName, username, password);
		this.startSemesterDate = startSemesterDate;
		this.isStudentNew = isStudentNew;
	}
	
	public String getStartSemesterDate() {
		return startSemesterDate;
	}
	
	public boolean isStudentNew() {
		return isStudentNew;
	}
}
