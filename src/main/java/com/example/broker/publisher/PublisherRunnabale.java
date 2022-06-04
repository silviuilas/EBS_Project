package com.example.broker.publisher;

import com.example.broker.pubsub.AtomicPublication;
import com.example.broker.pubsub.Publishing;
import com.google.gson.Gson;
import com.homework.generator.PublicationGeneratorAdapter;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeoutException;

import static com.example.broker.helper.Constants.PUBLISHING_EXCHANGE_NAME;

public class PublisherRunnabale implements Runnable {
    Gson gson = new Gson();
    PublicationGenerator publicationGenerator = new PublicationGeneratorAdapter();


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
            List<Publishing> publications = generatePublications(10);
            for(Publishing publishing : publications){
                String message = gson.toJson(publishing);

                channel.basicPublish(PUBLISHING_EXCHANGE_NAME, "", null, message.getBytes(StandardCharsets.UTF_8));
                System.out.println(" [P] Publisher Sent '" + message + "'");
            }

        } catch (IOException | TimeoutException e) {
            e.printStackTrace();
        }
    }

    private List<Publishing> generatePublications(int numberOfPublicationsToGenerate) {
        List<Publishing> publications = new ArrayList<>();
        List<List<AtomicPublication>> generatedPublications = publicationGenerator.getPublications(numberOfPublicationsToGenerate);
        for(List<AtomicPublication> atomicPublications : generatedPublications){
            publications.add(createPublication(atomicPublications));
        }
        return publications;
    }

    private Publishing createPublication(List<AtomicPublication> atomicPublications) {
        Publishing publication = new Publishing();
        for(AtomicPublication atomicPublication : atomicPublications) {
            publication.getAtomicPublications().add(atomicPublication);
        }
        return publication;
    }
}