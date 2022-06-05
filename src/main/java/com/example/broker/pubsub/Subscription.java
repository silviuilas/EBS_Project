package com.example.broker.pubsub;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class Subscription {
    String routeKey;
    HashMap<String, AtomicSubscription> atomicSubscriptions = new HashMap<>();

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

    public HashMap<String, AtomicSubscription> getAtomicSubscriptions() {
        return atomicSubscriptions;
    }

    public void setAtomicSubscriptions(HashMap<String, AtomicSubscription> atomicSubscriptions) {
        this.atomicSubscriptions = atomicSubscriptions;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Subscription that = (Subscription) o;
        return Objects.equals(routeKey, that.routeKey) && Objects.equals(atomicSubscriptions, that.atomicSubscriptions);
    }

    @Override
    public int hashCode() {
        return Objects.hash(routeKey, atomicSubscriptions);
    }
}
