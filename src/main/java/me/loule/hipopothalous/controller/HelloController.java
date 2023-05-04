package me.loule.hipopothalous.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;

public class HelloController {
    @FXML
    private AnchorPane ordersPanel;

    @FXML
    private Button dishesButton;

    @FXML
    private AnchorPane dishesPanel;



    @FXML
    protected void onDishesButtonClick() {
        dishesPanel.setVisible(true);
    }

    @FXML
    private void showOrders() {
        var isVisible = ordersPanel.isVisible();
        ordersPanel.setVisible(!isVisible);
    }

}