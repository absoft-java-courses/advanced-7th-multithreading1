package org.cource.advanced.multithreading1.beans;

import java.lang.reflect.Array;
import java.util.List;

public class ObjectExample {
    private volatile List<String> filed1;


    public List<String> getFiled1() {
        return filed1;
    }

    public void setFiled1(List<String> filed1) {
        this.filed1 = filed1;
    }
}
