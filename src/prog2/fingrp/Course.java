package prog2.fingrp;


import java.io.Serializable;


public class Course extends AbstractCourse implements Serializable {


    private String remarks;

    private String term;


    /*Course(){
        courseNo = "";
        courseName = "";
        unit = 0;
        grade = 0;

    } */

    public Course(byte year, byte semester, String courseNo, String courseName, float unit, float grade, boolean taking) {
        super(year, semester , courseNo, courseName, unit, grade, taking);
    }
    // Setter methods

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }



    public String getRemarks() {
        if(this.grade > 75){
            return "PASSING";
        }else if(this.grade < 75 && this.grade >= 60){
            return  "FAILING";
        }else{
            return "Not Taken Yet";
        }


    }
    public String getTerm(){
        if(semester == 1){
            return "First Semester";
        } else if (semester == 2) {
            return "Second Semester";
        }else{
            return "Short Term";
        }
    }





    @Override
    public String toString() {
        return "Course{" +
                "courseNo='" + courseNo + '\'' +
                ", courseName='" + courseName + '\'' +
                ", unit=" + unit1 +
                ", grade=" + grade +
                ", remarks='" + remarks + '\'' +
                ", taking=" + taking +
                ", year=" + year +
                ", semester=" + semester +
                ", term='" + term + '\'' +
                '}';
    }
}