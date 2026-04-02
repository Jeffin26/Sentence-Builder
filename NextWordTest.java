//Jeffin Plakuzhiyil Varghese
//JXP210029

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class NextWordTest {
    public static void main(String[] args) {
        try {
            Connection conn = DBConnection.getConnection();
            Statement stmt = conn.createStatement();

            String inputWord = "the";

            String sql = "SELECT w2.word_text " +
                    "FROM word_follows wf " +
                    "JOIN word w1 ON wf.word_id = w1.id " +
                    "JOIN word w2 ON wf.next_word_id = w2.id " +
                    "WHERE w1.word_text = '" + inputWord + "' " +
                    "ORDER BY wf.follow_count DESC LIMIT 1";

            ResultSet rs = stmt.executeQuery(sql);

            if (rs.next()) {
                System.out.println("Next word after '" + inputWord + "' is: " + rs.getString("word_text"));
            } else {
                System.out.println("No next word found.");
            }

            conn.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}