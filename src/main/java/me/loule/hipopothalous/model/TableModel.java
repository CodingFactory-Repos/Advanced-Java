package me.loule.hipopothalous.model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.sql.Timestamp;

public class TableModel {
    private final IntegerProperty id;
    private final IntegerProperty size;
    private final StringProperty location;
    private final Timestamp date;


    public TableModel(int id, int size, String location, Timestamp date) {
        this.id = new SimpleIntegerProperty(id);
        this.size = new SimpleIntegerProperty(size);
        this.location = new SimpleStringProperty(location);
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

    public int getSize() {
        return size.get();
    }

    public IntegerProperty sizeProperty() {
        return size;
    }

    public void setSize(int size) {
        this.size.set(size);
    }

    public String getLocation() {
        return location.get();
    }

    public StringProperty locationProperty() {
        return location;
    }

    public void setLocation(String location) {
        this.location.set(location);
    }

    public Timestamp getDate() {
        return date;
    }

    @Override
    public String toString() {
        return "TableModel{" +
                "id=" + id +
                ", size=" + size +
                ", location=" + location +
                '}';
    }
}
