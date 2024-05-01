package prog2.fingrp;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;


public class Curriculum implements Serializable{
    /**
     * <p>The Curriculum class represents a curriculum for a specific educational program.
     * It contains information about the name of the curriculum, the associated program,
     * a list of courses included in the curriculum, and a list of elective courses.
     * The class provides methods for initializing, accessing, and modifying curriculum data,
     * as well as for interacting with course information such as adding, editing, and displaying courses.</p>
     *
     * <p>Curriculum objects can be constructed either with an empty state or based on existing data
     * stored in serialized files. Serialized files are used to preserve curriculum data
     * between program sessions. If no serialized file is found for a specific curriculum,
     * a new file is created and initialized with default values.</p>
     *
     * <p>The curriculum data can be accessed and modified through getter and setter methods.
     * Additionally, the class provides methods for obtaining subjects for a specific semester,
     * displaying the curriculum with or without grades, adding, editing, and removing courses,
     * and managing elective courses.</p>
     *
     * <p>This class ensures the integrity and consistency of curriculum data,
     * including error handling for file operations and data manipulation.</p>
     *
     * @see Course
     * @see Elective
     * @see java.io.Serializable
     */
    private String IdNumber;
    private String program;
    protected ArrayList<Course> courses;

    protected ArrayList<Elective> electives;
    private transient Scanner scan;
    private final String FILE_NAME;
    private final String currentDirectory = System.getProperty("user.dir");
    private final String DATA_FILE_LOCATION = currentDirectory +"\\Data\\";
    private final String COURSE_CURRICULUM_REFERENCE_LOCATION = currentDirectory + "\\Courses\\";



    /**
     * Constructs a Curriculum object with the specified name and program.
     * Initializes the name, program, FILE_NAME, courses, and electives based on the provided parameters.
     * If a serialized file exists for the curriculum, it initializes the curriculum from the serialized file.
     * Otherwise, it initializes a new curriculum.
     *
     * @param IdNumber   the name of the curriculum.
     * @param program the program associated with the curriculum.
     * @throws IOException            if an I/O exception occurs.
     * @throws ClassNotFoundException if the class of a serialized object cannot be found.
     */
    public Curriculum(String IdNumber, String program) throws IOException, ClassNotFoundException {
        this.IdNumber = IdNumber;
        this.program = program;
        FILE_NAME =(this.IdNumber.toLowerCase() + this.program.toLowerCase()).replaceAll(" ", "")+".ser";
        courses = new ArrayList<>();
        electives = new ArrayList<>();
        if(fileExists(FILE_NAME)) {
            initializeSerializedFile();
        }else{
            initializeNewCurrculum();
        }
    }

    /**
     * Sets the idNumber of the curriculum.
     *
     * @param idNumber the name to set for the curriculum.
     */
    public void setIdNumber(String idNumber){
        this.IdNumber = idNumber;
    }
    /**
     * Sets the program associated with the curriculum.
     *
     * @param program the program to set for the curriculum.
     */
    public void setProgram(String program){
        this.program = program;
    }
    /**
     * Sets the list of courses for the curriculum.
     *
     * @param courses the list of courses to set for the curriculum.
     */

    public void setCourses(ArrayList<Course> courses) {
        this.courses = courses;
    }

    /**
     * Retrieves the idNumber of the curriculum.
     *
     * @return the idNumber of the curriculum.
     */
    public String getIdNumber(){
        return IdNumber;
    }
    /**
     * Retrieves the program associated with the curriculum.
     *
     * @return the program associated with the curriculum.
     */
    public String getProgram(){
        return program;
    }

    /**
     * Retrieves the list of courses included in the curriculum.
     *
     * @return the list of courses included in the curriculum.
     */
    public ArrayList<Course> getCourses() {
        return courses;
    }
    /**
     * Initializes the curriculum from a serialized file.
     * It deserializes the curriculum object from the specified file,
     * then assigns its attributes to the current instance.
     *
     * @throws IOException            if an I/O error occurs while reading the file.
     * @throws ClassNotFoundException if the class of a serialized object cannot be found.
     */

    private void initializeSerializedFile() throws IOException, ClassNotFoundException {
        Curriculum c = deserialize(FILE_NAME);
        this.IdNumber = c.IdNumber;
        this.program = c.program;
        this.courses = c.courses;
        this.electives = c.electives;
    }
    /**
     * Initializes a new curriculum for the specified program.
     * If the program file is not available, it throws an IllegalArgumentException.
     * Otherwise, it creates a new file for the curriculum and populates it with initial data.
     *
     * @throws IllegalArgumentException if the program file is not available.
     */
    private void initializeNewCurrculum() {
        if(!checkIfProgramisAvailable(program)){
            throw new IllegalArgumentException("program is not available");
        }
        makeNewFile();
    }
    /**
     * Checks if a program file is available.
     *
     * @param filename The name of the program file to check for availability.
     * @return true if the program file is available, false otherwise.
     */

    private boolean checkIfProgramisAvailable(String filename){
        File file = new File(COURSE_CURRICULUM_REFERENCE_LOCATION + filename + ".txt");
        return file.exists();
    }
    /**
     * Checks if a file exists.
     *
     * @param filename The name of the file to check for existence.
     * @return true if the file exists, false otherwise.
     */
    private boolean fileExists(String filename){
        File file = new File(DATA_FILE_LOCATION + filename);
        return file.exists();
    }

    /**
     * Deserializes a Curriculum object from a file.
     *
     * @param filename The name of the file to deserialize the Curriculum object from.
     * @return The deserialized Curriculum object.
     * @throws IOException            If an I/O error occurs while reading from the file.
     * @throws ClassNotFoundException If the class of a serialized object cannot be found.
     */
    private Curriculum deserialize(String filename) throws IOException, ClassNotFoundException{
        try (FileInputStream inputStream = new FileInputStream(DATA_FILE_LOCATION + filename);
            ObjectInputStream objectInputStream = new ObjectInputStream(inputStream)) {
                return (Curriculum) objectInputStream.readObject();
        }

    }


    /**
     * Creates a new data file by reading information from a course curriculum reference file and populating it with default values.
     * The method reads the course curriculum reference file line by line, appending "0,false" to each line to represent default grade and
     * not currently being taken. It then writes the modified lines to the new data file.
     * After creating the new data file, it populates the ArrayList of courses and the list of electives by calling respective methods.
     * If any NumberFormatException or IOException occurs during the process, a RuntimeException is thrown.
     */
    private void makeNewFile() {
        FileWriter writer;
        try {
            scan = new Scanner(new File(COURSE_CURRICULUM_REFERENCE_LOCATION + program.toLowerCase() + ".txt"));
            writer = new FileWriter(DATA_FILE_LOCATION + FILE_NAME);
            while (scan.hasNextLine()) {
                writer.write(scan.nextLine()+",0"+",false"+"\n");
            }
            scan.close();
            writer.close();
            populateArrayList();
            populateElectiveList();


        } catch (NumberFormatException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Populates the ArrayList of courses by reading data from a file.
     * Each line of the file contains information about a course, including its year, semester, course number, course name,
     * grade, units, and whether it's currently being taken.
     * The method reads the file line by line, parses each line to extract the information, and creates Course objects accordingly,
     * which are then added to the ArrayList of courses.
     * After populating the ArrayList, it saves the data to a file and closes the scanner.
     * If any NumberFormatException or IOException occurs during the process, a RuntimeException is thrown.
     */
    private void populateArrayList() {
        try {

            scan = new Scanner(new File(DATA_FILE_LOCATION + FILE_NAME));
            while (scan.hasNextLine()) {
                String line = scan.nextLine();
                String[] temp = line.split(",");

                courses.add(new Course(Byte.parseByte(temp[0]), Byte.parseByte(temp[1]), temp[2], temp[3],
                        Float.parseFloat(temp[4]), Float.parseFloat(temp[5]), Boolean.parseBoolean(temp[6])));
            }
            saveFile();
            scan.close();

        } catch (NumberFormatException | IOException e) {
            throw new RuntimeException(e);
        }
    }
    /**
     * Populates the list of elective courses by reading data from a file.
     * Each line of the file contains information about an elective course, including its code, type, course number, course name,
     * units, term, whether it's mandatory, and whether it can be taken or not.
     * The method reads the file line by line, parses each line to extract the information, and creates Elective objects accordingly,
     * which are then added to the list of electives.
     * If the file is not found, a RuntimeException is thrown.
     */
        private void populateElectiveList(){
            try {
                scan = new Scanner(new File(COURSE_CURRICULUM_REFERENCE_LOCATION + program +"electives.txt"));
                while(scan.hasNextLine()){
                    String line = scan.nextLine();
                    String[] temp = line.split(",");
                    electives.add(new Elective(temp[0], temp[1],
                            Byte.parseByte(temp[2]), Byte.parseByte(temp[3]), Boolean.parseBoolean(temp[4]), Boolean.parseBoolean(temp[5])));
                }
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
        }



    /**
     * Displays the curriculum in a GUI.
     * Allows navigating through semesters and viewing course details including course number, course name, and units.
     * Provides options to proceed to the next semester or close the display.
     * The GUI includes information such as student name, program, year, semester, course number, course name, and units.
     */

    public void displayCurriculum() { // option 1
        StringBuilder semesterInfo = new StringBuilder();
        JTextArea textArea = new JTextArea(30, 120);

        textArea.setEditable(false);
        textArea.setFont(new Font("Monospaced", Font.PLAIN, 12));

        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        byte sem = 0;
        Object[] options;
        semesterInfo.append("ID Number:").append(IdNumber.toUpperCase());
        semesterInfo.append(" Program:").append(program.toUpperCase());

        for (Course course : courses) {


            if (sem != course.getSemester()) {
                textArea.setText(semesterInfo.toString());

                if(hasNextSemester(course)) {
                    options = new Object[]{"Next"};
                }else {
                    options = new Object[]{"Close"};
                }
                int choice = JOptionPane.showOptionDialog(null, scrollPane, "BSIT Subjects", JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, options[0]);

                if (choice == 0) {
                    sem = course.getSemester();
                } else {
                    break;
                }
                semesterInfo.append("\n");
                semesterInfo.append(String.format("%-5s%-14d%s%-14s%n", " Year: ", course.getYear(), "Semester: ", course.getTerm()));
                semesterInfo.append(String.format("%-20s%-80s%-10s%n", "Course Number", "Course", "Units"));
                semesterInfo.append(String.format("%-20s%-80s%-10s%n", "---------------", "---------------------------------------------------", "-------"));

            }
            semesterInfo.append(String.format("%-20s%-80s%-10s%n", course.getCourseNo(), course.getCourseName(), course.getUnit()));


        }
    }
    /**
     * Displays the curriculum with grades in a GUI.
     * Allows navigating through semesters and viewing course details including grades.
     * Provides options to proceed to the next semester or close the display.
     * The GUI includes information such as student name, program, year, semester, course number, course name, units, grades, and remarks.
     */
    public void displayCurriculumWithGrades () { // option 2
        StringBuilder semesterInfo2 = new StringBuilder();
        JTextArea textArea2 = new JTextArea(30, 180);

        textArea2.setEditable(false);
        textArea2.setFont(new Font("Monospaced", Font.PLAIN, 12));

        JScrollPane scrollPane2 = new JScrollPane(textArea2);
        scrollPane2.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        byte sem2 = 0;
        Object[] options;

        semesterInfo2.append("ID Number:").append(IdNumber.toUpperCase());
        semesterInfo2.append(" Program:").append(program.toUpperCase());

        for (Course course : courses) {
            if (sem2 != course.getSemester()) {
                textArea2.setText(semesterInfo2.toString());
                if(hasNextSemester(course)) {
                    options = new Object[]{"Next"};
                }else {
                    options = new Object[]{"Close"};
                }

                int choice = JOptionPane.showOptionDialog(null, scrollPane2, "BSIT Subjects", JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, options[0]);

                if (choice == 0) {
                    sem2 = course.getSemester();
                } else {
                    break;
                }
                semesterInfo2.append("\n");
                semesterInfo2.append(String.format("%-5s%-14d%s%-14s%n", " Year: ", course.getYear(), "Semester: ", course.getTerm()));
                semesterInfo2.append(String.format("%-5s%-20s%-85s%-15s%-15s%-20s%5s%n","","Course Number", "Course", "Units", "Grade","Remarks",""));
                semesterInfo2.append(String.format("%-5s%-20s%-85s%-15s%-15s%-20s%5s%n", "","---------------","--------------------------------" +
                        "-------------------------------------------","-------", "-------","-------------", ""));
            }
            if(course.getGrade() == 0){
                semesterInfo2.append(String.format("%-5s%-20s%-85s%-15s%-15s%-20s%5s%n","", course.getCourseNo(), course.getCourseName(), course.getUnit(),"", course.getRemarks(),""));
            }else if(course.getGrade() < 65 && course.getGrade() > 0){
                semesterInfo2.append(String.format("%-5s%-20s%-85s%-15s%-15s%-20s%5s%n","", course.getCourseNo(), course.getCourseName(), course.getUnit(),"65.0", course.getRemarks(),""));
            }else {
                semesterInfo2.append(String.format("%-5s%-20s%-85s%-15s%-15s%-20s%5s%n", "", course.getCourseNo(), course.getCourseName(), course.getUnit(), course.getGrade(), course.getRemarks(), ""));
            }


        }
    }

    /**
     * Checks if there is a next semester for the given course.
     * If the year of the course is not set, it assumes there's no next semester.
     *
     * @param currentCourse The course for which the next semester is being checked.
     * @return True if there is a next semester, false otherwise.
     */
    private boolean hasNextSemester(Course currentCourse) {
        if (currentCourse.getYear() == 0) {
            return false;
        }
        return true;
    }


    /**
     * Displays a GUI for editing grades.
     * Provides options to select a course, enter a new grade, and save the changes.
     * Allows selecting a year to display courses taken in that year.
     * Includes a button to show the updated curriculum with grades.
     */

    public void editGrade() {
        JFrame frame = new JFrame("Edit Grade");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(600, 400); // Increase frame size to accommodate additional components
        frame.setLayout(new BorderLayout());
        frame.setLocationRelativeTo(null);
        ImageIcon icon = new ImageIcon("LogoIcon.png");
        Image image = icon.getImage();
        frame.setIconImage(image);

        // Panel for the edit section (course number, grade fields, and edit button)
        JPanel editPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(5, 5, 5, 5);


        Font calibriFont = new Font("Calibri", Font.PLAIN, 14);
        JLabel courseLabel = new JLabel("Enter Course Number:");
        courseLabel.setFont(calibriFont);
        JTextField courseField = new JTextField(15);
        JLabel gradeLabel = new JLabel("Enter Grade:");
        gradeLabel.setFont(calibriFont);
        JTextField gradeField = new JTextField(15);
        JButton editButton = new JButton("Enter");
        editButton.setFont(calibriFont);
        editButton.setBackground(Color.white);

        editButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    String courseNumber = courseField.getText();
                    int grade = Integer.parseInt(gradeField.getText());
                    editGradeInFile(courseNumber, grade);
                } catch (Exception exception) {
                    JOptionPane.showMessageDialog(null, "Invalid input.");
                }
            }
        });

        gbc.anchor = GridBagConstraints.EAST;
        editPanel.add(courseLabel, gbc);
        gbc.gridx++;
        editPanel.add(courseField, gbc);
        gbc.gridx = 0;
        gbc.gridy++;
        editPanel.add(gradeLabel, gbc);
        gbc.gridx++;
        editPanel.add(gradeField, gbc);
        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        editPanel.add(editButton, gbc);

        // Panel for the year dropdown
        JPanel yearDropdownPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel yearLabel = new JLabel("Select Year:");
        yearLabel.setFont(calibriFont);
        Integer[] years = getAvailableYearsArray(); // Get available years as an array
        JComboBox<Integer> yearDropdown = new JComboBox<>(years);
        yearDropdownPanel.add(yearLabel);
        yearDropdownPanel.add(yearDropdown);

        // Panel for the course list
        JPanel courseListPanel = new JPanel(new BorderLayout());
        JPanel coursePanel = new JPanel(new GridBagLayout());
        JScrollPane scrollPane = new JScrollPane(coursePanel);
        courseListPanel.add(scrollPane, BorderLayout.CENTER);

        yearDropdown.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedYear = (int) yearDropdown.getSelectedItem();
                displayCoursesForYear(selectedYear, coursePanel); // Display courses for the selected year
            }
        });

        // Panel for the show grades button
        JPanel showGradesPanel = new JPanel();
        JButton showGradesButton = new JButton("Show Updated Grades");
        showGradesButton.setBackground(Color.white);
        showGradesButton.setFont(calibriFont);

        showGradesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Implement logic to show updated grades
                displayCurriculumWithGrades();
            }
        });

        showGradesPanel.add(showGradesButton);

        // Add components to the frame
        frame.add(editPanel, BorderLayout.NORTH);
        frame.add(yearDropdownPanel, BorderLayout.WEST);
        frame.add(courseListPanel, BorderLayout.CENTER);
        frame.add(showGradesPanel, BorderLayout.SOUTH);

        frame.setVisible(true);
    }

    /**
     * Retrieves an array containing the available years in the list of courses.
     * Sorts the years in ascending order before returning the array.
     *
     * @return An array containing the available years in ascending order.
     */
    private Integer[] getAvailableYearsArray() {
        Set<Integer> yearsSet = new HashSet<>();
        for (Course course : courses) {
            yearsSet.add((int) course.getYear());
        }
        // Convert set to sorted array
        yearsSet.remove(0);
        Integer[] yearsArray = yearsSet.toArray(new Integer[0]);
        Arrays.sort(yearsArray);
        return yearsArray;
    }
    /**
     * Displays courses for a selected year on the provided JPanel.
     * Removes all existing components from the panel before displaying new ones.
     *
     * @param selectedYear The selected year for which courses are to be displayed.
     * @param coursePanel  The JPanel where the courses will be displayed.
     */
    private void displayCoursesForYear(int selectedYear, JPanel coursePanel) {
        coursePanel.removeAll(); // Clear the panel
        Font calibriFont = new Font("Calibri", Font.PLAIN, 14);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(5, 5, 5, 5);

        for (Course course : courses) {
            if (course.getYear() == selectedYear) {
                JLabel courseCodeLabel = new JLabel(course.getCourseNo());
                courseCodeLabel.setFont(calibriFont);
                coursePanel.add(courseCodeLabel, gbc);
                gbc.gridy++;
            }
        }

        coursePanel.revalidate();
        coursePanel.repaint();
    }


    /**
     * Edits the grade of a course in the file.
     * Checks if the provided grade is within a valid range (0 to 99).
     * If the course already has a grade, prompts the user to confirm overwriting it.
     * Updates the grade of the course and saves the changes to the file.
     * Displays error messages for invalid input or when prerequisites are not met.
     *
     * @param courseNo The course number of the course to be edited.
     * @param newGrade The new grade for the course.
     */
    public void editGradeInFile(String courseNo, float newGrade) {
        int i = search(courseNo);
        if (i != -1) {
            Course course = courses.get(i);
            if (newGrade < 0 || newGrade > 99) {
                JOptionPane.showMessageDialog(null, "Invalid grade input. Grade must be between 0 and 99.");
                return;
            }
            if (course.getGrade() != 0) {
                int choice = JOptionPane.showConfirmDialog(null, "A grade already exists for this course. Do you want to overwrite it?", "Grade Overwrite Confirmation", JOptionPane.YES_NO_OPTION);
                if (choice == JOptionPane.NO_OPTION) {
                    return; // Do nothing if the user chooses not to overwrite the grade
                } else {
                    course.setGrade(newGrade);
                    course.setTaking(false);
                    courses.set(i, course);
                    saveFile();
                }
            }
            // Check if prerequisites are met
            if (!checkPrerequisites(courseNo)) {
                JOptionPane.showMessageDialog(null, "Prerequisites not met for " + courseNo);
                return;
            } else {
                // Ensure the grade is within valid range
                if (newGrade < 0 || newGrade > 99) {
                    JOptionPane.showMessageDialog(null, "Invalid grade input. Grade must be between 0 and 99.");
                    return;
                }
                // Update the grade and save the file
                course.setGrade(newGrade);
                course.setTaking(false);
                courses.set(i, course);
                saveFile();
                JOptionPane.showMessageDialog(null, "Grade updated successfully for " + courseNo);
            }


        } else {
            JOptionPane.showMessageDialog(null, "Course not found.");
        }

    }
    /**
     * Displays a GUI for editing course information.
     * Lists all courses that have grades and prompts the user to enter a course number, new name, new grade, and new unit.
     * Upon clicking the edit button, it validates the input and edits the course information if the input is valid.
     * Displays error messages for invalid input.
     */
    public void editCourse() {

        // Create a StringBuilder to store the information
        StringBuilder gradesInfo = new StringBuilder();
        // Create a JTextArea to display the information
        JTextArea textArea = new JTextArea(30, 150);
        textArea.setEditable(false);

        gradesInfo.append(" Program:").append(program.toUpperCase());
        gradesInfo.append("\n");
        gradesInfo.append(String.format("%-20s%-85s%-15s%-20s%5s%n", "Course Number", "Course", "Grade", "Units", ""));
        gradesInfo.append(String.format("%-20s%-85s%-15s%-20s%5s%n", "---------------", "---------------------------------------------------", "-------", "-------", ""));
        // Append each course's information to the StringBuilder
        for (Course course : courses) {
            if (course.getYear() != 0) {
                gradesInfo.append(String.format("%-20s%-85s%-15s%-20s%5s%n", course.getCourseNo(), course.getCourseName(), course.getGrade(), course.getUnit(), ""));
            }
        }
        textArea.setText(gradesInfo.toString());
        textArea.setCaretPosition(0);

        // Create a scroll pane for the JTextArea
        JScrollPane textScrollPane = new JScrollPane(textArea);
        textScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);

        textArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        JFrame frame = new JFrame("Edit Course");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(1100, 750);
        frame.setLayout(new BorderLayout());
        frame.setLocationRelativeTo(null);
        ImageIcon icon = new ImageIcon("LogoIcon.png");
        Image image = icon.getImage();
        frame.setIconImage(image);


        // Existing panel for course number, grade fields, and edit button
        JPanel editPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(5, 5, 5, 5);

        Font calibriFont = new Font("Calibri", Font.PLAIN, 14);
        JLabel courseLabel = new JLabel("Enter Course Number:");
        courseLabel.setFont(calibriFont);
        JTextField courseField = new JTextField(15);
        JLabel nameLabel = new JLabel("Enter New Course Name:");
        nameLabel.setFont(calibriFont);
        JTextField nameField = new JTextField(15);
        JLabel gradeLabel = new JLabel("Enter New Grade:");
        gradeLabel.setFont(calibriFont);
        JTextField gradeField = new JTextField(15);
        JLabel unitLabel = new JLabel("Enter New Course Unit:");
        unitLabel.setFont(calibriFont);
        JTextField unitField = new JTextField(15);
        JButton editButton = new JButton("Edit Grade");
        editButton.setFont(calibriFont);
        editButton.setBackground(Color.white);
        JButton unitButton = new JButton("Edit Unit");
        unitButton.setFont(calibriFont);
        unitButton.setBackground(Color.white);
        JButton nameButton = new JButton("Edit Name");
        nameButton.setFont(calibriFont);
        nameButton.setBackground(Color.white);
        JButton removeButton = new JButton("Remove Course");
        removeButton.setBackground(Color.red);
        removeButton.setForeground(Color.white);

        editButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    String courseNumber = courseField.getText();
                    int grade = Integer.parseInt(gradeField.getText());
                    editCourseGradeInFile(courseNumber, grade);
                } catch (Exception exception) {
                    JOptionPane.showMessageDialog(null, "Invalid input.");
                }
            }
        });


        unitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    String courseNumber = courseField.getText();
                    int unit = Integer.parseInt(unitField.getText());
                    editCourseUnitInFile(courseNumber, unit);
                } catch (Exception exception) {
                    JOptionPane.showMessageDialog(null, "Invalid input.");
                }
            }
        });

        nameButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    String courseNumber = courseField.getText();
                    String name = nameField.getText();
                    editCourseNameInFile(courseNumber, name);
                } catch (Exception exception) {
                    JOptionPane.showMessageDialog(null, "Invalid input.");
                }
            }
        });

        removeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    // Get the course number from the courseField
                    String courseNumber = courseField.getText();
                    // Search for the course in the courses collection
                    int i = search(courseNumber);
                    int index = searchElectives(courseNumber);
                    if(index != -1) {
                        for (Elective elective : electives) {
                            if (elective.getCourseNo().equals(courseNumber.toUpperCase())) {
                                electives.get(index).setSelected(false);
                                electives.get(index).setcanBeTaken(true);
                            }
                        }
                    }
                     // If the course is found
                    if (i != -1) {

                        // Ask for confirmation before removing
                        int choice = JOptionPane.showConfirmDialog(null, "Are you sure you want to remove this course?", "Course Removal Confirmation", JOptionPane.YES_NO_OPTION);
                        if (choice == JOptionPane.YES_OPTION) {
                            // Remove the course from the courses collection

                            courses.remove(i);
                            saveFile();
                            JOptionPane.showMessageDialog(null, "Course removed.", "Course Removal Confirmation", JOptionPane.OK_OPTION);
                            frame.dispose();
                            editCourse();

                        } else {

                            return;
                        }
                    } else if(i == -1){

                    }
                        else {
                        JOptionPane.showMessageDialog(null, "Course not found.", "Course Removal Error", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (Exception exception) {

                    // Handle any exceptions, such as invalid input
                    JOptionPane.showMessageDialog(null, "Invalid input.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        gbc.anchor = GridBagConstraints.EAST;
        editPanel.add(courseLabel, gbc);
        gbc.gridx++;
        editPanel.add(courseField, gbc);
        gbc.gridx++;
        editPanel.add(removeButton, gbc);
        gbc.gridx = 0;
        gbc.gridy++;
        editPanel.add(nameLabel, gbc);
        gbc.gridx++;
        editPanel.add(nameField, gbc);
        gbc.gridx++;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        editPanel.add(nameButton, gbc);
        gbc.fill = 0;
        gbc.gridx = 0;
        gbc.gridy++;
        editPanel.add(gradeLabel, gbc);
        gbc.gridx++;
        editPanel.add(gradeField, gbc);
        gbc.gridx++;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        editPanel.add(editButton, gbc);
        gbc.fill = 0;
        gbc.gridx = 0;
        gbc.gridy++;
        editPanel.add(unitLabel, gbc);
        gbc.gridx++;
        editPanel.add(unitField, gbc);
        gbc.gridx++;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        editPanel.add(unitButton, gbc);
        gbc.fill = 0;
        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 2;

        // Create a scroll pane for the editPanel
        JScrollPane editScrollPane = new JScrollPane(editPanel);
        editScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);

        // Add both scroll panes to the frame
        frame.add(textScrollPane, BorderLayout.CENTER);
        frame.add(editScrollPane, BorderLayout.SOUTH);
        frame.setVisible(true);
    }
    /**
     * Edits a course in the file by updating its name, grade, and unit.
     * Checks if the provided grade is within a valid range (65 to 99).
     * If the course doesn't have a grade (i.e., grade is 0), it cannot be edited.
     * Checks if prerequisites are met for the course.
     * Updates the course with the new information and saves the changes to the file.
     *
     * @param courseNo The course number of the course to be edited.
     * @param newGrade The new grade for the course.
     */
    public void editCourseGradeInFile(String courseNo, float newGrade) {
        int i = search(courseNo);
        if (i != -1) {
            Course course = courses.get(i);
            if (newGrade < 65 || newGrade > 99) {
                JOptionPane.showMessageDialog(null, "Invalid grade input. Grade must be between 65 and 99.");
                return;
            }
            if (course.getGrade() == 0) {
                JOptionPane.showMessageDialog(null, "Course does not have grade, cannot edit.", "Cannot Edit", JOptionPane.OK_OPTION);
                return;
            } else {
                course.setGrade(newGrade);
                course.setTaking(false);
                courses.set(i, course);
                saveFile();
            }
            // Check if prerequisites are met
            if (!checkPrerequisites(courseNo)) {
                JOptionPane.showMessageDialog(null, "Prerequisites not met for " + courseNo);
                return;
            }
            // Ensure the grade is within valid range
            if (newGrade < 65 || newGrade > 99) {
                JOptionPane.showMessageDialog(null, "Invalid grade input. Grade must be between 65 and 99.");
                return;
            }
            // Update the grade and save the file
            JOptionPane.showMessageDialog(null, "Grade updated successfully for " + courseNo);
        } else {
            JOptionPane.showMessageDialog(null, "Course not found.");
        }
    }
    /**
     * Edits the unit of a course identified by the given course number and saves the changes to the file.
     *
     * @param courseNo the course number of the course to edit
     * @param newUnit the new unit value to set for the course
     */

    public void editCourseUnitInFile(String courseNo, float newUnit) {
        int i = search(courseNo);
        if (i != -1) {
            Course course = courses.get(i);
            // Check if prerequisites are met
            if (!checkPrerequisites(courseNo)) {
                JOptionPane.showMessageDialog(null, "Prerequisites not met for " + courseNo);
                return;
            }
            course.setUnit((byte) newUnit);
            course.setTaking(false);
            courses.set(i, course);
            saveFile();
            // Update the grade and save the file


            JOptionPane.showMessageDialog(null, "Unit updated successfully for " + courseNo);
        } else {
            JOptionPane.showMessageDialog(null, "Course not found.");
        }
    }

    /**
     * Edits the name of a course identified by the given course number and saves the changes to the file.
     *
     * @param courseNo the course number of the course to edit
     * @param newName the new name to set for the course
     */
    public void editCourseNameInFile(String courseNo, String newName) {
        int i = search(courseNo);
        if (i != -1) {
            Course course = courses.get(i);
            // Check if prerequisites are met
            if (!checkPrerequisites(courseNo)) {
                JOptionPane.showMessageDialog(null, "Prerequisites not met for " + courseNo);
                return;
            }
            course.setCourseName(newName);
            course.setTaking(false);
            courses.set(i, course);
            saveFile();
            // Update the grade and save the file


            JOptionPane.showMessageDialog(null, "Name updated successfully for " + courseNo);
        } else {
            JOptionPane.showMessageDialog(null, "Course not found.");
        }
    }

    /**
     * Displays a GUI for adding a course taken by the student.
     * The GUI prompts the user to input the year, semester, course number, course name, units, and grade.
     * Upon adding the course, it validates the input and adds the course to the curriculum if the input is valid.
     * Displays error messages for invalid input or successful addition of the course.
     */

    public void addCourseTaken() {
        JFrame frame = new JFrame("Add Course Taken");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(400, 300);
        frame.setLocationRelativeTo(null);
        ImageIcon icon = new ImageIcon("LogoIcon.png");
        Image image = icon.getImage();
        frame.setIconImage(image);

        JPanel addPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(5, 5, 5, 5);

        Font calibriFont = new Font("Calibri", Font.PLAIN, 14);
        JLabel yearLabel = new JLabel("Year:");
        yearLabel.setFont(calibriFont);
        JTextField yearField = new JTextField(10);
        JLabel semesterLabel = new JLabel("Semester:");
        semesterLabel.setFont(calibriFont);
        JTextField semesterField = new JTextField(10);
        JLabel courseNoLabel = new JLabel("Course Number:");
        courseNoLabel.setFont(calibriFont);
        JTextField courseNoField = new JTextField(10);
        JLabel courseNameLabel = new JLabel("Course Name:");
        courseNameLabel.setFont(calibriFont);
        JTextField courseNameField = new JTextField(10);
        JLabel unitLabel = new JLabel("Units:");
        unitLabel.setFont(calibriFont);
        JTextField unitField = new JTextField(10);
        JLabel gradeLabel = new JLabel("Grade:");
        gradeLabel.setFont(calibriFont);
        JTextField gradeField = new JTextField(10);
        JButton addButton = new JButton("Add Course");
        addButton.setFont(calibriFont);
        addButton.setBackground(Color.white);
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    byte year = Byte.parseByte(yearField.getText());
                    byte semester = Byte.parseByte(semesterField.getText());
                    String courseNo = courseNoField.getText();
                    String courseName = courseNameField.getText();
                    float unit = Float.parseFloat(unitField.getText());
                    float grade = Float.parseFloat(gradeField.getText());

                    if (year <= 0 || year > 4 || semester <= 0 || semester > 3 || unit <= 0 || grade < 0 || grade > 99) {
                        JOptionPane.showMessageDialog(null, "Year, semester, unit, and grade must be greater than zero.", "Input Error", JOptionPane.ERROR_MESSAGE);
                    } else {
                        addCourseTaken(year, semester, courseNo, courseName, unit, grade);
                        JOptionPane.showMessageDialog(null, "Course added successfully.");
                    }

                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "Invalid input. Please enter valid values.");
                }
            }
        });

        gbc.anchor = GridBagConstraints.EAST;
        addPanel.add(yearLabel, gbc);
        gbc.gridx++;
        addPanel.add(yearField, gbc);
        gbc.gridx = 0;
        gbc.gridy++;
        addPanel.add(semesterLabel, gbc);
        gbc.gridx++;
        addPanel.add(semesterField, gbc);
        gbc.gridx = 0;
        gbc.gridy++;
        addPanel.add(courseNoLabel, gbc);
        gbc.gridx++;
        addPanel.add(courseNoField, gbc);
        gbc.gridx = 0;
        gbc.gridy++;
        addPanel.add(courseNameLabel, gbc);
        gbc.gridx++;
        addPanel.add(courseNameField, gbc);
        gbc.gridx = 0;
        gbc.gridy++;
        addPanel.add(unitLabel, gbc);
        gbc.gridx++;
        addPanel.add(unitField, gbc);
        gbc.gridx = 0;
        gbc.gridy++;
        addPanel.add(gradeLabel, gbc);
        gbc.gridx++;
        addPanel.add(gradeField, gbc);
        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        addPanel.add(addButton, gbc);

        frame.add(addPanel);
        frame.setVisible(true);
    }

    /**
     * Adds a course taken by the student, updating or inserting it into the curriculum.
     * If the course already exists, updates its grade and sets its status to not taking.
     * If the course does not exist, inserts it into the curriculum.
     * Saves the changes to the file after the operation.
     *
     * @param year       The year when the course was taken.
     * @param semester   The semester when the course was taken.
     * @param courseNo   The course number.
     * @param courseName The name of the course.
     * @param unit       The number of units/credits for the course.
     * @param grade      The grade obtained in the course.
     */
    public void addCourseTaken(byte year, byte semester, String courseNo, String courseName, float unit, float grade) {
        int index = search(courseNo);
        if (index != -1) {
            Course existingCourse = courses.get(index);
            if (existingCourse.isTaking()) {
                existingCourse.setGrade(grade);
                existingCourse.setTaking(false);
            } else {
                insertCourseAtYearAndSemester(year, semester, new Course(year, semester, courseNo, courseName, unit, grade, false));
            }
        } else {
            insertCourseAtYearAndSemester(year, semester, new Course(year, semester, courseNo, courseName, unit, grade, false));
        }
        saveFile();
    }

    /**
     * Inserts the new course at the specified year and semester in the curriculum.
     *
     * @param year      The year in which the course should be inserted.
     * @param semester  The semester in which the course should be inserted.
     * @param newCourse The course to be inserted.
     */
    private void insertCourseAtYearAndSemester(byte year, byte semester, Course newCourse) {
        int insertIndex = -1;
        for (int i = 0; i < courses.size(); i++) {
            Course currentCourse = courses.get(i);
            if (currentCourse.getYear() == year && currentCourse.getSemester() == semester) {
                insertIndex = i;
                break;
            }
        }

        if (insertIndex != -1) {
            courses.add(insertIndex, newCourse);
        } else {
            courses.add(newCourse);
        }
    }
    /**
     * Displays the curriculum along with the GPA for each semester.
     * The information is displayed in a JOptionPane with options to navigate through semesters.
     * If the GPA for a semester is 85 or above, it notifies the user they are in the Dean's List.
     * If the GPA is 90 or above, it notifies the user they are eligible for Latin Honors.
     */
    public void displayCurriculumWithGPA () { // option 6
        StringBuilder semesterInfo2 = new StringBuilder();
        JTextArea textArea2 = new JTextArea(30, 180);
        textArea2.setEditable(false);
        textArea2.setFont(new Font("Monospaced", Font.PLAIN, 12));
        JScrollPane scrollPane2 = new JScrollPane(textArea2);
        scrollPane2.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        byte sem2 = 0;
        Object[] options;

        semesterInfo2.append("Name:").append(IdNumber.toUpperCase());
        semesterInfo2.append(" Program:").append(program.toUpperCase());

        for (Course course : courses) {
            if (sem2 != course.getSemester()) {
                // Update semester information
                textArea2.setText(semesterInfo2.toString());

                if (hasNextSemester(course)) {
                    options = new Object[]{"Next"};
                } else {
                    options = new Object[]{"Close"};
                }

                int choice = JOptionPane.showOptionDialog(null, scrollPane2, "BSIT Subjects", JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, options[0]);

                if (choice == 0) {
                    sem2 = course.getSemester();
                } else {
                    break;
                }
                // Reset semester information for the new semester

                semesterInfo2.append("Name:").append(IdNumber.toUpperCase());
                semesterInfo2.append(" Program:").append(program.toUpperCase());
                semesterInfo2.append("\n");
                semesterInfo2.append(String.format("%-5s%-14d%s%-14s%n", " Year: ", course.getYear(), "Semester: ", course.getTerm()));
                semesterInfo2.append(String.format("%-5s%-20s%-85s%-15s%-15s%-20s%5s%n","","Course Number", "Course", "Units", "Grade","Remarks",""));
                semesterInfo2.append(String.format("%-5s%-20s%-85s%-15s%-15s%-20s%5s%n", "","---------------","--------------------------------" +
                        "-------------------------------------------","-------", "-------","-------------", ""));

                // Filter courses for the current semester
                byte finalSem = sem2;
                List<Course> semesterCourses = courses.stream()
                        .filter(n -> n.getYear() == course.getYear())
                        .filter(n -> n.getSemester() == finalSem)
                        .collect(Collectors.toList());

                // Calculate GPA for this semester
                double sum = semesterCourses.stream()
                        .mapToDouble(AbstractCourse::getGrade)
                        .sum();
                double average = sum / semesterCourses.size();

                // Append course details
                semesterCourses.forEach(course2 -> {
                    if(course2.getGrade() == 0){
                        semesterInfo2.append(String.format("%-5s%-20s%-85s%-15s%-15s%-20s%5s%n","", course2.getCourseNo(), course2.getCourseName(), course2.getUnit(),"", course2.getRemarks(),""));
                    }else if(course2.getGrade() < 65 && course2.getGrade() > 0){
                        semesterInfo2.append(String.format("%-5s%-20s%-85s%-15s%-15s%-20s%5s%n","", course2.getCourseNo(), course2.getCourseName(), course2.getUnit(),"65.0", course2.getRemarks(),""));
                    }else {
                        semesterInfo2.append(String.format("%-5s%-20s%-85s%-15s%-15s%-20s%5s%n", "", course2.getCourseNo(), course2.getCourseName(), course2.getUnit(), course2.getGrade(), course2.getRemarks(), ""));
                    }
                });
                // Append GPA for this semester
                semesterInfo2.append("The GPA for this semester is: ").append(average).append("\n").append("\n");
                if(average >= 85)
                    semesterInfo2.append("\nYou are in the Dean's List \n");
                if(average>=90)
                    semesterInfo2.append("\nYou are eligible for Latin Honors\n");
            }
        }
    }
    /**
     * Displays the list of courses sorted alphabetically by course name.
     * The information is displayed in a JTextArea within a scrollable JOptionPane.
     */
    public void displayGradesAlpabetically() {

        Collections.sort(courses ,(c1, c2) -> c1.getCourseName().compareTo(c2.getCourseName()));


        StringBuilder courseData = new StringBuilder();

        JTextArea textArea = new JTextArea(30, 150);
        textArea.setEditable(false);
        textArea.setFont(new Font("Monospaced", Font.PLAIN, 12));


        courseData.append("Name:").append(IdNumber.toUpperCase());
        courseData.append(" Program:").append(program.toUpperCase());
        courseData.append("\n");
        courseData.append(String.format("%-20s%-85s%-15s%-20s%5s%n", "Course Number", "Course",  "Grade", "Remarks", ""));
        courseData.append(String.format("%-20s%-85s%-15s%-20s%5s%n", "---------------", "---------------------------------------------------", "-------", "-------------", ""));


        for (Course course : courses) {
            if(course.getYear() != 0) {
                courseData.append(String.format("%-20s%-85s%-15s%-20s%5s%n", course.getCourseNo(), course.getCourseName(), course.getGrade(), course.getRemarks(), ""));
            }
        }
        textArea.setText(courseData.toString());
        textArea.setCaretPosition(0);

        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);


        JOptionPane.showMessageDialog(null, scrollPane, "Courses Sorted Alphabetically ", JOptionPane.PLAIN_MESSAGE);
    }
    /**
     * Displays the grades of courses in descending order.
     * The information is displayed in a JTextArea within a scrollable JOptionPane.
     */
    public void displayGradesDescending() {
        // Sort the courses in descending order of grades
        Collections.sort(courses, (c1, c2) -> Float.compare(c2.getGrade(), c1.getGrade()));


        StringBuilder gradesInfo = new StringBuilder();

        JTextArea textArea = new JTextArea(30, 150);
        textArea.setEditable(false);
        textArea.setFont(new Font("Monospaced", Font.PLAIN, 12));



        gradesInfo.append("Name:").append(IdNumber.toUpperCase());
        gradesInfo.append(" Program:").append(program.toUpperCase());
        gradesInfo.append("\n");
        gradesInfo.append(String.format("%-20s%-85s%-15s%-20s%5s%n", "Course Number", "Course",  "Grade", "Remarks", ""));
        gradesInfo.append(String.format("%-20s%-85s%-15s%-20s%5s%n", "---------------", "---------------------------------------------------", "-------", "-------------", ""));

        // Append each course's information to the StringBuilder
        for (Course course : courses) {
            if(course.getYear() != 0) {
                gradesInfo.append(String.format("%-20s%-85s%-15s%-20s%5s%n", course.getCourseNo(), course.getCourseName(), course.getGrade(), course.getRemarks(), ""));
            }
        }
        textArea.setText(gradesInfo.toString());
        textArea.setCaretPosition(0);
        // Create a scroll pane for the JTextArea
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);


        // Display the information in a JOptionPane
        JOptionPane.showMessageDialog(null, scrollPane, "Grades from Highest to Lowest", JOptionPane.PLAIN_MESSAGE);
    }

    /**
     * Displays a dialog for selecting elective courses, allowing the user to take electives based on their prerequisites
     * and availability. The user can input the course number, select the year and semester, and click the "Take" button
     * to enroll in the elective course. After selecting an elective, the method updates the course information and saves
     * the changes to a file.
     */
    public void takeElective(){



        JPanel panel = new JPanel(new BorderLayout());

        JTextArea textArea = new JTextArea(30, 200);
        textArea.setEditable(false);
        textArea.setFont(new Font("Monospaced", Font.PLAIN, 12));

        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);


        updateElectiveInfo(textArea);
        panel.add(scrollPane, BorderLayout.CENTER);


        JPanel inputPanel = new JPanel(new GridLayout(1, 4));
        JLabel courseLabel = new JLabel("Enter Course Number:");
        inputPanel.add(courseLabel);


        JTextField courseField = new JTextField(10);
        inputPanel.add(courseField);

        JLabel yearLabel = new JLabel("Select Year:");
        inputPanel.add(yearLabel);

        JComboBox<Byte> yearComboBox = new JComboBox<>(new Byte[]{3, 4});
        inputPanel.add(yearComboBox);


        JLabel semesterLabel = new JLabel("Select Semester:");
        inputPanel.add(semesterLabel);

        JComboBox<Byte> semesterComboBox = new JComboBox<>(new Byte[]{1, 2, 3});
        inputPanel.add(semesterComboBox);

        JButton takeButton = new JButton("Take");
        inputPanel.add(takeButton);

        panel.add(inputPanel, BorderLayout.SOUTH);

        takeButton.addActionListener(e -> {
            String courseNumber = courseField.getText().toUpperCase();
            Byte year = (Byte) yearComboBox.getSelectedItem();
            Byte semester = (Byte) semesterComboBox.getSelectedItem();


            Elective selectedElective = null;
            for (Elective elective : electives) {
                if (elective.getCourseNo().equals(courseNumber)) {
                    if(!checkPrerequisites(elective.getCourseNo())) {
                        elective.setcanBeTaken(false);
                    }
                    selectedElective = elective;
                    break;
                }
            }

            if (year == 3) {

                if (countElectivesInYear((byte) 3, courseNumber) >= 2) {
                    JOptionPane.showMessageDialog(null, "You can only take 2 electives in Year 3.", "Limit Exceeded", JOptionPane.WARNING_MESSAGE);
                    return;
                }
            } else if (year == 4) {

                if (countElectivesInYear((byte) 4,courseNumber) >= 3) {
                    JOptionPane.showMessageDialog(null, "You can only take 3 electives in Year 4.", "Limit Exceeded", JOptionPane.WARNING_MESSAGE);
                    return;
                }
            }



            if (selectedElective != null && selectedElective.getCanBeTaken() && !selectedElective.getSelected()) {

                selectedElective.setSelected(true);

                insertCourseAtYearAndSemester(year, semester, new Course(year, semester, selectedElective.getCourseNo(), selectedElective.getCourseName(),
                        selectedElective.getUnit(), 0, false));

                if(selectedElective.getUnit2() != 0){
                    insertCourseAtYearAndSemester(year, semester, new Course(year, semester, selectedElective.getCourseNo() + "L", selectedElective.getCourseName() + "(LAB)",
                            selectedElective.getUnit(), 0, false));
                    saveFile();
                }
                saveFile();
                updateElectiveInfo(textArea);


                JOptionPane.showMessageDialog(null, "Elective " + selectedElective.getCourseName() + " has been successfully added to your courses.");
            } else {
                JOptionPane.showMessageDialog(null, "Invalid elective course number/Prerequisite not met", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        JOptionPane.showMessageDialog(null, panel, "Elective Courses", JOptionPane.PLAIN_MESSAGE);
    }
    /**
     * Updates the content of the JTextArea with the latest elective information.
     *
     * @param textArea the JTextArea to update with the elective information
     */
    public void updateElectiveInfo(JTextArea textArea) {
        StringBuilder electiveInfo = new StringBuilder();

        // Clear the existing content of the text area
        textArea.setText("");

        // Append new content based on the latest elective information
        electiveInfo.append("ID Number:").append(IdNumber.toUpperCase());
        electiveInfo.append(" Program:").append(program.toUpperCase());
        electiveInfo.append("\n");
        electiveInfo.append(String.format("%-20s%-80s%-10s%-40s%-40s%n", "Course Number", "Course", "Units", "Taken", "Selected"));
        electiveInfo.append(String.format("%-20s%-80s%-10s%-40s%-30s%n", "---------------", "---------------------------------------------------", "-------", "-------", "-------"));

        for (Elective elective : electives) {
            if(!checkPrerequisites(elective.getCourseNo())){
                elective.setcanBeTaken(false);
            }
            if (elective.getUnit2() != 0) {
                electiveInfo.append(String.format("%-20s%-80s%-10s%-40s%-40s%n", elective.getCourseNo(), elective.getCourseName() , elective.getUnit() + "/" +elective.getUnit2(), elective.isCanBeTaken(), elective.isSelected()));
            } else {
                electiveInfo.append(String.format("%-20s%-80s%-10s%-40s%-40s%n", elective.getCourseNo(), elective.getCourseName(), elective.getUnit(), elective.isCanBeTaken(), elective.isSelected()));
            }
        }

        // Update the text area with the new content
        textArea.setText(electiveInfo.toString());
    }

    /**
     * Counts the number of electives taken in a specific year for a given course number.
     *
     * @param year     the year for which to count the electives
     * @param courseNo the course number for which to count the electives
     * @return the number of electives taken in the specified year for the given course number
     */
    private int countElectivesInYear(byte year, String courseNo ) {
        int count = 0;
        for (Elective elective : electives) {
            if(elective.getCourseNo().equals(courseNo)){
                elective.setYear(year);
            }
            if (elective.getSelected() && elective.getYear() == year) {
                count++;
            }
        }

        return count;
    }
    /**
     * Prompts the user to confirm if they want to shift programs. If confirmed, the program shifts the student's
     * program from BSIT to BSCS or vice versa. It updates the program information in the data file, renames the data
     * file accordingly, and adjusts the course numbers of the courses taken by the student based on the new program.
     * If a course has a grade greater than 0, it sets the course as "taking". Finally, it saves the changes to the data file.
     */
    public void shiftPrograms() {
        int choice = JOptionPane.showConfirmDialog(null, "Are you sure you want to shift programs?", "Program Shift Confirmation", JOptionPane.YES_NO_OPTION);
        if (choice == JOptionPane.YES_OPTION) {
            String newProgram = (program.equals("BSIT")) ? "BSCS" : "BSIT";
            String newFileName = (IdNumber.toLowerCase() + newProgram.toLowerCase()).replaceAll(" ", "") + ".ser";


            File oldFile = new File(DATA_FILE_LOCATION + FILE_NAME);
            File newFile = new File(DATA_FILE_LOCATION + newFileName);
            if (oldFile.exists()) {
                boolean rename = oldFile.renameTo(newFile);
                if (!rename)
                    JOptionPane.showMessageDialog(null, "Failed to rename file.");
            }


            setProgram(newProgram);


            for (Course course : courses) {
                if (course.getCourseNo().startsWith("IT") && newProgram.equals("BSCS")) {
                    course.setCourseNo(course.getCourseNo().replace("IT", "CS"));
                } else if (course.getCourseNo().startsWith("CS") && newProgram.equals("BSIT")) {
                    course.setCourseNo(course.getCourseNo().replace("CS", "IT"));
                }
                for (Course newCourse : courses) {
                    if (course.getCourseNo().equals(newCourse.getCourseNo()) && newCourse.getGrade() > 0) {
                        course.setTaking(true);
                        break;
                    }
                }
            }


            saveFile();
            JOptionPane.showMessageDialog(null, "Program shifted successfully to " + newProgram);
        } else {
            JOptionPane.showMessageDialog(null, "Program shift canceled.");
        }
    }



    /**
     * Checks if a prerequisite course is met based on its course number and grade.
     *
     * @param prerequisiteCourse The course number of the prerequisite to check.
     * @return true if the prerequisite course is found in the list of courses
     *         and its grade is greater than 0, false otherwise.
     */
    private boolean isPrerequisiteMet(String prerequisiteCourse) {
        for (Course course : courses) {
            if (course.getCourseNo().equals(prerequisiteCourse) && course.getGrade() > 0) {
                return true; // Prerequisite course is found and grade is greater than 0
            }
        }
        return false; // Prerequisite course not found or grade is 0
    }
    /**
     * Reads and parses the prerequisites for courses from a file.
     *
     * @return A mapping of course numbers to their respective lists of prerequisites.
     */
    private Map<String, List<String>> readPrerequisites() {
        Map<String, List<String>> prerequisitesMap = new HashMap<>();
        try (Scanner scanner = new Scanner(new File("C:\\Users\\admin\\Downloads\\AgcaoiliBarryBibit-CheeBilalCarbonellCumtiEzperagoza9401FinProj1\\Courses\\"+ program +"prerequisites.txt"))) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] parts = line.split(",");
                String courseNo = parts[0].trim();
                List<String> prerequisites = Arrays.asList(parts).subList(1, parts.length);
                prerequisitesMap.put(courseNo, prerequisites);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return prerequisitesMap;
    }
    /**
     * Checks if the prerequisites for a course are met.
     *
     * @param courseNo The course number for which to check prerequisites.
     * @return true if the course has no prerequisites or if all prerequisites
     *         are met, false otherwise.
     */
    private boolean checkPrerequisites(String courseNo) {
        Map<String, List<String>> prerequisitesMap = readPrerequisites();

        List<String> prerequisites = prerequisitesMap.get(courseNo);
        if (prerequisites == null || prerequisites.isEmpty()) {
            return true; // No prerequisites or all prerequisites met
        }

        for (String prerequisite : prerequisites) {
            if (!isPrerequisiteMet(prerequisite)) {
                return false; // Prerequisite not met
            }
        }
        return true; // All prerequisites met
    }
    /**
     * Searches for an elective with the given course number.
     *
     * @param courseNo the course number to search for
     * @return the index of the elective with the given course number, or -1 if not found
     */

    public int searchElectives(String courseNo) {
        for (int i = 0; i < electives.size(); i++) {
            Elective course = electives.get(i);
            String courseNoLowerCase = course.getCourseNo().replace(" ", "").toLowerCase();
            if (courseNoLowerCase.equals(courseNo.replace(" ", "").toLowerCase())) {
                return i;
            }
        }
        return -1;
    }
    /**
     * Searches for a course in the list of courses by its course number.
     *
     * @param courseNo The course number to search for.
     * @return The index of the first occurrence of the course with the specified
     *         course number in the list of courses, or -1 if the course is not found.
     */
    public int search(String courseNo) {
        for (int i = 0; i < courses.size(); i++) {
            Course course = courses.get(i);
            String courseNoLowerCase = course.getCourseNo().replace(" ", "").toLowerCase();
            if (courseNoLowerCase.equals(courseNo.replace(" ", "").toLowerCase())) {
                return i;
            }
        }
        return -1;
    }
    /**
     * Saves the current instance of the object to a file using serialization.
     * The object is saved to the specified file location with the given file name.
     * If the file does not exist, it will be created. If the file already exists,
     * its contents will be overwritten.
     *
     * @throws IOException If an I/O error occurs while attempting to save the file.
     */

    public void saveFile() {
        try {
            FileOutputStream outputFile = new FileOutputStream(DATA_FILE_LOCATION + FILE_NAME);
            ObjectOutputStream objectOut = new ObjectOutputStream(outputFile);
            objectOut.writeObject(this);
            objectOut.close();


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}


