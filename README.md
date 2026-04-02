# Sentence-Builder
Senior design project for autocomplete and sentence generation system
# Sentence Builder (Bigram Model)

This project is a simple sentence generator built using Java and MySQL.  
It uses a Bigram model, which means each word predicts the next word based on stored word relationships.

## What the project does

- Stores words in a MySQL database
- Stores relationships between words (which word comes next)
- Connects Java to MySQL using JDBC
- Generates sentences step by step using the Bigram model

Example output:
the project gutenberg electronic works in the project gutenberg

## Technologies used

- Java
- MySQL
- JDBC (MySQL connector jar)
- IntelliJ IDEA

## How it works

1. Words are stored in the `word` table  
2. Word relationships are stored in the `word_follows` table  
3. Java connects to MySQL using JDBC  
4. The program:
   - starts with a word
   - finds the next word from the database
   - keeps repeating to build a sentence  

## Project structure

- `DBConnection.java` → connects Java to MySQL  
- `TestConnection.java` → tests database connection  
- `ViewWords.java` → displays words from database  
- `NextWordTest.java` → finds next word using Bigram  
- `GenerateSentence.java` → generates full sentence  

## How to run

1. Make sure MySQL is running  
2. Create the database using the provided SQL script  
3. Add MySQL connector jar to IntelliJ  
4. Run `TestConnection.java` to test connection  
5. Run `GenerateSentence.java` to generate sentences  

## Notes

- Sentences may repeat or not always make perfect sense this is normal for Bigram model
- The model depends on the input text data

## Author

Jeffin Plakuzhiyil Varghese
