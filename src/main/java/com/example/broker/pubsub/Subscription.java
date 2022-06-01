package com.example.broker.pubsub;

import java.util.ArrayList;
import java.util.List;

public class Subscription {
    String routeKey;
    List<AtomicSubscription> atomicSubscriptions = new ArrayList<>();

    @Override
    public String toString() {
        return "Subscription{" +
                "routeKey='" + routeKey + '\'' +
                ", atomicSubscriptions=" + atomicSubscriptions +
                '}';
    }

    public Subscription(String routeKey) {
        this.routeKey = routeKey;
    }

    public String getRouteKey() {
        return routeKey;
    }

    public void setRouteKey(String routeKey) {
        this.routeKey = routeKey;
    }

    public List<AtomicSubscription> getAtomicSubscriptions() {
        return atomicSubscriptions;
    }

    public void setAtomicSubscriptions(List<AtomicSubscription> atomicSubscriptions) {
        this.atomicSubscriptions = atomicSubscriptions;
    }
}
