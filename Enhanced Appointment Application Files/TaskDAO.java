package appointment;

import java.sql.*;

public class TaskDAO {

    public void addTask(Task task, String contactId) {
        String sql = "INSERT INTO tasks (task_id, contact_id, name, description) " +
                     "VALUES (?, ?, ?, ?)";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, task.getTaskId());
            stmt.setString(2, contactId);
            stmt.setString(3, task.getName());
            stmt.setString(4, task.getDescription());

            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error inserting task: " + e.getMessage(), e);
        }
    }

    public Task getTask(String taskId) {
        String sql = "SELECT task_id, name, description " +
                     "FROM tasks WHERE task_id = ?";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, taskId);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    String id   = rs.getString("task_id");
                    String name = rs.getString("name");
                    String desc = rs.getString("description");

                    return new Task(id, name, desc);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching task: " + e.getMessage(), e);
        }

        return null;
    }

    public void updateTaskName(String taskId, String newName) {
        String sql = "UPDATE tasks SET name = ? WHERE task_id = ?";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, newName);
            stmt.setString(2, taskId);
            int rows = stmt.executeUpdate();
            if (rows == 0) {
                throw new IllegalArgumentException("Task ID not found.");
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error updating task name: " + e.getMessage(), e);
        }
    }

    public void updateTaskDescription(String taskId, String newDescription) {
        String sql = "UPDATE tasks SET description = ? WHERE task_id = ?";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, newDescription);
            stmt.setString(2, taskId);
            int rows = stmt.executeUpdate();
            if (rows == 0) {
                throw new IllegalArgumentException("Task ID not found.");
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error updating task description: " + e.getMessage(), e);
        }
    }

    public void deleteTask(String taskId) {
        String sql = "DELETE FROM tasks WHERE task_id = ?";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, taskId);
            int rows = stmt.executeUpdate();
            if (rows == 0) {
                throw new IllegalArgumentException("Task ID not found.");
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error deleting task: " + e.getMessage(), e);
        }
    }

    public boolean exists(String taskId) {
        String sql = "SELECT 1 FROM tasks WHERE task_id = ?";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, taskId);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error checking task existence: " + e.getMessage(), e);
        }
    }
}
