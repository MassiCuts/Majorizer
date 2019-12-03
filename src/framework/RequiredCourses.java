package framework;
//import org.yaml.snakeyaml.Yaml;


import java.util.ArrayList;
import java.util.Iterator;
import java.util.Objects;
import java.util.function.Predicate;

public class RequiredCourses {
	
	private final RequiredCourseNode root;
	
	public RequiredCourses(String description) {
		this.root = null; //TODO make this actually be what it should be
		//Yaml yaml = new Yaml();
	}
	
	public RequiredCourses(RequiredCourseNode root) {
		this.root = root;
	}
	
	public RequiredCourseNode getRootCourseNode() {
		return root;
	}
	
	public boolean meetsRequirements(Predicate<Integer> courseIDPredicate) {
		if(root == null) return true;
		return root.meetsRequirements(courseIDPredicate);
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof RequiredCourses)
			return root.equals(((RequiredCourses)obj).root);
		return false;
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(root);
	}
	
	// nested classes:
	
	public static abstract class RequiredCourseNode {
		public abstract boolean meetsRequirements(Predicate<Integer> courseIDPredicate);
	}
	
	public static class RequiredCourseGroup extends RequiredCourseNode implements Iterable<RequiredCourseNode> {
		private final int amtMustChoose;
		private final ArrayList<RequiredCourseNode> requiredCourses;
		
		public RequiredCourseGroup(int amtMustChoose, ArrayList<RequiredCourseNode> requiredCourses) {
			this.amtMustChoose = amtMustChoose;
			this.requiredCourses = requiredCourses;
		}
		
		@Override
		public Iterator<RequiredCourseNode> iterator() {
			return requiredCourses.iterator();
		}
		
		public int getAmtMustChoose() {
			return amtMustChoose;
		}

		@Override
		public boolean meetsRequirements(Predicate<Integer> courseIDPredicate) {
			
			int amtMetRequirment = 0;
			
			for(RequiredCourseNode node: this) {
				if(node.meetsRequirements(courseIDPredicate))
					amtMetRequirment++;
				if(amtMetRequirment == amtMustChoose)
					return true; // fast close
			}
			return amtMetRequirment == amtMustChoose;
		}
		
		@Override
		public boolean equals(Object obj) {
			if(obj instanceof RequiredCourseGroup) {
				RequiredCourseGroup group = (RequiredCourseGroup) obj;
				return amtMustChoose == group.amtMustChoose && requiredCourses.equals(group.requiredCourses);
			}
			return false;
		}
		
		@Override
		public int hashCode() {
			return Objects.hash(amtMustChoose, requiredCourses);
		}
	}
	
	public static class RequiredCourse extends RequiredCourseNode {
		private final int courseID;

		public RequiredCourse(int courseID) {
			this.courseID = courseID;
		}
		
		public int getCourseID() {
			return courseID;
		}
		
		@Override
		public boolean meetsRequirements(Predicate<Integer> courseIDPredicate) {
			return courseIDPredicate.test(courseID);
		}
		
		@Override
		public boolean equals(Object obj) {
			if(obj instanceof RequiredCourse)
				return courseID == ((RequiredCourse) obj).courseID;
			return false;
		}
		
		@Override
		public int hashCode() {
			return Objects.hash(courseID);
		}
	}
	
}
