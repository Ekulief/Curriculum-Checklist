package prog2.fingrp;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import javax.swing.border.EmptyBorder;

import java.io.*;
import java.util.Scanner;

public class CheckList extends JFrame {
    /**
     * <p>Represents a checklist application for managing a curriculum. Allows users to log in, sign up, view curriculum,
     * enter grades, edit courses, add courses, display GPA, sort courses, and logout. Utilizes a file-based account
     * system and interacts with a Curriculum class to perform curriculum-related tasks.</p>
     */
    private String idNumber;
    private String program;

    private static String currentDirectory = System.getProperty("user.dir");
    private static final String ACCOUNTS_FILE = currentDirectory + "\\accounts.txt";


    /**
     * Initializes a window for managing the user's curriculum.
     * Sets the window title, size, layout, and appearance.
     * Prompts for user authentication on startup.
     */

    public CheckList(){
        setTitle("My SLU Checklist Management");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 375);
        setLayout(new BorderLayout(0,10));
        setLocationRelativeTo(null);
        setResizable(false);
        ImageIcon icon = new ImageIcon("LogoIcon.png");
        Image image = icon.getImage();
        setIconImage(image);
        Login();


        setVisible(true);
    }

    /**
     * Displays a login panel with fields for ID number and password, along with login and sign-up buttons.
     * Validates the entered ID number and password. If the ID number is valid but the account does not exist,
     * displays an error message. If the ID number and password are correct, initializes the main components
     * and hides the login panel.
     */
    private void Login() {
        // Create a JLabel with an ImageIcon
        JPanel loginPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        Font calibriFont = new Font("Calibri", Font.PLAIN, 14);
        JLabel welcome = new JLabel("Welcome to My SLU BSIT/BSCS Checklist Manager!");
        welcome.setFont(calibriFont);
        JLabel idLabel = new JLabel("ID Number:");
        idLabel.setFont(calibriFont);
        JTextField idField = new JTextField(12);
        idField.setFont(calibriFont);
        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setFont(calibriFont);
        JPasswordField passwordField = new JPasswordField(12);
        passwordField.setFont(calibriFont);
        JButton loginButton = new JButton("Login");
        loginButton.setFont(calibriFont);
        loginButton.setBackground(Color.white);
        JButton signUpButton = new JButton("Sign Up");
        signUpButton.setFont(calibriFont);
        signUpButton.setBackground(Color.blue);
        signUpButton.setForeground(Color.white);

        // Create a JLabel with the image and adjust its size
        JLabel imageLabel = new JLabel();
        ImageIcon imageIcon = new ImageIcon("Logo.png");
        Image image = imageIcon.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH); // Adjust size as needed
        imageIcon = new ImageIcon(image);
        imageLabel.setIcon(imageIcon);

        // Center the image label
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 0;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(5, 1, 5, 1); // Add some bottom margin
        loginPanel.add(imageLabel, gbc);
        gbc.gridy++;
        loginPanel.add(welcome, gbc);

        // Reset gridwidth for other components
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.gridy++;
        loginPanel.add(idLabel, gbc);
        gbc.gridx++;
        loginPanel.add(idField, gbc);
        gbc.gridx = 0;
        gbc.gridy++;
        loginPanel.add(passwordLabel, gbc);
        gbc.gridx++;
        loginPanel.add(passwordField, gbc);
        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        loginPanel.add(loginButton, gbc);
        gbc.gridy++;
        loginPanel.add(signUpButton, gbc);

        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                idNumber = idField.getText();
                String password = new String(passwordField.getPassword());

                if (validateIdNumber(idNumber)) {
                    if (!isAccountExists(idNumber)) {
                        JOptionPane.showMessageDialog(CheckList.this, "Account does not exist", "Login Error", JOptionPane.ERROR_MESSAGE);
                    }else if (!isAccountValid(idNumber, password)) {
                        JOptionPane.showInputDialog(CheckList.this, "Incorrect password.");
                    }
                    else {
                        dispose();
                        initializeComponents();
                        loginPanel.setVisible(false);
                    }
                } else {
                    JOptionPane.showMessageDialog(CheckList.this, "Invalid ID number", "Login Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        signUpButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                SignUpDialog();

            }
        });
        // Set the minimum size of the enclosing JFrame
        setMinimumSize(new Dimension(300, 150)); // Adjust as needed

        add(loginPanel, BorderLayout.CENTER);
    }
    /**
     * Displays a sign-up dialog window with fields for ID number, password, and program.
     * Allows the user to create a new account with a unique ID number and specified program (BSIT or BSCS).
     * Validates the entered ID number, ensures it does not already exist, and verifies the program input.
     * If successful, writes the account information to a file and displays a success message, then disposes
     * of the sign-up frame and initializes a new CheckList instance.
     */
    public void SignUpDialog() {
        JFrame signUpFrame = new JFrame("Sign Up");

        signUpFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        signUpFrame.setSize(350, 200);
        signUpFrame.setLocationRelativeTo(this);
        signUpFrame.setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();

        Font calibriFont = new Font("Calibri", Font.PLAIN, 14);

        JLabel idLabel = new JLabel("ID Number:");
        idLabel.setFont(calibriFont);
        JTextField idField = new JTextField(12);
        idField.setFont(calibriFont);

        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setFont(calibriFont);
        JPasswordField passwordField = new JPasswordField(12);
        passwordField.setFont(calibriFont);

        JLabel programLabel = new JLabel("Program(BSIT or BSCS):");
        programLabel.setFont(calibriFont);
        JTextField programField = new JTextField(12);
        programField.setFont(calibriFont);

        JButton createAccountButton = new JButton("Create Account");
        createAccountButton.setFont(calibriFont);
        createAccountButton.setBackground(Color.white);

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.insets = new Insets(5, 5, 5, 5);
        signUpFrame.add(idLabel, gbc);
        gbc.gridx = 1;
        signUpFrame.add(idField, gbc);
        gbc.gridx = 0;
        gbc.gridy = 1;
        signUpFrame.add(passwordLabel, gbc);
        gbc.gridx = 1;
        signUpFrame.add(passwordField, gbc);
        gbc.gridx = 0;
        gbc.gridy = 2;
        signUpFrame.add(programLabel, gbc);
        gbc.gridx = 1;
        signUpFrame.add(programField, gbc);
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        signUpFrame.add(createAccountButton, gbc);

        createAccountButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String id = idField.getText();
                String password = new String(passwordField.getPassword());
                String program = programField.getText();

                if (validateIdNumber(id) && !isAccountExists(id)) {
                    if (program.equalsIgnoreCase("bsit") || program.equalsIgnoreCase("bscs")) {
                        try (FileWriter writer = new FileWriter(ACCOUNTS_FILE, true)) {
                            writer.write(id + ":" + password + ":" + program + "\n");

                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }
                        JOptionPane.showMessageDialog(null, "Account created successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
                        signUpFrame.dispose();
                        new CheckList();
                    } else {
                        JOptionPane.showMessageDialog(null, "Invalid program. Please enter 'BSCS' or 'BSIT'", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Invalid ID number or account already exists", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        signUpFrame.setVisible(true);
    }

    /**
     * Validates the format of an ID number.
     *
     * @param id The ID number to validate.
     * @return true if the ID number is exactly 7 digits long and contains only digits; otherwise, false.
     */
    private boolean validateIdNumber(String id) {
        if (id.length() != 7 || !id.matches("\\d+")) {
            return false;
        }
        return true;
    }
    /**
     * Checks if the provided ID and password combination is valid by searching for a matching account in the accounts file.
     *
     * @param id The ID number of the account to check.
     * @param password The password to check against the account.
     * @return true if the provided ID and password match an existing account; otherwise, false.
     */
    private boolean isAccountValid(String id, String password) {
        try (Scanner scanner = new Scanner(new File(ACCOUNTS_FILE))) {
            while (scanner.hasNextLine()) {
                String account = scanner.nextLine();
                if (account.startsWith(id + ":")) {
                    String[] parts = account.split(":");
                    if (parts[1].equals(password)) {
                        return true;
                    } else {
                        // Incorrect password entered, ask for password again
                        String newPassword = JOptionPane.showInputDialog(CheckList.this, "Incorrect password. Please enter the correct password:");
                        if (newPassword != null && newPassword.equals(parts[1])) {
                            dispose();
                            initializeComponents();
                            return true;
                        }
                    }
                }
            }
        } catch (FileNotFoundException ex) {
            createAccountsFile();
        }
        return false;
    }
    /**
     * Checks if an account with the provided ID exists in the accounts file.
     *
     * @param id The ID number of the account to check.
     * @return true if an account with the provided ID exists in the accounts file; otherwise, false.
     */

    private boolean isAccountExists(String id) {
        try (Scanner scanner = new Scanner(new File(ACCOUNTS_FILE))) {
            while (scanner.hasNextLine()) {
                String account = scanner.nextLine();
                if (account.startsWith(id + ":")) {
                    program = getProgramFromAccount(account);
                    return true;
                }
            }
        } catch (FileNotFoundException ex) {
            createAccountsFile();
        }
        return false;
    }

    /**
     * Creates the accounts file if it does not exist.
     */
    private void createAccountsFile() {
        try {
            File file = new File(ACCOUNTS_FILE);
            if (file.createNewFile()) {
                System.out.println("File created: " + file.getAbsolutePath());
            } else {
                System.out.println("File already exists.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Extracts the program information from an account line in the format "ID:Password:Program".
     *
     * @param accountLine The line representing an account in the accounts file.
     * @return The program associated with the account.
     */
    private String getProgramFromAccount(String accountLine) {
        String[] parts = accountLine.split(":");
        return parts[2];
    }


    /**
     * Initializes the main components of the application, including an input panel with options for the user.
     * Allows the user to input a number corresponding to various actions, such as displaying subjects, entering grades,
     * editing courses, and logging out.
     */
    private void initializeComponents() {
        Font calibriFont = new Font("Calibri", Font.PLAIN, 15);
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new BorderLayout());
        inputPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        JLabel optionsLabel = new JLabel("<html><b>Options:</b><br>1. Show subjects for each school term <br>" +
                "2. Show subjects with grades for each term<br>" +
                "3. Enter grades for subjects recently finished<br>" +
                "4. Edit a course<br>" +
                "5. Add other courses taken<br>" +
                "6. Display courses with Grade point Average<br>" +
                "7. Display courses in alphabetical order<br>" +
                "8. Display courses with grades in Highest to Lowest<br>" +
                "9. Take an Elective course<br>" +
                "10. Shift Course<br>" +
                "11. Logout <br> "+
                "12. Quit</html>"
        );
        optionsLabel.setFont(calibriFont);
        inputPanel.add(optionsLabel, BorderLayout.NORTH);


        JTextField inputField = new JTextField(3);
        JButton enterButton = new JButton("Enter");
        enterButton.setFont(calibriFont);
        enterButton.setBackground(Color.white);

        enterButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    int option = Integer.parseInt(inputField.getText());
                    if (option >= 1 && option <= 12) {
                        handleOption(option);
                    } else {
                        JOptionPane.showMessageDialog(CheckList.this, "The number must be from 1 to 12.");
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(CheckList.this, "You entered an invalid integer. Please enter an integer.");
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                } catch (ClassNotFoundException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });

        inputPanel.add(inputField, BorderLayout.CENTER);
        inputPanel.add(enterButton, BorderLayout.SOUTH);
        setVisible(true);
        add(inputPanel, BorderLayout.CENTER);
    }

    /**
     * Handles the selected option by invoking corresponding methods from the Curriculum class.
     *
     * @param option The selected option to handle.
     * @throws IOException If an I/O error occurs.
     * @throws ClassNotFoundException If the class of a serialized object cannot be found.
     */
    private void handleOption(int option) throws IOException, ClassNotFoundException {

        Curriculum run = new Curriculum(idNumber, program);
        switch (option) {
            case 1:
                run.displayCurriculum();
                break;
            case 2:
                run.displayCurriculumWithGrades();
                break;
            case 3:
                run.editGrade();
                break;
            case 4:
                run.editCourse();
                break;
            case 5:
                run.addCourseTaken();
                break;
            case 6:
                run.displayCurriculumWithGPA();
                break;
            case 7:
                run.displayGradesAlpabetically();
                break;
            case 8:run.displayGradesDescending();
                break;
            case 9:
                   run.takeElective();
                break;
            case 10:
                run.shiftPrograms();
                break;
            case 11:
                int confirm2 = JOptionPane.showConfirmDialog(this, "Are you sure you want to return to the login screen?", "Confirm Exit", JOptionPane.YES_NO_OPTION);
                if (confirm2 == JOptionPane.YES_OPTION) {
                    getContentPane().removeAll();
                    Login();
                    revalidate();
                    repaint();
                }
                break;
            case 12:
                int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to exit?", "Confirm Exit", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    dispose();
                }

                break;
        }
    }


}