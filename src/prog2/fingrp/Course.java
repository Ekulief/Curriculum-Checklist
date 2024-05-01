package prog2.fingrp;


import java.io.Serializable;


public class Course extends AbstractCourse implements Serializable {

    /**
     * <p>Represents a course in a curriculum. Each course has attributes such as course number, course name, units,
     * grade, remarks, and semester information. Provides methods to set and get remarks based on the grade, retrieve
     * the term of the course based on the semester, and generate a string representation of the course.</p>
     *
     *
     *
     *
     * Template in file: Year, Semester, Course No., Course Name, Unit
     * Example: 2,3,GETHICS,ETHICS,3
     *
     */
    private String remarks;
    private boolean taking;
    private String term;

    /**
     * Constructs a Course object with the specified attributes.
     *
     * @param year The year of the course.
     * @param semester The semester of the course.
     * @param courseNo The course number.
     * @param courseName The name of the course.
     * @param unit The number of units for the course.
     * @param grade The grade obtained in the course.
     * @param taking Indicates whether the course is currently being taken.
     */

    public Course(byte year, byte semester, String courseNo, String courseName, float unit, float grade, boolean taking) {
        super(year, semester , courseNo, courseName, unit, grade);
        this.taking = taking;
    }
    // Setter methods

    /**
     * Sets the remarks for the course based on the grade obtained.
     *
     * @param remarks The remarks to set.
     */
    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    /**
     * Gets the remarks for the course based on the grade obtained.
     *
     * @return The remarks for the course.
     */

    public String getRemarks() {
        if(this.grade >= 75){
            return "PASSING";
        }else if(this.grade < 75 && this.grade > 0){
            return  "FAILING";
        }else{
            return "Not Taken Yet";
        }


    }
    /**
     * sets if the course is being taken
     * @param taking if course is being taken
     */
    public void setTaking(boolean taking) {
        this.taking = taking;
    }
    // Getter methods


    /**
     * Gets the term of the course based on the semester.
     *
     * @return The term of the course.
     */
    public String getTerm(){
        if(semester == 1){
            return "First Semester";
        } else if (semester == 2) {
            return "Second Semester";
        }else{
            return "Short Term";
        }
    }
    /**
     * gets the status of taking
     * @return taking
     */
    public boolean isTaking() {
        return taking;
    }



    /**
     * Returns a string representation of the Course object.
     *
     * @return A string representation of the Course object.
     */

    @Override
    public String toString() {
        return "Course{" +
                "courseNo='" + courseNo + '\'' +
                ", courseName='" + courseName + '\'' +
                ", unit=" + unit +
                ", grade=" + grade +
                ", remarks='" + remarks + '\'' +
                ", taking=" + taking +
                ", year=" + year +
                ", semester=" + semester +
                ", term='" + term + '\'' +
                '}';
    }
}