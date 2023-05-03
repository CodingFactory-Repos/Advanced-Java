package me.loule.hipopothalous.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.ResourceBundle;



public class HelloController implements Initializable {
    @FXML
    private Label welcomeText;

    @FXML
    private Label minutesLbl;

    @FXML
    private Label secondLbl;


    public Chronometry Chronometre;

    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("Welcome to JavaFX Application!");
    }

    @FXML
    public void OnClick(ActionEvent actionEvent) {



    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Chronometre = new Chronometry();
        Chronometre.initializeChronometry(false);


    }
}
