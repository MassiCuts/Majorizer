package framework;

import java.util.Calendar;

import utils.Pair;

public class Majorizer {
	
	private static final String F = "Fall";
	private static final String S = "Spring";
	
	private static User user = null;
	private static Advisor advisor = null;
	private static Student student = null;

	public static void setUser(User user)	{
		Majorizer.user = user;
		if(user.isUserIsStudent())
			student = (Student) user;
		else
			advisor = (Advisor) user;
	}
	
	public static void setStudent(Student student) {
		Majorizer.student = student;
	}
	
	public static User getUser()	{
		return user;
	}
	
	public static Student getStudent() {
		return student;
	}
	
	public static Advisor getAdvisor() {
		return advisor;
	}
	
	public static Pair<Integer, Integer> getCurrentSemesterRaw()	{
		Calendar calendar = Calendar.getInstance();
		int currentCalendarMonth = calendar.get(Calendar.MONTH);
		int currentSeason = 0;
		int currentYear = calendar.get(Calendar.YEAR);
				
		if(currentCalendarMonth >= Calendar.JANUARY && currentCalendarMonth<= Calendar.JUNE)
			currentSeason = 0;
		else if(currentCalendarMonth >= Calendar.JULY && currentCalendarMonth <= Calendar.DECEMBER)
			currentSeason = 1;
		
		return new Pair<Integer, Integer>(currentSeason, currentYear);
	}
	
	public static String getCurrentSemester()	{
		Pair<Integer, Integer> currentSemester = getCurrentSemesterRaw();
		return (currentSemester.getLeft() == 0 ? S : F) + ' ' + currentSemester.getRight();
	}

	public static int getStudentCurrentSemesterIndex()	{
		if(!user.isUserIsStudent())
			return 0;
		else {
			String startSemester[] = student.getAcademicPlan().getStartSemester().split(" ");
			int startSeason = ((startSemester[0].equals(S)) ? 0 : 1);
			int startYear = Integer.parseInt(startSemester[1]);
			
			Pair<Integer, Integer> currentSemester = getCurrentSemesterRaw();
			int currentSeason = currentSemester.getLeft();
			int currentYear = currentSemester.getRight();
			int semesterIndex = (currentYear - startYear) * 2 - startSeason + currentSeason;
			
			return semesterIndex;
		}
	}

	
	public static String getStudentCurrentSemesterString(int sem)	{
		String startSemester[] = student.getAcademicPlan().getStartSemester().split(" ");
		int startSeason = ((startSemester[0].equals(S)) ? 0 : 1);
		int semNum = startSeason + sem;
		
		int ansYear = Integer.parseInt(startSemester[1]);
		ansYear += semNum/2;
		semNum = semNum%2;
		String ansSem = (semNum == 1) ? F : S;
		
		return ansSem + " " + ansYear;
	}
	
	public static User authenticate(String username, String password)	{
		return DatabaseManager.authenticate(username, password);
	}
	
	//TESTING ONLY
	public static void main(String[] args)	{
		AcademicPlan academicPlanL = new AcademicPlan("SPRING 2017", null, null, null);
		User Lorenzo = new Student(0, "0755050", "Lorenzo", "Villani", "villanlj", "password", false, academicPlanL);
		setUser(Lorenzo);
		
		for(int i = 0; i <= 7; ++i)
			getStudentCurrentSemesterString(i);
		
//		System.out.println(getCurrentSemester());
//		System.out.println(getStudentCurrentSemester());
	}
}
