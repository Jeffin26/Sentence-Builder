//Jeffin Plakuzhiyil Varghese
//JXP210029

import java.sql.Connection;

public class TestConnection {
    public static void main(String[] args) {
        try {
            // Try to connect to the database
            Connection conn = DBConnection.getConnection();
            // If successful
            System.out.println("Connected to MySQL successfully!");
            // Close the connection
            conn.close();

            // If connection fails, print error
        } catch (Exception e) {
            System.out.println("Connection failed.");
            e.printStackTrace();
        }
    }
}