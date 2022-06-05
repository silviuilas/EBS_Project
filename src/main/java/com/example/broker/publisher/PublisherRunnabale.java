package com.example.broker.publisher;

import com.example.broker.helper.CustomLogger;
import com.example.broker.helper.CustomPrintln;
import com.example.broker.pubsub.AtomicPublication;
import com.example.broker.pubsub.Publication;
import com.google.gson.Gson;
import com.homework.generator.PublicationGeneratorAdapter;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.TimeoutException;

import static com.example.broker.helper.Constants.PUBLISHING_EXCHANGE_NAME;

public class PublisherRunnabale implements Runnable {
    Gson gson = new Gson();
    PublicationGenerator publicationGenerator = new PublicationGeneratorAdapter();


    public static void main(String[] args) {
        for (int i = 0; i < 2; i++) {
            Thread thread = new Thread(new PublisherRunnabale());
            thread.start();
        }
    }


    @Override
    public void run() {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        try (Connection connection = factory.newConnection();
             Channel channel = connection.createChannel()) {
            channel.exchangeDeclare(PUBLISHING_EXCHANGE_NAME, "fanout");
            List<Publication> publications = generatePublications(5000);
            int nr = 0;
            for (Publication publication : publications) {
                CustomLogger.publisherSentTimeStampHashMap.put(publication.hashCode(), new Date());
                CustomLogger.subscriberReceivedTimeStampHashMap.put(publication.hashCode(), new LinkedBlockingDeque<>());
                sendPublication(channel, publication, nr);
                Thread.sleep(36);
                nr += 1;
                CustomLogger.nrOfPublicationSent.addAndGet(1);
            }

        } catch (IOException | TimeoutException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void sendPublication(Channel channel, Publication publication, int number) throws IOException {
        String message = gson.toJson(publication);
        channel.basicPublish(PUBLISHING_EXCHANGE_NAME, "", null, message.getBytes(StandardCharsets.UTF_8));
        CustomPrintln.print(" [P] Publisher Sent '" + publication + "'" + "number " + number + " " + publication.hashCode());
    }

    private List<Publication> generatePublications(int numberOfPublicationsToGenerate) {
        List<Publication> publications = new ArrayList<>();
        List<List<AtomicPublication>> generatedPublications = publicationGenerator.getPublications(numberOfPublicationsToGenerate);
        for (List<AtomicPublication> atomicPublications : generatedPublications) {
            publications.add(createPublication(atomicPublications));
        }
        return publications;
    }

    private Publication createPublication(List<AtomicPublication> atomicPublications) {
        Publication publication = new Publication();
        for (AtomicPublication atomicPublication : atomicPublications) {
            publication.getAtomicPublications().add(atomicPublication);
        }
        return publication;
    }
}