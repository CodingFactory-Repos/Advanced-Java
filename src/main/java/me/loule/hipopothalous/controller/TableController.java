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

    public void initialize() {
        tableSize.setCellValueFactory(cellData -> cellData.getValue().sizeProperty().asObject());
        tableLocation.setCellValueFactory(cellData -> cellData.getValue().locationProperty());
        availableTables.setItems(tables);
        loadTablesFromDatabase();
    }

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

    private List<TableModel> resultSetToTableList(ResultSet resultSet) throws SQLException {
        return Stream.generate(() -> {
            try {
                return resultSet.next() ? new TableModel(resultSet.getInt("size"), resultSet.getString("location")) : null;
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }).takeWhile(Objects::nonNull).collect(Collectors.toList());
    }

    @FXML
    private void addTable(ActionEvent event) {
        int size = Integer.parseInt(tableSizeInput.getText());
        String location = tableLocationInput.getText();
        insertTable(size, location);
        tables.add(new TableModel(size, location));
        tableSizeInput.clear();
        tableLocationInput.clear();
    }

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

    @FXML
    private void releaseTable(ActionEvent event) {
        TableModel selectedTable = availableTables.getSelectionModel().getSelectedItem();
        if (selectedTable != null) {
            tables.remove(selectedTable);
        }
    }
}
