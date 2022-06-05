package com.example.broker.helper;

import java.util.Date;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class CustomLogger {
    public static AtomicInteger nrOfSubscriptionSent = new AtomicInteger(0);
    public static AtomicInteger nrOfSubscriptionReceived = new AtomicInteger(0);

    public static AtomicInteger nrOfPublicationSent = new AtomicInteger(0);
    public static AtomicInteger nrOfPublicationReceived = new AtomicInteger(0);

    public static AtomicInteger nrOfMatchesDone = new AtomicInteger(0);

    public static ConcurrentHashMap<String, Date> publisherSentTimeStampHashMap = new ConcurrentHashMap();
    public static ConcurrentHashMap<String, List<Date>> subscriberReceivedTimeStampHashMap = new ConcurrentHashMap();
}
