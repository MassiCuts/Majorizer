package framework;



import java.io.File;
import java.io.IOException;

import database.DatabaseTable;
import scheduler.Scheduler;
import scheduler.SchedulerGraph;
import utils.ResourceLoader;

public class Main {
	
	public static void main(String args[]) {
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

//		testRequiredCourses();
		testSchedulerGraph();
//		testCourseInfoLoad();
	}
	
	public static void loadSampleData() {
		System.out.println("--Loading Sample Data to Database--");
		try {
			File csFile = ResourceLoader.getYAMLFile("databaseSampleData/computer_science_major.yaml");
			File advisorsFile = ResourceLoader.getYAMLFile("databaseSampleData/advisors.yaml");
			File courseInfoFile = ResourceLoader.getYAMLFile("databaseSampleData/course_info.yaml");
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
	
	public static void testDatabase() {
//		 Print the database contents
		DatabaseTable[] tables = DatabaseManager.listTables();
		for(DatabaseTable table : tables) {
			DatabaseManager.printTable(table);
			System.out.println();
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
	
	public static void testScheduler() {
		Scheduler scheduler = new Scheduler(null);
	}
	
	public static void testSchedulerGraph(){
		Curriculum c = DatabaseManager.getCurriculum("Computer Science Major");
		SchedulerGraph requirementsGraph = new SchedulerGraph(c.getRequiredCourses());
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
