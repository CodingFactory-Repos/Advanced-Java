package me.loule.hipopothalous.model;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class DishesModel {
    private String name;
    private String description;
    private Double price;
    private String image;
    static Connection connection = DatabaseConnection.getConnection();


    public DishesModel( String name, String description, Double price, String image) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.image = image;
    }

    public static void addDish(String name, String description, Double price, String image) {
        try {
            try (Statement statement = connection.createStatement()) {
                statement.executeUpdate("INSERT INTO dishes (name, description, price, image) VALUES ('" + name + "', '" + description + "', " + price + ", '" + image + "')");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static List<DishesModel> getAllDish(){
        try {
            try (Statement statement = connection.createStatement()) {
                ResultSet rs = statement.executeQuery("SELECT * FROM dishes");
                List<DishesModel> dishes = new ArrayList<>();
                while (rs.next()) {
                    dishes.add(new DishesModel(rs.getString("name"), rs.getString("description"), rs.getDouble("price"), rs.getString("image")));
                }
                return dishes;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }


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
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", price=" + price +
                ", image='" + image + '\'' +
                '}';
    }
}
