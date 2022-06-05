package com.example.broker.broker;

import com.example.broker.pubsub.AtomicPublication;
import com.example.broker.pubsub.AtomicSubscription;
import com.example.broker.pubsub.Publication;
import com.example.broker.pubsub.Subscription;

import java.util.Objects;
import java.util.function.Predicate;

public class SubscriptionToPredicateTransformer {

    public static Predicate<Publication> transform(Subscription subscription) {
        return publication -> {
            for (AtomicSubscription atomicSubscription :
                    subscription.getAtomicSubscriptions()) {
                for (AtomicPublication atomicPublication :
                        publication.getAtomicPublications()) {
                    if (Objects.equals(atomicSubscription.getName(), atomicPublication.getName())) {
                        if(atomicPublication.getVal() instanceof String && atomicSubscription.getVal() instanceof String)
                            return compareString((String) atomicSubscription.getVal(), atomicSubscription.getOp(), (String) atomicPublication.getVal());
                        else if(atomicPublication.getVal() instanceof Double && atomicSubscription.getVal() instanceof Double)
                            return compareIntegers((Double) atomicPublication.getVal(), atomicSubscription.getOp(), (Double) atomicSubscription.getVal());
                        else
                            throw new RuntimeException("Unsupported comparison");
                    }
                }
            }
            return false;
        };
    }

    @SuppressWarnings("SwitchStatementWithTooFewBranches")
    private static boolean compareString(String compare, String op, String compareTo) {
        return switch (op) {
            case "=" -> Objects.equals(compare, compareTo);
            default -> throw new RuntimeException("Undefined operator for comparison between String and String");
        };
    }

    private static boolean compareIntegers(Double compare, String op, Double compareTo) {
        return switch (op) {
            case "=" -> Objects.equals(compare, compareTo);
            case ">" -> compare > compareTo;
            case ">=" -> compare >= compareTo;
            case "<" -> compare < compareTo;
            case "<=" -> compare <= compareTo;
            default -> throw new RuntimeException("Undefined operator for comparison between Integer and Integer");
        };
    }
}
