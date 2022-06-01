package com.example.broker;

import com.example.broker.broker.BrokerRunnable;
import com.example.broker.publisher.PublisherRunnabale;
import com.example.broker.subscriber.SubscriberRunnable;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Application {
    public static void main(String[] argv) {
        System.out.println("Main thread is - " + Thread.currentThread().getName());

        Thread receiveLogsThread = new Thread(new BrokerRunnable());
        receiveLogsThread.start();

        ScheduledExecutorService receiverExecutor = Executors.newScheduledThreadPool(1);

        receiverExecutor.scheduleAtFixedRate(new SubscriberRunnable(), 0, 5, TimeUnit.SECONDS);
        receiverExecutor.scheduleAtFixedRate(new PublisherRunnabale(), 0, 3, TimeUnit.SECONDS);
    }
}
