package framework;

import java.util.Calendar;

import utils.Pair;

public class Majorizer {
	
	private static User user;
	
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
		return (getCurrentSemesterRaw().getLeft() == 0 ? "FALL" : "SPRING") + ' ' + getCurrentSemesterRaw().getRight();
	}

	public static int getStudentCurrentSemester()	{
		if(!user.isUserIsStudent())
			return 0;
		else {
			String startSemester[] = ((Student)user).getAcademicPlan().getStartSemester().split(" ");
			int startSeason = ((startSemester[0].equals("FALL")) ? 0 : 1);
			return (((getCurrentSemesterRaw().getRight() - Integer.parseInt(startSemester[1]))*2) + (getCurrentSemesterRaw().getLeft() - startSeason) - 1);
		}
	}

	//TESTING ONLY
	public static User authenticate(String username, String password)	{
		User Sean = new Advisor(0, "0000006","Sean", "Banerjee", "banerjsk", "password", null, null);
		AcademicPlan academicPlanL = new AcademicPlan("FALL 2017", null, null, null);
		User Lorenzo = new Student(0, "0755050", "Lorenzo", "Villani", "villanlj", "password", false, academicPlanL);
		
		if(username.equals(Sean.getUsername()) && password.equals(Sean.getPassword()))
			return Sean;
		if(username.equals(Lorenzo.getUsername()) && password.equals(Lorenzo.getPassword()))
			return Lorenzo;
		else
			return null;
	}
	
	//TESTING ONLY
	public static void main(String[] args)	{
		AcademicPlan academicPlanL = new AcademicPlan("FALL 2017", null, null, null);
		User Lorenzo = new Student(0, "0755050", "Lorenzo", "Villani", "villanlj", "password", false, academicPlanL);
		setUser(Lorenzo);
		
		
		System.out.println(getCurrentSemester());
		System.out.println(getStudentCurrentSemester());
	}
}