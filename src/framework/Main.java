package framework;



import java.io.File;
import java.io.IOException;

import database.DatabaseTable;
import scheduler.Scheduler;
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
					sampleDataInit();
			}
		} else {
			throw new RuntimeException("[ERROR] Can not procede -- please specify a uri to the database as the first command line argument.");	
		}
		
		testDatabase();
//		testRequiredCourses();
//		testScheduler();
	}
	
	public static void sampleDataInit() {
		System.out.println("Adding Sample Data to Database:");
		
		Student max = new Student("0668364", "Massimiliano", "Cutugno", "cutugnma", "password", "Fall 2016");
		Student heet = new Student("0668365", "Heet", "Dave", "heetd", "password", "Fall 2016");
		
		System.out.println(DatabaseManager.saveStudent(max));
		System.out.println(DatabaseManager.saveStudent(heet));
	}
	
	public static void testDatabase() {
		// Print the database contents
		DatabaseTable[] tables = DatabaseManager.listTables();
		for(DatabaseTable table : tables) {
			DatabaseManager.printTable(table);
			System.out.println();
		}
		
		Student max = DatabaseManager.getStudent("cutugnma");
		System.out.println(max.getFirstName());
		System.out.println(max.getLastName());
		System.out.println(max.getUsername());
		System.out.println(max.getPassword());
		System.out.println(max.getUniversityID());
		System.out.println(max.getUserID());
		System.out.println(max.getAcademicPlan());
		Student heet = DatabaseManager.getStudent("heetd");
		System.out.println(heet.getFirstName());
		System.out.println(heet.getLastName());
		System.out.println(heet.getUsername());
		System.out.println(heet.getPassword());
		System.out.println(heet.getUniversityID());
		System.out.println(heet.getUserID());
		System.out.println(heet.getAcademicPlan());
		
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
		try {
			File f = ResourceLoader.getYAMLFile("computer_science_major.yaml");
			RequiredCourses required = RequiredCourses.load(f);
		} catch (IOException e) {
			e.printStackTrace();
		}
//		SchedulerGraph requirementsGraph = new SchedulerGraph(required);
	}
	
	
}
