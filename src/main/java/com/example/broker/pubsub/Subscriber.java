package com.example.broker.pubsub;

import com.example.broker.pubsub.AtomicSubscription;

import java.util.ArrayList;
import java.util.List;

public class Subscriber {
    String subscriberQueueId;
    List<AtomicSubscription> atomicSubscriptions = new ArrayList<>();

    public Subscriber(String subscriberQueueId) {
        this.subscriberQueueId = subscriberQueueId;
    }

    public String getSubscriberQueueId() {
        return subscriberQueueId;
    }

    public void setSubscriberQueueId(String subscriberQueueId) {
        this.subscriberQueueId = subscriberQueueId;
    }

    public List<AtomicSubscription> getAtomicSubscriptions() {
        return atomicSubscriptions;
    }

    public void setAtomicSubscriptions(List<AtomicSubscription> atomicSubscriptions) {
        this.atomicSubscriptions = atomicSubscriptions;
    }
}
