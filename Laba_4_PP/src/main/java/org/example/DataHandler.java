package org.example;

import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class DataHandler {

    // Method to write data to a file
    public void writeDataToFile(String fileName, List<Transplotation> transplotations) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            for (Transplotation transplotation : transplotations) {
                writer.write(transplotation.toString());
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Method to read data from a file
    public List<Transplotation> readDataFromFile(String fileName) {
        List<Transplotation> transplotations = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = reader.readLine()) != null) {
                Transplotation vehicle = parseTransplotation(line);
                transplotations.add(vehicle);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return transplotations;
    }

    // Method to parse a Transplotation object from a string
    private Transplotation parseTransplotation(String data) {
        String[] fields = data.split(", ");
        Transplotation transplotation = new Transplotation() {
            // Implementing abstract class Transplotation
        };
        transplotation.setId(Integer.parseInt(fields[0]));
        transplotation.setType(fields[1]);
        transplotation.setModel(fields[2]);
        transplotation.setReleaseDate(fields[3]);
        transplotation.setPrice(Double.parseDouble(fields[4]));
        return transplotation;
    }

    // Method to write data to a JSON file
    public void writeDataToJsonFile(String fileName, List<Transplotation> transplotations) {
        JSONArray jsonArray = new JSONArray();
        for (Transplotation transplotation : transplotations) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("id", transplotation.getId());
            jsonObject.put("type", transplotation.getType());
            jsonObject.put("model", transplotation.getModel());
            jsonObject.put("releaseDate", transplotation.getReleaseDate());
            jsonObject.put("price", transplotation.getPrice());
            jsonArray.put(jsonObject);
        }
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            writer.write(jsonArray.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Method to read data from a JSON file
    public List<Transplotation> readDataFromJsonFile(String fileName) {
        List<Transplotation> transplotations = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            StringBuilder jsonContent = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                jsonContent.append(line);
            }
            JSONArray jsonArray = new JSONArray(jsonContent.toString());
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                Transplotation transplotation = new Transplotation() {
                    // Implementing abstract class Transplotation
                };
                transplotation.setId(jsonObject.getInt("id"));
                transplotation.setType(jsonObject.getString("type"));
                transplotation.setModel(jsonObject.getString("model"));
                transplotation.setReleaseDate(jsonObject.getString("releaseDate"));
                transplotation.setPrice(jsonObject.getDouble("price"));
                transplotations.add(transplotation);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return transplotations;
    }

    // Method to write data to an XML file
    public void writeDataToXmlFile(String fileName, List<Transplotation> transplotations) {
        try {
            DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentFactory.newDocumentBuilder();
            Document document = documentBuilder.newDocument();

            Element root = document.createElement("transplotations");
            document.appendChild(root);

            for (Transplotation transplotation : transplotations) {
                Element transplotationElement = document.createElement("transplotation");

                Element id = document.createElement("id");
                id.appendChild(document.createTextNode(String.valueOf(transplotation.getId())));
                transplotationElement.appendChild(id);

                Element type = document.createElement("type");
                type.appendChild(document.createTextNode(transplotation.getType()));
                transplotationElement.appendChild(type);

                Element model = document.createElement("model");
                model.appendChild(document.createTextNode(transplotation.getModel()));
                transplotationElement.appendChild(model);

                Element releaseDate = document.createElement("releaseDate");
                releaseDate.appendChild(document.createTextNode(transplotation.getReleaseDate()));
                transplotationElement.appendChild(releaseDate);

                Element price = document.createElement("price");
                price.appendChild(document.createTextNode(String.valueOf(transplotation.getPrice())));
                transplotationElement.appendChild(price);

                root.appendChild(transplotationElement);
            }

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource domSource = new DOMSource(document);
            StreamResult streamResult = new StreamResult(new File(fileName));

            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.transform(domSource, streamResult);

        } catch (ParserConfigurationException | TransformerException e) {
            e.printStackTrace();
        }
    }

    // Method to read data from an XML file
    public List<Transplotation> readDataFromXmlFile(String fileName) {
        List<Transplotation> transplotations = new ArrayList<>();
        try {
            File xmlFile = new File(fileName);
            DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentFactory.newDocumentBuilder();
            Document document = documentBuilder.parse(xmlFile);

            document.getDocumentElement().normalize();
            NodeList nodeList = document.getElementsByTagName("transplotation");

            for (int i = 0; i < nodeList.getLength(); i++) {
                Node node = nodeList.item(i);

                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) node;

                    Transplotation transplotation = new Transplotation() {
                        // Implementing abstract class Transplotation
                    };
                    transplotation.setId(Integer.parseInt(element.getElementsByTagName("id").item(0).getTextContent()));
                    transplotation.setType(element.getElementsByTagName("type").item(0).getTextContent());
                    transplotation.setModel(element.getElementsByTagName("model").item(0).getTextContent());
                    transplotation.setReleaseDate(element.getElementsByTagName("releaseDate").item(0).getTextContent());
                    transplotation.setPrice(Double.parseDouble(element.getElementsByTagName("price").item(0).getTextContent()));

                    transplotations.add(transplotation);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return transplotations;
    }
}