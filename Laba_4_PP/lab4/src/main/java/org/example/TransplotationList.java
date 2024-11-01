package org.example;

import java.util.ArrayList;
import java.util.List;

public class TransplotationList {
    private List<Transplotation> transplotations = new ArrayList<>();

    public void addTransplotation(Transplotation transplotation) {
        transplotations.add(transplotation);
    }

    public List<Transplotation> getTransplotations() {
        return transplotations;
    }
}
