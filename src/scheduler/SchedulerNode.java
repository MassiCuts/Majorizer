package scheduler;
import java.util.ArrayList;
import java.util.Hashtable;

public class SchedulerNode {
	protected ArrayList<SchedulerNode> children;
	protected ArrayList<SchedulerNode> parents;
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
		this.gateinfo = new Hashtable<GateInfo, Integer>();// Otherwise you'll get an error when inserting
		this.gateinfo.put(GateInfo.OPTIONS, gate.options);
		this.gateinfo.put(GateInfo.REQUIRED, gate.required);
	}
	
	public SchedulerNode(SchedulerCourse course) {
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
		if (this.isgate) {
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
