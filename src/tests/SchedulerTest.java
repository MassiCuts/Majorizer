package tests;
import scheduler.Scheduler;
import scheduler.SchedulerGraph;
import scheduler.SchedulerCourse;
import scheduler.SchedulerGate;
import scheduler.SchedulerNode;
import framework.Course;

public class SchedulerTest {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Course c = new Course();
		SchedulerNode node = new SchedulerNode(true);
		SchedulerCourse course = new SchedulerCourse(c);
		SchedulerGate gate = new SchedulerGate(5,2);
		SchedulerNode cnode = (SchedulerNode) course;
		SchedulerNode gnode = (SchedulerNode) gate;
		SchedulerCourse course2 = (SchedulerCourse) cnode;
		SchedulerGate gate2 = (SchedulerGate) gnode;
		System.out.print(gate2.getName() == gate.getName());
		System.out.print(course2.getName() == course.getName());
	}

}
