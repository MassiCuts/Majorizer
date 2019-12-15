package framework;



import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import database.DatabaseTable;
import gui.UserInterface;
import scheduler.Scheduler;
import scheduler.SchedulerCourse;
import scheduler.SchedulerGraph;
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
			if(args.length >= 2) {
				if(args[1].equals("create")) {
					// create the database
					DatabaseManager.initializeDatabase();
					loadSampleData();
				} else if (args[1].equals("UI")) {
					// open client majorizer
					startUI(args);
				} else if (args[1].equals("schedulerTest")) {
					// test scheduler graph
					SchedulerGraph graph = testSchedulerGraph();
					testScheduler(graph);
				}
			}
		} else {
			throw new RuntimeException("[ERROR] Can not procede -- please specify a uri to the database as the first command line argument.");	
		}
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
	
	public static void startUI(String args[])	{
		UserInterface.callLaunch(args);
	}
	
	public static void testDatabase() {
//		 Print the database contents
		DatabaseTable[] tables = DatabaseManager.listTables();
		for(DatabaseTable table : tables) {
			DatabaseManager.printTable(table);
			System.out.println();
		}
//		
//		System.out.println("Getting student with username: cutugnma");
//		Student student = DatabaseManager.getStudent("cutugnma");
//		AcademicPlan pl = student.getAcademicPlan();
//		ArrayList<Integer> degreeIDs = pl.getDegreeIDs();
//		System.out.println("DegreeIDs:");
//		for(Integer ids : degreeIDs)
//			System.out.println("  ->  " + ids);
//		System.out.println();
//		Map<String, ArrayList<Integer>> courseIDMap = pl.getSelectedCourseIDs();
//		Set<String> semesterSet = courseIDMap.keySet();
//		System.out.println("Course setup: ");
//		for(String semester : semesterSet) {
//			System.out.println("  ->  " + semester);
//			ArrayList<Integer> selectedCourseIDs = courseIDMap.get(semester);
//			for(Integer courseID : selectedCourseIDs) {
//				String courseCode = DatabaseManager.getCourse(courseID).getCourseCode();
//				System.out.println("  --->  " + courseCode);
//			}
//		}
		
		Course c = DatabaseManager.getCourse("PH132");		
		if(c != null)
			c.getRequiredCourses().printCourseCodes();
	}
	
	
	
	public static void testScheduler(SchedulerGraph graph) throws Exception {
		Scheduler scheduler = new Scheduler();
		ArrayList<SchedulerCourse> added = new ArrayList<SchedulerCourse>();
		ArrayList<SchedulerCourse> dropped = new ArrayList<SchedulerCourse>();
		ArrayList<SchedulerCourse> taken = new ArrayList<SchedulerCourse>();
		System.out.println(graph.root.getName());
		scheduler.setNumCourses(5);
		scheduler.setNumSemesters(15);
		try{
			System.out.println(scheduler.schedule(graph, added, dropped, taken));
			System.out.print("Finished Schedule");
		} catch (Exception e) {
			System.out.println("Failed to create schedule because" + e);
			throw(e);
		}
	}
	
	public static SchedulerGraph testSchedulerGraph(){
		Scanner sc = new Scanner(System.in);                 
		String i;
		Curriculum cs = DatabaseManager.getCurriculum("Computer Science Major");
		SchedulerGraph CSRequirementsGraph = new SchedulerGraph(cs.getRequiredCourses());
		Curriculum ce = DatabaseManager.getCurriculum("Computer Engineering Major");
		SchedulerGraph CERequirementsGraph = new SchedulerGraph(ce.getRequiredCourses());
		
		Curriculum math_minor = DatabaseManager.getCurriculum("Mathematics Minor");
		SchedulerGraph MathematicsRequirementsGraph = new SchedulerGraph(math_minor.getRequiredCourses());
		System.out.println(MathematicsRequirementsGraph.getAsGraphVis());
		System.out.println("Math Minor (Press any key to continue)");
		i = sc.next();
		
		Curriculum ee_minor = DatabaseManager.getCurriculum("Electrical Engineering Minor");
		SchedulerGraph EEMinorRequirementsGraph = new SchedulerGraph(ee_minor.getRequiredCourses());
		System.out.println(EEMinorRequirementsGraph.getAsGraphVis());
		System.out.println("EE Minor (Press any key to continue)");
		i = sc.next();
		
		Curriculum cs_minor = DatabaseManager.getCurriculum("Computer Science Minor");
		SchedulerGraph CSMinorRequirementsGraph = new SchedulerGraph(cs_minor.getRequiredCourses());
		System.out.println(CSMinorRequirementsGraph.getAsGraphVis());
		System.out.println("CS Minor (Press any key to continue)");
		i = sc.next();
		//CERequirementsGraph.mergeGraphs(CSRequirementsGraph);
		CSRequirementsGraph.mergeGraphs(CERequirementsGraph);
		//Curriculum science = DatabaseManager.getCurriculum("Test Science Major");
		//SchedulerGraph ScienceRequirementsGraph = new SchedulerGraph(science.getRequiredCourses());
		//System.out.println(ScienceRequirementsGraph.getAsGraphVis());
		System.out.println(CSRequirementsGraph.getAsGraphVis());
		/* will wait for input then assign it to the variable,
		 * in this case it will wait for an int.
		 */
		return CSRequirementsGraph;
	}
	
}
