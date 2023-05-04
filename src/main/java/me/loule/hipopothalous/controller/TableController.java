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

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
                return resultSet.next() ? new TableModel(resultSet.getInt("size"), resultSet.getString("location")) : null;
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
        insertTable(size, location);
        tables.add(new TableModel(size, location));
        tableSizeInput.clear();
        tableLocationInput.clear();
    }

    /**
     * @param size
     * @param location
     * This function is used to insert a table in the database
     */
    private void insertTable(int size, String location) {
        String query = "INSERT INTO tables (size, location) VALUES (?, ?)";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, size);
            statement.setString(2, location);
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
