package scheduler;
import framework.Course;
import framework.DatabaseManager;
import framework.RequiredCourses;
import framework.RequiredCourses.RequiredCourse;
import framework.RequiredCourses.RequiredCourseGroup;
import framework.RequiredCourses.RequiredCourseNode;

import org.yaml.snakeyaml.Yaml;

import scheduler.SchedulerNode;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Map;
import java.util.Vector;

public class SchedulerGraph {
	public SchedulerNode root;
	private Vector<SchedulerCourse> all_courses;
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
		System.out.println(curriculum.getRootCourseNode());
		root = traverseRequiredCourses(curriculum.getRootCourseNode());
		System.out.println(root);
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
			SchedulerGate newGate = new SchedulerGate(numOptions, numRequired, childNodes);
			SchedulerNode gatenode = (SchedulerNode) newGate;
			for (SchedulerNode children : gatenode.getChildren()) {
				children.addParent(gatenode);
			}
			return new SchedulerNode(newGate);
		} else {
			Course course = DatabaseManager.getCourse(((RequiredCourse)subCurriculum).getCourseID());
			System.out.println("Course name" + course);
			SchedulerCourse newCourse = new SchedulerCourse(course);
			this.all_courses.add(newCourse);
			newCourse.addChild(traverseRequiredCourses(course.getRequiredCourses().getRootCourseNode()));
			newCourse.getChild().addParent(newCourse);
			return newCourse;
		}
	}
	
	public void mergeGraphs(SchedulerGraph sgraph) {}
	
}
