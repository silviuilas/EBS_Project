package com.example.broker.publisher;

import com.example.broker.pubsub.AtomicPublication;
import com.example.broker.pubsub.Publishing;
import com.google.gson.Gson;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

import static com.example.broker.helper.Constants.PUBLISHING_EXCHANGE_NAME;

public class PublisherRunnabale implements Runnable {
    Gson gson = new Gson();

    public static void main(String[] args) {
        Thread receiveLogsThread = new Thread(new PublisherRunnabale());
        receiveLogsThread.start();
    }


    @Override
    public void run() {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        try (Connection connection = factory.newConnection();
             Channel channel = connection.createChannel()) {
            channel.exchangeDeclare(PUBLISHING_EXCHANGE_NAME, "fanout");
            Publishing publishing = new Publishing();
            publishing.getAtomicPublications().add(new AtomicPublication("price", 4));
            String message = gson.toJson(publishing);

            channel.basicPublish(PUBLISHING_EXCHANGE_NAME, "", null, message.getBytes(StandardCharsets.UTF_8));
            System.out.println(" [P] Publisher Sent '" + message + "'");
        } catch (IOException | TimeoutException e) {
            e.printStackTrace();
        }
    }
}