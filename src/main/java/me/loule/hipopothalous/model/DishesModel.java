package me.loule.hipopothalous.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class DishesModel {
    private String name;
    private String description;
    private Double price;
    private String image;
    static Connection connection = DatabaseConnection.getConnection();

    public DishesModel(String name, String description, Double price, String image) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.image = image;
    }

    public static void addDish(String name, String description, Double price, String image) {
        String sql = "INSERT INTO dishes (name, description, price, image) VALUES (?, ?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, description);
            preparedStatement.setDouble(3, price);
            preparedStatement.setString(4, image);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            Logger.getLogger(e.getMessage());
        }
    }

    public static List<DishesModel> getAllDish() {
        String sql = "SELECT * FROM dishes";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            ResultSet rs = preparedStatement.executeQuery();
            List<DishesModel> dishes = new ArrayList<>();
            while (rs.next()) {
                dishes.add(new DishesModel(rs.getString("name"), rs.getString("description"), rs.getDouble("price"), rs.getString("image")));
            }
            return dishes;
        } catch (SQLException e) {
            Logger.getLogger(e.getMessage());
        }
        return List.of();
    }

    // Getters and setters

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    @Override
    public String toString() {
        return "DishesModel{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", price=" + price +
                ", image='" + image + '\'' +
                '}';
    }
}
