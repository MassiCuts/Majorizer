package framework;

import java.util.Objects;
import java.util.function.Predicate;

public class Curriculum {
	public static enum CurriculumType {
		MAJOR, MINOR;
	}
	
	private final int curriculumID;
	private final String curriculumName;
	private final CurriculumType curriculumType;
	private final RequiredCourses requiredCourses;
	
	public Curriculum(int curriculumID, String curriculumName, CurriculumType curriculumType, RequiredCourses requiredCourses) {
		this.curriculumID = curriculumID;
		this.curriculumName = curriculumName;
		this.curriculumType = curriculumType;
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
