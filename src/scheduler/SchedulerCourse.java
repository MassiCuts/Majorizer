package scheduler;

import java.util.ArrayList;

public class SchedulerCourse extends SchedulerNode {
	protected int available, taken, scheduled, added, dropped;
	
	public SchedulerCourse(SchedulerNode node) {
		this.name = node.name;
		if (node.courseinfo.isEmpty()) {
			//Throw some error so we know this shouldn't have been initialized
		}
		this.available = node.courseinfo.get(CourseInfo.AVAILABILITY);
		this.taken = node.courseinfo.get(CourseInfo.TAKEN);
		this.scheduled = node.courseinfo.get(CourseInfo.SCHEDULED);
		this.added = node.courseinfo.get(CourseInfo.ADDED);
		this.dropped = node.courseinfo.get(CourseInfo.DROPPED);
		this.children = node.children;
		this.parents = node.parents;
	}
	
	public SchedulerCourse(String Name) {
		this.name = name;
		this.children = new ArrayList<SchedulerNode>();
		if(this.children == null) {
			System.out.println("chilldren are null");
		}
		this.parents  = new ArrayList<SchedulerNode>();
		this.taken = 0;//TODO create setters for all of these 
		this.scheduled = 0;
		this.added = 0;
		this.dropped = 0;
	}
	
	public boolean available() { return this.available == 1;}
	
	
	public SchedulerNode getChild() {
		return this.children.get(0);	//Only one child because this is a cours
	}
};
