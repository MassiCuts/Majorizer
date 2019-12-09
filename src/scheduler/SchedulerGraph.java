package scheduler;
import framework.Course;
import framework.DatabaseManager;
import framework.RequiredCourses;
import framework.RequiredCourses.RequiredCourse;
import framework.RequiredCourses.RequiredCourseGroup;
import framework.RequiredCourses.RequiredCourseNode;

import org.yaml.snakeyaml.Yaml;

import scheduler.SchedulerNode;
//import scheduler.SchedulerNode.SchedulerGate;
import scheduler.SchedulerGate;
import scheduler.SchedulerCourse;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

public class SchedulerGraph {
	public SchedulerNode root;
	private Vector<SchedulerCourse> all_courses = new Vector<SchedulerCourse>();// TODO streamline this
	private HashMap<Integer, SchedulerCourse> all_courses_int_map = new HashMap<Integer, SchedulerCourse>();
	private HashMap<String, SchedulerCourse> all_course_string_map = new HashMap<String, SchedulerCourse>();
	private Vector<SchedulerNode> graph;
	// There needs to be some sort of hash map to uniquely identify course 

	public SchedulerGraph(String description) {
		//Yaml yaml = new Yaml();
		//InputStream inputStream = this.getClass()
		//		 .getClassLoader()
		//		 .getResourceAsStream("/home/david/dev/yogaandtheboys/data/example_schedule.yaml");
		//ArrayList<Map<Object, Object>> specification = yaml.load(example);
		//System.out.println(specification);
	}
	
	public SchedulerGraph(RequiredCourses curriculum) {
		System.out.println("Creating a scheduler graph from a curriculum");
		//System.out.println("Root node:" + curriculum.getRootCourseNode());
		root = traverseRequiredCourses(curriculum.getRootCourseNode());
		//System.out.println(root);
	}
	
	private SchedulerNode traverseRequiredCourses(RequiredCourseNode subCurriculum) {
		// If it is a course, then you simply return a course node with that course
		// If it is a group, you return a course group, which will involve a recursive call to create it
		if(subCurriculum instanceof RequiredCourseGroup) {
			int numRequired = ((RequiredCourseGroup) subCurriculum).getAmtMustChoose();
			int numOptions = ((RequiredCourseGroup) subCurriculum).getNumRequiredCourses();
			ArrayList<SchedulerNode> childNodes = new ArrayList<SchedulerNode>();
			for( RequiredCourseNode r : ((RequiredCourseGroup) subCurriculum)){
				SchedulerNode snode = traverseRequiredCourses((RequiredCourseNode)r);
				childNodes.add(snode);
			}
			SchedulerNode gatenode = new SchedulerGate(numOptions, numRequired, childNodes);
			for (SchedulerNode children : gatenode.getChildren()) {
				children.addParent(gatenode);
			}
			return gatenode;
		} else {
			int course_ID = ((RequiredCourse)subCurriculum).getNodeID();// This really shouldn't be adding the same course twice
			SchedulerCourse newCourse;
			if(all_courses_int_map.containsKey(course_ID)) {
				newCourse = all_courses_int_map.get(course_ID);// This really shouldn't be adding the same course twice
			}else {
				Course course = DatabaseManager.getCourse(((RequiredCourse)subCurriculum).getNodeID());// This really shouldn't be adding the same course twice
				newCourse = new SchedulerCourse(course);
				newCourse.addChild(traverseRequiredCourses(course.getRequiredCourses().getRootCourseNode()));
				//System.out.println(newCourse);
				this.all_courses_int_map.put(course_ID, newCourse);//For easy access later
				this.all_course_string_map.put(course.getCourseCode(), newCourse);
				this.all_courses.add(newCourse);
			}
			newCourse.getChild().addParent(newCourse);
			return newCourse;
		}
	}
	
	private SchedulerNode traverseRequiredCourses(SchedulerNode subCurriculum) {
		// If it is a course, then you simply return a course node with that course
		// If it is a group, you return a course group, which will involve a recursive call to create it
		if(subCurriculum instanceof SchedulerGate) {
			// Create a new gate to avoid modifying the old one
			ArrayList<SchedulerNode> children = new ArrayList<SchedulerNode>();
			for( SchedulerNode s : ((SchedulerGate) subCurriculum).children){
				children.add(traverseRequiredCourses(s));
			}
			int options =((SchedulerGate)subCurriculum).options;
			int required =((SchedulerGate)subCurriculum).required;
			SchedulerGate newGate = new SchedulerGate(options, required, children);// TODO we shouldn't create duplicate gates if all children are the same
			return newGate;
		} else {
			if(this.all_course_string_map.containsKey(subCurriculum.name)){
				return this.all_course_string_map.get(subCurriculum.name);// This is the course in the original tree
			}else {
				return subCurriculum;// This is the new course
			}
		}
	}
	
	public void mergeGraphs(SchedulerGraph sgraph) {
		// If called on itself this should yield a graph which is functionally equivilent
		// The biggest issue is making sure that there are not duplicate classes in the graph
		System.out.println("Merging the graphs ");
		SchedulerNode newRequiredPath = traverseRequiredCourses(sgraph.root);
		//The root now requires that you take all of the old requirements in addition to all the new requirements
		root = new SchedulerGate(2, 2, new ArrayList<SchedulerNode>(Arrays.asList(root, newRequiredPath)));
		
		
	}
	
	public String printElements(SchedulerNode node) {
		// If it is a group, you return a course group, which will involve a recursive call to create it
		if(node instanceof SchedulerGate) {
			// Create a new gate to avoid modifying the old one
			String output_string = "";
			ArrayList<SchedulerNode> children = new ArrayList<SchedulerNode>();
			for( SchedulerNode c : ((SchedulerGate) node).children){
				// Add the edge from parent to child
				output_string += node.name.replaceAll("\\s+","") + "->" + c.name.replaceAll("\\s+","") + "\n";// The replace all remove spaces
				output_string += printElements(c);// Recursive call
				// Add the recursive call
			}
			return output_string;
		} else {
			return "";// This does nothing since parents are responsible for connecting children
		}
	}
	
	public String getAsGraphVis() {
		return "Go to https://dreampuf.github.io/GraphvizOnline to visualize\n"
				+ "digraph G {\n"  
				+ "ROOT[label=< <B>ROOT NODE</B>>]\n" 
				+ "ROOT -> " + root.name.replaceAll("\\s+", "")+ "\n" 
				+ this.printElements(root)  
				+ "}";
	}
}
