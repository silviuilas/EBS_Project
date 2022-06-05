package com.example.broker.pubsub;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Publication implements Serializable {
    List<AtomicPublication> atomicPublications = new ArrayList<>();

    @Override
    public String toString() {
        return "Publishing{" +
                "atomicPublications=" + atomicPublications +
                '}';
    }

    public List<AtomicPublication> getAtomicPublications() {
        return atomicPublications;
    }

    public void setAtomicPublications(List<AtomicPublication> atomicPublications) {
        this.atomicPublications = atomicPublications;
    }
}
