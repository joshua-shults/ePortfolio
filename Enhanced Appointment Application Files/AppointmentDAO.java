package appointment;

import java.sql.*;
import java.util.Date;

public class AppointmentDAO {

    public void addAppointment(Appointment appointment, String contactId) {
        String sql = "INSERT INTO appointments " +
                     "(appointment_id, contact_id, appointment_date, description) " +
                     "VALUES (?, ?, ?, ?)";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, appointment.getAppointmentID());
            stmt.setString(2, contactId);
            stmt.setTimestamp(3, new Timestamp(appointment.getAppointmentDate().getTime()));
            stmt.setString(4, appointment.getDescription());

            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error inserting appointment: " + e.getMessage(), e);
        }
    }

    public Appointment getAppointment(String appointmentId) {
        String sql = "SELECT appointment_id, appointment_date, description " +
                     "FROM appointments WHERE appointment_id = ?";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, appointmentId);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    String id = rs.getString("appointment_id");
                    Timestamp ts = rs.getTimestamp("appointment_date");
                    Date date = new Date(ts.getTime());
                    String desc = rs.getString("description");

                    return new Appointment(id, date, desc);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching appointment: " + e.getMessage(), e);
        }

        return null;
    }

    public void deleteAppointment(String appointmentId) {
        String sql = "DELETE FROM appointments WHERE appointment_id = ?";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, appointmentId);
            int rows = stmt.executeUpdate();
            if (rows == 0) {
                throw new IllegalArgumentException("Appointment ID not found.");
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error deleting appointment: " + e.getMessage(), e);
        }
    }

    public boolean exists(String appointmentId) {
        String sql = "SELECT 1 FROM appointments WHERE appointment_id = ?";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, appointmentId);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error checking appointment existence: " + e.getMessage(), e);
        }
    }
}
