package com.example.broker.broker;

import com.example.broker.helper.CustomLogger;
import com.example.broker.helper.CustomPrintln;
import com.example.broker.helper.GsonFactory;
import com.example.broker.pubsub.Publication;
import com.example.broker.pubsub.Subscription;
import com.google.gson.Gson;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeoutException;
import java.util.function.Predicate;

import static com.example.broker.helper.Constants.SUBSCRIBING_EXCHANGE_NAME;


public class SubscriptionManager {
    Set<Subscription> subscription = ConcurrentHashMap.newKeySet();
    Map<Subscription, Predicate<Publication>> subscriptionPredicateMap = new HashMap<>();
    Map<String, Channel> subscriptionCachedChannels = new HashMap<>();
    ConnectionFactory factory;
    Connection connection;
    Gson gson = GsonFactory.get();

    public SubscriptionManager(ConnectionFactory factory) {
        this.factory = factory;
    }

    public void addSubscription(Subscription subscription) {
        this.subscription.add(subscription);
        subscriptionPredicateMap.putIfAbsent(subscription, SubscriptionToPredicateTransformer.transform(subscription));
    }

    public void notifySubscribers(Publication publication) {
        int counter = 0;
        for (Subscription subscription : this.subscription) {
            if (subscriptionPredicateMap.getOrDefault(subscription, (publication1) -> false).test(publication)) {
                try {
                    notifySubscriber(subscription, publication);
                    counter += 1;
                } catch (IOException | TimeoutException e) {
                    e.printStackTrace();
                }
            }
        }
        CustomLogger.nrOfMatchesDone.addAndGet(counter);
        if (counter != 0) {
            CustomLogger.matchesMade.add(Map.entry(this.subscription.size(), counter));
        }
        CustomPrintln.print(" [B] Notifying " + counter + " subscribers");
    }

    Channel getChannel(String routeKey) throws IOException, TimeoutException {
        Channel cachedChannel = subscriptionCachedChannels.get(routeKey);
        if (cachedChannel != null && cachedChannel.isOpen()) {
            return cachedChannel;
        }

        if (connection == null) {
            connection = factory.newConnection();
            return getChannel(routeKey);
        }
        Channel channel = connection.createChannel();
        if (channel == null) {
            connection = factory.newConnection();
            return getChannel(routeKey);
        }
        subscriptionCachedChannels.put(routeKey, channel);
        channel.exchangeDeclare(SUBSCRIBING_EXCHANGE_NAME, "direct");
        return channel;
    }

    public void notifySubscriber(Subscription subscription, Publication publication) throws IOException, TimeoutException {
        Channel channel = getChannel(subscription.getRouteKey());
        channel.basicPublish(SUBSCRIBING_EXCHANGE_NAME, subscription.getRouteKey(), null, gson.toJson(publication).getBytes(StandardCharsets.UTF_8));
    }
}

