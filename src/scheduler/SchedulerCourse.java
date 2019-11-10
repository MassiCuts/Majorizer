package scheduler;

public class SchedulerCourse extends SchedulerNode {
	
	public String name;
	
	public SchedulerCourse(SchedulerNode node) {
//		this.available = node.courseinfo[AVAILABILITY]
	}
	public SchedulerCourse() {
		
	}
	
	public boolean available() {
		return false;
	}
	public float getPathScore() {
		return 0f;
	}
	public float getPriceScore() {
		return 0f;
	}
	public SchedulerNode getChild() {
		return null;
	}
}
