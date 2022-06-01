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
}
