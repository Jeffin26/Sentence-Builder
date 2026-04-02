//Jeffin Plakuzhiyil Varghese

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class ViewWords {
    public static void main(String[] args) {
        try {
            // connect to database
            Connection conn = DBConnection.getConnection();

            // create statement
            Statement stmt = conn.createStatement();

            // run query
            ResultSet rs = stmt.executeQuery("SELECT word_text, total_count FROM word LIMIT 20");

            // print results
            while (rs.next()) {
                String word = rs.getString("word_text");
                int count = rs.getInt("total_count");

                System.out.println(word + " - " + count);
            }

            conn.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
