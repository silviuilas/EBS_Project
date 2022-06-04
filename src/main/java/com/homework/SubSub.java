package com.homework;

public class SubSub {
    String name;
    String op;
    Object val;


    public SubSub(String name, Object val) {
        this.name = name;
        this.val = val;
    }

    @Override
    public String toString() {
        return "(" + this.name + "," + this.op + "," + this.val + ")";
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOp() {
        return op;
    }

    public void setOp(String op) {
        this.op = op;
    }

    public Object getVal() {
        return val;
    }

    public void setVal(Object val) {
        this.val = val;
    }
}
