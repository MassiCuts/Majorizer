package framework;

import java.util.Objects;
import java.util.function.Predicate;

public class Curriculum {
	public static enum CurriculumType {
		MAJOR, MINOR;
	}
	
	private int curriculumID;
	private String curriculumName;
	private CurriculumType curriculumType;
	private RequiredCourses requiredCourses;
	
	public Curriculum(String curriculumName, CurriculumType curriculumType, RequiredCourses requiredCourses) {
		this(DatabaseManager.REQUEST_NEW_ID, curriculumName, curriculumType, requiredCourses);
	}
	
	public Curriculum(int curriculumID, String curriculumName, CurriculumType curriculumType, RequiredCourses requiredCourses) {
		this.curriculumID = curriculumID;
		this.curriculumName = curriculumName;
		this.curriculumType = curriculumType;
		this.requiredCourses = requiredCourses;
	}
	
	public void setCurriculumID(int curriculumID) {
		this.curriculumID = curriculumID;
	}
	
	public void setRequiredCourses(RequiredCourses requiredCourses) {
		this.requiredCourses = requiredCourses;
	}
	
	public int getCurriculumID() {
		return curriculumID;
	}
	
	public String getName() {
		return curriculumName;
	}
	
	public boolean meetsRequirements(Predicate<Integer> courseIDPredicate) {
		return requiredCourses.meetsRequirements(courseIDPredicate);
	}
	
	public CurriculumType getCurriculumType() {
		return curriculumType;
	}
	
	public RequiredCourses getRequiredCourses() {
		return requiredCourses;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof Curriculum)
			return curriculumID == ((Curriculum) obj).curriculumID;
		return false;
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(curriculumID);
	}
}
