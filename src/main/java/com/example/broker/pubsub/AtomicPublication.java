package com.example.broker.pubsub;

import java.io.Serializable;

public class AtomicPublication implements Serializable {
    String name;
    Object val;

    public AtomicPublication(String name, Object val) {
        this.name = name;
        this.val = val;
    }

    @Override
    public String toString() {
        return "('" + name + '\'' +
                ", " + val + ")";
    }
}
