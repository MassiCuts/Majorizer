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
					sampleDataInit();
			}
		} else {
			throw new RuntimeException("[ERROR] Can not procede -- please specify a uri to the database as the first command line argument.");	
		}
		
		testDatabase();

//		testRequiredCourses();
//		testSchedulerGraph();
//		testCourseInfoLoad();
	}
	
	public static void sampleDataInit() {
		System.out.println("Adding Sample Data to Database:");
		
		Student max = new Student("0668364", "Massimiliano", "Cutugno", "cutugnma", "password", "Fall 2016");
		Student heet = new Student("0668365", "Heet", "Dave", "dheet", "password1", "Fall 2016");
		
		System.out.println(DatabaseManager.saveStudent(max));
		System.out.println(DatabaseManager.saveStudent(heet));
		
		Advisor banerjee = new Advisor("1", "Sean", "Banerjee", "sbanerjee", "password2", max.getUserID(), heet.getUserID());
		System.out.println(DatabaseManager.saveAdvisor(banerjee));
	}
	
	public static void testDatabase() {
		// Print the database contents
		DatabaseTable[] tables = DatabaseManager.listTables();
		for(DatabaseTable table : tables) {
			DatabaseManager.printTable(table);
			System.out.println();
		}
		
		User[] users = new User[3];
		Student max = DatabaseManager.getStudent("cutugnma");
		Student heet = DatabaseManager.getStudent("dheet");
		Advisor banerjee = DatabaseManager.getAdvisor("sbanerjee");
		banerjee.setFirstName("Joe");
		banerjee.getAdviseeIDs().remove(1);
		DatabaseManager.saveAdvisor(banerjee);
		banerjee = DatabaseManager.getAdvisor("sbanerjee");
		
		
		users[0] = max;
		users[1] = heet;
		users[2] = banerjee;
		
		for(User user : users) {
			System.out.println(user.getFirstName());
			System.out.println(user.getLastName());
			System.out.println(user.getUsername());
			System.out.println(user.getPassword());
			System.out.println(user.getUniversityID());
			System.out.println(user.getUserID());
			
			if(user.isUserIsStudent()) {
				Student student = (Student) user;
				System.out.println(student.getAcademicPlan().getStartSemester());
			} else {
				Advisor advisor = (Advisor) user;
				for(int studentID : advisor.getAdviseeIDs())
					System.out.println("[student] " + studentID);
			}
		}
		
		// Print the database contents
		System.out.println("\n\n After Changes: ");
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
		try {
			File f = ResourceLoader.getYAMLFile("computer_science_major.yaml");
			RequiredCourses required = RequiredCourses.load(f);
			SchedulerGraph requirementsGraph = new SchedulerGraph(required);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void testCourseInfoLoad(){
		try {
			File f = ResourceLoader.getYAMLFile("course_info.yaml");
			System.out.println(f);
			DatabaseManager database = new DatabaseManager();
			database.loadStudents(f);
			System.out.println("Loaded students");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
}
