package me.loule.hipopothalous.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import me.loule.hipopothalous.model.DishesModel;

import java.net.URL;
import java.util.Comparator;
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
    public DishComponent dishComponent;
    public ScrollPane listScrollPane;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        dishes = DishesModel.getAllDish();
        assert dishes != null;
        dishes.stream()
                .sorted(Comparator.comparing(DishesModel::getName))
                .forEach(dish -> {
                    System.out.println(dish.getName());
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
        String dishName = nameTextField.getText();
        String dishDescription = descriptionTextField.getText();
        String dishPrice = priceTextField.getText();
        String dishPicture = pictureTextField.getText();

        //If the picture is empty we set a default picture
        if(dishPicture.isEmpty()) dishPicture = "https://upload.wikimedia.org/wikipedia/commons/thumb/6/65/No-Image-Placeholder.svg/1665px-No-Image-Placeholder.svg.png";

        //We check if all the fields are filled, if not we show an error message
        if (dishName.isEmpty() || dishDescription.isEmpty() || dishPrice.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "", ButtonType.OK);
            alert.setTitle("Error");
            alert.setHeaderText("Please fill all the fields");
            alert.showAndWait();
            return;
        }
        
        //We check if the price is a number, if it not we show an error message
        try {
            Double.valueOf(dishPrice);
        } catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "", ButtonType.OK);
            alert.setTitle("Error");
            alert.setHeaderText("Please enter a number for the price");
            alert.showAndWait();
            return;
        }


        DishesModel.addDish(dishName, dishDescription, Double.valueOf(dishPrice), dishPicture);
        dishComponent = new DishComponent(dishPicture, dishName, Double.valueOf(dishPrice), dishDescription);

        dishes.add(new DishesModel(dishName, dishDescription, Double.valueOf(dishPrice), dishPicture));

        listVBox.getChildren().add(dishComponent);

        addDishesHBox.setVisible(!addDishesHBox.isVisible());
        listScrollPane.setVisible(!listScrollPane.isVisible());
    }
}
