package org.example;

import java.util.HashMap;
import java.util.Map;

public class TransplotationMap {
    private Map<Integer, Transplotation> transplotationMap = new HashMap<>();

    public void addTransplotation(Transplotation transplotation) {
        transplotationMap.put(transplotation.getId(), transplotation);
    }

    public Map<Integer, Transplotation> getTransplotationMap() {
        return transplotationMap;
    }
}
