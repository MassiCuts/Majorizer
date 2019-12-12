package scheduler;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Random;

public class SchedulerGate extends SchedulerNode {
	private Random rand = new Random();

	static int numberOfGates = 0;
	{
		numberOfGates += 1;
	}
	
	public SchedulerGate(SchedulerNode node) throws Exception{
		if (node.gateinfo.isEmpty()) {
			throw new Exception("Tried to initialize a gate with a node that was not a gate");//Throw some error so we know this shouldn't have been happened
		}
		this.children = node.children;
		this.parents = node.parents;
		this.name = node.name;
		this.gateinfo = new Hashtable<GateInfo, Integer>();// Otherwise you'll get an error when inserting
		this.gateinfo.put(GateInfo.OPTIONS, node.gateinfo.get(GateInfo.OPTIONS));
		this.gateinfo.put(GateInfo.REQUIRED, node.gateinfo.get(GateInfo.REQUIRED));
	}
	
	public SchedulerGate(int options, int required, ArrayList<SchedulerNode> children) {
		this(options, required);
		this.name = "GATE " + Integer.toString(numberOfGates);
		if(children == null) {
			System.out.println("chilldren are null");
		}
		this.children = children;
		this.gateinfo = new Hashtable<GateInfo, Integer>();// Otherwise you'll get an error when inserting
		this.gateinfo.put(GateInfo.OPTIONS, options);
		this.gateinfo.put(GateInfo.REQUIRED, required);
	}

	public SchedulerGate(int options, int required) {
		this.name = "GATE " + Integer.toString(numberOfGates);
		this.children = new ArrayList<SchedulerNode>();
		this.parents  = new ArrayList<SchedulerNode>();
		this.gateinfo = new Hashtable<GateInfo, Integer>();// Otherwise you'll get an error when inserting
		this.gateinfo.put(GateInfo.OPTIONS, options);
		this.gateinfo.put(GateInfo.REQUIRED, required);
	}
	
	public ArrayList<Float> getPathLengths(int semester_num) throws Exception {
		assert(selfcheck());
		ArrayList<Float> scores = new ArrayList<Float>();
		for (int i = 0; i < this.children.size(); ++i) {
			scores.add(this.children.get(i).getPathLength(semester_num));
		}
		return scores;
	}
	
	public float getSinglePathLength(int semester_num) throws Exception {
		return this.getBestChild(Integer.MAX_VALUE, semester_num).getPathLength(semester_num);
	}
	
	public ArrayList<Float> getCosts(int semester_num) throws Exception {
		assert(selfcheck());
		ArrayList<Float> scores = new ArrayList<Float>();
		for (SchedulerNode node : this.children) {
			scores.add(node.getCost(semester_num));
		}
		return scores;
	}
	
	public float getCost(int semester_num) throws Exception {
		/*
		 * Returns the sum of the k smallest price scores
		 */
		assert(selfcheck());
		ArrayList<Float> scores = this.getCosts(semester_num);
		float sum = 0;
		for (int i = 0; i < scores.size(); ++i) {
			
		}
		return sum;
	}
	
	public SchedulerNode getBestChild(int semesters_remaining, int semester_num) throws Exception {
		assert(selfcheck());
		ArrayList<Integer> choices = this.getChoices( semesters_remaining, semester_num);
		int index = this.getLongestChoice( choices, 0, semesters_remaining, semester_num);
		SchedulerNode next_node = this.getChild(index);
		return next_node;
	}
	
	public SchedulerNode getGoodChild(int semesters_remaining, int semester_num) throws Exception {
		assert(selfcheck());
		ArrayList<Integer> choices = this.getChoices(semesters_remaining, semester_num);
		int index = this.getLongestChoice(choices, 1, semesters_remaining, semester_num);
		SchedulerNode next_node = this.getChild(index);
		return next_node;
	}
	
	public SchedulerNode getChild(int index) {
		return this.children.get(index);
	}
	
	protected ArrayList<Integer> getChoices(int semesters_remaining, int semester_num) throws Exception {
		/*
		 * Get the k smallest prices that are feasible to complete, given the semesters left
		 * Returns size k list of ints representing indexes of the k smallest scores
		 */
		//TODO: base this on semesters remaining
		ArrayList<Float> lengths = this.getPathLengths(semester_num);
		ArrayList<Float> costs = this.getCosts(semester_num);
		ArrayList<Integer> choices = new ArrayList<>();
		//for (int i = 0; i < this.required; ++i) {
		for (int i = 0; i < this.gateinfo.get(GateInfo.REQUIRED); ++i) {
			int min_idx = -1;
			float min_val = (float) Integer.MAX_VALUE;
			for (int j = 0; j < costs.size(); ++j) {
				if (costs.get(j) < min_val && lengths.get(j) <= semesters_remaining) {
					min_val = costs.get(j);
					min_idx = j;
				}
			}
			if (min_idx == -1) {
				System.out.println(this.name + " ");
				System.out.println(semesters_remaining + " " + semester_num);
				for(SchedulerNode child: this.children) {
					float plength = child.getPathLength(semester_num);
					System.out.print(child.name + ":" + child.getPathLength(semester_num));
					if (child.isGate()) {
						System.out.println("\t" + child.gateinfo.get(GateInfo.REQUIRED));
					} else {System.out.println();}
					if (plength > semesters_remaining) {
						for (SchedulerNode child2: child.children) {
							System.out.print("\t" + child2.name + ":" + child2.getPathLength(semester_num));
							if (child2.isGate()) {
								System.out.println("\t" + child2.gateinfo.get(GateInfo.REQUIRED));
							} else {System.out.println();}
							for (SchedulerNode child3: child2.children) {
								System.out.print("\t\t" + child3.name + ":" + child3.getPathLength(semester_num));
								if (child3.isGate()) {
									System.out.println("\t" + child3.gateinfo.get(GateInfo.REQUIRED));
								} else {System.out.println();}
								for (SchedulerNode child4: child3.children) {
									System.out.print("\t\t\t" + child4.name + ":" + child4.getPathLength(semester_num));
									if (child4.isGate()) {
										System.out.println("\t" + child4.gateinfo.get(GateInfo.REQUIRED));
									} else {System.out.println();}
								}
							}
						}
					}
				}
				throw new Exception("No way to satisfy this gate in required remaining semesters");
			}
			choices.add(min_idx);
			costs.set(min_idx, (float) Integer.MAX_VALUE);
		}
		return choices;
	}
	
	private int getLongestChoice( ArrayList<Integer> choices, int attempt, int semesters_remaining, int semester_num) throws Exception {
		/*
		 * If attempt = 0, get the longest path value
		 * If attempt != 0, get a random path value with probability skewed toward longer paths, unless semesters_remaining == longest_path, in which case you must take longest path
		 * Returns the best index in scores, given that the indexes are in choices
		 */
		//TODO: calculate probability of path selection so randomness is actually kinda smart instead of very dumb
		ArrayList<Float> scores = this.getPathLengths(semester_num);
		if (attempt==0) {
			float max_length = 0;
			int max_index = 0;
			for (int i = 0; i < choices.size(); ++i) {
				int choice_idx = choices.get(i);
				float choice_length = scores.get(choice_idx);
				if (choice_length > max_length) {
					max_length = choice_length;
					max_index = choice_idx;
				}
			}
			return max_index;
		} else {
			int actual_longest_choice = this.getLongestChoice(choices, 0, semesters_remaining, semester_num);
			if (semesters_remaining == this.children.get(actual_longest_choice).getPathLength(semester_num)) {
				return actual_longest_choice;
			}
			int choice_idx = rand.nextInt(choices.size());
			int attempts = 0;
			while(this.getChild(choices.get(choice_idx)).getPathLength(semester_num) == 0 && attempts < 1000) {
				choices.remove(choice_idx);
				choice_idx = rand.nextInt(choices.size());
				attempts += 1;
			}
			if ( attempts == 1000) {throw new Exception("Dear god why");}
			//if (this.name.contentEquals("GATE 170")) {System.out.println("GATE 170: Longest choice is index " + choices.get(choice_idx) + " which has path length " + this.getChild(choices.get(choice_idx)).getPathLength(semester_num));}
			return (int) choices.get(choice_idx);
		}
	}
	
	private boolean selfcheck() {
		/*
		 * Makes sure that all incoming edges have been attached.
		 * Otherwise, the gate is not ready for use
		 */
		//return this.options == this.children.size();
		return this.gateinfo.get(GateInfo.OPTIONS) == this.children.size();
	}
	
	public boolean equals(SchedulerNode node) {
		if (node.isGate()){
			if (node.children == this.children) {
				return true;
			}
		}
		return false;
	}
};
