package com.example.broker.broker;

import com.example.broker.pubsub.AtomicPublication;
import com.example.broker.pubsub.AtomicSubscription;
import com.example.broker.pubsub.Publication;
import com.example.broker.pubsub.Subscription;

import java.util.Date;
import java.util.Objects;
import java.util.function.Predicate;

public class SubscriptionToPredicateTransformer {

    public static Predicate<Publication> transform(Subscription subscription) {
        return publication -> {
            for (AtomicPublication atomicPublication :
                    publication.getAtomicPublications()) {
                AtomicSubscription atomicSubscription = subscription.getAtomicSubscriptions().get(atomicPublication.getName());
                if (atomicSubscription != null) {
                    if (atomicPublication.getVal() instanceof String && atomicSubscription.getVal() instanceof String)
                        return compareString((String) atomicSubscription.getVal(), atomicSubscription.getOp(), (String) atomicPublication.getVal());
                    else if (atomicPublication.getVal() instanceof Double && atomicSubscription.getVal() instanceof Double)
                        return compareIntegers((Double) atomicPublication.getVal(), atomicSubscription.getOp(), (Double) atomicSubscription.getVal());
                    else if (atomicPublication.getVal() instanceof Date && atomicSubscription.getVal() instanceof Date)
                        return compareDates((Date) atomicPublication.getVal(), atomicSubscription.getOp(), (Date) atomicSubscription.getVal());
                    else
                        throw new RuntimeException("Unsupported comparison");
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

    private static boolean compareDates(Date compare, String op, Date compareTo) {
        return switch (op) {
            case "=" -> Objects.equals(compare, compareTo);
            case ">" -> compare.before(compareTo);
            case ">=" -> compare.before(compareTo) || compare.equals(compareTo);
            case "<" -> compare.after(compareTo);
            case "<=" -> compare.after(compareTo) || compare.equals(compareTo);
            default -> throw new RuntimeException("Undefined operator for comparison between Integer and Integer");
        };
    }
}
