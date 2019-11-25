package framework;

public abstract class User {
	private int userID;
	private String universityID;
	private String firstName;
	private String lastName;
	private String username;
	private String password;
	
	public User(int userID, String universityID, String firstName, String lastName, String username, String password) {
		this.userID = userID;
		this.universityID = universityID;
		this.firstName = firstName;
		this.lastName = lastName;
		this.username = username;
		this.password = password;
	}

	public void setUserID(int userID) {
		this.userID = userID;
	}
	
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	
	public void setUsername(String username) {
		this.username = username;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
	
	public void setUniversityID(String universityID) {
		this.universityID = universityID;
	}
	
	public String getUniversityID() {
		return universityID;
	}
	
	public String getFirstName() {
		return firstName;
	}

	public String getLastName() {
		return lastName;
	}
	
	public String getUsername() {
		return username;
	}

	public String getPassword() {
		return password;
	}

	public int getUserId() {
		return userID;
	}

	public boolean isUserIsStudent() {
		return this instanceof Student;
	}
}
