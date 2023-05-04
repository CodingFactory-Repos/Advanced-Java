package me.loule.hipopothalous.controller;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

import java.lang.reflect.Type;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.Consumer;

public class HelloController implements Initializable {

    public AnchorPane workersPane;
    private Chronometry Chronometre;

    @FXML
    private VBox welcomeVBox;

    @FXML
    private HBox parentHbox;

    @FXML
    private Pane dishesPanel;

    @FXML
    private Label timerLbl;

    @FXML
    private Pane tablesPanel;

    @FXML
    private Pane ordersCheckPanel;

    @FXML
    private AnchorPane ordersPanel;

    //When we click on the welcomeButton, the welcomeVBox will be visible

    @FXML
    protected void onWelcomeButtonClick() {
        dishesPanel.setVisible(false);
        tablesPanel.setVisible(false);
        ordersPanel.setVisible(false);
        ordersCheckPanel.setVisible(false);
        workersPane.setVisible(false);
        welcomeVBox.setVisible(true);
    }

    //When whe click on dishesButton, the dishesPanel will be visible
    @FXML
    protected void onDishesButtonClick() {
        welcomeVBox.setVisible(false);
        tablesPanel.setVisible(false);
        ordersPanel.setVisible(false);
        ordersCheckPanel.setVisible(false);
        workersPane.setVisible(false);
        dishesPanel.setVisible(true);

    }

    @FXML
    private void onOrdersButtonClick() {
        var isVisible = ordersPanel.isVisible();
        welcomeVBox.setVisible(false);
        dishesPanel.setVisible(false);
        tablesPanel.setVisible(false);
        workersPane.setVisible(false);
        ordersCheckPanel.setVisible(false);
        ordersPanel.setVisible(!isVisible);
    }
    @FXML
    protected void onTablesButtonClick() {
        welcomeVBox.setVisible(false);
        dishesPanel.setVisible(false);
        ordersPanel.setVisible(false);
        ordersCheckPanel.setVisible(false);
        workersPane.setVisible(false);
        tablesPanel.setVisible(true);
    }

    @FXML
    protected void onOrdersCheckButtonClick() {
        welcomeVBox.setVisible(false);
        dishesPanel.setVisible(false);
        tablesPanel.setVisible(false);
        ordersPanel.setVisible(false);
        workersPane.setVisible(false);
        ordersCheckPanel.setVisible(true);
    }

    @FXML
    protected void onWorkersButtonClick() {
        welcomeVBox.setVisible(false);
        dishesPanel.setVisible(false);
        tablesPanel.setVisible(false);
        ordersPanel.setVisible(false);
        ordersCheckPanel.setVisible(false);
        workersPane.setVisible(true);
    }

    public void SetTimer(){
        KeyFrame RefreshView = new KeyFrame(Duration.millis(1000), e -> RefreshAll());
        Timeline refreshTimeline= new Timeline((RefreshView));
        refreshTimeline.setCycleCount((Timeline.INDEFINITE));
        refreshTimeline.play();
    }

    private void RefreshAll(){
        if (Chronometre.minutes <=15){
            forEachButton(parentHbox,button -> button.setDisable(true));
        }
        else{
            forEachButton(parentHbox,button -> button.setDisable(false));
        }
        timerLbl.setText(String.valueOf(Chronometre.minutes) + ":" + String.valueOf(Chronometre.seconds));
    }


    public void forEachButton(Parent root, Consumer<Button> action){ // Methode permettant de naviguer dans tout les boutons d'un parent jusqu'à l'enfant.
        for (Node node : root.getChildrenUnmodifiable()){
            if (node instanceof Button){
                action.accept((Button)node);
            }
            else if (node instanceof Parent){
                forEachButton((Parent) node,action);
            }
        }
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Chronometre = new Chronometry();
        SetTimer();
    }

    public void ChangeService(ActionEvent actionEvent) {
        if (Chronometre.ChronoThread != null) {
            if(Chronometre.ChronoThread.isAlive()){
                Chronometre.ChronoThread.interrupt();
            }
        }
        Chronometre.isServiceChrono=!Chronometre.isServiceChrono;
        Chronometre.initializeChronometry(Chronometre.isServiceChrono);
        Chronometre.ChronoThread.start();
    }
}