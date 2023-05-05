package me.loule.hipopothalous.controller;

import javafx.event.ActionEvent;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.util.Pair;
import me.loule.hipopothalous.model.DatabaseConnection;
import me.loule.hipopothalous.model.Dishes;
import me.loule.hipopothalous.model.Orders;

import java.sql.*;
import java.sql.Date;
import java.util.*;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class Dashboard {

    public ScrollPane listScrollPane;
    private String dishName;
    private String dishDescription;
    private Double dishPrice;
    static Connection connection = DatabaseConnection.getConnection();
    List<Dishes> localDishes = new ArrayList<>();

    List<Orders> localorders = new ArrayList<>();


    public static void addDish(String name, String description, Double price) {
        String sql = "INSERT INTO dishes (name, description, price, image) VALUES (?, ?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, description);
            preparedStatement.setDouble(3, price);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            Logger.getLogger(e.getMessage());
        }
    }

    public static List<Dishes> getAllDish() {
        String sql = "SELECT * FROM dishes";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            ResultSet rs = preparedStatement.executeQuery();
            List<Dishes> dishes = new ArrayList<>();
            while (rs.next()) {
                dishes.add(new Dishes(rs.getString("name"), rs.getString("description"), rs.getDouble("price"), rs.getString("image")));
            }
            return dishes;
        } catch (SQLException e) {
            Logger.getLogger(e.getMessage());
        }
        return List.of();
    }
    public void priceOrdreDesc(ActionEvent actionEvent) {
        try {
            Connection connection = DatabaseConnection.getConnection();
            ResultSet rs;
            try (Statement statement = connection.createStatement()) {
                String sql = "SELECT * FROM dishes ORDER BY price DESC";
                rs = statement.executeQuery(sql);
                if (rs.next()) {
                    String name = rs.getString("name");
                    double price = rs.getDouble("price");
                    Label nameLabel = new Label(name + " - " + price + "€");
                    nameLabel.setPrefWidth(228);
                    nameLabel.setWrapText(true);
                    listScrollPane.setContent(nameLabel);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void priceOrdreAsc(ActionEvent actionEvent) {
        try {
            Connection connection = DatabaseConnection.getConnection();
            ResultSet rs;
            try (Statement statement = connection.createStatement()) {
                String sql = "SELECT * FROM dishes ORDER BY price ASC";
                rs = statement.executeQuery(sql);
                if (rs.next()) {
                    String name = rs.getString("name");
                    double price = rs.getDouble("price");
                    Label nameLabel = new Label(name + " - " + price + "€");
                    nameLabel.setPrefWidth(228);
                    nameLabel.setWrapText(true);
                    listScrollPane.setContent(nameLabel);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void totalPrice(ActionEvent event) {
        try {
            Connection connection = DatabaseConnection.getConnection();
            ResultSet rs;
            try (Statement statement = connection.createStatement()) {
                String sql = "SELECT * FROM dishes ORDER BY name ASC";
                rs = statement.executeQuery(sql);
                double totalPrice = 0.0; // Initialisation du prix total
                while (rs.next()) {
                    String name = rs.getString("name");
                    double price = rs.getDouble("price");
                    localDishes.add(new Dishes(name, rs.getString("description"), price, rs.getString("image")));
                    totalPrice += price;
                }
                Label totalPriceLabel = new Label("Prix total : " + totalPrice + "€");
                totalPriceLabel.setPrefWidth(228);
                listScrollPane.setContent(totalPriceLabel);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void selectedCommandInProgress(ActionEvent event) {
        try {
            Connection connection = DatabaseConnection.getConnection();
            ResultSet rs;
            try (Statement statement = connection.createStatement()) {
                String sql = "SELECT * FROM orders";
                rs = statement.executeQuery(sql);
                System.out.println("Je suis la");
                List<Orders> localorders = new ArrayList<>();
                VBox container = new VBox();
                while (rs.next()) {
                    String status = rs.getString("status");
                    if (status.equals("delivered")) {
                        int price = rs.getInt("price");
                        Timestamp date = rs.getTimestamp("date");
                        int dishes = rs.getInt("id");
                        int table = rs.getInt("table_number");
                        int personInTable = rs.getInt("persons_Per_Table");
                        localorders.add(new Orders(dishes,rs.getString("status"),price, table, personInTable,date));
                        Label totalPriceLabel = new Label("Table :"+table + " Status:" + status +" Dates :" + date);
                        System.out.println(status);
                        totalPriceLabel.setPrefWidth(228);
                        container.getChildren().add(totalPriceLabel);
                    }
                }
                listScrollPane.setContent(container);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void selectedCommandPerDate(ActionEvent event) {
        try {
            Connection connection = DatabaseConnection.getConnection();
            ResultSet rs;
            try (Statement statement = connection.createStatement()) {
                String sql = "SELECT * FROM orders ORDER BY status ASC";
                rs = statement.executeQuery(sql);
                System.out.println("Je suis la");
                List<Orders> localorders = new ArrayList<>();
                VBox container = new VBox();
                while (rs.next()) {
                    String status = rs.getString("status");
                        float price = rs.getFloat("price");
                        Timestamp date = rs.getTimestamp("date");
                        int dishes = rs.getInt("id");
                        int table = rs.getInt("table_number");
                        int personInTable = rs.getInt("persons_Per_Table");
                    localorders.add(new Orders(dishes,rs.getString("status"),price, table, personInTable,date));
                }
                List<Orders> sortedOrders = localorders.stream()
                        .sorted(Comparator.comparing(Orders::getDate))
                        .collect(Collectors.toList());
                for (Orders order : sortedOrders) {
                    int table = order.getTableNumber();
                    String status = order.getStatus();
                    Label totalPriceLabel = new Label("Table: " + table + " | Status: " + status);
                    System.out.println(status);
                    totalPriceLabel.setPrefWidth(228);
                    container.getChildren().add(totalPriceLabel);
                }
                listScrollPane.setContent(container);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void selectPriceInTable(ActionEvent event) {
        try {
            Connection connection = DatabaseConnection.getConnection();
            ResultSet rs;
            try (Statement statement = connection.createStatement()) {
                String sql = "SELECT * FROM orders ORDER BY status ASC";
                rs = statement.executeQuery(sql);
                List<Orders> localorders = new ArrayList<>();
                VBox container = new VBox();
                while (rs.next()) {
                    String status = rs.getString("status");
                    float price = rs.getFloat("price");
                    Timestamp date = rs.getTimestamp("date");
                    int dishes = rs.getInt("id");
                    int table = rs.getInt("table_number");
                    int personInTable = rs.getInt("persons_Per_Table");
                    localorders.add(new Orders(dishes,rs.getString("status"),price, table, personInTable,date));
                }

                Map<String, Float> tablePriceMap = new HashMap<>();

                for (Orders order : localorders) {
                    int table = order.getTableNumber();
                    String status = order.getStatus();
                    double price = order.getPrice();
                    if (status.equals("pending") || status.equals("ordered")|| status.equals("delivered")) {
                        float tablePrice = tablePriceMap.getOrDefault(table, 0.0f);
                        tablePrice += price;
                        tablePriceMap.put(String.valueOf(table), tablePrice);
                    }
                }

                List<String> sortedTables = new ArrayList<>(tablePriceMap.keySet());
                Collections.sort(sortedTables);

                for (String table : sortedTables) {
                    Label totalPriceLabel = new Label("Table: " + table + " | Total Price: " + tablePriceMap.get(table));
                    totalPriceLabel.setPrefWidth(228);
                    container.getChildren().add(totalPriceLabel);
                }
                listScrollPane.setContent(container);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void pointerPriceIndividual(ActionEvent event) {
        try {
            Connection connection = DatabaseConnection.getConnection();
            ResultSet rs;
            try (Statement statement = connection.createStatement()) {
                String sql = "SELECT * FROM orders ORDER BY status ASC";
                rs = statement.executeQuery(sql);
                List<Orders> localorders = new ArrayList<>();
                VBox container = new VBox();
                while (rs.next()) {
                    String status = rs.getString("status");
                    float price = rs.getFloat("price");
                    Timestamp date = rs.getTimestamp("date");
                    int dishes = rs.getInt("id");
                    int table = rs.getInt("table_number");
                    int personInTable = rs.getInt("persons_Per_Table");
                    localorders.add(new Orders(dishes,rs.getString("status"),price, table, personInTable,date));
                }

                List<Pair<Integer, Double>> tablePriceList = new ArrayList<>();

                for (Orders order : localorders) {
                    int table = order.getTableNumber();
                    String status = order.getStatus();
                    double price = order.getPrice();
                    if (status.equals("paid")) {
                        tablePriceList.add(new Pair<>(table, price));
                    }
                }

                for (Pair<Integer, Double> tablePrice : tablePriceList) {
                    Label totalPriceLabel = new Label("Table: " + tablePrice.getKey() + " | Price paid: " + tablePrice.getValue());
                    totalPriceLabel.setPrefWidth(228);
                    container.getChildren().add(totalPriceLabel);
                }

                listScrollPane.setContent(container);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


}
