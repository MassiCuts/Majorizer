package framework;

import java.util.ArrayList;

public class Advisor extends User {
	private ArrayList<Integer> adviseeIDs;
	private ArrayList<Integer> requestsIDs;
	
	public Advisor(String universityID, String firstName, String lastName, String username, String password, ArrayList<Integer> adviseeIDs) {
		this(DatabaseManager.REQUEST_NEW_ID, universityID, firstName, lastName, username, password, adviseeIDs, new ArrayList<>());
	}
	
	public Advisor(int userID, String universityID, String firstName, String lastName, String username, String password, ArrayList<Integer> adviseeIDs, ArrayList<Integer> requestsIDs) {
		super(userID, universityID, firstName, lastName, username, password);
		this.adviseeIDs = adviseeIDs;
		this.requestsIDs = requestsIDs;
	}
	
	public ArrayList<Integer> getAdviseeIDs() {
		return adviseeIDs;
	}
	
	public ArrayList<Integer> getRequestIDs() {
		return requestsIDs;
	}
	
}
