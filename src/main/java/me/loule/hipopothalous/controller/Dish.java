package me.loule.hipopothalous.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import me.loule.hipopothalous.model.DishesModel;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;


public class Dish implements Initializable {

    public Button addDishesButton;

    public List<DishesModel> dishes;
    @FXML
    private VBox listVBox;
    @FXML
    private AnchorPane dishAnchorPane;

    private DishComponent dishComponent;

//    public Dish() {
//
//    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        dishes = DishesModel.getAllDish();
        assert dishes != null;
        dishes.stream().forEach(dish -> {
            System.out.println(dish.getName());
            dishComponent = new DishComponent(dish.getImage(), dish.getName(), dish.getPrice());
            listVBox.getChildren().add(dishComponent);
        });
    }
}
