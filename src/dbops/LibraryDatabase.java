package dbops;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

// Main database class
public class LibraryDatabase {
    private Connection connection;

    public Connection getConnection() {
        return connection;
    }

    // Constructor initializes the database and creates the Book table
    public LibraryDatabase() {
        startDatabase();
        createBookTable();
    }

    // Starts the database connection
    private void startDatabase() {
        try {
            Class.forName("org.h2.Driver"); // Load the H2 database driver
            connection = DriverManager.getConnection("jdbc:h2:~/test", "sa", ""); // Establish the connection
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    // Creates the Book table if it doesn't exist
    private void createBookTable() {
        String createTableSQL = """
            CREATE TABLE IF NOT EXISTS Book (
                title VARCHAR(255) NOT NULL,
                description TEXT,
                author VARCHAR(255),
                releaseyear SMALLINT,
                PRIMARY KEY (title)
            )
        """;

        executeQuery(createTableSQL);
    }

    // Adds a new book to the Book table
    public void addBook(String title, String description, String author, int releaseYear) {
        String insertSQL = "INSERT INTO Book (title, description, author, releaseyear) VALUES (?, ?, ?, ?)";
        try (var preparedStatement = connection.prepareStatement(insertSQL)) {
            preparedStatement.setString(1, title);
            preparedStatement.setString(2, description);
            preparedStatement.setString(3, author);
            preparedStatement.setInt(4, releaseYear);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Removes a book from the Book table by title
    public void removeBook(String title) {
        String deleteSQL = "DELETE FROM Book WHERE title = ?";
        try (var preparedStatement = connection.prepareStatement(deleteSQL)) {
            preparedStatement.setString(1, title);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Edits the details of an existing book
    public void editBook(String title, String newDescription, String newAuthor, int newReleaseYear) {
        String updateSQL = "UPDATE Book SET description = ?, author = ?, releaseyear = ? WHERE title = ?";
        try (var preparedStatement = connection.prepareStatement(updateSQL)) {
            preparedStatement.setString(1, newDescription);
            preparedStatement.setString(2, newAuthor);
            preparedStatement.setInt(3, newReleaseYear);
            preparedStatement.setString(4, title);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    // Executes a given SQL query
    public void executeQuery(String sql) {
        try (var statement = connection.createStatement()) {
            statement.execute(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
