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

    /**
     * This function is called when the application starts
     * It will initialize the table view
     * It will also initialize all the buttons and forms
     */
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

    /**
     * This function will get the total hours of a worker
     */
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

    /**
     * This function will get all the workers from the database
     */
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

    /**
     * When the user clicks on the button, the form will be displayed
     */
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
    }

    /**
     * When the user clicks on the button, a form will be displayed to add a new worker
     */
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

    /**
     * When the user clicks on the button, a form will be displayed to add hours to a worker
     */
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

    /**
     * This function will add more hours to a worker
     */
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

    /**
     * @param id the id of the worker
     * @return
     * This function will get the total hours of a worker
     * It will be used to display the total hours of a worker
     */
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
            Logger.getLogger(e.getMessage());
        }
        return totalHours;
    }

    /**
     * This function will delete a worker from the database
     * It will also delete the hours of the worker from the database
     */
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