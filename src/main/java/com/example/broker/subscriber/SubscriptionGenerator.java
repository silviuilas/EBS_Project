package com.example.broker.subscriber;

import com.example.broker.pubsub.AtomicSubscription;

import java.util.List;

public interface SubscriptionGenerator {

    List<List<AtomicSubscription>> getSubscriptions(int numberOfSubscriptions);

}
