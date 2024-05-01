package prog2.fingrp;

import java.io.Serializable;

@FunctionalInterface
public interface Search extends Serializable {
    int find(String s1);
}

