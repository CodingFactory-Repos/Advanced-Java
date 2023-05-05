package me.loule.hipopothalous.model;
public class WorkersModel {
    private int id;
    private String lastName;
    private String firstName;
    private String post;
    private int deal_hours;

    public WorkersModel(int id, String lastName, String firstName, String post, int deal_hours) {
        this.id = id;
        this.lastName = lastName;
        this.firstName = firstName;
        this.post = post;
        this.deal_hours = deal_hours;
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

    public int getDeal_hours() {
        return deal_hours;
    }

    public void setDeal_hours(int deal_hours) {
        this.deal_hours = deal_hours;
    }
}