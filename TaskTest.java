package appointment;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*; 

public class TaskTest {
	@Test
    void testValidTaskCreation() {
        Task task = new Task("12345", "Task Name", "Task Description");
        assertEquals("12345", task.getTaskId());
        assertEquals("Task Name", task.getName());
        assertEquals("Task Description", task.getDescription());
    }

    @Test
    void testInvalidTaskId() {
        assertThrows(IllegalArgumentException.class, () -> new Task(null, "Name", "Description"));
        assertThrows(IllegalArgumentException.class, () -> new Task("12345678901", "Name", "Description"));
    }

    @Test
    void testInvalidName() {
        assertThrows(IllegalArgumentException.class, () -> new Task("12345", null, "Description"));
        assertThrows(IllegalArgumentException.class, () -> new Task("12345", "excessivelylongnamethatwillthrowwexeption", "Description"));
    }

    @Test
    void testInvalidDescription() {
        assertThrows(IllegalArgumentException.class, () -> new Task("12345", "Name", null));
        assertThrows(IllegalArgumentException.class, () -> new Task("12345", "Name", "This description is far too long to be valid and exceeds fifty characters"));
    }

    @Test
    void testSetters() {
        Task task = new Task("12345", "Old Name", "Old Description");
        task.setName("New Name");
        task.setDescription("New Description");

        assertEquals("New Name", task.getName());
        assertEquals("New Description", task.getDescription());
    }

}
