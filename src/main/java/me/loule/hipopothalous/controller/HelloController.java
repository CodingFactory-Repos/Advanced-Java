package me.loule.hipopothalous.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;

public class HelloController {
    @FXML
    private Label welcomeText;

    @FXML
    private Button dishesButton;

    @FXML
    private AnchorPane dishesPanel;



    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("Welcome to JavaFX Application!");
    }

    @FXML
    protected void onDishesButtonClick() {
        dishesPanel.setVisible(true);
    }
}
