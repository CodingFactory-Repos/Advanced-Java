package me.loule.hipopothalous.controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import me.loule.hipopothalous.model.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

class OrderDish {
    private final String name;
    private int quantity;
    private final double price;


    public OrderDish(String name, int quantity, double price) {
        this.name = name;
        this.quantity = quantity;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public int getQuantity() {
        return quantity;
    }


    @Override
    public boolean equals(Object obj) {
        if (obj instanceof OrderDish orderDish) {
            return orderDish.getName().equals(this.name);
        }
        return false;
    }

    public void incrementQuantity() {
        this.quantity++;
    }

    public void decrementQuantity() {
        this.quantity--;
    }

    public double getPrice() {
        return price;
    }

    public double getTotalPrice() {
        return price * quantity;
    }

    public String toString() {
        return name + " x" + quantity;
    }
}

public class OrdersController {

    @FXML
    Pagination dishesPagination;
    @FXML
    ListView<OrderDish> lvOrder;
    @FXML
    Button btnConfirmOrder;
    @FXML
    Label lblTotalPrice;
    @FXML
    ChoiceBox<String> tfTableNumber;
    @FXML
    TextField tfPersonNumber;
    ArrayList<OrderDish> orderDishes = new ArrayList<>();
    List<OrderDish> dishes = new ArrayList<>();

    public void initialize() {
        try {
            Connection connection = DatabaseConnection.getConnection();
            ResultSet rs;
            try (Statement statement = connection.createStatement()) {
                String sql = "SELECT * FROM dishes ORDER BY name ASC";
                rs = statement.executeQuery(sql);
                while (rs.next()) {
                    dishes.add(new OrderDish(rs.getString("name"), 1, rs.getDouble("price")));
                }

                dishesPagination.setPageCount((int) Math.ceil(dishes.size() * 1.0 / 10));
                dishesPagination.setPageFactory(param -> {
                    GridPane gridPane = new GridPane();
                    gridPane.setHgap(10);
                    gridPane.setVgap(10);

                    dishes.stream()
                            .skip(param * 10)
                            .limit(10)
                            .forEach(dish -> {
                                VBox vBox = new VBox();
                                vBox.setSpacing(10);
                                vBox.setPrefWidth(150);
                                vBox.setPrefHeight(100);
                                vBox.setStyle("-fx-background-color: #ffffff; -fx-border-color: #000000; -fx-border-width: 2px; -fx-border-radius: 5px; -fx-padding: 10px;");
                                Label lblName = new Label(dish.getName());
                                Label lblPrice = new Label(dish.getPrice() + "€");
                                vBox.getChildren().addAll(lblName, lblPrice);
                                lblName.prefWidthProperty().bind(vBox.widthProperty());
                                lblName.prefHeightProperty().bind(vBox.heightProperty());
                                lblName.setStyle("-fx-alignment: center;");
                                lblPrice.prefWidthProperty().bind(vBox.widthProperty());
                                lblPrice.prefHeightProperty().bind(vBox.heightProperty());
                                lblPrice.setStyle("-fx-alignment: center;");

                                vBox.setOnMouseClicked(event -> {
                                    OrderDish orderDish = new OrderDish(lblName.getText(), 1, dish.getPrice());
                                    if (orderDishes.contains(orderDish)) {
                                        orderDishes.get(orderDishes.indexOf(orderDish)).incrementQuantity();
                                    } else {
                                        orderDishes.add(orderDish);
                                    }
                                    lvOrder.getItems().clear();
                                    lvOrder.getItems().addAll(orderDishes);
                                    String price = String.format("%.2f", orderDishes.stream().mapToDouble(OrderDish::getTotalPrice).sum());
                                    lblTotalPrice.setText("Total: %s€".formatted(price));
                                });

                                gridPane.add(vBox, dishes.indexOf(dish) % 5, dishes.indexOf(dish) / 5);
                            });
                    return gridPane;
                });

                // Fetch all table numbers
                String tableSql = "SELECT * FROM tables";
                ResultSet tableRs = statement.executeQuery(tableSql);
                while (tableRs.next()) {
                    tfTableNumber.getItems().add(tableRs.getString("location"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void addOrder() {
        if (tfPersonNumber.getText().equals("") || orderDishes.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error adding order");
            alert.setHeaderText("Error adding order");
            alert.setContentText("Please fill in all the fields");
            alert.showAndWait();
        } else {
            Connection connection = DatabaseConnection.getConnection();
            Timestamp timestamp = new Timestamp(System.currentTimeMillis());
            try {
                double price = 0.0;
                try (Statement statement = connection.createStatement()) {
                    for (OrderDish orderDish : orderDishes) {
                        ResultSet rs = statement.executeQuery("SELECT price FROM dishes WHERE name = '" + orderDish.getName() + "'");
                        rs.next();
                        price += rs.getDouble(1) * orderDish.getQuantity();
                    }

                    String sql = "INSERT INTO orders (status, price,table_number, persons_per_table,date) VALUES (?, ?, ?, ?, ?)";
                    try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                        preparedStatement.setString(1, "pending");
                        preparedStatement.setDouble(2, Math.round(price * 100.0) / 100.0);
                        preparedStatement.setInt(3, Integer.parseInt(tfTableNumber.getValue()));
                        preparedStatement.setInt(4, Integer.parseInt(tfPersonNumber.getText()));
                        preparedStatement.setTimestamp(5, timestamp);
                        preparedStatement.executeUpdate();
                    }

                    ResultSet rs = statement.executeQuery("SELECT MAX(id) FROM orders");
                    rs.next();

                    int orderId = rs.getInt(1);
                    for (OrderDish orderDish : orderDishes) {
                        sql = "INSERT INTO order_dishes (order_id, dish_id, quantity) VALUES (" + orderId + ", (SELECT dishes_id FROM dishes WHERE name = '" + orderDish.getName() + "'), " + orderDish.getQuantity() + ")";
                        statement.executeUpdate(sql);
                    }
                }

                orderDishes.clear();
                lvOrder.getItems().clear();
                lblTotalPrice.setText("Total: 0€");
                tfPersonNumber.setText("");
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Order added");
                alert.setHeaderText("Order added");
                alert.setContentText("Order added successfully");
                alert.showAndWait();

            } catch (SQLException e) {
                e.printStackTrace();
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Error");
                alert.setContentText("Error while adding order");
                alert.showAndWait();
            }
        }
    }
}
