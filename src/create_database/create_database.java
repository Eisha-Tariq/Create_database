package create_database;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

interface DatabaseDao {
    void connect(Statement statement) throws SQLException;

    void createResetDatabase(Statement statement) throws SQLException;
}

class DatabaseDaoImpl implements DatabaseDao {
    @Override
    public void connect(Statement statement) throws SQLException {
        // Implement the connection to the database here
    }

    @Override
    public void createResetDatabase(Statement statement) throws SQLException {
        // Drop the database if it exists
        statement.executeUpdate("DROP DATABASE IF EXISTS mydatabase");

        // Create the database
        statement.executeUpdate("CREATE DATABASE mydatabase");

        // Use the database
        statement.executeUpdate("USE mydatabase");

        // Create the table
        String createTableQuery = "CREATE TABLE mytable (id INT PRIMARY KEY, name VARCHAR(50))";
        statement.executeUpdate(createTableQuery);

        // Insert sample data into the table
        String insertDataQuery = "INSERT INTO mytable (id, name) VALUES (1, 'John'), (2, 'Jane')";
        statement.executeUpdate(insertDataQuery);
    }
}

public class create_database {

    public static void main(String[] args) {
        String DATABASE_URL = "jdbc:mysql://localhost:3306/mydatabase";
        String USERNAME = "root";
        String PASSWORD = "1122";
        DatabaseDao databaseDao = new DatabaseDaoImpl();

        // Create a JFrame to hold the components
        JFrame frame = new JFrame("Create Database");

        // Create a button to create/reset the database
        JButton createResetButton = new JButton("Create Database");
        createResetButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Connection connection = null;
                Statement statement = null;
                ResultSet resultSet = null;

                try {
                    try {
                        Class.forName("com.mysql.cj.jdbc.Driver");
                    } catch (ClassNotFoundException ex) {
                        System.err.println("Error loading MySQL driver: " + ex.getMessage());
                        ex.printStackTrace();
                        return;
                    }

                    // Establish a connection to the database
                    connection = DriverManager.getConnection(DATABASE_URL, USERNAME, PASSWORD);

                    // Create a statement object
                    statement = connection.createStatement();

                    // Connect to the database
                    databaseDao.connect(statement);

                    // Create or reset the database
                    databaseDao.createResetDatabase(statement);

                    // Display a message dialog
                    JOptionPane.showMessageDialog(frame, "Database Create!");
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(frame, "Error connecting to the database!");
                } finally {
                    // Close the result set, statement, and connection
                    if (resultSet != null) {
                        try {
                            resultSet.close();
                        } catch (SQLException ex) {
                            ex.printStackTrace();
                        }
                    }
                    if (statement != null) {
                        try {
                            statement.close();
                        } catch (SQLException ex) {
                            ex.printStackTrace();
                        }
                    }
                    if (connection != null) {
                        try {
                            connection.close();
                        } catch (SQLException ex) {
                            ex.printStackTrace();
                        }
                    }
                }
            }
        });

        // Add the button to the JFrame
        frame.getContentPane().add(createResetButton);

        // Set the JFrame size and make it visible
        frame.setSize(300, 200);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}
