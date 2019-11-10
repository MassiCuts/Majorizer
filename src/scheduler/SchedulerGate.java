package scheduler;
import java.util.Vector;

public class SchedulerGate extends SchedulerNode {
	public SchedulerGate(SchedulerNode node) {}
	public int options;
	public int required;
	
	public Vector<Float> getPathScores() {
		return null;
	}
	
	public float getSinglePathScore() {
		return 0f;
	}
	
	public Vector<Float> getPriceScores() {
		return null;
	}
	
	public float getSinglePriceScore() {
		return 0f;
	}
	
	public SchedulerNode getChild(int index) {
		return null;
	}
}
