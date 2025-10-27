import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Statement; 
import java.sql.ResultSet; 
import javax.swing.table.DefaultTableModel;

/**
 * This class represents a simple login application with a graphical user interface.
 * It allows users to log in and perform various actions in the dashboard.
 */

public class LoginApp {
    private static JFrame frame;
    private static JFrame dashboardFrame;

    /**
     * Main method to start the application.
     *
     * @param args Command-line arguments (not used in this application).
     */

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> createAndShowGUI());
    }

    /**
     * Creates and displays the login GUI.
     */

    private static void createAndShowGUI() {
        frame = new JFrame("Login Screen");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(300, 200);

        JPanel loginPanel = new JPanel(new GridLayout(3, 2));
        frame.add(loginPanel);

        JLabel usernameLabel = new JLabel("Username:");
        JTextField usernameField = new JTextField();
        JLabel passwordLabel = new JLabel("Password:");
        JPasswordField passwordField = new JPasswordField();
        JButton loginButton = new JButton("Login");

        loginPanel.add(usernameLabel);
        loginPanel.add(usernameField);
        loginPanel.add(passwordLabel);
        loginPanel.add(passwordField);
        loginPanel.add(loginButton);

        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                char[] password = passwordField.getPassword();
                String passwordString = new String(password);

                if (validateLogin(username, passwordString)) {
                    // Successfully logged in, open the dashboard
                    createDashboard();
                    frame.setVisible(false);
                } else {
                    JOptionPane.showMessageDialog(frame, "Invalid credentials. Please try again.");
                }
            }
        });

        frame.setVisible(true);
    }

    /**
     * Creates and displays the dashboard GUI upon successful login.
     */

     private static void createDashboard() {
        dashboardFrame = new JFrame("Dashboard");
        dashboardFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        dashboardFrame.setSize(400, 300);
    
        JPanel dashboardPanel = new JPanel(new GridLayout(6, 1));
        dashboardFrame.add(dashboardPanel);
    
        JButton payFeeButton = new JButton("Pay Fee");
        JButton registrationButton = new JButton("New Registration");
        JButton studentDetailsButton = new JButton("Student Details");
        JButton createCourseButton = new JButton("Create Course");
        JButton courseListButton = new JButton("Course List"); 
    
        dashboardPanel.add(payFeeButton);
        dashboardPanel.add(registrationButton);
        dashboardPanel.add(studentDetailsButton);
        dashboardPanel.add(createCourseButton);
        dashboardPanel.add(courseListButton); 
    
        payFeeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                createFeePaymentForm();
            }
        });
    
        registrationButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                createRegistrationForm();
            }
        });
    
        studentDetailsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                displayStudentDetails();
            }
        });
    
        createCourseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                createCourseForm();
            }
        });
    
        courseListButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                displayCourseList();
            }
        });
    
        dashboardFrame.setVisible(true);
    }

    /**
     * Displays the course list.
     */

     private static void displayCourseList() {
        // Fetch course list from the database and display it in a table
        String url = "jdbc:mysql://localhost:3306/sms";
        String dbUsername = "root";
        String dbPassword = "";
    
        try {
            Connection connection = DriverManager.getConnection(url, dbUsername, dbPassword);
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM courses");
    
            // Create a table model to hold the data
            DefaultTableModel tableModel = new DefaultTableModel();
            tableModel.addColumn("Course Name");
    
            // Populate the table model with data from the result set
            while (resultSet.next()) {
                String courseName = resultSet.getString("name");
                tableModel.addRow(new Object[]{courseName});
            }
    
            // Create a JTable with the table model
            JTable courseTable = new JTable(tableModel);
    
            // Display the JTable in a scrollable pane
            JScrollPane scrollPane = new JScrollPane(courseTable);
    
            // Show the JTable in a dialog
            JOptionPane.showMessageDialog(dashboardFrame, scrollPane, "Course List", JOptionPane.INFORMATION_MESSAGE);
    
            resultSet.close();
            statement.close();
            connection.close();
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(dashboardFrame, "Error fetching course list.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }    

    /**
     * Displays the course form.
     */
    
     private static void createCourseForm() {
        JFrame courseFrame = new JFrame("Create Course");
        courseFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        courseFrame.setSize(400, 200);

        JPanel coursePanel = new JPanel(new GridLayout(2, 2));
        courseFrame.add(coursePanel);

        JLabel nameLabel = new JLabel("Course Name:");
        JTextField nameField = new JTextField();
        JButton submitButton = new JButton("Submit");

        coursePanel.add(nameLabel);
        coursePanel.add(nameField);
        coursePanel.add(new JLabel()); // Placeholder for spacing
        coursePanel.add(submitButton);

        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String courseName = nameField.getText();

                if (insertCourseRecord(courseName)) {
                    courseFrame.dispose();
                } else {
                    JOptionPane.showMessageDialog(courseFrame, "Failed to insert course record. Please try again.");
                }
            }
        });

        courseFrame.setVisible(true);
    }   

    /**
     * Inserts the course into table.
     */

     private static boolean insertCourseRecord(String courseName) {
        String url = "jdbc:mysql://localhost:3306/sms";
        String dbUsername = "root";
        String dbPassword = "";
    
        try {
            Connection connection = DriverManager.getConnection(url, dbUsername, dbPassword);
            String query = "INSERT INTO courses (name) VALUES (?)";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, courseName);
    
            int rowsInserted = preparedStatement.executeUpdate();
    
            if (rowsInserted > 0) {
                JOptionPane.showMessageDialog(null, "Course '" + courseName + "' has been successfully inserted!", "Success", JOptionPane.INFORMATION_MESSAGE);
                return true;
            } else {
                JOptionPane.showMessageDialog(null, "Failed to insert course record. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
                return false;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }    

    /**
     * Displays the students list.
     */
    
     private static void displayStudentDetails() {
        // Fetch student list from the database and display it
        String url = "jdbc:mysql://localhost:3306/sms";
        String dbUsername = "root";
        String dbPassword = "";

        try {
            Connection connection = DriverManager.getConnection(url, dbUsername, dbPassword);
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM students");

            // Create a table model to hold the data
            DefaultTableModel tableModel = new DefaultTableModel();

            // Add columns to the model
            tableModel.addColumn("Name");
            tableModel.addColumn("Address");
            tableModel.addColumn("Father's Name");
            tableModel.addColumn("Mother's Name");
            tableModel.addColumn("Gender");
            tableModel.addColumn("Date of Birth");
            tableModel.addColumn("Blood Group");

            // Add rows to the model
            while (resultSet.next()) {
                Object[] rowData = {
                        resultSet.getString("student_name"),
                        resultSet.getString("student_address"),
                        resultSet.getString("student_father_name"),
                        resultSet.getString("student_mother_name"),
                        resultSet.getString("student_gender"),
                        resultSet.getString("student_dob"),
                        resultSet.getString("student_blood_group")
                };
                tableModel.addRow(rowData);
            }

            // Create a table with the model
            JTable studentTable = new JTable(tableModel);

            // Add the table to a scroll pane for scrollability
            JScrollPane scrollPane = new JScrollPane(studentTable);

            // Display the table in a dialog
            JOptionPane.showMessageDialog(dashboardFrame, scrollPane, "Student List", JOptionPane.INFORMATION_MESSAGE);

            resultSet.close();
            statement.close();
            connection.close();
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(dashboardFrame, "Error fetching student list.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Creates and displays the student registration form.
     */

    private static void createRegistrationForm() {
        JFrame registrationFrame = new JFrame("Registration Form");
        registrationFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        registrationFrame.setSize(400, 300);

        JPanel registrationPanel = new JPanel(new GridLayout(9, 2));
        registrationFrame.add(registrationPanel);

        JLabel nameLabel = new JLabel("Name:");
        JTextField nameField = new JTextField();
        JLabel addressLabel = new JLabel("Address:");
        JTextField addressField = new JTextField();
        JLabel fatherNameLabel = new JLabel("Father's Name:");
        JTextField fatherNameField = new JTextField();
        JLabel motherNameLabel = new JLabel("Mother's Name:");
        JTextField motherNameField = new JTextField();
        JLabel genderLabel = new JLabel("Gender:");
        JTextField genderField = new JTextField();
        JLabel dobLabel = new JLabel("Date of Birth:");
        JTextField dobField = new JTextField();
        JLabel bloodGroupLabel = new JLabel("Blood Group:");
        JTextField bloodGroupField = new JTextField();
        JButton submitButton = new JButton("Submit");

        registrationPanel.add(nameLabel);
        registrationPanel.add(nameField);
        registrationPanel.add(addressLabel);
        registrationPanel.add(addressField);
        registrationPanel.add(fatherNameLabel);
        registrationPanel.add(fatherNameField);
        registrationPanel.add(motherNameLabel);
        registrationPanel.add(motherNameField);
        registrationPanel.add(genderLabel);
        registrationPanel.add(genderField);
        registrationPanel.add(dobLabel);
        registrationPanel.add(dobField);
        registrationPanel.add(bloodGroupLabel);
        registrationPanel.add(bloodGroupField);
        registrationPanel.add(new JLabel());
        registrationPanel.add(submitButton);

        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String name = nameField.getText();
                String address = addressField.getText();
                String fatherName = fatherNameField.getText();
                String motherName = motherNameField.getText();
                String gender = genderField.getText();
                String dob = dobField.getText();
                String bloodGroup = bloodGroupField.getText();

                if (insertStudentRecord(name, address, fatherName, motherName, gender, dob, bloodGroup)) {
                    registrationFrame.dispose();
                } else {
                    JOptionPane.showMessageDialog(registrationFrame, "Failed to insert record. Please try again.");
                }
            }
        });

        registrationFrame.setVisible(true);
    }

    /**
     * Creates and displays the fee payment form.
     */

    private static void createFeePaymentForm() {
        JFrame feePaymentFrame = new JFrame("Fee Payment Form");
        feePaymentFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        feePaymentFrame.setSize(400, 200);

        JPanel feePaymentPanel = new JPanel(new GridLayout(4, 2));
        feePaymentFrame.add(feePaymentPanel);

        JLabel admissionNumberLabel = new JLabel("Admission Number:");
        JTextField admissionNumberField = new JTextField();
        JLabel classLabel = new JLabel("Class:");
        JTextField classField = new JTextField();
        JLabel amountLabel = new JLabel("Amount:");
        JTextField amountField = new JTextField();
        JButton submitButton = new JButton("Submit");

        feePaymentPanel.add(admissionNumberLabel);
        feePaymentPanel.add(admissionNumberField);
        feePaymentPanel.add(classLabel);
        feePaymentPanel.add(classField);
        feePaymentPanel.add(amountLabel);
        feePaymentPanel.add(amountField);
        feePaymentPanel.add(new JLabel());
        feePaymentPanel.add(submitButton);

        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String admissionNumber = admissionNumberField.getText();
                String studentClass = classField.getText();
                String amount = amountField.getText();

                if (insertFeeRecord(admissionNumber, studentClass, amount)) {
                    feePaymentFrame.dispose();
                } else {
                    JOptionPane.showMessageDialog(feePaymentFrame, "Failed to insert fee record. Please try again.");
                }
            }
        });

        feePaymentFrame.setVisible(true);
    }

    /**
     * Validates the user login based on the provided username and password.
     *
     * @param username The user's username.
     * @param password The user's password.
     * @return true if the login is successful, false otherwise.
     */

     private static boolean validateLogin(String username, String password) {
        String url = "jdbc:mysql://localhost:3306/sms";
        String dbUsername = "root";
        String dbPassword = "";
    
        try {
            Connection connection = DriverManager.getConnection(url, dbUsername, dbPassword);
            String query = "SELECT * FROM admins WHERE username = ? AND password = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);
    
            ResultSet resultSet = preparedStatement.executeQuery();
            boolean isValid = resultSet.next(); // Check if there is at least one result
    
            resultSet.close();
            preparedStatement.close();
            connection.close();
    
            return isValid;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    /**
     * Inserts a new student record into the database.
     *
     * @param name         The name of the student.
     * @param address      The student's address.
     * @param fatherName   The name of the student's father.
     * @param motherName   The name of the student's mother.
     * @param gender       The gender of the student.
     * @param dob          The date of birth of the student.
     * @param bloodGroup   The blood group of the student.
     * @return true if the record was successfully inserted, false otherwise.
     */

    private static boolean insertStudentRecord(String name, String address, String fatherName, String motherName, String gender, String dob, String bloodGroup) {
        // Replace with your actual database connection details
        String url = "jdbc:mysql://localhost:3306/sms";
        String dbUsername = "root";
        String dbPassword = "";

        try {
            Connection connection = DriverManager.getConnection(url, dbUsername, dbPassword);
            String query = "INSERT INTO students (student_name, student_address, student_father_name, student_mother_name, student_gender, student_dob, student_blood_group) VALUES (?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, address);
            preparedStatement.setString(3, fatherName);
            preparedStatement.setString(4, motherName);
            preparedStatement.setString(5, gender);
            preparedStatement.setString(6, dob);
            preparedStatement.setString(7, bloodGroup);

            return preparedStatement.executeUpdate() > 0;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    /**
     * Inserts a new fee payment record into the database.
     *
     * @param admissionNumber The student's admission number.
     * @param studentClass    The class to which the student belongs.
     * @param amount          The amount of the fee payment.
     * @return true if the fee record was successfully inserted, false otherwise.
     */

    private static boolean insertFeeRecord(String admissionNumber, String studentClass, String amount) {
        // Replace with your actual database connection details
        String url = "jdbc:mysql://localhost:3306/sms";
        String dbUsername = "root";
        String dbPassword = "";

        try {
            Connection connection = DriverManager.getConnection(url, dbUsername, dbPassword);
            String query = "INSERT INTO fees (admission_number, student_class, amount) VALUES (?, ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, admissionNumber);
            preparedStatement.setString(2, studentClass);
            preparedStatement.setString(3, amount);

            return preparedStatement.executeUpdate() > 0;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }
}
