package scheduler;

import java.util.ArrayList;
import java.util.Hashtable;

import framework.Course;

//availability: Bit 0: Odd Fall, Bit 1: Even Spring, Bit 2: Even Fall, Bit 3: Even Spring

public class SchedulerCourse extends SchedulerNode {
	protected int availability, taken, scheduled, added, dropped;
	public SchedulerCourse(SchedulerNode node) {
		this.name = node.name;
		if (node.courseinfo.isEmpty()) {
			//Throw some error so we know this shouldn't have been initialized
		}
		this.courseinfo.put(CourseInfo.TAKEN, node.courseinfo.get(CourseInfo.TAKEN));
		this.courseinfo.put(CourseInfo.AVAILABILITY, node.courseinfo.get(CourseInfo.AVAILABILITY));
		this.courseinfo.put(CourseInfo.SCHEDULED, node.courseinfo.get(CourseInfo.SCHEDULED));
		this.courseinfo.put(CourseInfo.ADDED, node.courseinfo.get(CourseInfo.ADDED));
		this.courseinfo.put(CourseInfo.DROPPED, node.courseinfo.get(CourseInfo.DROPPED));
		this.children = node.children;
		this.parents = node.parents;
	}
	
	public SchedulerCourse(Course c) {
		this.name = c.getCourseCode();// Not all courses have names if the class info sheet isn't filled out
		this.children = new ArrayList<SchedulerNode>();
		if(this.children == null) {
			System.out.println("chilldren are null");
		}
		this.parents  = new ArrayList<SchedulerNode>();
		availability = 0b1111;
		taken = -1;
		scheduled = -1;
		added = -1;
		dropped = -1;
		this.courseinfo = new Hashtable<CourseInfo, Integer>();// Otherwise you'll get an error when inserting
		this.courseinfo.put(CourseInfo.TAKEN, this.taken);
		this.courseinfo.put(CourseInfo.AVAILABILITY, this.availability);
		this.courseinfo.put(CourseInfo.SCHEDULED, this.scheduled);
		this.courseinfo.put(CourseInfo.ADDED, this.added);
		this.courseinfo.put(CourseInfo.DROPPED, this.dropped);
	}
	
	public boolean isAvailable(int semester_num) {
		semester_num %= 4;
		boolean available = ((this.availability >> semester_num) & 1) == 1;
		return available;
	}
	
	public boolean isDropped(int semester) {return this.courseinfo.get(CourseInfo.DROPPED)== semester;}
	
	public void addToSemester(int semester_num) {this.courseinfo.put(CourseInfo.ADDED, semester_num); }
	public void addTakenSemester(int semester_num) {this.courseinfo.put(CourseInfo.TAKEN, semester_num); }
	public void removeFromSemester(int semester_num) {this.courseinfo.put(CourseInfo.DROPPED, semester_num);}	
	public SchedulerNode getChild() {
		return this.children.get(0);	//Only one child because this is a course
	}
	
	public boolean equals(SchedulerNode node) {
		if (!node.isGate()) {
			if (this.name == ((SchedulerCourse) node).name) {return true;}
		}
		return false;
	}
	
	@Override
	public String toString() {
		return name;
	}
};
