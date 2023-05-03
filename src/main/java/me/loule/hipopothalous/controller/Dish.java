package me.loule.hipopothalous.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import me.loule.hipopothalous.model.DishesModel;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;


public class Dish implements Initializable {

    public Button addDishesButton;

    public List<DishesModel> dishes;
    public TextField nameTextField;
    public TextField descriptionTextField;
    public TextField priceTextField;
    public TextField pictureTextField;
    public Button validateButton;
    public HBox addDishesHBox;
    @FXML
    public VBox listVBox;
    @FXML
    public AnchorPane dishAnchorPane;

    public DishComponent dishComponent;
    public ScrollPane listScrollPane;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        dishes = DishesModel.getAllDish();
        assert dishes != null;
        dishes.stream().forEach(dish -> {
//            System.out.println(dish.getName());
            dishComponent = new DishComponent(dish.getImage(), dish.getName(), dish.getPrice(), dish.getDescription());
            listVBox.getChildren().add(dishComponent);
        });
    }

    @FXML
    protected void onAddDishesButtonClick() {
        addDishesHBox.setVisible(!addDishesHBox.isVisible());
        nameTextField.setText("");
        descriptionTextField.setText("");
        priceTextField.setText("");
        pictureTextField.setText("");
        listScrollPane.setVisible(!listScrollPane.isVisible());
    }

    @FXML
    protected void addDishes() {
        DishesModel.addDish(nameTextField.getText(), descriptionTextField.getText(), Double.parseDouble(priceTextField.getText()), pictureTextField.getText());
        dishComponent = new DishComponent(pictureTextField.getText(), nameTextField.getText(), Double.parseDouble(priceTextField.getText()), descriptionTextField.getText());
        listVBox.getChildren().add(dishComponent);
        addDishesHBox.setVisible(!addDishesHBox.isVisible());
        listScrollPane.setVisible(!listScrollPane.isVisible());
    }
}
