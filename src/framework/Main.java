package framework;



import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.Set;

import database.DatabaseTable;
import gui.UserInterface;
import javafx.application.Application;
import javafx.stage.Stage;
import scheduler.Scheduler;
import scheduler.SchedulerGraph;
import scheduler.SchedulerCourse;
import utils.ResourceLoader;

public class Main {
	
	public static void main(String args[]) throws Exception {
		// Describe the command line args
		System.out.println("Main [database_file [create_database]]\n"
				+ "Enter arguments on the command line or using Run->Run Configurations->Arguments\n"
				+ "database_file : string, optional. The location of the database folder\n"
				+ "create_database : string, optional. If equal to 'create' a new database will be created");
		
		// Initialize the database based on the args
		if(args.length >= 1) {
			DatabaseManager.connect(args[0]);
			if(args.length >= 2 && args[1].equals("create")) {
					// create the database
					DatabaseManager.initializeDatabase();
					loadSampleData();
			}
		} else {
			throw new RuntimeException("[ERROR] Can not procede -- please specify a uri to the database as the first command line argument.");	
		}
		
		testDatabase();
//		startUI(args);
		testRequiredCourses();
		SchedulerGraph graph = testSchedulerGraph();
		testScheduler(graph);

//		testCourseInfoLoad();
	}
	
	public static void loadSampleData() {
		System.out.println("--Loading Sample Data to Database--");
		try {
			File csFile = ResourceLoader.getYAMLFile("databaseSampleData/curriculums.yaml");
			File advisorsFile = ResourceLoader.getYAMLFile("databaseSampleData/advisors.yaml");
			File courseInfoFile = ResourceLoader.getYAMLFile("databaseSampleData/courses.yaml");
			File studentsFile = ResourceLoader.getYAMLFile("databaseSampleData/students.yaml");
			
			DatabaseLoader.loadCourses(courseInfoFile);
			DatabaseLoader.loadCurriculums(csFile);
			DatabaseLoader.loadStudents(studentsFile);
			DatabaseLoader.loadAdvisors(advisorsFile);
			
			System.out.println("--Loading Complete--");
		} catch (IOException e) {
			System.err.println("--Loading Error--");
			e.printStackTrace();
		}
	}
	
	//public static void startUI(String args[])	{
	//	UserInterface.callLaunch(args);
	//}
	
	public static void testDatabase() {
//		 Print the database contents
		DatabaseTable[] tables = DatabaseManager.listTables();
		for(DatabaseTable table : tables) {
			DatabaseManager.printTable(table);
			System.out.println();
		}
		
		System.out.println("Getting student with username: cutugnma");
		Student student = DatabaseManager.getStudent("cutugnma");
		AcademicPlan pl = student.getAcademicPlan();
		ArrayList<Integer> degreeIDs = pl.getDegreeIDs();
		System.out.println("DegreeIDs:");
		for(Integer ids : degreeIDs)
			System.out.println("  ->  " + ids);
		System.out.println();
		Map<String, ArrayList<Integer>> courseIDMap = pl.getSelectedCourseIDs();
		Set<String> semesterSet = courseIDMap.keySet();
		System.out.println("Course setup: ");
		for(String semester : semesterSet) {
			System.out.println("  ->  " + semester);
			ArrayList<Integer> selectedCourseIDs = courseIDMap.get(semester);
			for(Integer courseID : selectedCourseIDs) {
				String courseCode = DatabaseManager.getCourse(courseID).getCourseCode();
				System.out.println("  --->  " + courseCode);
			}
		}
		
	}
	
	public static void testRequiredCourses() {
		try {
			File f = ResourceLoader.getYAMLFile("computer_science_major.yaml");
			RequiredCourses required = RequiredCourses.load(f);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void testScheduler(SchedulerGraph graph) throws Exception {
		Scheduler scheduler = new Scheduler();
		ArrayList<SchedulerCourse> added = new ArrayList<SchedulerCourse>();
		ArrayList<SchedulerCourse> dropped = new ArrayList<SchedulerCourse>();
		System.out.println(graph.root.getName());
		try{
			System.out.println(scheduler.schedule(graph, added, dropped));
		} catch (Exception e) {
			System.out.println("Failed to create schedule because");
			throw(e);
		}
	}
	

	public static SchedulerGraph testSchedulerGraph(){
		Curriculum cs = DatabaseManager.getCurriculum("Computer Science Major");
		SchedulerGraph CSRequirementsGraph = new SchedulerGraph(cs.getRequiredCourses());
		Curriculum ce = DatabaseManager.getCurriculum("Computer Engineering Major");
		SchedulerGraph CERequirementsGraph = new SchedulerGraph(ce.getRequiredCourses());
		CSRequirementsGraph.mergeGraphs(CERequirementsGraph);
		Curriculum science = DatabaseManager.getCurriculum("Test Science Major");
		SchedulerGraph ScienceRequirementsGraph = new SchedulerGraph(science.getRequiredCourses());
		//System.out.println(ScienceRequirementsGraph.getAsGraphVis());
		System.out.println(CSRequirementsGraph.getAsGraphVis());
		System.out.println("merged");
		return CSRequirementsGraph;
		//return ScienceRequirementsGraph;
	}
	
//	public static void testCourseInfoLoad(){
//		try {
//			File f = ResourceLoader.getYAMLFile("course_info.yaml");
//			System.out.println(f);
//			DatabaseManager database = new DatabaseManager();
//			database.loadStudents(f);
//			System.out.println("Loaded students");
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//	}
	
	
}
