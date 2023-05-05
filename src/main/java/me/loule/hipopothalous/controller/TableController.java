package me.loule.hipopothalous.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import me.loule.hipopothalous.model.DatabaseConnection;
import me.loule.hipopothalous.model.TableModel;

import java.sql.*;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TableController {
    @FXML
    private TableView<TableModel> availableTables;
    @FXML
    private TableColumn<TableModel, Integer> tableSize;
    @FXML
    private TableColumn<TableModel, String> tableLocation;
    @FXML
    private TextField tableSizeInput;
    @FXML
    private TextField tableLocationInput;

    private ObservableList<TableModel> tables = FXCollections.observableArrayList();

    /**
     * This function is called when the application starts
     * It will initialize the table view
     */
    public void initialize() {
        tableSize.setCellValueFactory(cellData -> cellData.getValue().sizeProperty().asObject());
        tableLocation.setCellValueFactory(cellData -> cellData.getValue().locationProperty());
        availableTables.setItems(tables);
        loadTablesFromDatabase();
    }

    /**
     * This function is used to load the tables from the database
     * It will be called when the application starts
     */
    private void loadTablesFromDatabase() {
        String query = "SELECT * FROM tables";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {

            tables.setAll(resultSetToTableList(resultSet));

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * @param resultSet
     * @return
     * @throws SQLException
     * This function is used to convert a ResultSet to a list of TableModel
     * It will be used in loadTablesFromDatabase()
     */
    private List<TableModel> resultSetToTableList(ResultSet resultSet) throws SQLException {
        return Stream.generate(() -> {
            try {
                return resultSet.next() ? new TableModel(resultSet.getInt("id") ,resultSet.getInt("size"), resultSet.getString("location") , resultSet.getTimestamp("date")) : null;
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }).takeWhile(Objects::nonNull).collect(Collectors.toList());
    }

    /**
     * @param event
     * This function is called when the user clicks on the "Ajouter" button
     * It will add a table to the database and to the table view
     */
    @FXML
    private void addTable(ActionEvent event) {
        int size = Integer.parseInt(tableSizeInput.getText());
        String location = tableLocationInput.getText();
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        //got rid of miliseconds
        timestamp.setNanos(0);

        insertTable(size, location, timestamp);
        int id = 0;
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement("SELECT id FROM tables WHERE size = ? AND location = ? AND date = ?")) {
            statement.setInt(1, size);
            statement.setString(2, location);
            statement.setTimestamp(3, timestamp);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                id = resultSet.getInt("id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println(id);
        tables.add(new TableModel(id, size, location, timestamp));
        tableSizeInput.clear();
        tableLocationInput.clear();
    }

    /**
     * @param size
     * @param location
     * This function is used to insert a table in the database
     */
    private void insertTable(int size, String location, Timestamp timestamp) {
        String query = "INSERT INTO tables (size, location, date) VALUES (?, ?, ?)";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, size);
            statement.setString(2, location);
            statement.setTimestamp(3, timestamp);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void assignTable(ActionEvent event) {
        // Implémentez la logique pour assigner une table à un client en enregistrant sa commande.
    }

    /**
     * @param event
     * This function is called when the user clicks on the "Libérer" button
     * It will remove a table from the database and from the table view
     */
    @FXML
    private void releaseTable(ActionEvent event) {
        TableModel selectedTable = availableTables.getSelectionModel().getSelectedItem();
        if (selectedTable != null) {
            tables.remove(selectedTable);
        }
    }
}
