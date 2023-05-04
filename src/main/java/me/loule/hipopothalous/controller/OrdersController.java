package me.loule.hipopothalous.controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import me.loule.hipopothalous.model.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.Calendar;
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

                dishesPagination.setPageCount(dishes.size() / 10);
                dishesPagination.setPageFactory(param -> {
                    GridPane gridPane = new GridPane();
                    gridPane.setHgap(10);
                    gridPane.setVgap(10);

                    for (int i = 0; i < dishes.size(); i++) {
                        if (param * 10 + i < dishes.size()) {
                            Button btn = new Button(dishes.get(param * 10 + i).getName());
                            btn.setPrefWidth(100);
                            btn.setPrefHeight(100);
                            int finalI = i;
                            btn.setOnAction(event -> {
                                OrderDish orderDish = new OrderDish(btn.getText(), 1,  dishes.get(param * 10 + finalI).getPrice());
                                if (orderDishes.contains(orderDish)) {
                                    orderDishes.get(orderDishes.indexOf(orderDish)).incrementQuantity();
                                } else {
                                    orderDishes.add(orderDish);
                                }
                                lvOrder.getItems().clear();
                                lvOrder.getItems().addAll(orderDishes);
                                String price = String.format("%.2f", orderDishes.stream().mapToDouble(OrderDish::getTotalPrice).sum());
                                lblTotalPrice.setText("Total: " + price + "€");


                            });
                            gridPane.add(btn, i % 5, i / 5);
                        }
                    }
                    return gridPane;
                });
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }


        // on listview item press decrement quantity
        lvOrder.setOnMouseClicked(event1 -> {
            if (event1.getClickCount() == 2) {
                OrderDish orderDish1 = lvOrder.getSelectionModel().getSelectedItem();
                if (orderDish1.getQuantity() > 1) {
                    orderDish1.decrementQuantity();
                    lblTotalPrice.setText("Total: " + String.format("%.2f", orderDishes.stream().mapToDouble(OrderDish::getTotalPrice).sum()) + "€");
                } else {
                    orderDishes.remove(orderDish1);
                    lblTotalPrice.setText("Total: " + String.format("%.2f", orderDishes.stream().mapToDouble(OrderDish::getTotalPrice).sum()) + "€");
                }
                lvOrder.getItems().clear();
                lvOrder.getItems().addAll(orderDishes);
            }
        });

    }


    public void addOrder() {
        try {
            Connection connection = DatabaseConnection.getConnection();
            try (Statement statement = connection.createStatement()) {
                double price = 0.0;
                for (OrderDish orderDish : orderDishes) {
                    ResultSet rs = statement.executeQuery("SELECT price FROM dishes WHERE name = '" + orderDish.getName() + "'");
                    rs.next();
                    price += rs.getDouble(1) * orderDish.getQuantity();
                }

                String sql = "INSERT INTO orders (status, price,table_number, persons_per_table,date) VALUES ('ordered', " + Math.round(price * 100.0) / 100.0 + ", 1, 1, " + "'" + new Timestamp(Calendar.getInstance().getTimeInMillis()) + "'" + ")";
                statement.executeUpdate(sql);
                ResultSet rs = statement.executeQuery("SELECT MAX(id) FROM orders");
                rs.next();
                int orderId = rs.getInt(1);
                for (OrderDish orderDish : orderDishes) {
                    sql = "INSERT INTO order_dishes (order_id, dish_id, quantity) VALUES (" + orderId + ", (SELECT dishes_id FROM dishes WHERE name = '" + orderDish.getName() + "'), " + orderDish.getQuantity() + ")";
                    statement.executeUpdate(sql);
                }

                orderDishes.clear();
                lvOrder.getItems().clear();
                lblTotalPrice.setText("Total: 0€");
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Order added");
                alert.setHeaderText("Order added");
                alert.setContentText("Order added successfully");
                alert.showAndWait();
            }

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