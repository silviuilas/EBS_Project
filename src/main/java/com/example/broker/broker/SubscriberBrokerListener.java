package com.example.broker.broker;

import com.example.broker.helper.*;
import com.example.broker.pubsub.AtomicSubscription;
import com.example.broker.pubsub.Subscription;
import com.google.gson.Gson;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.DeliverCallback;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Optional;

import static com.example.broker.helper.Constants.*;

public class SubscriberBrokerListener {
    private final Gson gson = GsonFactory.get();
    Connection connection;
    SubscriptionManager subscriptionManager;
    DateParser dateParserUsingDateFormat = new DateParserUsingDateFormat("yyyy-MM-dd'T'HH:mm:ssz");

    public SubscriberBrokerListener(Connection connection, SubscriptionManager subscriptionManager) {
        this.connection = connection;
        this.subscriptionManager = subscriptionManager;
    }

    public void run() throws IOException {
        init(connection);
    }

    private void init(Connection connection) throws IOException {
        Channel channel = connection.createChannel();

        channel.exchangeDeclare(SUBSCRIBING_EXCHANGE_NAME, "direct");
        String queueName = channel.queueDeclare(SUBSCRIBING_QUEUE_NAME, true, false, false, null).getQueue();
        channel.queueBind(queueName, SUBSCRIBING_EXCHANGE_NAME, SUBSCRIBING_ROUTE_KEY);

        CustomPrintln.print(" [B] Waiting for messages from subscriber. To exit press CTRL+C");

        DeliverCallback deliverCallback = getSubscriberDeliverCallback();
        channel.basicConsume(queueName, true, deliverCallback, consumerTag -> {
        });
    }

    private DeliverCallback getSubscriberDeliverCallback() {
        return (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), StandardCharsets.UTF_8);
            Subscription subscription = gson.fromJson(message, Subscription.class);
            extractDateOnNecessaryFields(subscription);

            CustomPrintln.print(" [B] Broker Received '" + subscription + "'");

            subscriptionManager.addSubscription(subscription);
            CustomLogger.nrOfSubscriptionReceived.addAndGet(1);
        };
    }

    private void extractDateOnNecessaryFields(Subscription subscription) {
        HashMap<String, AtomicSubscription> atomicSubscriptions = subscription.getAtomicSubscriptions();
        for(String key: atomicSubscriptions.keySet()){
            AtomicSubscription atomicSubscription = atomicSubscriptions.get(key);
            Object val = atomicSubscription.getVal();
            if(val instanceof String) {
                Optional<Date> dateOptional = dateParserUsingDateFormat.parse((String) val);
                dateOptional.ifPresent(atomicSubscription::setVal);
            }
        }
    }

}
