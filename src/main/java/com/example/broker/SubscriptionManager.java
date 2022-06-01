package com.example.broker;

import com.example.broker.pubsub.AtomicSubscription;
import com.example.broker.pubsub.Publishing;
import com.example.broker.pubsub.Subscriber;
import com.example.broker.pubsub.SubscriberToPredicateTransformer;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeoutException;
import java.util.function.Predicate;

import static com.example.broker.Constants.SUBSCRIBING_EXCHANGE_NAME;
import static com.example.broker.Constants.SUBSCRIBING_ROUTE_KEY;


public class SubscriptionManager {
    List<Subscriber> subscribers = new ArrayList<>();
    Map<Subscriber, Predicate<Publishing>> subscriberPredicateMap= new HashMap<>();

    public void addSubscription(Subscriber subscriber){
        subscribers.add(subscriber);
        subscriberPredicateMap.putIfAbsent(subscriber, SubscriberToPredicateTransformer.transform(subscriber));

    }

    public void notifySubscribers(Publishing publishing) {
        // TODO maybe try catch?
        for(Subscriber subscriber : subscribers){
            if(subscriberPredicateMap.get(subscriber).test(publishing)){
                try {
                    notifySubscriber(subscriber);
                } catch (IOException | TimeoutException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void notifySubscriber(Subscriber subscriber) throws IOException, TimeoutException {
        System.out.println("Notifying " + subscriber.getSubscriberQueueId());
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");

        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();
        channel.exchangeDeclare(SUBSCRIBING_EXCHANGE_NAME, "direct");

        channel.basicPublish(SUBSCRIBING_EXCHANGE_NAME, "someId", null, "Sending something".getBytes(StandardCharsets.UTF_8));
    }
}

