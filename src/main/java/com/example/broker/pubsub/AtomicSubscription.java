package com.example.broker.pubsub;

public class AtomicSubscription {
    String name;
    String op;
    Object val;


    public AtomicSubscription(String name, String op, Object val) {
        this.name = name;
        this.op = op;
        this.val = val;
    }

    @Override
    public String toString() {
        return "AtomicSubscription{" +
                "name='" + name + '\'' +
                ", op='" + op + '\'' +
                ", val=" + val +
                '}';
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
