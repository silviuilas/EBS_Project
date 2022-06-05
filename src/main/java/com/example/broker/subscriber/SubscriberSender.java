package com.example.broker.subscriber;

import com.example.broker.pubsub.Subscription;
import com.google.gson.Gson;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static com.example.broker.helper.Constants.SUBSCRIBING_EXCHANGE_NAME;
import static com.example.broker.helper.Constants.SUBSCRIBING_ROUTE_KEY;

public class SubscriberSender {
    Connection connection;
    Gson gson = new Gson();

    public SubscriberSender(Connection connection) {
        this.connection = connection;
    }

    public Subscription subscribe(Subscription subscription, int nr) throws IOException {
        Channel channel = connection.createChannel();
        channel.exchangeDeclare(SUBSCRIBING_EXCHANGE_NAME, "direct");
        String message = gson.toJson(subscription);

        channel.basicPublish(SUBSCRIBING_EXCHANGE_NAME, SUBSCRIBING_ROUTE_KEY, null, message.getBytes(StandardCharsets.UTF_8));
        System.out.println(" [SS] Subscription Sent '" + message + "' number " + nr);

        return subscription;
    }
}
