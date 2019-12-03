package scheduler;
import framework.Course;
import org.yaml.snakeyaml.Yaml;

import scheduler.SchedulerNode;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Map;
import java.util.Vector;

public class SchedulerGraph {
	public SchedulerNode top;
	public String example = "- grad:\n" + 
			"    num: 3\n" +  
			"    requirments:\n" + 
			"      - breath\n" + 
			"      - CS142\n" + 
			"      - CS444\n" +
			"- breath:\n" + 
			"    num: 1\n" +  
			"    requirments:\n" + 
			"      - PH100\n" + 
			"      - PH200\n" +
			"- CS142:\n" + 
			"    num: 0\n" +  
			"    requirments:\n" +
			"- CS444:\n" + 
			"    num: 0\n" +  
			"    requirments:\n" +
			"- PH100:\n" + 
			"    num: 0\n" +  
			"    requirments:\n"+
			"- PH200:\n" + 
			"    num: 0\n" +  
			"    requirments:\n"
			;  
	private Vector<Course> all_courses;
	private Vector<SchedulerNode> graph;
	

	public SchedulerGraph(String description) {
		Yaml yaml = new Yaml();
		InputStream inputStream = this.getClass()
				 .getClassLoader()
				 .getResourceAsStream("/home/david/dev/yogaandtheboys/data/example_schedule.yaml");
		ArrayList<Map<Object, Object>> specification = yaml.load(example);
		System.out.println(specification);
	}
	
	public void mergeGraphs(SchedulerGraph sgraph) {}
}
