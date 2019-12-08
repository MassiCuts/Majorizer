package framework;

public class Student extends User {
	
	private boolean isStudentNew;
	private AcademicPlan academicPlan;
	
	public Student(String universityID, String firstName, String lastName, String username, String password, String startSemester) {
		this(universityID, firstName, lastName, username, password, AcademicPlan.getEmptyPlan(startSemester));
	}
	
	public Student(String universityID, String firstName, String lastName, String username, String password, AcademicPlan academicPlan) {
		this(universityID, firstName, lastName, username, password, true, academicPlan);
	}
	
	public Student(String universityID, String firstName, String lastName, String username, String password, boolean isStudentNew, AcademicPlan academicPlan) {
		this(DatabaseManager.REQUEST_NEW_ID, universityID, firstName, lastName, username, password, isStudentNew, academicPlan);
	}
	
	public Student(int userID, String universityID, String firstName, String lastName, String username, String password, boolean isStudentNew, AcademicPlan academicPlan) {
		super(userID, universityID, firstName, lastName, username, password);
		this.isStudentNew = isStudentNew;
		this.academicPlan = academicPlan;
	}
	
	public boolean isStudentNew() {
		return isStudentNew;
	}
	
	public AcademicPlan getAcademicPlan() {
		return academicPlan;
	}
	
}
