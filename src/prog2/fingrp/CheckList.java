package prog2.fingrp;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import javax.swing.border.EmptyBorder;

public class CheckList extends JFrame {
private String name;
private String program;


 public CheckList(){
     setTitle("My Checklist Management");
     setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
     setSize(500, 275);
     setLayout(new BorderLayout(0,10));
     setLocationRelativeTo(null);
     setResizable(false);

     getNameAndProgram();

     setVisible(true);
 }

    private void getNameAndProgram() {
        JTextField nameField = new JTextField(10);
        JTextField programField = new JTextField(10);

        JPanel panel = new JPanel(new GridLayout(2, 2));
        panel.add(new JLabel("Name:"));
        panel.add(nameField);
        panel.add(new JLabel("Program (BSIT or BSCS):"));
        panel.add(programField);

        while (true) {
            int result = JOptionPane.showConfirmDialog(null, panel, "Enter Name and Program",
                    JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
            if (result == JOptionPane.OK_OPTION) {
                name = nameField.getText();
                program = programField.getText();
                if (program.equalsIgnoreCase("BSIT") || program.equalsIgnoreCase("BSCS")) {
                    initializeComponents();
                    break;
                } else {
                    JOptionPane.showMessageDialog(null, "Please enter either 'BSIT' or 'BSCS'.", "Invalid Program", JOptionPane.ERROR_MESSAGE);
                }
            } else {

                System.exit(0);
            }
        }
    }

    private void initializeComponents() {
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new BorderLayout());
        inputPanel.setBorder(new EmptyBorder(10, 10, 10, 10));


        JLabel optionsLabel = new JLabel("<html><b>Options:</b><br>1. Show subjects for each school term in BSIT<br>" +
                "2. Show subjects with grades for each term<br>" +
                "3. Enter grades for subjects recently finished<br>" +
                "4. Edit a course<br>" +
                "5. Add other courses taken<br>" +
                "6. Display courses with Grade point Average<br>" +
                "7. Display courses in alphabetical order<br>" +
                "8. Display courses with grades in Highest to Lowest<br>" +
                "9. Quit</html>");
        inputPanel.add(optionsLabel, BorderLayout.NORTH);


        JTextField inputField = new JTextField(3);
        JButton enterButton = new JButton("Enter");


        enterButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    int option = Integer.parseInt(inputField.getText());
                    if (option >= 1 && option <= 9) {
                        handleOption(option);
                    } else {
                        JOptionPane.showMessageDialog(CheckList.this, "The number must be from 1 to 9.");
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
    // test only

    private void handleOption(int option) throws IOException, ClassNotFoundException {
        Curriculum test = new Curriculum(name, program);
        switch (option) {
            case 1:
                  test.displayCurriculum();
                break;
            case 2:
                test.displayCurriculumWithGrades();
                break;
            case 3:
                test.editGrade();
                break;
            case 4:

                break;
            case 5:

                break;
            case 6:

                break;
            case 7:

                break;
            case 8:

                break;
            case 9:
                int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to exit?", "Confirm Exit", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    dispose(); // Close the window
                }

                break;
        }
    }


}