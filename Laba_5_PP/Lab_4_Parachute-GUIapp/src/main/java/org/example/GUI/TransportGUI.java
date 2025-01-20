package org.example.GUI;

import org.example.api.Dto.ParachuteDTO;
import org.example.api.Factory.ParachuteFactory;
import org.example.api.Misc.Archiver;
import org.example.persistence.Repositories.AbstractStorage;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.Comparator;

public class TransportGUI {

    private AbstractStorage<TransportDTO> storage;
    private JFrame frame;
    private JTextField costField, nameField, descriptionField;
    private JTable table;
    private DefaultTableModel tableModel;

    public TransportGUI() {
        storage = TransportFactory.getInstance();
    }

    public void createAndShowGUI() {
        frame = new JFrame("Parachute Manager");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600); // Увеличим размер окна для таблицы

        // Панель для ввода данных
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new GridLayout(4, 2));

        inputPanel.add(new JLabel("Cost:"));
        costField = new JTextField();
        inputPanel.add(costField);

        inputPanel.add(new JLabel("Name:"));
        nameField = new JTextField();
        inputPanel.add(nameField);

        inputPanel.add(new JLabel("Description:"));
        descriptionField = new JTextField();
        inputPanel.add(descriptionField);

        JButton addButton = new JButton("Add transport");
        addButton.addActionListener(new AddButtonListener());

        inputPanel.add(addButton);

        // Модель таблицы для отображения парашютов
        tableModel = new DefaultTableModel(new Object[] {"Cost", "Name", "Description"}, 0);
        table = new JTable(tableModel);
        JScrollPane tableScrollPane = new JScrollPane(table);

        // Панель для выбора файлов
        JPanel Panel = new JPanel();
        JButton readButton = new JButton("Read Data");
        readButton.addActionListener(new ReadButtonListener());

        JButton writeButton = new JButton("Write Data");
        writeButton.addActionListener(new WriteButtonListener());

        Panel.add(readButton);
        Panel.add(writeButton);

        // Сортировка
        JButton sortButton = new JButton("Sort Data");
        sortButton.addActionListener(new SortButtonListener());

        Panel.add(sortButton);

        // Архив
        JButton archiveButton = new JButton("Create Archive");
        archiveButton.addActionListener(new ArchiveButtonListener());

        Panel.add(archiveButton);

        // Размещение элементов
        frame.setLayout(new BorderLayout());
        frame.add(inputPanel, BorderLayout.NORTH);
        frame.add(tableScrollPane, BorderLayout.CENTER); // Добавляем таблицу в центр
        frame.add(Panel, BorderLayout.SOUTH);  // Панель для кнопок внизу

        frame.setVisible(true);
    }

    private class AddButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String cost = costField.getText();
            String name = nameField.getText();
            String description = descriptionField.getText();

            if (!cost.isEmpty() && !name.isEmpty() && !description.isEmpty()) {
                try {
                    int costInt = Integer.parseInt(cost);
                    TransportDTO transport = new TransportDTO(costInt, name, description);

                    // Проверка на дублирование
                    boolean isDuplicate = storage.getList().stream()
                            .anyMatch(p -> p.getName().equalsIgnoreCase(name));

                    if (isDuplicate) {
                        JOptionPane.showMessageDialog(frame, "Parachute with the same name already exists.", "Error", JOptionPane.ERROR_MESSAGE);
                    } else {
                        storage.addToListStorage(transport);
                        storage.addToMapStorage(costInt, transport);

                        // Обновление таблицы
                        tableModel.addRow(new Object[] {transport.getCost(), transport.getName(), transport.getDescription()});
                    }

                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(frame, "Invalid cost format", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(frame, "All fields are required", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private class ReadButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String[] options = {"transport.txt", "transport.xml", "transport.json"};
            String fileType = (String) JOptionPane.showInputDialog(frame,
                    "Select file to read from", "Select File",
                    JOptionPane.PLAIN_MESSAGE, null, options, options[0]);

            if (fileType != null) {
                new Thread(() -> {
                    try {
                        // Очищаем данные в таблице и в хранилище перед чтением нового файла
                        tableModel.setRowCount(0);
                        storage.getList().clear();

                        // Читаем данные из выбранного файла
                        switch (fileType) {
                            case "transport.txt":
                                storage.readFromFile(fileType);
                                break;
                            case "transport.xml":
                                storage.setListStorage(storage.readFromXml(fileType));
                                break;
                            case "transport.json":
                                storage.setListStorage(storage.readDataFromJsonFile(fileType));
                                break;
                            default:
                                throw new IOException("Unsupported file format");
                        }

                        // Обновляем таблицу с новыми данными
                        updateTable();

                        JOptionPane.showMessageDialog(frame, "Data successfully loaded from " + fileType,
                                "Success", JOptionPane.INFORMATION_MESSAGE);
                    } catch (IOException ex) {
                        JOptionPane.showMessageDialog(frame, "Error reading file: " + ex.getMessage(),
                                "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }).start();
            }
        }
    }



    private class WriteButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String[] options = {"transport.txt", "transport.xml", "transport.json"};
            String fileType = (String) JOptionPane.showInputDialog(frame,
                    "Select file to write to", "Select File",
                    JOptionPane.PLAIN_MESSAGE, null, options, options[0]);

            if (fileType != null) {
                new Thread(() -> {
                    switch (fileType) {
                        case "transport.txt":
                            storage.writeToFile(fileType);
                            break;
                        case "transport.xml":
                            storage.writeToXml(fileType, storage.getList());
                            break;
                        case "transport.json":
                            storage.writeDataToJsonFile(fileType, storage.getList());
                            break;
                    }
                    JOptionPane.showMessageDialog(frame, "Data written to " + fileType, "Success", JOptionPane.INFORMATION_MESSAGE);
                }).start();
            }
        }
    }

    private class SortButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String[] options = {"cost", "name", "description"};
            String field = (String) JOptionPane.showInputDialog(frame,
                    "Select sorting field", "Sort by",
                    JOptionPane.PLAIN_MESSAGE, null, options, options[0]);

            if (field != null) {
                new Thread(() -> {
                    switch (field) {
                        case "cost":
                            storage.getList().sort(Comparator.comparingInt(TransportDTO::getCost));
                            break;
                        case "name":
                            storage.getList().sort(Comparator.comparing(TransportDTO::getName));
                            break;
                        case "description":
                            storage.getList().sort(Comparator.comparing(TransportDTO::getDescription));
                            break;
                    }
                    // Обновляем таблицу после сортировки
                    updateTable();
                }).start();
            }
        }
    }

    private class ArchiveButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            Archiver archiver = new Archiver();
            String[] files = {"transport.txt", "transport.json", "transport.xml"};

            new Thread(() -> {
                try {
                    archiver.createZipArchive("TransportArchive.zip", files);
                    archiver.createJarArchive("TransportArchive.jar", files);
                    JOptionPane.showMessageDialog(frame, "Archive Created", "Success", JOptionPane.INFORMATION_MESSAGE);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }).start();
        }
    }

    // Метод для обновления таблицы
    private void updateTable() {
        // Очистить текущую таблицу
        tableModel.setRowCount(0);

        // Добавить все парашюты в таблицу
        for (TransportDTO transport : storage.getList()) {
            tableModel.addRow(new Object[] {transport.getCost(), transport.getName(), transport.getDescription()});
        }
    }
}
