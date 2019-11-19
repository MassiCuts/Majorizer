package scheduler;
import java.util.Vector;
import java.lang.IllegalArgumentException;
import framework.Course;

public class Scheduler {
	
	private int num_courses=6;
	private int num_semesters=8;
	private int current_semester=0;
	private SchedulerGraph graph;
	static int MAX_ATTEMPTS = 100;
	
	public Scheduler(SchedulerGraph graph) {
		this.graph = graph;
	}
	
	public Scheduler(SchedulerGraph graph, int num_courses, int num_semesters) {
		this.num_courses = num_courses;
		this.num_semesters = num_semesters;
		this.graph = graph;
	}
	
	public Vector<Vector<String>> schedule(Vector<Course> requested_courses, Vector<Course> requested_dropped_courses){
		SchedulerCourse course;
		Vector<Vector<String>> sched = new Vector<Vector<String>>();
		for (int i = 0; i < this.num_semesters; ++i) {
			sched.add(new Vector<String>());
			if (i < current_semester) {
				//Add all the classes taken during this semester then skip
			}
			int added_this_semester;
			//Go through added courses to see if any were added this semester
			for(int j = 0; j < this.num_courses-added_this_semester; ++j) {
				SchedulerNode node = this.graph.top;
				SchedulerNode next_node = this.graph.top;
				int attempt = 0;
				do {
					node = next_node;
					if (node.isGate()) {
						//Select which path to take (Longest path of the k shortest plausible paths)
						SchedulerGate gate = (SchedulerGate) node;
						next_node = gate.getBestChild(this.num_semesters-i);
					} else {
						//since we know the child isn't satisfied due to the while condition, move to child node
						course = (SchedulerCourse) node;
						next_node = course.getChild();
					}
					if (!course.available()){
						++attempt;
						next_node = this.graph.top;
						if (attempt > MAX_ATTEMPTS) {
							throw Exception("Your stupid program cant find a feasible schedule you dumb fuck");
						}
					}
				} while (!next_node.isSatisfied());
				node.courseinfo.put(CourseInfo.SCHEDULED, i);
				sched.get(i).add(node.name);
			}
		}
		if (!graph.top.isSatisfied()) {
			throw (IllegalArgumentException("could not graduate in the required semester and credit hours per semester restraints"));
		}
		return sched;
	}
	
	
};
