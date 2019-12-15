package framework;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Map;

import scheduler.Scheduler;
import scheduler.SchedulerCourse;
import scheduler.SchedulerGraph;
import utils.Pair;

public class Majorizer {
	
	private static final String F = "Fall";
	private static final String S = "Spring";
	
	private static User user = null;
	private static Advisor advisor = null;
	private static Student student = null;
	
	private static Scheduler scheduler = new Scheduler();
	
	public static void setUser(User user)	{
		Majorizer.user = user;
		if(user == null)
			return;
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
		if(student == null)
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

	public static ArrayList<Student> getAdvisees() {
		ArrayList<Student> students = new ArrayList<>();
		ArrayList<Integer> adviseeIDS = Majorizer.getAdvisor().getAdviseeIDs();
		
		for(int i = 0; i < adviseeIDS.size(); i++) {
			int id = adviseeIDS.get(i);
			Student student = DatabaseManager.getStudent(id);
			if (student != null)
				students.add(student);
		}
		
		return students;
	}
	
	public static ArrayList<Request> getRequests() {
		ArrayList<Request> requests = new ArrayList<>();
		ArrayList<Integer> requestIDs = Majorizer.getAdvisor().getRequestIDs();
		
		for(int i=0; i < requestIDs.size(); i++) {
			int id = requestIDs.get(i);
			Request request = DatabaseManager.getRequest(id);
			if(request != null)
				requests.add(request);
		}
		
		return requests;
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
	
	
	public static void scheduleForStudent() {
		SchedulerGraph curriculumGraph = student.getAcademicPlan().getCurriculumSchedulerGraph();
		
		ArrayList<SchedulerCourse> takenCourses = new ArrayList<>();
		ArrayList<SchedulerCourse> addedCourses = new ArrayList<>();
		
		Map<String, ArrayList<Integer>> studentCoursesMap = student.getAcademicPlan().getSelectedCourseIDs();
		int currentSemester = getStudentCurrentSemesterIndex();
		
		for (int i = 0; i < 8; i++) {
			String currentSemesterString = getStudentCurrentSemesterString(i);
			ArrayList<Integer> courses = studentCoursesMap.get(currentSemesterString);
			for(int courseID : courses) {
				Course c = DatabaseManager.getCourse(courseID);
				SchedulerCourse schedulerCourse = new SchedulerCourse(c);
				if(i < currentSemester) {
					schedulerCourse.addTakenSemester(i);
					takenCourses.add(schedulerCourse);
				} else {
					schedulerCourse.addToSemester(i);
					addedCourses.add(schedulerCourse);
				}
			}
		}
		
		
		ArrayList<ArrayList<String>> updatedCourses;
		try {
			updatedCourses = scheduler.schedule(curriculumGraph, addedCourses, new ArrayList<>(), takenCourses, currentSemester);
		} catch (Exception e) {
			return;
		}
		
		printInfo(updatedCourses, curriculumGraph);
	}
	
	public static void printInfo (ArrayList<ArrayList<String>> updatedCourses, SchedulerGraph curriculumGraph) {
		File f = new File("C:/Users/Massimiliano Cutugno/Desktop/test.txt");
		try (
			FileWriter fw = new FileWriter(f);
		){
			
			fw.append('\n');
			fw.append('\n');
			if(curriculumGraph != null)
				fw.append(curriculumGraph.getAsGraphVis());
			fw.append('\n');
			fw.append('\n');
			if(updatedCourses != null) {
				for(ArrayList<String> semester : updatedCourses) {
					fw.append(semester.toString());
					fw.append('\n');
				}
			}
			fw.flush();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	
	
	
	
}
