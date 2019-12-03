package framework;

import database.DatabaseTable;
import scheduler.Scheduler;
import scheduler.SchedulerGraph;

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
//					DatabaseManager.makeAdvisorAccount("Sean Banerjee", "seanBanerjee", "lovesColor", 0);
//					DatabaseManager.makeAdvisorAccount("Alexis Maciel", "alexisMaciel", "loveAutomata", 1);
//					DatabaseManager.makeStudentAccount("Massimiliano Cutuno", "cutugnma", "12346", 668364, false);
//					DatabaseManager.makeStudentAccount("Heet Dave", "heetd", "password", 120, false);
			}
		} else {
			throw new RuntimeException("[ERROR] Can not procede -- please specify a uri to the database as the first command line argument.");	
		}
		
		// Print the database contents
		DatabaseTable[] tables = DatabaseManager.listTables();
		for(DatabaseTable table : tables) {
			DatabaseManager.printTable(table);
			System.out.println();
		}
		
		String example = "- grad:\n" + 
				"    num: 3\n" +  
				"    requirments:\n" + 
				"      - breath\n" + 
				"      - CS142\n" + 
				"      - CS444\n" +
				"- breath:\n" + 
				"    num: 1\n" +  
				"    requirments:\n" + 
				"      - PH100\n" + 
				"      - PH200\n" +
				"- CS142:\n" + 
				"    num: 0\n" +  
				"    requirments:\n" +
				"- CS444:\n" + 
				"    num: 0\n" +  
				"    requirments:\n" +
				"- PH100:\n" + 
				"    num: 0\n" +  
				"    requirments:\n"+
				"- PH200:\n" + 
				"    num: 0\n" +  
				"    requirments:\n"
				;  
	
		
		
		RequiredCourses required = new RequiredCourses("/home/david/dev/yogaandtheboys/data/curriculums/computer_science_major.yaml");
//		Scheduler scheduler = new Scheduler(3);
	}
}
