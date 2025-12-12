package appointment;

public class Task {
	private final String taskId;
	private String name;
	private String description;
	
	//constructor
	public Task(String taskId, String name, String description) {
		if (taskId == null || taskId.length() > 10) {
			throw new IllegalArgumentException("Invalid Task ID");
		}
		if (name == null || name.length() > 20) {
			throw new IllegalArgumentException("Invalid Name");
		}
		if (description == null || description.length() > 50) {
			throw new IllegalArgumentException("Invalid description");
		}
		this.taskId = taskId;
		this.name = name;
		this.description = description;
	}
	
	//"getter" methods
	public String getTaskId() {
		return taskId;
	}
	public String getName() {
		return name;
	}
	public String getDescription() {
		return description;
	}
	//"setter" methods
	public void setDescription(String description) {
		if (description == null || description.length() > 50) {
			throw new IllegalArgumentException("Invalid Description");
		}
		this.description = description;
	}
	public void setName(String name) {
		if (name == null || name.length() > 20) {
			throw new IllegalArgumentException("Invalid name");
		}
		this.name = name;
	}

}
