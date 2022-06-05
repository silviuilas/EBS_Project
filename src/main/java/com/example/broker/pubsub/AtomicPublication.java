package com.example.broker.pubsub;

import java.io.Serializable;
import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AtomicPublication that = (AtomicPublication) o;
        return Objects.equals(name, that.name) && Objects.equals(val, that.val);
    }

    @Override
    public int hashCode() {
        if(Objects.equals(name, "Date"))
            return 0;
        return Objects.hash(name, val);
    }
}
