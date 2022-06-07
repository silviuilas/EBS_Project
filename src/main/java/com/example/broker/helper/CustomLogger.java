package com.example.broker.helper;

import java.util.Date;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.atomic.AtomicInteger;

public class CustomLogger {
    public static AtomicInteger nrOfSubscriptionSent = new AtomicInteger(0);
    public static AtomicInteger nrOfSubscriptionReceived = new AtomicInteger(0);

    public static AtomicInteger nrOfPublicationSent = new AtomicInteger(0);
    public static AtomicInteger nrOfPublicationReceived = new AtomicInteger(0);

    public static AtomicInteger nrOfMatchesDone = new AtomicInteger(0);

    public static ConcurrentHashMap<Integer, Date> publisherSentTimeStampHashMap = new ConcurrentHashMap<>();
    public static ConcurrentHashMap<Integer, Queue<Date>> subscriberReceivedTimeStampHashMap = new ConcurrentHashMap<>();

    public static Queue<Map.Entry<Integer, Integer>> matchesMade = new LinkedBlockingDeque<>();
}
