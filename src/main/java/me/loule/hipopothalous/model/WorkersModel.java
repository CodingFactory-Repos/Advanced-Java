package me.loule.hipopothalous.model;

import javafx.beans.value.ObservableValue;

public class WorkersModel {
    private int id;
    private String lastName;
    private String firstName;
    private String post;
    private String hours;

    public WorkersModel(int id, String lastName, String firstName, String post, String hours) {
        this.id = id;
        this.lastName = lastName;
        this.firstName = firstName;
        this.post = post;
        this.hours = hours;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getPost() {
        return post;
    }

    public void setPost(String post) {
        this.post = post;
    }

    public String getHours() {
        return hours;
    }

    public void setHours(String hours) {
        this.hours = hours;
    }


}