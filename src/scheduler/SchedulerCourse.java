package scheduler;
import framework.Course;

public class SchedulerCourse extends SchedulerNode {
	
	public String name;
	
	public SchedulerCourse(SchedulerNode node) {
		this.available = node.courseinfo[AVAILABILITY]
	}
	public SchedulerCourse() {}
	
	public boolean available() {}
	public float getPathScore() {}
	public float getPriceScore() {}
	public SchedulerNode getChild() {}
}
