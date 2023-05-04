package me.loule.hipopothalous.model;

import java.sql.*;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;
import java.util.logging.Logger;

public class Accounting {

    private String type;
    private Double money;
    private Timestamp date;

    static Connection connection = DatabaseConnection.getConnection();

    public Accounting(String type, Double money, Timestamp date) {
        this.type = type;
        this.money = money;
        this.date = date;
    }

    public static void createAccounting(String type, Double money, Timestamp date) {
        String sql = "INSERT INTO accounting (type, money, date) VALUES (?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, type);
            preparedStatement.setDouble(2, money);
            preparedStatement.setTimestamp(3, date);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            Logger.getLogger(e.getMessage());
        }
    }

    public static List<Accounting> getAllAccounting(){
        String sql = "SELECT * FROM accounting";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            ResultSet rs = preparedStatement.executeQuery();
            List<Accounting> accounting = new ArrayList<>();
            while (rs.next()) {
                accounting.add(new Accounting(rs.getString("type"), rs.getDouble("money"), rs.getTimestamp("date")));
            }
            return accounting;
        } catch (SQLException e) {
            Logger.getLogger(e.getMessage());
        }
        return List.of();
    }

    public String getType() {
        return type;
    }

    public Double getMoney() {
        return money;
    }

    public Timestamp getDate() {
        return date;
    }

    @Override
    public String toString() {
        return "Accounting{" +
                "type='" + type + '\'' +
                ", money=" + money +
                ", date=" + date +
                '}';
    }
}
