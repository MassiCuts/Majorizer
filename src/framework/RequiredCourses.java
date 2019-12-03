package framework;
import org.yaml.snakeyaml.Yaml;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.nio.file.Files; // suggested by https://www.vogella.com/tutorials/JavaIO/article.html
import java.nio.file.Paths;
import java.util.function.Predicate;

import org.yaml.snakeyaml.Yaml;

public class RequiredCourses {
	
	private final RequiredCourseNode root;
	
	public RequiredCourses(String description){
		this.root = null; //TODO make this actually be what it should be
		Yaml yaml = new Yaml();
		//InputStream inputStream = this.getClass()
		//		 .getClassLoader()
		//		 .getResourceAsStream("/home/david/dev/yogaandtheboys/data/example_schedule.yaml");
		List<String> strings;
		try {
			strings = Files.readAllLines(Paths.get(description));
			String string = String.join("\n", strings);
			LinkedHashMap specification = yaml.load(string);// TODO determine what type this really is
			System.out.println("Read from file");
			System.out.println(specification);
			System.out.println(specification.keySet());
			System.out.println(specification.get("grad"));

			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println(description + " could not be loaded");// TODO figure out more proper method to do this
			e.printStackTrace();
		}
		
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
