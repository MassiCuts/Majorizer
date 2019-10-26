package framework;

public abstract class User {
	private String name;
	private String username;
	private String password;
	private int id;
	private boolean userIsStudent;
	
	public User(String name, String username, String password, int id, boolean userIsStudent) {
		this.name = name;
		this.username = username;
		this.password = password;
		this.id = id;
		this.userIsStudent = userIsStudent;
	}

	public String getName() {
		return name;
	}

	public String getUsername() {
		return username;
	}

	public String getPassword() {
		return password;
	}

	public int getId() {
		return id;
	}

	public boolean isUserIsStudent() {
		return userIsStudent;
	}
}
