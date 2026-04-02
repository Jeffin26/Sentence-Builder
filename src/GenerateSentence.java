//Jeffin Plakuzhiyil Varghese

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class GenerateSentence {
    public static void main(String[] args) {
        try {
            Connection conn = DBConnection.getConnection();
            Statement stmt = conn.createStatement();

            String currentWord = "the"; // starting word
            String sentence = currentWord;

            for (int i = 0; i < 10; i++) {

                String sql = "SELECT w2.word_text " +
                        "FROM word_follows wf " +
                        "JOIN word w1 ON wf.word_id = w1.id " +
                        "JOIN word w2 ON wf.next_word_id = w2.id " +
                        "WHERE w1.word_text = '" + currentWord + "' " +
                        "ORDER BY wf.follow_count DESC LIMIT 1";

                ResultSet rs = stmt.executeQuery(sql);

                if (rs.next()) {
                    String nextWord = rs.getString("word_text");
                    sentence += " " + nextWord;
                    currentWord = nextWord;
                } else {
                    break;
                }
            }

            System.out.println("Generated sentence:");
            System.out.println(sentence);

            conn.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
