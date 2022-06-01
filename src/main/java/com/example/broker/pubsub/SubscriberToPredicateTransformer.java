package com.example.broker.pubsub;

import java.util.Objects;
import java.util.function.Predicate;

public class SubscriberToPredicateTransformer {

    public static Predicate<Publishing> transform(Subscriber subscriber) {
        return publishing -> {
            for (AtomicSubscription atomicSubscription :
                    subscriber.atomicSubscriptions) {
                for ( AtomicPublication atomicPublication :
                        publishing.atomicPublications ) {
                    if(Objects.equals(atomicSubscription.name, atomicPublication.name)){
                        // TODO take in to account the operator and value
                        return true;
                    }
                }
            }
            return false;
        };
    }
}
