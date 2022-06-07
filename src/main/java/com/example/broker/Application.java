package com.example.broker;

import com.example.broker.broker.BrokerRunnable;
import com.example.broker.helper.CustomPrintln;
import com.example.broker.publisher.PublisherRunnabale;
import com.example.broker.subscriber.SubscriberRunnable;

import java.math.BigInteger;
import java.util.Date;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.TimeUnit;

import static com.example.broker.helper.CustomLogger.*;


public class Application {

    public static final int NUMBER_OF_BROKERS = 2;
    public static final int NUMBER_OF_SUBSCRIBERS = 3;
    public static final int NUMBER_OF_PUBLISHERS = 2;

    public static void main(String[] argv) {
        CustomPrintln.print("Main thread is - " + Thread.currentThread().getName());

        long startTime = System.nanoTime();

        for (int i = 0; i < NUMBER_OF_BROKERS; i++) {
            Thread thread = new Thread(new BrokerRunnable());
            thread.start();
        }

        for (int i = 0; i < NUMBER_OF_SUBSCRIBERS; i++) {
            Thread thread = new Thread(new SubscriberRunnable());
            thread.start();
        }

        for (int i = 0; i < NUMBER_OF_PUBLISHERS; i++) {
            Thread thread = new Thread(new PublisherRunnabale());
            thread.start();
        }

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println(" [Shutting Down] In shutdown hook");
            System.out.println(" [Shutting Down] Nr of nrOfSubscriptionSent " + nrOfSubscriptionSent);
            System.out.println(" [Shutting Down] Nr of nrOfSubscriptionReceived " + nrOfSubscriptionReceived);
            System.out.println(" [Shutting Down] Nr of nrOfPublicationSent " + nrOfPublicationSent);
            System.out.println(" [Shutting Down] Nr of nrOfPublicationReceived " + (nrOfPublicationReceived.intValue() / NUMBER_OF_BROKERS));
            System.out.println(" [Shutting Down] Nr of nrOfMatchesDone " + nrOfMatchesDone);
            System.out.println(" [Shutting Down] Match rate percent is " + getMatchingRate() * 100 + "%");
            long endTime = System.nanoTime();
            long duration = (endTime - startTime);
            System.out.println(" [Shutting Down] Took " + getAverageLatency() + " milliseconds for a package to arrive");
            System.out.println(" [Shutting Down] Took " + duration / 1000000 / 1000 + " seconds");
        }, "Shutdown-thread"));
    }

    private static long getAverageLatency() {
        long secondsSum = 0;
        long counter = 0;
        for (Integer hash :
                subscriberReceivedTimeStampHashMap.keySet()) {
            Queue<Date> queue = subscriberReceivedTimeStampHashMap.get(hash);
            if (queue.size() > 0) {
                Date averageTime = getAverageTimeOnQueue(queue);
                long diff = averageTime.getTime() - publisherSentTimeStampHashMap.get(hash).getTime();//as given

                long seconds = TimeUnit.MILLISECONDS.toMillis(diff);
                secondsSum += seconds;
                counter += 1;
            }
        }
        return secondsSum / counter;
    }

    private static Date getAverageTimeOnQueue(Queue<Date> queue) {
        BigInteger total = BigInteger.ZERO;
        for (Date date : queue) {
            total = total.add(BigInteger.valueOf(date.getTime()));
        }
        BigInteger averageMillis = total.divide(BigInteger.valueOf(queue.size()));
        return new Date(averageMillis.longValue());
    }

    private static double getMatchingRate() {
        double matchRateSum = 0.0;
        for (Map.Entry<Integer, Integer> entry :
                matchesMade) {
            matchRateSum = matchRateSum + ((double) (entry.getValue()) / (double) entry.getKey());
        }
        return matchRateSum / matchesMade.size();
    }
}
