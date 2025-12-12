package appointment;
import static org.junit.Assert.*;
import org.junit.Test;

public class TaskServiceTest {
	 @Test
	    void testAddTask() {
	        TaskService service = new TaskService();
	        String taskId = service.addTask("Task Name", "Task Description");

	        Task task = service.getTask(taskId);
	        assertNotNull(task);
	        assertEquals("Task Name", task.getName());
	        assertEquals("Task Description", task.getDescription());
	    }

	    @Test
	    void testDeleteTask() {
	        TaskService service = new TaskService();
	        String taskId = service.addTask("Task Name", "Task Description");

	        service.deleteTask(taskId);
	        assertNull(service.getTask(taskId));
	    }

	    @Test
	    void testUpdateTaskName() {
	        TaskService service = new TaskService();
	        String taskId = service.addTask("Old Name", "Description");

	        service.updateTaskName(taskId, "New Name");
	        assertEquals("New Name", service.getTask(taskId).getName());
	    }

	    @Test
	    void testUpdateTaskDescription() {
	        TaskService service = new TaskService();
	        String taskId = service.addTask("Name", "Old Description");

	        service.updateTaskDescription(taskId, "New Description");
	        assertEquals("New Description", service.getTask(taskId).getDescription());
	    }
	

}
