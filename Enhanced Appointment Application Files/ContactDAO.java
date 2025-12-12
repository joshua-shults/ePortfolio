package appointment;

import java.sql.*;
//Contact is primary key, have one before testing other methods
public class ContactDAO {

    public void addContact(Contact contact) {
        String sql = "INSERT INTO contacts (contact_id, first_name, last_name, phone, address) " +
                     "VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, contact.getContactId());
            stmt.setString(2, contact.getFirstName());
            stmt.setString(3, contact.getLastName());
            stmt.setString(4, contact.getPhone());
            stmt.setString(5, contact.getAddress());

            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error inserting contact: " + e.getMessage(), e);
        }
    }

    public Contact getContact(String contactId) {
        String sql = "SELECT contact_id, first_name, last_name, phone, address " +
                     "FROM contacts WHERE contact_id = ?";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, contactId);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    String id    = rs.getString("contact_id");
                    String first = rs.getString("first_name");
                    String last  = rs.getString("last_name");
                    String phone = rs.getString("phone");
                    String addr  = rs.getString("address");

                    return new Contact(id, first, last, phone, addr);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching contact: " + e.getMessage(), e);
        }

        return null; // not found
    }

    public boolean exists(String contactId) {
        String sql = "SELECT 1 FROM contacts WHERE contact_id = ?";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, contactId);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error checking contact existence: " + e.getMessage(), e);
        }
    }

    public void updatePhone(String contactId, String newPhone) {
        String sql = "UPDATE contacts SET phone = ? WHERE contact_id = ?";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, newPhone);
            stmt.setString(2, contactId);

            int rows = stmt.executeUpdate();
            if (rows == 0) {
                throw new IllegalArgumentException("Contact ID not found.");
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error updating phone: " + e.getMessage(), e);
        }
    }

    public void updateAddress(String contactId, String newAddress) {
        String sql = "UPDATE contacts SET address = ? WHERE contact_id = ?";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, newAddress);
            stmt.setString(2, contactId);

            int rows = stmt.executeUpdate();
            if (rows == 0) {
                throw new IllegalArgumentException("Contact ID not found.");
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error updating address: " + e.getMessage(), e);
        }
    }

    public void deleteContact(String contactId) {
        String sql = "DELETE FROM contacts WHERE contact_id = ?";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, contactId);
            int rows = stmt.executeUpdate();
            if (rows == 0) {
                throw new IllegalArgumentException("Contact ID not found.");
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error deleting contact: " + e.getMessage(), e);
        }
    }
}
