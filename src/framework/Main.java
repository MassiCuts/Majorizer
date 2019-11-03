package framework;

import database.DatabaseTable;

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
					DatabaseManager.makeAdvisorAccount("Sean Banerjee", "seanBanerjee", "lovesColor", 0);
					DatabaseManager.makeAdvisorAccount("Alexis Maciel", "alexisMaciel", "loveAutomata", 1);
					DatabaseManager.makeStudentAccount("Massimiliano Cutuno", "cutugnma", "12346", 668364, false);
					DatabaseManager.makeStudentAccount("Heet Dave", "heetd", "password", 120, false);
			}
		} else {
			DatabaseManager.connect();
			System.out.println("Using the default database location");		
		}
		
		// Print the database contents
		DatabaseTable[] tables = DatabaseManager.listTables();
		for(DatabaseTable table : tables) {
			DatabaseManager.printTable(table);
			System.out.println();
		}
		
		Scheduler scheduler = new Scheduler(3);
	}
}
