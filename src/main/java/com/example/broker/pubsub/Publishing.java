package com.example.broker.pubsub;

import com.example.broker.pubsub.AtomicPublication;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Publishing implements Serializable {
    List<AtomicPublication> atomicPublications = new ArrayList<>();

    public void setAtomicPublications(List<AtomicPublication> atomicPublications) {
        this.atomicPublications = atomicPublications;
    }

    public List<AtomicPublication> getAtomicPublications() {
        return atomicPublications;
    }
}
