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
import java.util.HashSet;
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
		System.out.println(this.all_course_string_map);
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
				System.out.println("course is " + course);
				newCourse = new SchedulerCourse(course);
				newCourse.addChild(traverseRequiredCourses(course.getRequiredCourses().getRootCourseNode()));
				System.out.println(newCourse);
				this.all_courses_int_map.put(course_ID, newCourse);//For easy access later
				this.all_course_string_map.put(course.getCourseCode(), newCourse);
				this.all_courses.add(newCourse);
				System.out.println(course_ID + ":" + course.getCourseCode());
				System.out.println(this.all_course_string_map);
			}
			return newCourse;
		}
	}
	
	private SchedulerNode traverseRequiredCourses(SchedulerNode subCurriculum, HashMap<String, SchedulerCourse> sgraph_course_string_map) {
		// If it is a course, then you simply return a course node with that course
		// If it is a group, you return a course group, which will involve a recursive call to create it
		if(subCurriculum instanceof SchedulerGate) {
			// Create a new gate to avoid modifying the old one
			ArrayList<SchedulerNode> children = new ArrayList<SchedulerNode>();
			for( SchedulerNode s : ((SchedulerGate) subCurriculum).children){ 
				SchedulerNode new_node = traverseRequiredCourses(s, sgraph_course_string_map); 
				children.add(new_node);
				new_node.parents.add(s);
			}
			int options =((SchedulerGate)subCurriculum).gateinfo.get(GateInfo.OPTIONS);
			int required =((SchedulerGate)subCurriculum).gateinfo.get(GateInfo.REQUIRED);
			SchedulerGate newGate = new SchedulerGate(options, required, children);// TODO we shouldn't create duplicate gates if all children are the same
			return newGate;
		} else {
			if(this.all_course_string_map.containsKey(subCurriculum.name)){
				SchedulerNode course = this.all_course_string_map.get(subCurriculum.name);// This is the course in the original tree
				if (course.name.contentEquals("MA132")) {System.out.println("MA132 in map has hash id: " + System.identityHashCode(course));}
				return course;
			}else {
				SchedulerNode new_node = traverseRequiredCourses(subCurriculum.getChildren().get(0), sgraph_course_string_map);
				subCurriculum.children.set(0, new_node);
				new_node.parents.add(subCurriculum);
				if (new_node.name.contentEquals("MA132")) {
					System.out.println("MA132 being added hash id: " + System.identityHashCode(new_node));
				}
				//this.all_courses_int_map.put(subCurriculum.name, newCourse);//For easy access later
				SchedulerCourse newCourse = sgraph_course_string_map.get(subCurriculum.name);
				this.all_course_string_map.put(subCurriculum.name, newCourse);
				this.all_courses.add(newCourse);
				return subCurriculum;// This is the new course
			}
		}
	}
	
	public void mergeGraphs(SchedulerGraph sgraph) {
		// If called on itself this should yield a graph which is functionally equivilent
		// The biggest issue is making sure that there are not duplicate classes in the graph
		System.out.println("Merging the graphs ");
		System.out.println(this.all_course_string_map);
		System.out.println(sgraph.all_course_string_map);
		SchedulerNode newRequiredPath = traverseRequiredCourses(sgraph.root, sgraph.all_course_string_map);
		//The root now requires that you take all of the old requirements in addition to all the new requirements
		root = new SchedulerGate(2, 2, new ArrayList<SchedulerNode>(Arrays.asList(root, newRequiredPath)));
		System.out.println(this.all_course_string_map);
	}
	
	public void setCourseAttribute(String course_code, CourseInfo attribute, int value) throws Exception {
		if (this.all_course_string_map.containsValue(null)) {
			throw new Exception("AAAAAAAAA (null inside our map wtf");
		}
		if (!this.all_course_string_map.containsKey(course_code)) {
			throw new Exception(course_code + " is not in the map " + this.all_course_string_map);
		}
		SchedulerCourse c = this.all_course_string_map.get(course_code);
		
		System.out.println(this.all_course_string_map);
		System.out.println(c);
		if (c == null) {
			throw new Exception ("AAAAAAHHH (c is null wtf");
		}
		System.out.println(c.name);
		System.out.println(c.courseinfo);
		c.courseinfo.get(attribute);
		c.courseinfo.put(attribute, value);
	}
	
	public SchedulerCourse getCourse(String course_code) {
		return this.all_course_string_map.get(course_code);
	}
	
	public String printElements(SchedulerNode node) {
		// If it is a group, you return a course group, which will involve a recursive call to create it
		if(node instanceof SchedulerCourse) {
			String output_string = "";
			ArrayList<SchedulerNode> children = new ArrayList<SchedulerNode>();
			for( SchedulerNode c : node.children){
				// Add the edge from parent to child
				if(((SchedulerGate)c).gateinfo.get(GateInfo.REQUIRED) != 0) { // Do not visualize terminal gates with no requirements
					output_string += node.name.replaceAll("\\s+","") + "->" + c.name.replaceAll("\\s+","") + "\n";// The replace all remove spaces
					output_string += printElements(c);// Recursive call
				}
			}
			return output_string;
		}else {
			String output_string = "";
			ArrayList<SchedulerNode> children = new ArrayList<SchedulerNode>();
			for( SchedulerNode c : node.children){
				// Add the edge from parent to child
				output_string += node.name.replaceAll("\\s+","") + "->" + c.name.replaceAll("\\s+","") + "\n";// The replace all remove spaces
				output_string += printElements(c);// Recursive call
				// Add the recursive call
			}
			return output_string;
		}
	}
	
	public String getAsGraphVis() {
		String output_string = this.printElements(root);
		String[] lines = output_string.split("\n");
		String[] unique = new HashSet<String>(Arrays.asList(lines)).toArray(new String[0]);
		output_string = String.join("\n", unique);
				
		output_string ="Go to https://dreampuf.github.io/GraphvizOnline to visualize\n"
				+ "digraph G {\n"  
				+ "ROOT[label=< <B>ROOT NODE</B>>]\n" 
				+ "ROOT -> " + root.name.replaceAll("\\s+", "")+ "\n" 
				+ output_string
				+ "}";
	    
		return output_string; 
	}
}
