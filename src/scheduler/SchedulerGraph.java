package scheduler;
import framework.Course;
import scheduler.SchedulerNode;
import java.util.Map;
import java.util.Vector;

public class SchedulerGraph {
	public SchedulerNode top;
	private Vector<Course> all_courses;
	private Vector<SchedulerNode> graph;
	
	public SchedulerGraph(String description) {
		//Yaml yaml = new Yaml();
		//Map<Object, Object> document = yaml.load(description);
	}
	
	public void mergeGraphs(SchedulerGraph sgraph) {}
}
