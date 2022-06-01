package com.example.broker.pubsub;

import java.io.Serializable;

public class AtomicPublication implements Serializable {
    String name;
    Object val;

    public AtomicPublication(String name, Object val) {
        this.name = name;
        this.val = val;
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

    @Override
    public String toString() {
        return "('" + name + '\'' +
                ", " + val + ")";
    }
}
