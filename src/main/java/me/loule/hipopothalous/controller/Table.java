package me.loule.hipopothalous.controller;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Table {
    private final IntegerProperty size;
    private final StringProperty location;

    public Table(int size, String location) {
        this.size = new SimpleIntegerProperty(size);
        this.location = new SimpleStringProperty(location);
    }

    public int getSize() {
        return size.get();
    }

    public void setSize(int size) {
        this.size.set(size);
    }

    public IntegerProperty sizeProperty() {
        return size;
    }

    public String getLocation() {
        return location.get();
    }

    public void setLocation(String location) {
        this.location.set(location);
    }

    public StringProperty locationProperty() {
        return location;
    }

    @Override
    public String toString() {
        return "Table{" +
                "size=" + size +
                ", location=" + location +
                '}';
    }
}
