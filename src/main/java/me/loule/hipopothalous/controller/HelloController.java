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

import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.Consumer;

public class HelloController implements Initializable {

    @FXML
    private Pane ordersCheckPanel;
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
    private AnchorPane ordersPanel;

    @FXML
    private AnchorPane workersPane;

    //When we click on the welcomeButton, the welcomeVBox will be visible

    /**
     * This function show the welcome vbox and hide everything else
     */
    @FXML
    protected void onWelcomeButtonClick() {
        dishesPanel.setVisible(false);
        tablesPanel.setVisible(false);
        ordersPanel.setVisible(false);
        ordersCheckPanel.setVisible(false);
        workersPane.setVisible(false);
        welcomeVBox.setVisible(true);
    }

    /**
     * This function show the dishes panel and hide everything else
     */
    @FXML
    protected void onDishesButtonClick() {
        welcomeVBox.setVisible(false);
        tablesPanel.setVisible(false);
        ordersPanel.setVisible(false);
        ordersCheckPanel.setVisible(false);
        workersPane.setVisible(false);
        dishesPanel.setVisible(true);

    }

    /**
     * This function show the order panel and hide everything else
     */
    @FXML
    private void onOrdersButtonClick() {
        welcomeVBox.setVisible(false);
        dishesPanel.setVisible(false);
        tablesPanel.setVisible(false);
        ordersCheckPanel.setVisible(false);
        workersPane.setVisible(false);
        ordersPanel.setVisible(true);
    }

    /**
     * This function show the orders check panel and hide everything else
     */
    @FXML
    protected void onOrdersCheckButtonClick() {
        welcomeVBox.setVisible(false);
        dishesPanel.setVisible(false);
        tablesPanel.setVisible(false);
        ordersPanel.setVisible(false);
        workersPane.setVisible(false);
        ordersCheckPanel.setVisible(true);
    }

    /**
     * This function show the tables panel and hide everything else
     */
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
    protected void onWorkersButtonClick() {
        welcomeVBox.setVisible(false);
        dishesPanel.setVisible(false);
        ordersPanel.setVisible(false);
        ordersCheckPanel.setVisible(false);
        tablesPanel.setVisible(false);
        workersPane.setVisible(true);
    }

    /**
     * This function is used to start the timer
     */
    public void setTimer(){
        KeyFrame refreshView = new KeyFrame(Duration.millis(1000), e -> refreshAll());
        Timeline refreshTimeline= new Timeline((refreshView));
        refreshTimeline.setCycleCount((Timeline.INDEFINITE));
        refreshTimeline.play();
    }

    /**
     * This function is used to disable button when under 15min of the timer
     */
    private void refreshAll(){
        if (Chronometre.minutes <=15){
            forEachButton(parentHbox,button -> button.setDisable(true));
        }
        else{
            forEachButton(parentHbox,button -> button.setDisable(false));
        }
        timerLbl.setText(String.valueOf(Chronometre.minutes) + ":" + String.valueOf(Chronometre.seconds));
    }


    /**
     * @param root
     * @param action
     * This function is used in order to navigate in every button
     */
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


    /**
     * @param url            The location used to resolve relative paths for the root object, or
     *                       {@code null} if the location is not known.
     * @param resourceBundle The resources used to localize the root object, or {@code null} if
     *                       the root object was not localized.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Chronometre = new Chronometry();
        setTimer();
    }

    /**
     * @param actionEvent
     *
     */
    public void changeService(ActionEvent actionEvent) {
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