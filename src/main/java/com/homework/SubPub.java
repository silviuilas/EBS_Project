package com.homework;

public class SubPub {
    String name;
    Object val;

    public SubPub(String name, Object val) {
        this.name = name;
        this.val = val;
    }

    @Override
    public String toString() {
        return "('" + name + '\'' +
                ", " + val + ")";
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Object getVal() {
        return val;
    }

    public void setVal(Object val) {
        this.val = val;
    }
}
