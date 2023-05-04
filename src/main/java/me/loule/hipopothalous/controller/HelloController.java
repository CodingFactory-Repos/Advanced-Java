package me.loule.hipopothalous.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import me.loule.hipopothalous.model.DishesModel;

public class HelloController {
    @FXML
    private VBox welcomeVBox;

    @FXML
    private Pane dishesPanel;

    @FXML
    private Pane tablesPanel;

    //When we click on the welcomeButton, the welcomeVBox will be visible
    @FXML
    protected void onWelcomeButtonClick() {
        dishesPanel.setVisible(false);
        tablesPanel.setVisible(false);
        welcomeVBox.setVisible(true);
    }

    //When whe click on dishesButton, the dishesPanel will be visible
    @FXML
    protected void onDishesButtonClick() {
        welcomeVBox.setVisible(false);
        tablesPanel.setVisible(false);
        dishesPanel.setVisible(true);
    }

    //When we click on the validateButton, the dish will be added to the database
    @FXML
    protected void onTablesButtonClick() {
        welcomeVBox.setVisible(false);
        dishesPanel.setVisible(false);
        tablesPanel.setVisible(true);
    }
}
