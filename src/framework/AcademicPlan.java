package framework;

import java.util.ArrayList;
import java.util.Map;

import scheduler.SchedulerGraph;

public class AcademicPlan {
	private String startSemester;
	private ArrayList<Integer> degreeIDs;
	private Map<String, ArrayList<Integer>> selectedCourseIDs;
	private SchedulerGraph schedulerGraph;
	
	public AcademicPlan(String startSemester, ArrayList<Integer> degreesIDs, Map<String, ArrayList<Integer>> selectedCoursesIDs, SchedulerGraph schedulerGraph) {
		this.startSemester = startSemester;
		this.degreeIDs = degreesIDs;
		this.selectedCourseIDs = selectedCoursesIDs;
		this.schedulerGraph = schedulerGraph;
	}
	
	public String getStartSemester() {
		return startSemester;
	}
	
	public ArrayList<Integer> getDegreeIDs() {
		return degreeIDs;
	}
	
	public Map<String, ArrayList<Integer>> getSelectedCourseIDs() {
		return selectedCourseIDs;
	}
	
	public SchedulerGraph getSchedulerGraph() {
		return schedulerGraph;
	}
	
}
