package com.example.broker.broker;

import com.example.broker.helper.CustomLogger;
import com.example.broker.pubsub.Publication;
import com.google.gson.Gson;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.DeliverCallback;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static com.example.broker.helper.Constants.PUBLISHING_EXCHANGE_NAME;

public class PublisherBrokerListener {
    private final Gson gson = new Gson();
    Connection connection;
    SubscriptionManager subscriptionManager;


    public PublisherBrokerListener(Connection connection, SubscriptionManager subscriptionManager) {
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

        System.out.println(" [B] Waiting for messages from publisher. To exit press CTRL+C");

        DeliverCallback deliverCallback = getPublisherDeliverCallback();
        channel.basicConsume(queueName, true, deliverCallback, consumerTag -> {
        });
    }

    private DeliverCallback getPublisherDeliverCallback() {
        return (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), StandardCharsets.UTF_8);
            Publication publication = gson.fromJson(message, Publication.class);

            System.out.println(" [B] Broker Received '" + publication + "'");

            subscriptionManager.notifySubscribers(publication);
            CustomLogger.nrOfPublicationReceived.addAndGet(1);
        };
    }

}
