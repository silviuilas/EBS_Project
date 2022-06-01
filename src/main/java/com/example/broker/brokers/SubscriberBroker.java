package com.example.broker.brokers;

import com.example.broker.SubscriptionManager;
import com.example.broker.pubsub.Subscriber;
import com.google.gson.Gson;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.DeliverCallback;

import java.io.IOException;

import static com.example.broker.Constants.SUBSCRIBING_EXCHANGE_NAME;
import static com.example.broker.Constants.SUBSCRIBING_ROUTE_KEY;

public class SubscriberBroker {
    Connection connection;
    SubscriptionManager subscriptionManager;
    private final Gson gson = new Gson();


    public SubscriberBroker(Connection connection, SubscriptionManager subscriptionManager) {
        this.connection = connection;
        this.subscriptionManager = subscriptionManager;
    }

    public void run() throws IOException {
        init(connection);
    }

    private void init(Connection connection) throws IOException {
        Channel channel = connection.createChannel();

        channel.exchangeDeclare(SUBSCRIBING_EXCHANGE_NAME, "direct");
        String queueName = channel.queueDeclare().getQueue();
        channel.queueBind(queueName, SUBSCRIBING_EXCHANGE_NAME, SUBSCRIBING_ROUTE_KEY);

        System.out.println(" [*] Waiting for messages. To exit press CTRL+C");

        DeliverCallback deliverCallback = getSubscriberDeliverCallback();
        channel.basicConsume(queueName, true, deliverCallback, consumerTag -> { });
    }

    private DeliverCallback getSubscriberDeliverCallback() {
        return (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), "UTF-8");
            Subscriber subscriber = gson.fromJson(message, Subscriber.class);

            System.out.println(" [x] Subscriber Broker Received '" + subscriber + "'");

            subscriptionManager.addSubscription(subscriber);
        };
    }

}
