package framework;
import org.yaml.snakeyaml.Yaml;

import java.io.IOException;
import java.io.InputStream;
import java.util.Vector;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.nio.file.Files; // suggested by https://www.vogella.com/tutorials/JavaIO/article.html
import java.nio.file.Paths;
import java.util.function.Predicate;

import org.yaml.snakeyaml.Yaml;

public class RequiredCourses {
	
	private RequiredCourseNode root = null;
	private Integer CurrentCourseID = 0;
	private LinkedHashMap<String, Integer> CourseIDs = new LinkedHashMap<String, Integer>();
	
	//public RequiredCourses(String curriculumFilename) { //TODO determine how to do a one-argument constructor
	//	RequiredCourses(curriculumFilename, "");
	//}

	public RequiredCourses(String curriculumFilename, String prerequisitesFilename){
		Yaml yaml = new Yaml();// Create the parser
		List<String> strings;
		try {
			strings = Files.readAllLines(Paths.get(curriculumFilename));// Read in all lines
			String string = String.join("\n", strings); // Concatenate with newlines
			LinkedHashMap<String, LinkedHashMap> specification = yaml.load(string);// Parse the YAML representation
			this.root = TraverseRequirements(specification, "grad"); // It is assumed that grad will always be the root of the requirements graph
			strings = Files.readAllLines(Paths.get(prerequisitesFilename));// Read in all lines
			string = String.join("\n", strings); // Concatenate with newlines
			LinkedHashMap<String, ArrayList<String>> prerequisites = yaml.load(string);
			TraversePrerequisites(prerequisites);
			System.out.println("Course name : ID mappings" + CourseIDs);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println(curriculumFilename + " could not be loaded or parsed correctly");// TODO figure out more proper method to do this
			e.printStackTrace();
		}
	}
	
	private RequiredCourseNode TraverseRequirements(LinkedHashMap<String, LinkedHashMap> description, String tag){
		if(description.containsKey(tag)) {//This means it must be a course, as they don't have requirements
			LinkedHashMap newNode = (LinkedHashMap) description.get(tag);// Get the new node from the description
			ArrayList<String> requirementNames = (ArrayList) newNode.get("requirements");// Get all the names of things it depends on
			int numRequired = (int) newNode.get("num");
			ArrayList<RequiredCourseNode> createdRequirements = new ArrayList<RequiredCourseNode>();
			for (String rec : requirementNames) { 
	            createdRequirements.add(TraverseRequirements(description, rec));// Add all of the requirements recursively
			}
			return new RequiredCourseGroup(numRequired, createdRequirements);
		}else {
			if(!CourseIDs.containsKey(tag)) { // Maintain a mapping from course name to course ID
				CourseIDs.put(tag, CurrentCourseID);
				CurrentCourseID++;
			}
			Integer courseNumber = CourseIDs.get(tag);
			return new RequiredCourse(courseNumber);		
		}
		
    } 
	
	// TraverseRequirements is assumed to be called first 
	// I'm not sure it makes sense to encode the prerequisites here
	private void TraversePrerequisites(LinkedHashMap<String, ArrayList<String>> prerequisites) {
		Integer courseNumber;
		for(String key : prerequisites.keySet()) {
			System.out.println(key);
			if(!CourseIDs.containsKey(key)) { // Maintain a mapping from course name to course ID
				CourseIDs.put(key, CurrentCourseID);
				CurrentCourseID++;
			}
			courseNumber = CourseIDs.get(key);
			System.out.println("Course: " + key + " Number: " + courseNumber);
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
		
		public int getNumRequiredCourses() {
			return requiredCourses.size();
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
		
		@Override
	    public String toString() {
			String output = "Group Containing: \n";//Maybe unnecessary
			for(RequiredCourseNode r : requiredCourses) {
				output += r.toString() + " ";
			}
			output += "\n";
	        return output; 
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
		
		public String toString() {
		    return "Course ID: " + courseID;
		}
	}
	
}
