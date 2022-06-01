package com.example.broker.brokers;

import com.example.broker.SubscriptionManager;
import com.example.broker.pubsub.Publishing;
import com.example.broker.pubsub.Subscriber;
import com.google.gson.Gson;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.DeliverCallback;

import java.io.IOException;

import static com.example.broker.Constants.*;
import static com.example.broker.Constants.PUBLISHING_EXCHANGE_NAME;

public class PublisherBroker {
    Connection connection;
    SubscriptionManager subscriptionManager;
    private final Gson gson = new Gson();


    public PublisherBroker(Connection connection, SubscriptionManager subscriptionManager) {
        this.connection = connection;
        this.subscriptionManager = subscriptionManager;
    }

    public void run() throws IOException {
        init(connection);
    }

    private void init(Connection connection) throws IOException {
        Channel channel = connection.createChannel();

        channel.exchangeDeclare(PUBLISHING_EXCHANGE_NAME, "fanout");
        String queueName = channel.queueDeclare().getQueue();
        channel.queueBind(queueName, PUBLISHING_EXCHANGE_NAME, "");

        System.out.println(" [*] Waiting for messages. To exit press CTRL+C");

        DeliverCallback deliverCallback = getPublisherDeliverCallback();
        channel.basicConsume(queueName, true, deliverCallback, consumerTag -> { });
    }

    private DeliverCallback getPublisherDeliverCallback() {
        return (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), "UTF-8");
            Publishing publishing = gson.fromJson(message, Publishing.class);

            System.out.println(" [x] Publisher Broker Received '" + publishing + "'");

            subscriptionManager.notifySubscribers(publishing);
        };
    }

}
