package appointment;
//import maps to store tasks and UUID to create unique IDs for the tasks
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class TaskService {
	private final Map<String, Task> taskMap = new HashMap<>();
	
	//first generate a unique ID for the tasks
	private String generateUniqueId() {
		return UUID.randomUUID().toString().substring(0, 10); //trucate this to only accept 10 characters
	}
	//method to add a task
	public String addTask(String name, String description) {
		String taskId = generateUniqueId();
		Task task = new Task(taskId, name, description);
		taskMap.put(taskId, task);
		return taskId;
	}
	//method to delete a task by its ID
	public void deleteTask(String taskId) {
		if (!taskMap.containsKey(taskId)) {
			throw new IllegalArgumentException("Task with that ID was not found.");
		}
		taskMap.remove(taskId);
	}
	//method for updating a task name
	public void updateTaskName(String taskId, String name) {
		Task task = taskMap.get(taskId);
		if (task == null) {
			throw new IllegalArgumentException("Task with that ID was not found.");
		}
		task.setName(name);
	}
	//method for updating a task's description
	public void updateTaskDescription(String taskId, String description) {
		Task task = taskMap.get(taskId);
		if (task == null) {
			throw new IllegalArgumentException("Task with that ID was not found.");
		}
		task.setDescription(description);
	}
	//method to retrieve a task
	public Task getTask(String taskId) {
		return taskMap.get(taskId);
	}

}
