package me.loule.hipopothalous.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import me.loule.hipopothalous.model.DatabaseConnection;
import me.loule.hipopothalous.model.Orders;

import java.io.FileNotFoundException;
import java.sql.*;
import java.util.logging.Logger;

public class CheckOrders {
    private final ObservableList<Orders> orders = FXCollections.observableArrayList();
    @FXML
    TableView<Orders> ordersTableView;
    @FXML
    TableColumn<Orders, Integer> orderId;
    @FXML
    TableColumn<Orders, String> orderStatus;
    @FXML
    TableColumn<Orders, Integer> orderTable;
    @FXML
    TableColumn<Orders, Integer> orderPersons;
    @FXML
    TableColumn<Orders, Timestamp> orderDate;
    @FXML
    TableColumn<Orders, Double> orderTotalPrice;


    @FXML
    VBox vbTextFields;
    @FXML
    TextField tfOrderId;
    @FXML
    MenuButton mbOrderStatus;
    @FXML
    Button btnConfirmUpdate;
    @FXML
    Button btnCloseVbox;
    @FXML
    Button btnCancelOrder;
    @FXML
    HBox hbButtons;
    Object[] selectedOrder;
    String newStatusValue = "";


    private static TableCell<Orders, String> capitalize(TableColumn<Orders, String> column) {
        return new TableCell<Orders, String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty ? "" : item.substring(0, 1).toUpperCase() + item.substring(1));
            }
        };
    }

    public void initialize() {
        orderId.setCellValueFactory(cellData -> cellData.getValue().idProperty().asObject());
        orderStatus.setCellValueFactory(cellData -> cellData.getValue().statusProperty());
        orderTable.setCellValueFactory(cellData -> cellData.getValue().tableNumberProperty().asObject());
        orderPersons.setCellValueFactory(cellData -> cellData.getValue().personsPerTableProperty().asObject());
        orderDate.setCellValueFactory(cellData -> cellData.getValue().dateProperty());
        orderTotalPrice.setCellValueFactory(cellData -> cellData.getValue().priceProperty().asObject());
        ordersTableView.setItems(orders.filtered(order -> order.getStatus().equals("ordered")));

        orderStatus.setCellFactory(CheckOrders::capitalize);
        getData();
        selectRow();
        handleMenuButton();
    }

    /**
     * @throws FileNotFoundException
     * This function is used to call the "createPdf" function
     */
    @FXML
    protected void createPdf() throws FileNotFoundException {
        PdfConverter.createPdf();
    }

    private void getData() {
        try {
            Connection connection = DatabaseConnection.getConnection();
            ResultSet rs;
            try (Statement statement = connection.createStatement()) {
                String sql = "SELECT * FROM orders ORDER BY date DESC";
                rs = statement.executeQuery(sql);
                while (rs.next()) {
                    orders.add(new Orders(
                            rs.getInt("id"),
                            rs.getString("status"),
                            rs.getDouble("price"),
                            rs.getInt("persons_per_table"),
                            rs.getInt("table_number"),
                            rs.getTimestamp("date")
                    ));
                }
            }


        } catch (SQLException e) {
            Logger.getLogger(e.getMessage());
        }
    }

    private void selectRow() {
        ordersTableView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                try {
                    Connection connection = DatabaseConnection.getConnection();
                    ResultSet rs;
                    try (Statement statement = connection.createStatement()) {
                        String sql = "SELECT * FROM orders WHERE id = " + newValue.getId();
                        rs = statement.executeQuery(sql);
                        while (rs.next()) {
                            selectedOrder = new Object[]{
                                    rs.getInt("id"),
                                    rs.getString("status"),
                            };
                            newStatusValue = selectedOrder[1].toString();
                        }
                    }

                    tfOrderId.setText(String.valueOf(selectedOrder[0]));
                    mbOrderStatus.setText(selectedOrder[1].toString().substring(0, 1).toUpperCase() + selectedOrder[1].toString().substring(1));
                    vbTextFields.setVisible(true);
                    hbButtons.setVisible(true);
                } catch (SQLException e) {
                    Logger.getLogger(e.getMessage());
                }

            }

        });
    }

    private void handleMenuButton() {
        mbOrderStatus.getItems().forEach(item -> item.setOnAction(event -> {
            mbOrderStatus.setText(item.getText());
            String status = item.getText();
            switch (status) {
                case "Prepared" -> newStatusValue = "prepared";
                case "Paid" -> newStatusValue = "paid";
                case "Delivered" -> newStatusValue = "delivered";
                case "Cooking" -> newStatusValue = "cooking";
                case "Canceled" -> newStatusValue = "canceled";
                default -> newStatusValue = "ordered";
            }
        }));
    }

    @FXML
    private void handleUpdate() {
        if (selectedOrder != null) {
            try {
                Connection connection = DatabaseConnection.getConnection();
                String sql = "UPDATE orders SET status = ? WHERE id = ?";
                try (PreparedStatement statement = connection.prepareStatement(sql)) {
                    statement.setString(1, newStatusValue);
                    statement.setInt(2, Integer.parseInt(tfOrderId.getText()));
                    statement.executeUpdate();
                }
                connection.close();
                orders.clear();
                getData();
                handleCloseVbox();
                handleHboxCancel();
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Success");
                alert.setHeaderText("Order updated successfully");
                alert.showAndWait();
            } catch (SQLException e) {
                Logger.getLogger(e.getMessage());
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Error while updating order");
                alert.showAndWait();
            }
        }
    }

    @FXML
    private void handleCloseVbox() {
        vbTextFields.setVisible(false);
    }

    @FXML
    private void handleHboxCancel() {
        hbButtons.setVisible(false);
    }

    @FXML
    private void handleCancelOrder() {
        if (selectedOrder != null) {
            try {
                Connection connection = DatabaseConnection.getConnection();
                String sql = "UPDATE orders SET status = ? WHERE id = ?";
                try (PreparedStatement statement = connection.prepareStatement(sql)) {
                    statement.setString(1, "canceled");
                    statement.setInt(2, (int) selectedOrder[0]);
                    statement.executeUpdate();
                }
                connection.close();
                orders.clear();
                getData();
                handleCloseVbox();
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Success");
                alert.setHeaderText("Order canceled successfully");
                alert.showAndWait();
            } catch (SQLException e) {
                Logger.getLogger(e.getMessage());
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Error while canceling order");
                alert.showAndWait();
            }
        }
    }
}