package scheduler;
import java.util.Vector;

public class SchedulerGate extends SchedulerNode {
	public SchedulerGate(SchedulerNode node) {}
	public int options;
	public int required;
	
	public Vector<Float> getPathScores() {}
	public float getSinglePathScore() {}
	public Vector<Float> getPriceScores() {}
	public float getSinglePriceScore() {}
	public SchedulerNode getChild(int index) {}
}
