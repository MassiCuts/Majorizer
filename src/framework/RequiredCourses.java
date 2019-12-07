package framework;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Objects;
import java.util.function.Predicate;

import org.yaml.snakeyaml.Yaml;

public class RequiredCourses {
	
	private RequiredCourseNode root = null;
	private int requiredCoursesID;
	
	
	public static RequiredCourses load(File yamlFile) {
		final String ROOT_NAME = "grad"; // It is assumed that this will always be the root of the requirements graph
		
		Yaml yaml = new Yaml(); // Create the parser
		try (
			InputStream is = new FileInputStream(yamlFile);	
		) {
			LinkedHashMap<String, LinkedHashMap> specification = yaml.load(is);// Parse the YAML representation
			RequiredCourseNode root = traverseRequirements(specification, ROOT_NAME);
			return new RequiredCourses(root);
		} catch (IOException e) {
			System.out.println(yamlFile.getAbsolutePath() + " could not be loaded or parsed correctly");
			e.printStackTrace();
			return null;
		}
	}
	
	
	private static RequiredCourseNode traverseRequirements(LinkedHashMap<String, LinkedHashMap> description, String tag){
		if(description.containsKey(tag)) {//This means it must be a course, as they don't have requirements
			LinkedHashMap newNode = (LinkedHashMap) description.get(tag);// Get the new node from the description
			ArrayList<String> requirementNames = (ArrayList) newNode.get("requirements");// Get all the names of things it depends on
			int numRequired = (int) newNode.get("num");
			ArrayList<RequiredCourseNode> createdRequirements = new ArrayList<RequiredCourseNode>();
			for (String rec : requirementNames) { 
	            createdRequirements.add(traverseRequirements(description, rec));// Add all of the requirements recursively
			}
			return new RequiredCourseGroup(numRequired, createdRequirements);
		} else {
			Course c = DatabaseManager.getCourse(tag);
			return new RequiredCourse(c.getCourseID());		
		}
		
    } 
	
	// TraverseRequirements is assumed to be called first 
	// I'm not sure it makes sense to encode the prerequisites here
//	private void TraversePrerequisites(LinkedHashMap<String, ArrayList<String>> prerequisites) {
//		Integer courseNumber;
//		for(String key : prerequisites.keySet()) {
//			System.out.println(key);
//			if(!CourseIDs.containsKey(key)) { // Maintain a mapping from course name to course ID
//				CourseIDs.put(key, CurrentCourseID);
//				CurrentCourseID++;
//			}
//			courseNumber = CourseIDs.get(key);
//			System.out.println("Course: " + key + " Number: " + courseNumber);
//		}
//	}
	
	public RequiredCourses() {
		this(null);
	}
	
	public RequiredCourses(RequiredCourseNode root) {
		this(DatabaseManager.REQUEST_NEW_ID, root);
	}
	
	public RequiredCourses(int requiredCourseID, RequiredCourseNode root) {
		this.root = root;
		this.requiredCoursesID = requiredCourseID;
	}
	
	public int getRequiredCourseID() {
		return requiredCoursesID;
	}
	
	public boolean hasRequirements() {
		return root != null;
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
		private final int nodeID;
		
		public RequiredCourseNode(int nodeID) {
			this.nodeID = nodeID;
		}
		
		public int getNodeID() {
			return nodeID;
		}
	}
	
	public static class RequiredCourseGroup extends RequiredCourseNode implements Iterable<RequiredCourseNode> {
		private final int amtMustChoose;
		private final ArrayList<RequiredCourseNode> requiredCourses;
		
		public RequiredCourseGroup(int amtMustChoose, ArrayList<RequiredCourseNode> requiredCourses) {
			this(DatabaseManager.REQUEST_NEW_ID, amtMustChoose, requiredCourses);
		}
		
		public RequiredCourseGroup(int nodeID, int amtMustChoose, ArrayList<RequiredCourseNode> requiredCourses) {
			super(nodeID);
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

		public RequiredCourse() {
			this(DatabaseManager.REQUEST_NEW_ID);
		}
		
		public RequiredCourse(int courseID) {
			super(courseID);
		}
		
		
		@Override
		public boolean meetsRequirements(Predicate<Integer> courseIDPredicate) {
			return courseIDPredicate.test(getNodeID());
		}
		
		@Override
		public boolean equals(Object obj) {
			if(obj instanceof RequiredCourse)
				return getNodeID() == ((RequiredCourse) obj).getNodeID();
			return false;
		}
		
		@Override
		public int hashCode() {
			return Objects.hash(getNodeID());
		}
		
		public String toString() {
		    return "Course ID: " + getNodeID();
		}
	}
	
}
