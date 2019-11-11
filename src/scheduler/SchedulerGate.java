package scheduler;
import java.util.Vector;

public class SchedulerGate extends SchedulerNode {
	public int options;
	public int required;
	
	public SchedulerGate(SchedulerNode node) {
		this.options = node.gateinfo.get(GateInfo.OPTIONS);
		this.required = node.gateinfo.get(GateInfo.REQUIRED);
	}

	public SchedulerGate(int options, int required) {
		this.options = options;
		this.required = required;
	}
	
	public Vector<Float> getPathScores() {
		assert(selfcheck());
		Vector<Float> scores = 
	}
	public float getSinglePathScore() {
		/*
		 * Returns the longest path score out of the k shortest price scores
		 */
		assert(selfcheck());
		Vector<Float> prices = getPriceScores();
		
		for (int i = 0; i < prices.size())
	}
	
	public Vector<Float> getPriceScores() {
		assert(selfcheck());
	}
	
	public float getSinglePriceScore() {
		/*
		 * Returns the sum of the k smallest price scores
		 */
		assert(selfcheck());
	}
	public SchedulerNode getChild(int index) {}
	
	private boolean selfcheck() {
		/*
		 * Makes sure that all incoming edges have been attached.
		 * Otherwise, the gate is not ready for use
		 */
		return this.options == this.children.size();
	}
}
