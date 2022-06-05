package com.example.broker.pubsub;

public class AtomicSubscription {
    String op;
    Object val;


    public AtomicSubscription(String op, Object val) {
        this.op = op;
        this.val = val;
    }

    @Override
    public String toString() {
        return "AtomicSubscription{" +
                ", op='" + op + '\'' +
                ", val=" + val +
                '}';
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
