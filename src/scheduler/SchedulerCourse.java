package scheduler;

import java.util.ArrayList;

import framework.Course;

//availability: Bit 0: Odd Fall, Bit 1: Even Spring, Bit 2: Even Fall, Bit 3: Even Spring

public class SchedulerCourse extends SchedulerNode {
	protected int availability, taken, scheduled, added, dropped;
	
	public SchedulerCourse(SchedulerNode node) {
		this.name = node.name;
		if (node.courseinfo.isEmpty()) {
			//Throw some error so we know this shouldn't have been initialized
		}
		this.availability = node.courseinfo.get(CourseInfo.AVAILABILITY);
		this.taken = node.courseinfo.get(CourseInfo.TAKEN);
		this.scheduled = node.courseinfo.get(CourseInfo.SCHEDULED);
		this.added = node.courseinfo.get(CourseInfo.ADDED);
		this.dropped = node.courseinfo.get(CourseInfo.DROPPED);
		this.children = node.children;
		this.parents = node.parents;
	}
	
	public SchedulerCourse(Course c) {
		this.name = c.getCourseName();
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
	
	public boolean isAvailable(int semester_num) {
		boolean a = false;
		semester_num %= 4;
		return ((this.availability >> semester_num) & 1) == 1;
	}
	
	public boolean isDropped(int semester) {return this.dropped == semester;}
	
	public void addToSemester(int semester_num) {this.added = semester_num;}
	public void removeFromSemester(int semester_num) {this.dropped = semester_num;}
	
	public SchedulerNode getChild() {
		return this.children.get(0);	//Only one child because this is a cours
	}
	
	public boolean equals(SchedulerNode node) {
		if (!node.isGate()) {
			if (this.name == ((SchedulerCourse) node).name) {return true;}
		}
		return false;
	}
};
