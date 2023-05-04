package me.loule.hipopothalous.controller;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import me.loule.hipopothalous.model.DatabaseConnection;
import me.loule.hipopothalous.model.WorkersModel;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class WorkersController {

    @FXML
    TextField tfWorkerLastName;
    @FXML
    TextField tfWorkerFirstName;
    @FXML
    TextField tfWorkerPost;
    @FXML
    TextField tfWorkerHours;
    @FXML
    Button btnAddWorker;
    @FXML
    GridPane gpFormWorkers;
    @FXML
    TableView<WorkersModel> tvWorkers;
    @FXML
    TableColumn<WorkersModel, String> tcWorkerLastName;
    @FXML
    TableColumn<WorkersModel, String> tcWorkerFirstName;
    @FXML
    TableColumn<WorkersModel, String> tcWorkerPost;
    @FXML
    TableColumn<WorkersModel, String> tcWorkerHours;


    private final ObservableList<WorkersModel> workersList = FXCollections.observableArrayList();

    public void initialize() {
        tcWorkerLastName.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getLastName()));
        tcWorkerFirstName.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getFirstName()));
        tcWorkerPost.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getPost()));
        tcWorkerHours.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getHours()));
        tvWorkers.setItems(workersList);
        onAddWorkerButtonClick();
        getWorkers();
    }

    @FXML
    protected void onAddWorkerButtonClick() {
        Connection connection = DatabaseConnection.getConnection();
        btnAddWorker.setOnAction(event -> {
            String lastName = tfWorkerLastName.getText();
            String firstName = tfWorkerFirstName.getText();
            String post = tfWorkerPost.getText();
            String hours = tfWorkerHours.getText();
            String query = "INSERT INTO workers (firstName,lastName, post, hours) VALUES (?, ?, ?, ?)";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, firstName);
                preparedStatement.setString(2, lastName);
                preparedStatement.setString(3, post);
                preparedStatement.setString(4, hours);
                preparedStatement.executeUpdate();

                tfWorkerLastName.clear();
                tfWorkerFirstName.clear();
                tfWorkerPost.clear();
                tfWorkerHours.clear();

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Information");
                alert.setHeaderText("Worker added");
                alert.setContentText("Worker added successfully");
                alert.showAndWait();

                workersList.clear();
                getWorkers();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    protected void getWorkers() {
        Connection connection = DatabaseConnection.getConnection();
        String query = "SELECT * FROM workers";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                workersList.add(new WorkersModel(
                        rs.getInt("worker_id"),
                        rs.getString("lastName"),
                        rs.getString("firstName"),
                        rs.getString("post"),
                        rs.getString("hours")
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}