package com.example.broker.subscriber;

import com.example.broker.pubsub.AtomicSubscription;

import java.util.HashMap;
import java.util.List;

public interface SubscriptionGenerator {

    List<HashMap<String, AtomicSubscription>> getSubscriptions(int numberOfSubscriptions);

}
