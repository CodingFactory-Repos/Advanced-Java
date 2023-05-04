package me.loule.hipopothalous.controller;

import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

public class HelloController {
    @FXML
    private VBox welcomeVBox;

    @FXML
    private Pane dishesPanel;

    @FXML
    private Pane tablesPanel;

    @FXML
    private AnchorPane ordersPanel;

    //When we click on the welcomeButton, the welcomeVBox will be visible
    @FXML
    protected void onWelcomeButtonClick() {
        dishesPanel.setVisible(false);
        tablesPanel.setVisible(false);
        ordersPanel.setVisible(false);
        welcomeVBox.setVisible(true);
    }

    @FXML
    protected void onDishesButtonClick() {
        welcomeVBox.setVisible(false);
        tablesPanel.setVisible(false);
        ordersPanel.setVisible(false);
        dishesPanel.setVisible(true);

    }

    @FXML
    private void onOrdersButtonClick() {
        var isVisible = ordersPanel.isVisible();
        welcomeVBox.setVisible(false);
        dishesPanel.setVisible(false);
        tablesPanel.setVisible(false);
        ordersPanel.setVisible(!isVisible);
    }
    @FXML
    protected void onTablesButtonClick() {
        welcomeVBox.setVisible(false);
        dishesPanel.setVisible(false);
        ordersPanel.setVisible(false);
        tablesPanel.setVisible(true);
    }
}