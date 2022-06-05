package com.example.broker.broker;

import com.example.broker.pubsub.Publication;
import com.example.broker.pubsub.Subscription;
import com.google.gson.Gson;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeoutException;
import java.util.function.Predicate;

import static com.example.broker.helper.Constants.SUBSCRIBING_EXCHANGE_NAME;


public class SubscriptionManager {
    List<Subscription> subscription = new ArrayList<>();
    Map<Subscription, Predicate<Publication>> subscriptionPredicateMap = new HashMap<>();
    Gson gson = new Gson();

    public void addSubscription(Subscription subscription) {
        this.subscription.add(subscription);
        subscriptionPredicateMap.putIfAbsent(subscription, SubscriptionToPredicateTransformer.transform(subscription));
    }

    public void notifySubscribers(Publication publication) {
        for (Subscription subscription : new ArrayList<>(this.subscription)) {
            if (subscriptionPredicateMap.get(subscription) != null && subscriptionPredicateMap.get(subscription).test(publication)) {
                try {
                    notifySubscriber(subscription);
                } catch (IOException | TimeoutException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void notifySubscriber(Subscription subscription) throws IOException, TimeoutException {
        System.out.println(" [B] Broker Notifying " + subscription.getRouteKey());
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");

        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();
        channel.exchangeDeclare(SUBSCRIBING_EXCHANGE_NAME, "direct");

        channel.basicPublish(SUBSCRIBING_EXCHANGE_NAME, subscription.getRouteKey(), null, gson.toJson(subscription).getBytes(StandardCharsets.UTF_8));
    }
}

