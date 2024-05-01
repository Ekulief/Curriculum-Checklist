package prog2.fingrp;

import java.io.Serializable;

@FunctionalInterface
public interface CourseProperty<T> extends Serializable {

    T getCourse(AbstractCourse course);

}
