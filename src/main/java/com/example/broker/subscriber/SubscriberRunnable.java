package com.example.broker.subscriber;

import com.example.broker.helper.CustomLogger;
import com.example.broker.helper.RandomString;
import com.example.broker.pubsub.AtomicSubscription;
import com.example.broker.pubsub.Subscription;
import com.homework.generator.SubscriptionGeneratorAdapter;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeoutException;

import static com.example.broker.helper.Constants.SUBSCRIBING_EXCHANGE_NAME;

public class SubscriberRunnable implements Runnable {

    RandomString gen = new RandomString(8, ThreadLocalRandom.current());
    SubscriptionGenerator subscriptionGenerator = new SubscriptionGeneratorAdapter();
    String name;

    public static void main(String[] args) {
        for (int i = 0; i < 3; i++) {
            Thread thread = new Thread(new SubscriberRunnable());
            thread.start();
        }
    }

    @Override
    public void run() {
        name = gen.nextString();
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        try {
            Connection connection = factory.newConnection();
            Channel channel = connection.createChannel();
            channel.exchangeDeclare(SUBSCRIBING_EXCHANGE_NAME, "direct");

            SubscriberListener subscriberListener = new SubscriberListener(connection);
            subscriberListener.listenTo(name);

            List<Subscription> subscriptions = generateSubscriptions(name, 3334);
            int nr = 0;
            for (Subscription subscription : subscriptions) {
                SubscriberSender subscriberSender = new SubscriberSender(connection);
                subscriberSender.subscribe(subscription, nr, channel);
                Thread.sleep(54);
                CustomLogger.nrOfSubscriptionSent.addAndGet(1);
                nr += 1;
            }
        } catch (IOException | TimeoutException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    private List<Subscription> generateSubscriptions(String routeKey, int numberOfSubscriptionsToGenerate) {
        List<Subscription> subscriptions = new ArrayList<>();
        List<HashMap<String, AtomicSubscription>> generatedSubscriptions = subscriptionGenerator.getSubscriptions(numberOfSubscriptionsToGenerate);
        for (HashMap<String, AtomicSubscription> atomicSubscriptions : generatedSubscriptions) {
            subscriptions.add(createSubscriptionWithRouteKey(routeKey, atomicSubscriptions));
        }
        return subscriptions;
    }

    private Subscription createSubscriptionWithRouteKey(String routeKey, HashMap<String, AtomicSubscription> atomicSubscriptions) {
        Subscription subscription = new Subscription(routeKey);
        subscription.setAtomicSubscriptions(atomicSubscriptions);
        return subscription;
    }
}