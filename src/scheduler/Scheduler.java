package scheduler;
import java.util.Vector;
import java.util.Random;
import java.lang.IllegalArgumentException;

public class Scheduler {
	
	private int num_courses=6;
	private int num_semesters=8;
	private SchedulerGraph graph = new SchedulerGraph();
	private Random rand = new Random();
	static int MAX_ATTEMPTS = 100;
	
	public Scheduler(SchedulerGraph graph) {
		this.graph = graph;
	}
	
	public Scheduler(SchedulerGraph graph, int num_courses, int num_semesters) {
		this.num_courses = num_courses;
		this.num_semesters = num_semesters;
		this.graph = graph;
	}
	
	public Vector<Vector<String>> schedule(){
		Vector<Vector<String>> sched = new Vector<Vector<String>>();
		for (int i = 0; i < this.num_semesters; ++i) {
			sched.add(new Vector<String>());
			for(int j = 0; j < this.num_courses; ++j) {
				SchedulerCourse course = new SchedulerCourse();
				SchedulerNode node = this.graph.top;
				int attempt = 0;
				while (!node.child.satisfied());
					if (node.isgate()) {
						//Select which path to take (Longest path of the k shortest plausible paths)
						SchedulerGate gate = SchedulerGate(node);
						Vector<Float> scores = gate.pathscores;
						Vector<Float> prices = gate.pricescores;
						int n = gate.options;
						int k = gate.required;
						int index = 0;
						Vector<Integer> choices = getChoices(scores,prices,k,attempt);
						index = getLongestChoice(choices,attempt);
						node = gate.getchild(index);
					} else {
						//since we know the child isn't satisfied due to the while condition, move to child node
						node = node.child
					}
					if (!course.available()){
						++attempt;
						node = this.graph.top;
						if (attempt > MAX_ATTEMPTS) {
							throw Exception("Your stupid program cant find a feasible schedule you dumb fuck");
						}
					}
					
				course = node;
				sched.get(i).add(course.name);
			}
		}
		if (!graph.top.satisfied()) {
			throw Exception("could not graduate in the required semester and credit hours per semester restraints");
		}
		return sched;
	}
	
	private Vector<Integer> getChoices(Vector<Float> scores, Vector<Float> prices, int k, int semesters_remaining) {
		/*
		 * Get the k smallest prices that are feasible to complete, given the semesters left
		 * Returns size k list of ints representing indexes of the k smalles scores
		 */
		Vector<Integer> choices = new Vector<>();
		for (int i = 0; i < k; ++i) {
			int min_idx = -1;
			float min_val = (float) Integer.MAX_VALUE;
			for (int j = 0; j < scores.size(); ++j) {
				if (scores.get(j) < min_val) {
					min_val = scores.get(j);
					min_idx = j;
				}
			}
			choices.add(min_idx);
			scores.set(min_idx, (float) Integer.MAX_VALUE);
		}
		return choices;
	}
	
	private int getLongestChoice(Vector<Float> scores, Vector<Integer> choices, int attempt, int semesters_remaining) {
		/*
		 * If attempt = 0, get the longest path value
		 * If attempt != 0, get a random path value unless semesters_remaining = longest_path, in which case you must take longest path
		 * Returns the best index in scores, given that the indexes are in choices
		 */
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
			int actual_longest_choice = getLongestChoice(scores, choices, 0, semesters_remaining);
			if (semesters_remaining == actual_longest_choice) {
				return actual_longest_choice;
			}
			int choice_idx = rand.nextInt(choices.size());
			return (int) choices.get(choice_idx);
		}
	}
}
