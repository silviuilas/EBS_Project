package com.example.broker.runners;

import com.example.broker.Constants;
import com.example.broker.pubsub.AtomicPublication;
import com.example.broker.pubsub.Publishing;
import com.google.gson.Gson;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import static com.example.broker.Constants.PUBLISHING_EXCHANGE_NAME;

public class PublisherRunnabale implements Runnable {
    Gson gson = new Gson();

    @Override
    public void run() {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        try (Connection connection = factory.newConnection();
             Channel channel = connection.createChannel()) {
            channel.exchangeDeclare(PUBLISHING_EXCHANGE_NAME, "fanout");
            Publishing publishing = new Publishing();
            publishing.getAtomicPublications().add(new AtomicPublication("company", "google"));
            String message = gson.toJson(publishing);

            channel.basicPublish(PUBLISHING_EXCHANGE_NAME, "", null, message.getBytes("UTF-8"));
            System.out.println(" [x] Publisher Sent '" + message + "'");
        } catch (IOException | TimeoutException e) {
            e.printStackTrace();
        }
    }
}