package framework;

import database.DatabaseTable;

public class Main {
	
	public static void main(String args[]) {
		DatabaseManager.connect();
		DatabaseManager.initializeDatabase();
		
		DatabaseManager.makeAdvisorAccount("Sean Banerjee", "seanBanerjee", "lovesColor", 0);
		DatabaseManager.makeAdvisorAccount("Alexis Maciel", "alexisMaciel", "loveAutomata", 1);
		DatabaseManager.makeStudentAccount("Massimiliano Cutuno", "cutugnma", "12346", 668364, false);
		DatabaseManager.makeStudentAccount("Heet Dave", "heetd", "password", 120, false);
		
		DatabaseTable[] tables = DatabaseManager.listTables();
		for(DatabaseTable table : tables) {
			DatabaseManager.printTable(table);
			System.out.println();
		}
	}
}
