package scheduler;
import java.util.Vector;
import framework.Course;

public class SchedulerNode {
	private Vector<SchedulerNode> children;
	private Vector<SchedulerNode> parents;
	private int depth;
	private boolean isgate;
	private Vector<Integer> courseinfo; //Taken, available, added, dropped, scheduled
	private Vector<Integer> gateinfo;	//n, k
	
	public SchedulerNode() {}
	public SchedulerNode(Course c) {}
	public SchedulerNode( boolean isroot) {}
	
	public void attachOutgoing(SchedulerNode node) {}
	public void attachIncoming(SchedulerNode node) {}
	public boolean isGate() { return this.isgate;}
	public boolean isSatisfied() {
		/*
		 * If it is a gate and k out of the n children are satisfied
		 * If it is a course and the child is satisfied and the course was added this semester
		 * If it is a course and was already taken or scheduled
		 * If it is a null node
		 */
		return false;
	}
}
