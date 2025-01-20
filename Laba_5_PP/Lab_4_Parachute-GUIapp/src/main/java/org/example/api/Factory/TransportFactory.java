package org.example.api.Factory;

import org.example.api.Dto.ParachuteDTO;
import org.example.persistence.Repositories.AbstractStorage;
import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class TransportFactory extends AbstractStorage<TransportDTO> {

    private static TransportFactory instance;

    private TransportFactory() {}

    public static TransportFactory getInstance() {
        if (instance == null) {
            instance = new TransportFactory();
        }
        return instance;
    }

    @Override
    public void readFromFile(String filename) {
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                try {
                    String[] parts = line.split(",");
                    int cost = Integer.parseInt(parts[0]);
                    String name = parts[1];
                    String desc = parts[2];

                    TransportDTO parachute = new TransportDTO(cost, name, desc);
                    addToListStorage(transport);
                    addToMapStorage(cost, transport);
                }
                catch (Exception e1)
                {
                    continue;
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void writeToFile(String filename) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filename))) {
            for (TransportDTO bus : listStorage) {
                bw.write(bus.getCost() + "," +
                        bus.getName() + "," +
                        bus.getDescription()+"\n");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<TransportDTO> readFromXml(String filename) {
        List<TransportDTO> list = new ArrayList<>();
        try {
            File xmlFile = new File(filename);
            DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentFactory.newDocumentBuilder();
            Document document = documentBuilder.parse(xmlFile);

            document.getDocumentElement().normalize();
            NodeList nodeList = document.getElementsByTagName("transport");

            for (int i = 0; i < nodeList.getLength(); i++) {
                Node node = nodeList.item(i);

                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) node;

                    ParachuteDTO parachute = new ParachuteDTO();
                    parachute.setCost(Integer.parseInt(element.getElementsByTagName("cost").item(0).getTextContent()));
                    parachute.setName(element.getElementsByTagName("name").item(0).getTextContent());
                    parachute.setDescription(element.getElementsByTagName("description").item(0).getTextContent());

                    list.add(parachute);
                }
            }
        } catch (ParserConfigurationException | IOException | SAXException e) {
            throw new RuntimeException(e);
        }
        return list;
    }

    @Override
    public void writeToXml(String filename, List<ParachuteDTO> list) {
        try {
            DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentFactory.newDocumentBuilder();
            Document document = documentBuilder.newDocument();

            Element root = document.createElement("transports");
            document.appendChild(root);

            for (TransportDTO vehicle : list) {
                Element transport = document.createElement("transport");

                Element cost = document.createElement("cost");
                cost.appendChild(document.createTextNode(String.valueOf(vehicle.getCost())));
                transport.appendChild(cost);

                Element type = document.createElement("name");
                type.appendChild(document.createTextNode(vehicle.getName()));
                transport.appendChild(type);

                Element model = document.createElement("description");
                model.appendChild(document.createTextNode(vehicle.getDescription()));
                transport.appendChild(model);

                root.appendChild(transport);
            }

            Transformer tr = TransformerFactory.newInstance().newTransformer();
            DOMSource source = new DOMSource(document);
            FileOutputStream fos = new FileOutputStream("transport.xml");
            StreamResult result = new StreamResult(new File(filename));

            tr.setOutputProperty(OutputKeys.INDENT, "yes");
            tr.transform(source, result);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public TransportDTO findByName(String name) {
        return listStorage.stream().filter(c -> c.getName().equals(name)).findFirst().orElse(new TransportDTO(-1,"",""));
    }

    public List<TransportDTO> readDataFromJsonFile(String fileName) {
        List<TransportDTO> transport = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            StringBuilder jsonContent = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                jsonContent.append(line);
            }
            JSONArray jsonArray = new JSONArray(jsonContent.toString());
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                TransportDTO transport1 = new ParachuteDTO();
                transport1.setCost(jsonObject.getInt("cost"));
                transport1.setName(jsonObject.getString("name"));
                transport1.setDescription(jsonObject.getString("description"));
                transport.add(transport1);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Transport;
    }

    public void writeDataToJsonFile(String fileName, List<TransportDTO> transports) {
        JSONArray jsonArray = new JSONArray();
        for (ParachuteDTO transport : transports) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("cost", transport.getCost());
            jsonObject.put("name", transport.getName());
            jsonObject.put("description", transport.getDescription());
            jsonArray.put(jsonObject);
        }
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            writer.write(jsonArray.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
