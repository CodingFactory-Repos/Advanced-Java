package me.loule.hipopothalous.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import me.loule.hipopothalous.model.DishesModel;

public class HelloController {
    @FXML
    private Label welcomeText;

    @FXML
    private Button dishesButton;

    @FXML
    private Pane dishesPanel;

    @FXML
    private Pane tablesPanel;

    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("Welcome to JavaFX Application!");
    }

    //When whe click on dishesButton, the dishesPanel will be visible
    @FXML
    protected void onDishesButtonClick() {
        tablesPanel.setVisible(false);
        dishesPanel.setVisible(true);
    }

    //When we click on the validateButton, the dish will be added to the database
    @FXML
    protected void onTablesButtonClick() {
        dishesPanel.setVisible(false);
        tablesPanel.setVisible(true);
    }
}
