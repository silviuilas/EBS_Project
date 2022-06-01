package com.example.broker.broker;

import com.example.broker.pubsub.AtomicPublication;
import com.example.broker.pubsub.AtomicSubscription;
import com.example.broker.pubsub.Publishing;
import com.example.broker.pubsub.Subscription;

import java.util.Objects;
import java.util.function.Predicate;

public class SubscriptionToPredicateTransformer {

    public static Predicate<Publishing> transform(Subscription subscription) {
        return publishing -> {
            for (AtomicSubscription atomicSubscription :
                    subscription.getAtomicSubscriptions()) {
                for (AtomicPublication atomicPublication :
                        publishing.getAtomicPublications()) {
                    if (Objects.equals(atomicSubscription.getName(), atomicPublication.getName())) {
                        // TODO take in to account the operator and value
                        return true;
                    }
                }
            }
            return false;
        };
    }
}
