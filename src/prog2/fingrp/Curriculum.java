package prog2.fingrp;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.InputMismatchException;
import java.util.*;
import java.util.List;


public class Curriculum implements Serializable{
    protected String name;
    protected String program;
    protected ArrayList<Course> courses;
    private transient Scanner scan;
    private final String FILE_NAME;
    private final String DATA_FILE_LOCATION = "C:\\DEMOS JAVA\\AgcaoiliBarryBibit-CheeBilalCarbonellCumtiEzperagoza9401FinProj1\\Data\\"; // temporary Storage
    private final String COURSE_CURRICULUM_REFERENCE_LOCATION = "C:\\DEMOS JAVA\\AgcaoiliBarryBibit-CheeBilalCarbonellCumtiEzperagoza9401FinProj1\\Courses\\"; // temporary Storage

    public Curriculum(){
        name = "";
        program = "";
        FILE_NAME = "";
        courses = new ArrayList<>();
    }


    public Curriculum(String name, String program) throws IOException, ClassNotFoundException {
        this.name = name;
        this.program = program;
        FILE_NAME =(this.name.toLowerCase() + this.program.toLowerCase()).replaceAll(" ", "")+".ser";
        courses = new ArrayList<>();
        if(fileExists(FILE_NAME)) {
            initializeSerializedFile();
        }else{
            initializeNewCurrculum();
        }
    }

    public void setName(String name){
        this.name = name;
    }
    public void setProgram(String program){
        this.program = program;
    }
    public void setCourses(ArrayList<Course> courses) {
        this.courses = courses;
    }
    public String getName(){
        return name;
    }
    public String getProgram(){
        return program;
    }


    public ArrayList<Course> getCourses() {
        return courses;
    }
    public List<Course> getSubjectsForSemester(int semesterIndex) {
        List<Course> subjectsForSemester = new ArrayList<>();
        for (Course course : courses) {
            if (course.getSemester() == semesterIndex) {
                subjectsForSemester.add(course);
            }
        }
        return subjectsForSemester;
    }



    private void initializeSerializedFile() throws IOException, ClassNotFoundException {
        Curriculum c = deserialize(FILE_NAME);
        this.courses = c.courses;

    }

    private void initializeNewCurrculum() {
        if(!checkIfProgramisAvailable(program)){
            throw new IllegalArgumentException("program is not available");
        }
        makeNewFile();
    }
    private boolean checkIfProgramisAvailable(String filename){
        File file = new File(COURSE_CURRICULUM_REFERENCE_LOCATION + filename + ".txt");
        return file.exists();
    }
    private boolean fileExists(String filename){
        File file = new File(DATA_FILE_LOCATION + filename);
        return file.exists();
    }
    private Curriculum deserialize(String filename) throws IOException, ClassNotFoundException{
        try (FileInputStream inputStream = new FileInputStream(DATA_FILE_LOCATION + filename);
            ObjectInputStream objectInputStream = new ObjectInputStream(inputStream)) {
                return (Curriculum) objectInputStream.readObject();
        }

    }

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
        } catch (NumberFormatException | IOException e) {
            throw new RuntimeException(e);
        }
    }



    private void populateArrayList() {
        try {

            scan = new Scanner(new File(DATA_FILE_LOCATION + FILE_NAME));
            while (scan.hasNextLine()) {
                String line = scan.nextLine();
                String[] temp = line.split(",");
                boolean field7;
                if (temp[6].equalsIgnoreCase("true")) {
                    field7 = true;
                } else if (temp[6].equalsIgnoreCase("false")) {
                    field7 = false;
                } else {
                    throw new InputMismatchException("Invalid boolean: " + temp[6]);
                }
                courses.add(new Course(Byte.parseByte(temp[0]), Byte.parseByte(temp[1]), temp[2], temp[3],
                        Float.parseFloat(temp[4]), Float.parseFloat(temp[5]), field7));
            }
            saveFile();
            scan.close();

        } catch (NumberFormatException | IOException e) {
            throw new RuntimeException(e);
        }
    }


    public void displayCurriculum() { // option 1
        StringBuilder semesterInfo = new StringBuilder();
        JTextArea textArea = new JTextArea(30, 120);

        textArea.setEditable(false);
        textArea.setFont(new Font("Monospaced", Font.PLAIN, 12));

        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        byte sem = 0;
        Object[] options;
        semesterInfo.append("Name:").append(name.toUpperCase());
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

    public void displayCurriculumWithGrades () { // option 2
        StringBuilder semesterInfo2 = new StringBuilder();
        JTextArea textArea2 = new JTextArea(30, 180);

        textArea2.setEditable(false);
        textArea2.setFont(new Font("Monospaced", Font.PLAIN, 12));

        JScrollPane scrollPane2 = new JScrollPane(textArea2);
        scrollPane2.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        byte sem2 = 0;
        Object[] options;

        semesterInfo2.append("Name:").append(name.toUpperCase());
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
            if(course.getGrade() < 60){
                semesterInfo2.append(String.format("%-5s%-20s%-85s%-15s%-15s%-20s%5s%n","", course.getCourseNo(), course.getCourseName(), course.getUnit(),"", course.getRemarks(),""));
            }else {
                semesterInfo2.append(String.format("%-5s%-20s%-85s%-15s%-15s%-20s%5s%n", "", course.getCourseNo(), course.getCourseName(), course.getUnit(), course.getGrade(), course.getRemarks(), ""));
            }


        }
    }
    private boolean hasNextSemester(Course currentCourse) {
        if (currentCourse.getYear() == 0) {
            return false;
        }
        return true;
    }


    public void editCourse(String courseNoToEdit, String newCourse) {
        int i = search(courseNoToEdit);
        String program = "BS"+newCourse.substring(0,2); // temporary
        String[] newData = getCourseData(program, newCourse);
        Course course = courses.get(i);
        course.setCourseNo(newData[0]);
        course.setCourseName(newData[1]);
        course.setUnit(Byte.parseByte(newData[2]));
        courses.set(i,course);
    }

    public void editGrade(String courseNo, float newGrade) {
        int i = search(courseNo);
        if(i != 1) {
            Course course = courses.get(i);
            course.setGrade(newGrade);
            course.setTaking(false);
            courses.set(i, course);
        }
    }

    private String[] getCourseData (String program, String courseNo) {
        try {
            scan = new Scanner(new File(COURSE_CURRICULUM_REFERENCE_LOCATION +program.toLowerCase()+".txt"));
            while (scan.hasNextLine()) {
                String line = scan.nextLine();
                if (line.contains(courseNo)) {
                    scan.close();
                    return line.substring(4).split(",");
                }
            }
        } catch (FileNotFoundException ignored){}
        scan.close();
        return new String[]{"Error", "Error", "0"};
    }


  public final Search getIndexofCourseNo = s1 -> search(s1, AbstractCourse::getCourseNo);
    public final Search getIndexofCourseName = s1 -> search(s1, AbstractCourse::getCourseName);

    private int search(String searchValue1, CourseProperty<String> searchProperty2){
        for(int i = 0; i < courses.size(); i++)
            if(searchValue1.equals(searchProperty2.getCourse(courses.get(i)))) return i;

        return -1;


    }
    private boolean isValidGrade(float grade) {
        return grade >= 0 && grade <= 99;
    }
    public void editGrade(){
        JFrame frame = new JFrame("Edit Grade");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(400, 200);
        frame.setLayout(new BorderLayout());



        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(5, 5, 5, 5);


        JLabel courseLabel = new JLabel("Enter Course Number:");
        JTextField courseField = new JTextField(15);
        JLabel gradeLabel = new JLabel("Enter Grade:");
        JTextField gradeField = new JTextField(15);


        JButton editButton = new JButton("Enter");
        editButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try{
                    String courseNumber = courseField.getText();
                    int grade = Integer.parseInt(gradeField.getText());
                    editGradeInFile(courseNumber, grade);
                }catch (Exception exception){
                    JOptionPane.showMessageDialog(null, "Invalid input.");
                }
            }});


        panel.add(courseLabel, gbc);
        gbc.gridx++;
        panel.add(courseField, gbc);
        gbc.gridx = 0;
        gbc.gridy++;
        panel.add(gradeLabel, gbc);
        gbc.gridx++;
        panel.add(gradeField, gbc);
        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 2;
        panel.add(editButton, gbc);


        frame.add(panel, BorderLayout.CENTER);
        frame.setVisible(true);
    }


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
            course.setGrade(newGrade);
            course.setTaking(false);
            courses.set(i, course);
            saveFile();
            JOptionPane.showMessageDialog(null, "Grade updated successfully for " + courseNo);
        } else {
            JOptionPane.showMessageDialog(null, "Course not found.");
        }
    }


    private boolean checkPrerequisite(String courseId){
        int index = getIndexofCourseNo.find(courseId);
        Course c = courses.get(index);
        return c.getGrade() > 75 && c.getGrade() < 100;
    }






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




    public void addCourse(byte year, byte semester, String courseNo) {

    }

    public void shiftProgram(String program) {

    }
    // Serialize the object to a file
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


  /*private ArrayList<Course> deserializeCourses(String filename) throws IOException, ClassNotFoundException {
        ArrayList<Course> courses = new ArrayList<>();
        try (FileInputStream inputStream = new FileInputStream(DATA_FILE_LOCATION + filename);
             ObjectInputStream objectInputStream = new ObjectInputStream(inputStream)) {
            while (true) {
                try {
                    Course course = (Course) objectInputStream.readObject();
                    courses.add(course);
                } catch (EOFException e) {

                    break;
                }
            }
        }
        return courses;
    }
*/







   /* private int countFileLines() {
        int lines = 0;
        try {
            scan = new Scanner(new File(COURSE_CURRICULUM_REFERENCE_LOCATION + program.toLowerCase()+".txt"));
            while (scan.hasNextLine()) {
                lines++;
                scan.nextLine();
            }
            scan.close();
        } catch (NumberFormatException | FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        return lines;
    } */