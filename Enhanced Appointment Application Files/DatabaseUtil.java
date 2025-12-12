package appointment;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseUtil {
	
	private static final String URL  =
            "jdbc:mysql://127.0.0.1:3306/cs_capstone?useSSL=false&serverTimezone=UTC";
    private static final String USER = "root";
    private static final String PASS = "";   // XAMPP default

    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("MySQL JDBC driver not found", e);
        }
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASS);
    }
}


