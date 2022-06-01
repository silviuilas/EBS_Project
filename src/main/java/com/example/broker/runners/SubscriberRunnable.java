package com.example.broker.runners;

import com.example.broker.Constants;
import com.example.broker.pubsub.AtomicPublication;
import com.example.broker.pubsub.AtomicSubscription;
import com.example.broker.pubsub.Publishing;
import com.example.broker.pubsub.Subscriber;
import com.google.gson.Gson;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import static com.example.broker.Constants.*;

public class SubscriberRunnable implements Runnable {


    Gson gson = new Gson();

    @Override
    public void run() {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        try (Connection connection = factory.newConnection()) {
            Subscriber subscriber = subscribeToBroker(connection);
            listenToSubscriber(connection, subscriber);
        } catch (IOException | TimeoutException e) {
            e.printStackTrace();
        }
    }

    private Subscriber subscribeToBroker(Connection connection) throws IOException {
        Channel channel = connection.createChannel();
        channel.exchangeDeclare(SUBSCRIBING_EXCHANGE_NAME, "direct");
        String queueName = channel.queueDeclare().getQueue();
        Subscriber subscriber = new Subscriber(queueName);
        subscriber.getAtomicSubscriptions().add(new AtomicSubscription("company", "=", "facebook"));
        String message = gson.toJson(subscriber);

        channel.basicPublish(SUBSCRIBING_EXCHANGE_NAME, SUBSCRIBING_ROUTE_KEY, null, message.getBytes("UTF-8"));
        System.out.println(" [x] Subscriber Sent '" + message + "'");

        return subscriber;
    }

    private void listenToSubscriber(Connection connection, Subscriber subscriber) throws IOException {
        Channel channel = connection.createChannel();
        channel.exchangeDeclare(SUBSCRIBING_EXCHANGE_NAME, "direct");
        String queueName = channel.queueDeclare().getQueue();
        channel.queueBind(queueName, SUBSCRIBING_EXCHANGE_NAME, "someId");

        System.out.println(" [**] Subscriber waiting for messages on id " + subscriber.getSubscriberQueueId());

        DeliverCallback deliverCallback = getDeliverCallback();
        channel.basicConsume(queueName, true, deliverCallback, consumerTag -> { });
    }

    private DeliverCallback getDeliverCallback() {
        return (consumerTag, delivery) -> {
            System.out.println("Receiving notification WOWWWWWWW");
        };
    }
}