package scheduler;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Random;

public class SchedulerNode {
	protected ArrayList<SchedulerNode> children;
	protected ArrayList<SchedulerNode> parents;
	protected boolean isgate;
	protected boolean root = false;
	protected String name = null;
	protected Hashtable<CourseInfo, Integer> courseinfo = new Hashtable<CourseInfo, Integer>(); //Taken, available, added, dropped, scheduled
	protected Hashtable<GateInfo, Integer> gateinfo = new Hashtable<GateInfo, Integer>();	//n, k
	
	public SchedulerNode(SchedulerGate gate) {
		System.out.println("Making a gate");
		this.children = gate.children;
		this.parents = gate.parents;
		this.isgate = true;
		this.name = gate.name;
		this.gateinfo = new Hashtable<GateInfo, Integer>();// Otherwise you'll get an error when inserting
		this.gateinfo.put(GateInfo.OPTIONS, gate.gateinfo.get(GateInfo.OPTIONS));
		this.gateinfo.put(GateInfo.REQUIRED, gate.gateinfo.get(GateInfo.REQUIRED));
	}
	
	public SchedulerNode(SchedulerCourse course) {
		System.out.println("Making a course");
		this.children = course.children;
		this.parents = course.parents;
		this.isgate = false;
		this.name = course.name;
		this.courseinfo = new Hashtable<CourseInfo, Integer>();// Otherwise you'll get an error when inserting
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
	
	public void addParent(SchedulerNode node) {
		this.parents.add(node);
	}
	public void addChild(SchedulerNode node) {
		this.children.add(node);
	}
	
	public String getName() {return this.name;}
	public ArrayList<SchedulerNode> getChildren(){return this.children;}
	public boolean isGate() { 
		String classname = this.getClass().getSimpleName();
		return classname.contentEquals("SchedulerGate");}//this.isgate;}
	
	public boolean isSatisfied(int semester_num) {
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
				if (children.get(i).isSatisfied(semester_num)) {
					++counter;
				}
			}
			if (counter >= this.gateinfo.get(GateInfo.REQUIRED)) {
				return true;
			} else { return false;}
		} else {
			int scheduled = this.courseinfo.get(CourseInfo.SCHEDULED);
			int taken = this.courseinfo.get(CourseInfo.TAKEN);
			if (isNull()) {
				return true;
			} else if (scheduled >= 0 && scheduled <= semester_num){
				return true;
			} else if (taken >= 0) {
				return true;
			} else {
				//if (this.getChildren().get(0).isSatisfied(semester_num)) {
					//System.out.println("Not satisfied as of semester" + semester_num + ": " + this.name);
				//}
				return false;
			}
		}
	}
	
	public boolean isNull() {
		return (this.name==null && !isGate());
	}
	
	public float getPathLength(int semester_num) throws Exception {
		float plength;
		if (isSatisfied(semester_num)) {
			plength = 0;
			return 0;
		} else if (this.isGate()) {
			plength = 0 + ((SchedulerGate) this).getBestChild(Integer.MAX_VALUE, semester_num).getPathLength(semester_num);
		} else {
			assert(this.children.size() <= 1);
			plength = 1 + ((SchedulerCourse) this).getChild().getPathLength(semester_num);
		}
		if (this.name.contentEquals("MA132") && plength == 0) { System.out.println("MA132 has path length 0");}
		return plength;
	}
	
	public float getCost(int semester_num) throws Exception {
		float overlap = this.getOverlapScore();
		if (isNull()) {
			return 0;
		}else if(this.isGate()) {
			SchedulerGate gate = (SchedulerGate) this;
			float cost = 0;
			for (int idx : gate.getChoices(Integer.MAX_VALUE, semester_num)) {
				cost += gate.getChild(idx).getCost(semester_num);
			}
			return cost / overlap;
		} else {
			assert(this.children.size() <= 1);
			return 1 + ((SchedulerCourse) this).getChild().getCost(semester_num);
		}
	}
	
	private float getOverlapScore() {
		//TODO: traverse parents until at same depth, then analyze the gate. Overlap score = gate.options
		/*ArrayList<SchedulerNode> temp_nodes = new ArrayList<SchedulerNode>();
		for(SchedulerNode parent: this.parents) {
			temp_nodes.add(parent);
		} 
		while() {
			int min_depth = getMinDepth(temp_nodes);
			for(int i = 0; i < temp_nodes.size(); ++i) {
				if (temp_nodes.get(i) < min_depth) {
					temp_nodes.set(i, )
				}
			}
		}*/
		return this.parents.size();
	}
	
	protected int getDepth() {
		//TODO: largest depth of parents + 1
		if (this.root) {
			return 0;
		}
		int max_depth = 0;
		for (SchedulerNode parent: this.parents) {
			int depth = parent.getDepth();
			if (depth > max_depth) {max_depth = depth;}
		}
		return max_depth+1;
	}
	
	public boolean equals(SchedulerNode node) {
		if (this.isGate()) {
			return (SchedulerGate) this == node;
		} else {
			return (SchedulerCourse) this == node;
		}
	}
	
	@Override
    public String toString() {
		return traverseNodes(this.children);
	}
	
	private String traverseNodes(ArrayList<SchedulerNode> nodes) {
		String res;
		ArrayList<SchedulerNode> children;
		String output = "";
		for(SchedulerNode n : nodes) {
			children = n.getChildren();
			if(null == children) {
				System.out.println("Nodes are null");
			}
			res = traverseNodes(children);
			output += res;
		}
		return output;
	}
};
