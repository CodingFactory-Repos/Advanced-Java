package me.loule.hipopothalous.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import me.loule.hipopothalous.model.DatabaseConnection;
import me.loule.hipopothalous.model.Orders;

import java.sql.*;

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
    TextField tfOrderTable;
    @FXML
    TextField tfOrderPersons;
    @FXML
    TextField tfOrderDate;
    @FXML
    TextField tfOrderTotal;
    @FXML
    Button btnConfirmUpdate;
    @FXML
    Button btnCloseVbox;
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
        ordersTableView.setItems(orders);

        orderStatus.setCellFactory(CheckOrders::capitalize);
        getData();
        selectRow();
        handleMenuButton();
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
            e.printStackTrace();
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
                                    rs.getDouble("price"),
                                    rs.getInt("persons_per_table"),
                                    rs.getInt("table_number"),
                                    rs.getTimestamp("date")
                            };
                        }
                    }

                    tfOrderId.setText(String.valueOf(selectedOrder[0]));
                    mbOrderStatus.setText(String.valueOf(selectedOrder[1].toString().substring(0, 1).toUpperCase() + selectedOrder[1].toString().substring(1)));
                    tfOrderTotal.setText(String.valueOf(selectedOrder[2]));
                    tfOrderPersons.setText(String.valueOf(selectedOrder[3]));
                    tfOrderTable.setText(String.valueOf(selectedOrder[4]));
                    tfOrderDate.setText(String.valueOf(selectedOrder[5]));
                    vbTextFields.setVisible(true);
                } catch (SQLException e) {
                    e.printStackTrace();
                }

            }

        });
    }

    private void handleMenuButton() {
        mbOrderStatus.getItems().forEach(item -> item.setOnAction(event -> {
            mbOrderStatus.setText(item.getText());
            String status = item.getText();
            if (status.equals("Pending"))
                newStatusValue = "pending";
            else if (status.equals("Paid"))
                newStatusValue = "paid";
            else if (status.equals("Delivered"))
                newStatusValue = "delivered";
            else if (status.equals("Cooking"))
                newStatusValue = "cooking";
            else if (status.equals("Canceled"))
                newStatusValue = "canceled";
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
                    statement.setInt(2, (int) selectedOrder[0]);
                    statement.executeUpdate();
                }
                connection.close();
                orders.clear();
                getData();
                handleCloseVbox();
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Success");
                alert.setHeaderText("Order updated successfully");
                alert.showAndWait();
            } catch (SQLException e) {
                e.printStackTrace();
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
}