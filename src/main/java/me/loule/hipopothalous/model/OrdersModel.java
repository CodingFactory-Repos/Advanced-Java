package me.loule.hipopothalous.model;

import javafx.beans.property.*;
import javafx.beans.value.ObservableValue;

import java.sql.Timestamp;

public class OrdersModel {
    private IntegerProperty id;
    private StringProperty status;
    private DoubleProperty price;
    private IntegerProperty tableNumber;
    private IntegerProperty personsPerTable;
    private Timestamp date;

    public OrdersModel(int id, String status, double price, int tableNumber, int personsPerTable, Timestamp date) {
        this.id = new SimpleIntegerProperty(id);
        this.status = new SimpleStringProperty(status);
        this.price = new SimpleDoubleProperty(price);
        this.tableNumber = new SimpleIntegerProperty(tableNumber);
        this.personsPerTable = new SimpleIntegerProperty(personsPerTable);
        this.date = date;
    }

    public int getId() {
        return id.get();
    }

    public IntegerProperty idProperty() {
        return id;
    }

    public void setId(int id) {
        this.id.set(id);
    }

    public String getStatus() {
        return status.get();
    }

    public StringProperty statusProperty() {
        return status;
    }

    public void setStatus(String status) {
        this.status.set(status);
    }

    public double getPrice() {
        return price.get();
    }

    public DoubleProperty priceProperty() {
        return price;
    }

    public void setPrice(double price) {
        this.price.set(price);
    }

    public int getTableNumber() {
        return tableNumber.get();
    }

    public IntegerProperty tableNumberProperty() {
        return tableNumber;
    }

    public void setTableNumber(int tableNumber) {
        this.tableNumber.set(tableNumber);
    }

    public int getPersonsPerTable() {
        return personsPerTable.get();
    }

    public IntegerProperty personsPerTableProperty() {
        return personsPerTable;
    }

    public void setPersonsPerTable(int personsPerTable) {
        this.personsPerTable.set(personsPerTable);
    }

    public Timestamp getDate() {
        return date;
    }

    public void setDate(Timestamp date) {
        this.date = date;
    }

    public ObservableValue<Timestamp> dateProperty() {
        return new SimpleObjectProperty<>(date);
    }



}