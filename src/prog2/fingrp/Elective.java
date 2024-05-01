package prog2.fingrp;

import java.io.Serializable;


public class Elective extends AbstractCourse implements Serializable {
     char original;


      protected Elective(char elective, char electiveNum, String courseNo, String courseName,
                         byte unit1, byte unit2, boolean selected, boolean canBeTaken){
          super((byte) elective ,(byte) electiveNum, courseNo, courseName, unit1, unit2, selected , canBeTaken);
          original = electiveNum;

      }



public void setSelected(boolean select){
          this.selected = select;
}
public void setcanBeTaken(boolean takeable){
          this.canBeTaken = takeable;
}
public boolean getSelected(){     // implementation needed
          return true;
}

    public boolean getcanBeTaken(){
        return true;
    }







}
