package me.loule.hipopothalous.model;

import java.util.Date;

public class Orders {
    private float price;
    private String status;
    private Date date;
    private int dishId;
    private String table;
    private int personsPerTable;

    public Orders(float price, String status, Date date, int dishId, String table, int personsPerTable) {
        this.price = price;
        this.status = status;
        this.date = date;
        this.dishId = dishId;
        this.table = table;
        this.personsPerTable = personsPerTable;
    }
}