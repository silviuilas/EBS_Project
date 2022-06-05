package com.example.broker.pubsub;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Publication implements Serializable {
    List<AtomicPublication> atomicPublications = new ArrayList<>();

    @Override
    public String toString() {
        return "Publication{" +
                "atomicPublications=" + atomicPublications +
                '}';
    }

    public List<AtomicPublication> getAtomicPublications() {
        return atomicPublications;
    }

    public void setAtomicPublications(List<AtomicPublication> atomicPublications) {
        this.atomicPublications = atomicPublications;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Publication that = (Publication) o;
        return Objects.equals(atomicPublications, that.atomicPublications);
    }

    @Override
    public int hashCode() {
        return Objects.hash(atomicPublications);
    }
}
