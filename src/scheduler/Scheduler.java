package scheduler;


import java.util.ArrayList;

public class Scheduler {
	
	private int num_courses=6;
	private int num_semesters=8;
	//private int current_semester=0;
	static int MAX_ATTEMPTS = 1000;
	
	public Scheduler() {}
	
	public void setNumSemesters(int num_semesters) {this.num_semesters = num_semesters;}
	public void setNumCourses(int num_courses) {this.num_courses = num_courses;}
	public ArrayList<ArrayList<String>> makeList(String [][] templist)
	{
		ArrayList<ArrayList<String>> list = new ArrayList<ArrayList<String>>();
		for(String [] i: templist) {
			ArrayList<String> sublist = new ArrayList<String>();
			for (String j: i) {
				sublist.add(j);
			}
			list.add(sublist);
		}
		return list;
	}
	public ArrayList<ArrayList<String>> schedule(SchedulerGraph graph, ArrayList<SchedulerCourse> requested_adds, ArrayList<SchedulerCourse> requested_drops, ArrayList<SchedulerCourse> taken_courses, int current_semester) throws Exception {
		// TODO make sure you don't add courses to a past semester
		//return makeList(new String[][] /{new String[] {"CS141","MA131","PH131"}, new String[] {"CS142","MA132","CM131"}});
		SchedulerCourse course = null;
		ArrayList<ArrayList<String>> sched = new ArrayList<ArrayList<String>>();
		for (int i = 0; i < this.num_semesters; ++i) {
			sched.add(new ArrayList<String>());
			if (i < current_semester) {
				//TODO: Add all the classes taken during this semester then skip
				for(SchedulerCourse c: taken_courses) {
					System.out.println(c.name + "was taken semester " + c.courseinfo.get(CourseInfo.TAKEN));
					if(c.courseinfo.get(CourseInfo.TAKEN) == i) {
						graph.setCourseAttribute(c.name, CourseInfo.TAKEN, i);
						sched.get(i).add(c.name);
						System.out.println("Already taken course" + c.name + " in semester " + i);
					}
				}
				continue;
			}
			int added_this_semester=0;
			for(SchedulerCourse c : requested_adds) {
				if (c.added == i) {
					if(!c.isAvailable(i)) {throw new Exception("this course isn't available for the semester it was added to");}
					sched.get(i).add(c.name);
					graph.setCourseAttribute(c.name, CourseInfo.SCHEDULED, i);
					continue;
				}
			}
			
			for(int j = 0; j < this.num_courses-added_this_semester; ++j) {
				SchedulerNode node = graph.root;
				SchedulerNode next_node = graph.root;
				int attempt = 0;
				do {
					node = next_node;
					if (node.isGate()) {
						//Select which path to take (Longest path of the k shortest plausible paths)
						SchedulerGate gate = (SchedulerGate) node;
						if (attempt > 0) {
							next_node = gate.getGoodChild(this.num_semesters, i);
						} else {
							next_node = gate.getBestChild(this.num_semesters-i, i);
						}
					} else {
						//since we know the child isn't satisfied due to the while condition, move to child node
						course = (SchedulerCourse) node;
						next_node = course.getChild();
						if (next_node.isSatisfied(i)) {
							if (!course.isAvailable(i) || course.isDropped(i) || this.containsString(sched, course.name) || !next_node.isSatisfied(i-1)){
								++attempt;
								next_node = graph.root;
								if (attempt > MAX_ATTEMPTS) {
									if (i < this.num_semesters-1) {break;}	//If cannot schedule a class this semester, but still have more semesters left, just don't fill this semester
									else {	//If there are no semesters left, then a feasible schedule cannot be made
										throw new RuntimeException("Cannot find a feasible schedule");
									}
								}
							} else {
								System.out.println(next_node.name);
								System.out.println(sched);
							}
						}
					}
				} while (!next_node.isSatisfied(i));
				if (graph.root.isSatisfied(i)) {break;}
				if (attempt > MAX_ATTEMPTS) {break;}
				node.courseinfo.put(CourseInfo.SCHEDULED, i);
				sched.get(i).add(node.name);
				System.out.println("Added course" + node.name + " to semester " + i);
				graph.setCourseAttribute(node.name, CourseInfo.SCHEDULED, i);
			}
			for (String name : sched.get(i)) {
				SchedulerCourse c = graph.getCourse(name);
				System.out.println("Course: " + c.name + "Scheduled for semester: " + c.courseinfo.get(CourseInfo.SCHEDULED));
			}
			if (graph.root.isSatisfied(i)) {break;}
		}
		if (!graph.root.isSatisfied(this.num_semesters)) {
			throw new Exception("could not graduate in the required semester and credit hours per semester restraints");
		}
		System.out.println("Scheduling Complete!");
		return sched;
	}
	
	private boolean containsString(ArrayList<ArrayList<String>> doublelist, String s){
		for (ArrayList<String> list: doublelist) {
			if (list.contains(s)){return true;}
		}
		return false;
	}
};
