package org.example;

import java.io.IOException;
import java.util.List;

public class Main {
    public static void main(String[] args) throws IOException {
        DataHandler dataHandler = new DataHandler();

        List<Transplotation> transplotationsFromText = dataHandler.readDataFromFile("src/main/resources/vehicles.txt");
        TransplotationSorter.sortTransplotation(transplotationsFromText, "id");
       dataHandler.writeDataToFile("src/main/resources/result.txt", transplotationsFromText);

       List<Transplotation> transplotationsFromJson = dataHandler.readDataFromJsonFile("src/main/resources/vehicles.json");
       TransplotationSorter.sortTransplotation(transplotationsFromJson, "model");
        dataHandler.writeDataToJsonFile("src/main/resources/result.json", transplotationsFromJson);

        List<Transplotation> transplotationsFromXml = dataHandler.readDataFromXmlFile("src/main/resources/vehicles.xml");
        TransplotationSorter.sortTransplotation(transplotationsFromXml, "price");
        dataHandler.writeDataToXmlFile("src/main/resources/result.xml", transplotationsFromXml);

        String[] files = new String[] {
                "src/main/resources/result.txt",
                "src/main/resources/result.json",
                "src/main/resources/result.xml"
        };

        Archiver archiver = new Archiver();
        archiver.createZipArchive("src/main/resources/zipResult.zip", files);
        archiver.createJarArchive("src/main/resources/jarResult.jar", files);
    }
}