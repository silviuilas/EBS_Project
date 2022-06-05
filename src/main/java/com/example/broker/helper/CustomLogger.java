package com.example.broker.helper;

import java.util.concurrent.atomic.AtomicInteger;

public class CustomLogger {
    public static AtomicInteger nrOfSubscriptionSent = new AtomicInteger(0);
    public static AtomicInteger nrOfSubscriptionReceived = new AtomicInteger(0);

    public static AtomicInteger nrOfPublicationSent = new AtomicInteger(0);
    public static AtomicInteger nrOfPublicationReceived = new AtomicInteger(0);

    public static AtomicInteger nrOfMatchesDone = new AtomicInteger(0);
}
