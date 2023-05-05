package me.loule.hipopothalous.controller;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import me.loule.hipopothalous.model.DatabaseConnection;
import me.loule.hipopothalous.model.TableModel;

import java.sql.*;
import java.util.List;
import java.util.Objects;
import java.util.logging.Logger;
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
    @FXML
    private TableColumn<TableModel, String> tableAssigned;

    private ObservableList<TableModel> tables = FXCollections.observableArrayList();

    /**
     * This function is called when the application starts
     * It will initialize the table view
     */
    public void initialize() {
        tableSize.setCellValueFactory(cellData -> cellData.getValue().sizeProperty().asObject());
        tableLocation.setCellValueFactory(cellData -> cellData.getValue().locationProperty());
        tableAssigned.setCellValueFactory(cellData -> {
            System.out.println(cellData.getValue().statusProperty().getValue());
            if (cellData.getValue().statusProperty().getValue() == null) {
                return new SimpleStringProperty("Libre");
            } else if (cellData.getValue().statusProperty().getValue().equals("[pending]")) {
                return new SimpleStringProperty("Occupé");
            } else {
                return new SimpleStringProperty("Libre");
            }
        });

        availableTables.setItems(tables);
        loadTablesFromDatabase();
    }

    /**
     * This function is used to load the tables from the database
     * It will be called when the application starts
     */
    private void loadTablesFromDatabase() {
        String query = "SELECT tables.*, last_order.status FROM tables LEFT JOIN (SELECT table_number, status FROM orders WHERE date = (SELECT MAX(date) FROM orders AS o WHERE o.table_number = orders.table_number)) AS last_order ON tables.id = last_order.table_number;";

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
                return resultSet.next() ? new TableModel(resultSet.getInt("id"), resultSet.getInt("size"), resultSet.getString("location"), resultSet.getTimestamp("date"), resultSet.getString("status")) : null;
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
        String status = "";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement("SELECT id FROM tables WHERE size = ? AND location = ? AND date = ?")) {
            statement.setInt(1, size);
            statement.setString(2, location);
            statement.setTimestamp(3, timestamp);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                id = resultSet.getInt("id");
                status = resultSet.getString("status");
            }
        } catch (SQLException e) {
            Logger.getLogger("Failed to get id from database!");
        }
        System.out.println(id);
        tables.add(new TableModel(id, size, location, timestamp, status));
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

    /**
     * @param event
     * This function is called when the user clicks on the "Libérer" button
     * It will remove a table from the database and from the table view
     */
    @FXML
    private void releaseTable(ActionEvent event) {
        TableModel selectedTable = availableTables.getSelectionModel().getSelectedItem();

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation");
        alert.setHeaderText("Êtes-vous sûr de vouloir libérer cette table ?");
        alert.setContentText("Cette action est irréversible.");
        alert.showAndWait();

        if (alert.getResult().getText().equals("OK")) {
            // Set the table status to "paid" in the database
            String query = "UPDATE orders SET status = 'paid' WHERE table_number = ? AND status = 'pending'";
            try (Connection connection = DatabaseConnection.getConnection();
                 PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setInt(1, selectedTable.getId());
                statement.executeUpdate();

                alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Information");
                alert.setHeaderText("La table a été libérée avec succès !");
                alert.showAndWait();

                // Set the table status to "paid" in the table view
                selectedTable.setStatus("Disponible");
                availableTables.refresh();
            } catch (SQLException e) {
                e.printStackTrace();

                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Erreur");
                alert.setHeaderText("La table n'a pas pu être libérée !");
                alert.showAndWait();
            }
        }
    }

    /**
     * @param event
     * This function is called when the user clicks on the "Supprimer" button
     * It will remove a table from the database and from the table view
     */
    @FXML
    private void deleteTable(ActionEvent event) {
        TableModel selectedTable = availableTables.getSelectionModel().getSelectedItem();

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation");
        alert.setHeaderText("Êtes-vous sûr de vouloir supprimer cette table ?");
        alert.setContentText("Cette action est irréversible.");
        alert.showAndWait();

        if (alert.getResult().getText().equals("OK")) {
            // Delete the table from the database
            String query = "DELETE FROM tables WHERE id = ?";
            try (Connection connection = DatabaseConnection.getConnection();
                 PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setInt(1, selectedTable.getId());
                statement.executeUpdate();

                alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Information");
                alert.setHeaderText("La table a été supprimée avec succès !");
                alert.showAndWait();

                // Delete the table from the table view
                tables.remove(selectedTable);
            } catch (SQLException e) {
                e.printStackTrace();

                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Erreur");
                alert.setHeaderText("La table n'a pas pu être supprimée !");
                alert.showAndWait();
            }
        }
    }

    /**
     * @param event
     * This function is called when the user clicks on the "Rafraîchir" button
     * It will refresh the table view
     */
    @FXML
    private void refreshAvailableTables() {
        initialize();
    }
}
