package org.example;

import java.util.Comparator;
import java.util.List;

public class TransplotationSorter {

    public static void sortTransplotation(List<Transplotation> transplotations, String field) {
        Comparator<Transplotation> comparator = switch (field) {
            case "id" -> Comparator.comparingInt(Transplotation::getId);
            case "type" -> Comparator.comparing(Transplotation::getType);
            case "model" -> Comparator.comparing(Transplotation::getModel);
            case "releaseDate" -> Comparator.comparing(Transplotation::getReleaseDate);
            case "price" -> Comparator.comparingDouble(Transplotation::getPrice);
            default -> throw new IllegalArgumentException("Invalid field: " + field);
        };

        transplotations.sort(comparator);
    }
}