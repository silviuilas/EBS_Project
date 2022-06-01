package com.example.broker.subscriber;

import com.example.broker.pubsub.Subscription;
import com.google.gson.Gson;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.DeliverCallback;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

import static com.example.broker.helper.Constants.SUBSCRIBING_EXCHANGE_NAME;

public class SubscriberListener {
    Connection connection;
    Gson gson = new Gson();

    public SubscriberListener(Connection connection) {
        this.connection = connection;
    }

    public void listenTo(String routeKey) throws IOException, TimeoutException {
        try {
            Channel channel = connection.createChannel();
            channel.exchangeDeclare(SUBSCRIBING_EXCHANGE_NAME, "direct");
            String queueName = channel.queueDeclare().getQueue();
            channel.queueBind(queueName, SUBSCRIBING_EXCHANGE_NAME, routeKey);

            System.out.println(" [SL] Waiting for messages. To exit press CTRL+C");

            DeliverCallback deliverCallback = getDeliverCallback();
            channel.basicConsume(queueName, true, deliverCallback, consumerTag -> {
            });
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public DeliverCallback getDeliverCallback() {
        return (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), StandardCharsets.UTF_8);
            Subscription subscription = gson.fromJson(message, Subscription.class);
            System.out.println(" [SL] Receiving notification on subscriber " + subscription.getRouteKey());
        };
    }
}
