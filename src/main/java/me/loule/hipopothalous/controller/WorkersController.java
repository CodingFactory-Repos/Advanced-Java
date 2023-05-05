package me.loule.hipopothalous.controller;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import me.loule.hipopothalous.model.DatabaseConnection;
import me.loule.hipopothalous.model.WorkersModel;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Comparator;
import java.util.logging.Logger;

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
    TextField tfWorkerLastName1;
    @FXML
    TextField tfWorkerFirstName1;
    @FXML
    TextField tfWorkerPost1;
    @FXML
    TextField tfWorkerAddHours;
    @FXML
    Button btnAddWorker;
    @FXML
    Button btnDeleteWorker;
    @FXML
    Button btnWorkerAddHours;
    @FXML
    Button btnWorkerAddMoreHours;
    @FXML
    Button btnShowAddForm;
    @FXML
    VBox vbFormWorker;
    @FXML
    VBox vbFormHoursWorker;
    @FXML
    GridPane gpFormWorkers;
    @FXML
    TableView<WorkersModel> tvWorkers;
    @FXML
    TableColumn<WorkersModel, Integer> tcWorkerID;
    @FXML
    TableColumn<WorkersModel, String> tcWorkerLastName;
    @FXML
    TableColumn<WorkersModel, String> tcWorkerFirstName;
    @FXML
    TableColumn<WorkersModel, String> tcWorkerPost;
    @FXML
    TableColumn<WorkersModel, String> tcWorkerDealHours;
    @FXML
    TableColumn<WorkersModel, String> tcWorkerTotalHours;


    private final ObservableList<WorkersModel> workersList = FXCollections.observableArrayList();

    int selectedWorkerId;

    public void initialize() {
        tcWorkerID.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getId()).asObject());
        tcWorkerLastName.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getLastName()));
        tcWorkerFirstName.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getFirstName()));
        tcWorkerPost.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getPost()));
        tcWorkerDealHours.setCellValueFactory(cellData -> new SimpleStringProperty(String.valueOf(cellData.getValue().getDeal_hours())));
        tcWorkerTotalHours.setCellValueFactory(cellData -> new SimpleStringProperty(getTotalHours(cellData.getValue().getId())));
        tvWorkers.setItems(workersList);

        onAddWorkerButtonClick();
        getWorkers();
        handleSelectWorker();
        showAddForm();
        showAddHoursForm();
        addHours();
        handleFireWorker();
    }

    @FXML
    protected void onAddWorkerButtonClick() {
        Connection connection = DatabaseConnection.getConnection();
        btnAddWorker.setOnAction(event -> {
            String lastName = tfWorkerLastName.getText();
            String firstName = tfWorkerFirstName.getText();
            String post = tfWorkerPost.getText();
            String hours = tfWorkerHours.getText();
            String query = "INSERT INTO workers (firstName,lastName, post, deal_hours) VALUES (?, ?, ?, ?)";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, firstName);
                preparedStatement.setString(2, lastName);
                preparedStatement.setString(3, post);
                preparedStatement.setInt(4, Integer.parseInt(hours));
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
                        rs.getInt("deal_hours")

                ));
                workersList.sort(Comparator.comparing(WorkersModel::getLastName));
            }

        } catch (Exception e) {
            Logger.getLogger(e.getMessage());
        }
    }

    private void handleSelectWorker() {
        tvWorkers.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                btnDeleteWorker.setVisible(true);
                btnWorkerAddHours.setVisible(true);
                tfWorkerLastName1.setText(newValue.getLastName());
                tfWorkerFirstName1.setText(newValue.getFirstName());
                tfWorkerPost1.setText(newValue.getPost());
                selectedWorkerId = newValue.getId();
            }
        });

        btnDeleteWorker.setOnAction(event -> {
            WorkersModel selectedWorker = tvWorkers.getSelectionModel().getSelectedItem();
            if (selectedWorker != null) {
                Connection connection = DatabaseConnection.getConnection();
                String query = "DELETE FROM workers WHERE worker_id = ?";
                try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                    preparedStatement.setInt(1, selectedWorker.getId());
                    preparedStatement.executeUpdate();
                    workersList.remove(selectedWorker);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void showAddForm() {
        btnShowAddForm.setOnAction(event -> {
            if (vbFormWorker.isVisible()) {
                vbFormWorker.setVisible(false);
                btnShowAddForm.setText("Show add form");
            } else {
                vbFormWorker.setVisible(true);
                vbFormHoursWorker.setVisible(false);
                btnShowAddForm.setText("Hide add form");
            }
        });
    }

    private void showAddHoursForm() {
        btnWorkerAddHours.setOnAction(event -> {
            if (vbFormHoursWorker.isVisible()) {
                vbFormHoursWorker.setVisible(false);
                btnWorkerAddHours.setText("Add hours");
            } else {
                vbFormHoursWorker.setVisible(true);
                vbFormWorker.setVisible(false);
                btnWorkerAddHours.setText("Hide add hours");
            }
        });
    }

    private void addHours() {
        btnWorkerAddMoreHours.setOnAction(event -> {
            Connection connection = DatabaseConnection.getConnection();
            String query = "INSERT INTO workers_hours (worker_id, hours) VALUES (?, ?)";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setInt(1, selectedWorkerId);
                preparedStatement.setDouble(2, Double.parseDouble(tfWorkerAddHours.getText()));
                preparedStatement.executeUpdate();

                tfWorkerLastName1.clear();
                tfWorkerFirstName1.clear();
                tfWorkerPost1.clear();
                tfWorkerAddHours.clear();
                vbFormHoursWorker.setVisible(false);
                btnDeleteWorker.setVisible(false);
                btnWorkerAddHours.setVisible(false);

                workersList.clear();
                getWorkers();

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Information");
                alert.setHeaderText("Hours added");
                alert.setContentText("Hours added successfully");
                alert.showAndWait();

            } catch (Exception e) {
                Logger.getLogger(e.getMessage());
            }
        });
    }

    private String getTotalHours(int id) {
        String totalHours = "";
        Connection connection = DatabaseConnection.getConnection();
        String query = "SELECT SUM(hours) FROM workers_hours WHERE worker_id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, id);
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                totalHours = rs.getString("SUM(hours)");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return totalHours;
    }

    private void handleFireWorker() {
        btnDeleteWorker.setOnAction(event -> {
            WorkersModel selectedWorker = tvWorkers.getSelectionModel().getSelectedItem();
            if (selectedWorker != null) {
                Connection connection = DatabaseConnection.getConnection();
                String query = "DELETE FROM workers WHERE worker_id = ?";
                String query2 = "DELETE FROM workers_hours WHERE worker_id = ?";
                try (PreparedStatement preparedStatement = connection.prepareStatement(query2)) {
                    preparedStatement.setInt(1, selectedWorker.getId());
                    preparedStatement.executeUpdate();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                    preparedStatement.setInt(1, selectedWorker.getId());
                    preparedStatement.executeUpdate();
                    workersList.remove(selectedWorker);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}