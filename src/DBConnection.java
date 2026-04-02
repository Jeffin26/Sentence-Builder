//Jeffin Plakuzhiyil Varghese
//JXP210029

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    // Database URL
    private static final String URL = "jdbc:mysql://localhost:3306/sentence_builder";
    // MySQL username
    private static final String USER = "root";
    // MySQL password
    private static final String PASSWORD = "xyzCompany123";
    // This method connects to the MySQL database and returns the connection
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
