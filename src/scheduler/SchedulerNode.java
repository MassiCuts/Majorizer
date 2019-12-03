package scheduler;
import java.util.Vector;
import java.util.Hashtable;

public class SchedulerNode {
	protected Vector<SchedulerNode> children;
	protected Vector<SchedulerNode> parents;
	protected int depth;
	protected boolean isgate;
	protected boolean root = false;
	protected String name = null;
	protected Hashtable<CourseInfo, Integer> courseinfo; //Taken, available, added, dropped, scheduled
	protected Hashtable<GateInfo, Integer> gateinfo;	//n, k
	
	public SchedulerNode(SchedulerGate gate) {
		this.children = gate.children;
		this.parents = gate.parents;
		this.isgate = true;
		this.name = gate.name;
		this.gateinfo.put(GateInfo.OPTIONS, gate.options);
		this.gateinfo.put(GateInfo.REQUIRED, gate.required);
	}
	
	public SchedulerNode(SchedulerCourse course) {
		this.children = course.children;
		this.parents = course.parents;
		this.isgate = false;
		this.name = course.name;
		this.courseinfo.put(CourseInfo.TAKEN, course.taken);
		this.courseinfo.put(CourseInfo.SCHEDULED, course.scheduled);
		this.courseinfo.put(CourseInfo.ADDED, course.added);
		this.courseinfo.put(CourseInfo.DROPPED, course.dropped);
	}
	
	public SchedulerNode(boolean isroot) {
		this.root = true;
		this.name = "ROOT";
	}
	
	public SchedulerNode() {}
	
	public void attachOutgoing(SchedulerNode node) {
		this.parents.add(node);
	}
	public void attachIncoming(SchedulerNode node) {
		this.children.add(node);
	}
	
	
	public String getName() {return this.name;}
	public boolean isGate() { return this.isgate;}
	
	public boolean isSatisfied() {
		/*
		 * Return True:
		 * 	If it is a gate and k out of the n children are satisfied
		 * 	If it is a course and was already taken or scheduled
		 * 	If it is a null node
		 * Otherwise False
		 */
		if (isGate()) {
			int counter = 0;
			for (int i = 0; i < children.size(); ++i) {
				if (children.get(i).isSatisfied()) {
					++counter;
				}
			}
			if (counter > this.gateinfo.get(GateInfo.REQUIRED)) {
				return true;
			} else { return false;}
		} else {
			if (isNull()) {
				return true;
			} else if (this.courseinfo.get(CourseInfo.SCHEDULED) >= 0){
				return true;
			} else if (this.courseinfo.get(CourseInfo.TAKEN) >= 0) {
				return true;
			} else {return false;}
		}
	}
	
	public boolean isNull() {
		return (this.name==null && !isGate());
	}
	
	public float getPathLength() {
		if (isNull()) {
			return 0;
		} else if (isGate()) {
			return 1 + ((SchedulerGate) this).getBestChild(Integer.MAX_VALUE).getPathLength();
		} else {
			return 1 + ((SchedulerCourse) this).getChild().getPathLength();
		}
		
	}
	
	public float getCost() {
		//TODO: Overlap calculation
		float overlap = this.getOverlapScore();
		if (isNull()) {
			return 0;
		}else if(isGate()) {
			SchedulerGate gate = (SchedulerGate) this;
			float cost = 0;
			for (int idx : gate.getChoices(Integer.MAX_VALUE)) {
				cost += gate.getChild(idx).getCost();
			}
			return cost / overlap;
		} else {
			return 1 + ((SchedulerCourse) this).getChild().getCost();
		}
	}
	
	private float getOverlapScore() {
		this.getDepth();
		return this.parents.size();
	}
	
	private float getDepth() {
		return 0;
	}
};
