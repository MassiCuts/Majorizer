package framework;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import scheduler.SchedulerGraph;

public class AcademicPlan {
	private String startSemester;
	private ArrayList<Integer> degreeIDs;
	private Map<String, ArrayList<Integer>> selectedCourseIDs;
	
	
	public static AcademicPlan getEmptyPlan(String startSemester) {
		return new AcademicPlan(startSemester, new ArrayList<>(), new HashMap<>(), null);
	}
	
	public AcademicPlan(String startSemester, ArrayList<Integer> degreesIDs, Map<String, ArrayList<Integer>> selectedCoursesIDs, SchedulerGraph schedulerGraph) {
		this.startSemester = startSemester;
		this.degreeIDs = degreesIDs;
		this.selectedCourseIDs = selectedCoursesIDs;
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
	
	public SchedulerGraph getCurriculumSchedulerGraph() {
		if(degreeIDs.isEmpty())
			return null;
		
		int firstID = degreeIDs.get(0);
		Curriculum curriculum = DatabaseManager.getCurriculum(firstID);
		RequiredCourses rc = curriculum.getRequiredCourses();
		SchedulerGraph root = new SchedulerGraph(rc);
		
		for(int i = 1; i < degreeIDs.size(); i++) {
			int currentID = degreeIDs.get(i);
			curriculum = DatabaseManager.getCurriculum(currentID);
			rc = curriculum.getRequiredCourses();
			SchedulerGraph graph = new SchedulerGraph(rc);
			root.mergeGraphs(graph);
		}
		return root;
	}
	
	
	
}
