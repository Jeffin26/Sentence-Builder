//Jeffin Plakuzhiyil Varghese

import java.io.BufferedReader;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;

public class DataImporter {

    public static void main(String[] args) {
        // text files to import
        String[] files = {
                "data/Dracula.txt",
                "data/A_Room_with_a_View.txt",
                "data/War_and_Peace.txt",
                "data/Romeo_and_Juliet.txt",
                "data/The_Return_of_Sherlock_Holmes.txt",
                "data/The_Adventures_of_Sherlock_Holmes.txt"
        };

        try {
            // connect to database
            Connection conn = DBConnection.getConnection();
            // turn off auto commit to make import faster
            conn.setAutoCommit(false);
            // store word ids in memory so we don’t query the same word again and again
            Map<String, Integer> wordCache = new HashMap<>();

            // insert word or update total count
            PreparedStatement insertWord = conn.prepareStatement(
                    "INSERT INTO word (word_text, total_count, start_count, end_count) " +
                            "VALUES (?, 1, 0, 0) " +
                            "ON DUPLICATE KEY UPDATE total_count = total_count + 1"
            );

            // update start count
            PreparedStatement updateStart = conn.prepareStatement(
                    "UPDATE word SET start_count = start_count + 1 WHERE word_text = ?"
            );

            // update end count
            PreparedStatement updateEnd = conn.prepareStatement(
                    "UPDATE word SET end_count = end_count + 1 WHERE word_text = ?"
            );

            // get word id from database
            PreparedStatement getWordId = conn.prepareStatement(
                    "SELECT id FROM word WHERE word_text = ?"
            );

            // insert bigram or update count
            PreparedStatement insertBigram = conn.prepareStatement(
                    "INSERT INTO word_follows (word_id, next_word_id, follow_count) " +
                            "VALUES (?, ?, 1) " +
                            "ON DUPLICATE KEY UPDATE follow_count = follow_count + 1"
            );

            int lineCount = 0;
            int batchCount = 0;

            // go through each file
            for (String fileName : files) {
                // Print which file is being processed
                System.out.println("Reading file: " + fileName);

                // Open the file for reading line by line
                BufferedReader br = new BufferedReader(new FileReader(fileName));
                String line;

                // read file line by line
                while ((line = br.readLine()) != null) {
                    // Count total lines processed
                    lineCount++;

                    // make lowercase and remove punctuation
                    line = line.toLowerCase();
                    line = line.replaceAll("[^a-z ]", " ").trim();

                    // Skip empty lines after cleaning
                    if (line.isEmpty()) {
                        continue;
                    }

                    // split line into words
                    String[] words = line.split("\\s+");

                    for (int i = 0; i < words.length; i++) {
                        String word = words[i];

                        if (word.isEmpty()) {
                            continue;
                        }

                        // // Insert or update the word count in the database
                        insertWord.setString(1, word);
                        insertWord.executeUpdate();

                        // Increment start_count for the first word in the line
                        if (i == 0) {
                            updateStart.setString(1, word);
                            updateStart.executeUpdate();
                        }

                        // Increment end_count for the last word in the line
                        if (i == words.length - 1) {
                            updateEnd.setString(1, word);
                            updateEnd.executeUpdate();
                        }

                        // create bigram relationship
                        if (i > 0) {
                            String prevWord = words[i - 1];

                            // Get database IDs for the previous and current words
                            int prevId = getCachedWordId(conn, prevWord, wordCache, getWordId);
                            int currentId = getCachedWordId(conn, word, wordCache, getWordId);

                            // Insert a bigram relationship
                            insertBigram.setInt(1, prevId);
                            insertBigram.setInt(2, currentId);
                            insertBigram.executeUpdate();
                        }
                    }

                    batchCount++;

                    // commit every 500 lines make it fast
                    if (batchCount % 500 == 0) {
                        conn.commit();
                        System.out.println("Processed " + lineCount + " lines...");
                    }
                }

                // Close the file reader
                br.close();

                // commit after each file
                conn.commit();
                System.out.println("Finished: " + fileName);
            }

            // Close the database connection and print final status
            conn.close();
            System.out.println("All files imported successfully.");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // get word id from cache or database
    public static int getCachedWordId(Connection conn, String word,
                                      Map<String, Integer> wordCache,
                                      PreparedStatement getWordId) {
        try {
            // if already in cache, return the ID from memory
            if (wordCache.containsKey(word)) {
                return wordCache.get(word);
            }

            // set the word in the SQL query, run it, get the word id from the result,
            // store it in cache so we don’t query again, and return the id
            // otherwise get it from database
            getWordId.setString(1, word);
            ResultSet rs = getWordId.executeQuery();

            if (rs.next()) {
                int id = rs.getInt("id");
                // save it in cache
                wordCache.put(word, id);
                return id;
            }

        }
        // If something goes wrong print error
        catch (Exception e) {
            e.printStackTrace();
        }

        return -1;
    }
}
