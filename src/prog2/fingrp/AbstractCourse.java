package prog2.fingrp;

import java.io.Serializable;

public abstract class AbstractCourse implements Serializable {

    protected byte year;
    protected byte semester;
    protected String courseNo;
    protected String courseName;
    protected  float grade;

    protected float unit1;

    protected  float  unit2 = 0;
    protected boolean selected;

    protected boolean taking;
    protected boolean canBeTaken;


    public AbstractCourse(byte year, byte semester, String courseNo, String courseName, float unit, float grade, boolean taking) {

        this.year = year;
        this.semester = semester;
        this.courseNo = courseNo;
        this.courseName = courseName;
        this.unit1 = unit;
        this.grade = grade;
        this.taking = taking;
    }

    public AbstractCourse(byte year, byte semester, String courseNo, String courseName, float unit1, float unit2, boolean selected, boolean canBeTaken ) {

        this.year = year;
        this.semester = semester;
        this.courseNo = courseNo;
        this.courseName = courseName;
        this.unit1 = unit1;
        this.unit2 = unit2;
        this.selected = selected;
        this.canBeTaken = canBeTaken;



    }

// setters and getters
public void setCourseNo(String courseNo) {
    this.courseNo = courseNo;
}

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public void setUnit(byte unit) {
        this.unit1 = unit;
    }

    public void setGrade(float grade) {
        this.grade = grade;
    }
    public void setTaking(boolean taking) {
        this.taking = taking;
    }
    // Getter methods
    public String getCourseNo() {
        return courseNo;
    }

    public String getCourseName() {
        return courseName;
    }

    public float getUnit() {
        return unit1;
    }

    public float getGrade() {
        return grade;
    }
    public boolean isTaking() {
        return taking;
    }

    public byte getYear() {
        return year;
    }


    public byte getSemester() {
        return semester;
    }
    public void setYear(byte year) {
        this.year = year;
    }

    public void setSemester(byte semester) {
        this.semester = semester;
    }

}