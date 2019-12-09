package framework;

import java.util.Calendar;

import utils.Pair;

public class Majorizer {
	
	private static final String F = "Fall";
	private static final String S = "Spring";
	
	private static User user;
	public static int currentSelectedSemester;
	
	public static void setUser(User user)	{
		Majorizer.user = user;
	}
	
	public static User getUser()	{
		return user;
	}
	
	public static Pair<Integer, Integer> getCurrentSemesterRaw()	{
		Calendar calendar = Calendar.getInstance();
		int currentCalendarMonth = calendar.get(Calendar.MONTH);
		int currentSeason = 0;
		int currentYear = calendar.get(Calendar.YEAR);
				
		if(currentCalendarMonth >= Calendar.JULY && currentCalendarMonth <= Calendar.DECEMBER)
			currentSeason = 0;
		else if(currentCalendarMonth >= Calendar.JANUARY && currentCalendarMonth<= Calendar.JUNE)
			currentSeason = 1;
		
		return new Pair<Integer, Integer>(currentSeason, currentYear);
	}
	
	public static String getCurrentSemester()	{
		return (getCurrentSemesterRaw().getLeft() == 0 ? F : S) + ' ' + getCurrentSemesterRaw().getRight();
	}

	public static int getStudentCurrentSemester()	{
		if(!user.isUserIsStudent())
			return 0;
		else {
			String startSemester[] = ((Student)user).getAcademicPlan().getStartSemester().split(" ");
			int startSeason = ((startSemester[0].equals(F)) ? 0 : 1);
			
			return (((getCurrentSemesterRaw().getRight() - Integer.parseInt(startSemester[1]))*2) + (getCurrentSemesterRaw().getLeft() - startSeason) - 1);
		}
	}

	
	public static String getStudentCurrentSemesterString(int sem)	{
		String startSemester[] = ((Student)user).getAcademicPlan().getStartSemester().split(" ");
		int startSeason = ((startSemester[0].equals(F)) ? 1 : 0);
		int semNum = startSeason + sem;
		
		int ansYear = Integer.parseInt(startSemester[1]);
		ansYear += semNum/2;
		semNum = semNum%2;
		String ansSem = (semNum == 1) ? F : S;
		
		return ansSem + " " + ansYear;
	}
	
	public static User authenticate(String username, String password)	{
		System.out.println(username);
		System.out.println(password);
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
