package com.example.broker.publisher;

import com.example.broker.pubsub.AtomicPublication;

import java.util.List;

public interface PublicationGenerator {
    List<List<AtomicPublication>> getPublications(int numberOfSubscriptions);
}
