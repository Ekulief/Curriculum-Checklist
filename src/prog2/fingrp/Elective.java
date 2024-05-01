package prog2.fingrp;

import java.io.Serializable;


public class Elective extends AbstractCourse implements Serializable {
    /**
     * <p>Represents an elective course in a curriculum. Each elective course extends AbstractCourse and inherits attributes
     * such as course number, course name, units, and semester information. Additionally, it has specific properties
     * like whether it has been selected and whether it can be taken. Provides methods to set and get these properties.</p>
     *
     * Template in file: CourseNo, Coursename, unit1, unit2, selected, canBeTaken
     * Example: ITE 11,Advanced Networking,3,1,false,true
     */
     protected float unit2;
     protected boolean selected;
     protected boolean canBeTaken;
    /**
     * Constructs an Elective object with the specified attributes.
     *
     * @param courseNo The course number.
     * @param courseName The name of the course.
     * @param unit1 The number of units for the course.
     * @param unit2 Additional unit information for the course.
     * @param selected Indicates whether the course has been selected.
     * @param canBeTaken Indicates whether the course can be taken.
     */
      protected Elective(String courseNo, String courseName,
                         byte unit1, byte unit2, boolean selected, boolean canBeTaken){
          super(courseNo, courseName, unit1);
      this.unit2 = unit2;
      this.canBeTaken = canBeTaken;
      this.selected = selected;
      }

    /**
     * Sets the unit2 value for the elective.
     *
     * @param unit2 the unit2 value to set
     */
    public void setUnit2(float unit2) {
        this.unit2 = unit2;
    }

    /**
     * Sets whether the elective is selected or not.
     *
     * @param select true if the elective is selected, false otherwise
     */
    public void setSelected(boolean select){
          this.selected = select;
}
    /**
     * Sets whether the elective can be taken or not.
     *
     * @param takeable true if the elective can be taken, false otherwise
     */
public void setcanBeTaken(boolean takeable){
          this.canBeTaken = takeable;
}
    /**
     * Gets whether the elective is selected or not.
     *
     * @return true if the elective is selected, false otherwise
     */

public boolean getSelected(){
          return selected;
}

    /**
     * Gets whether the elective can be taken or not.
     *
     * @return true if the elective can be taken, false otherwise
     */
public boolean getCanBeTaken(){
          return canBeTaken;
}
    /**
     * Checks if the elective is selected and returns the corresponding string.
     *
     * @return "Selected" if the elective is selected, "Not Selected" otherwise
     */
public String isSelected(){
          if(selected){
              return "Selected";
          }else{
              return "Not Selected";
          }
}
    /**
     * Checks if the prerequisites for the elective are met and returns the corresponding string.
     *
     * @return "Prerequisites met" if the prerequisites are met, "PreRequisites not met" otherwise
     */
    public String isCanBeTaken(){
        if(canBeTaken){
            return "Prerequisites met";
        }else{
            return "PreRequisites not met";
        }
    }
    /**
     * Gets the unit2 lab value of the elective.
     *
     * @return the unit2 value of the elective
     */
    public float getUnit2() {
        return unit2;
    }
    /**
     * Returns a string representation of the elective object.
     *
     * @return a string representation of the elective
     */
    @Override
    public String toString() {
        return "Elective{" +

                "year=" + year +
                ", semester=" + semester +
                ", courseNo='" + courseNo + '\'' +
                ", courseName='" + courseName + '\'' +
                ", grade=" + grade +
                ", unit=" + unit +
                ", unit2=" + unit2 +
                ", selected=" + selected +
                ", canBeTaken=" + canBeTaken +
                '}';
    }
}
