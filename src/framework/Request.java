package framework;

import java.util.Objects;

public class Request {
	
	private int requestID;
	private int studentID;
	private boolean isAdding;
	private int curriculumID;
	
	public Request(int studentID, boolean isAdding, int curriculumID) {
		this(DatabaseManager.REQUEST_NEW_ID, studentID, isAdding, curriculumID);
	}
	
	public Request(int requestID, int studentID, boolean isAdding, int curriculumID) {
		this.requestID = requestID;
		this.studentID = studentID;
		this.isAdding = isAdding;
		this.curriculumID = curriculumID;
	}
	
	public int getRequestID() {
		return requestID;
	}
	
	public int getStudentID() {
		return studentID;
	}
	
	public boolean isAdding() {
		return isAdding;
	}
	
	public int getCurriculumID() {
		return curriculumID;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof Request)
			return requestID == ((Request) obj).requestID;
		return false;
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(requestID);
	}
}
