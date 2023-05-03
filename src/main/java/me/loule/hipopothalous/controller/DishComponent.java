package me.loule.hipopothalous.controller;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

public class DishComponent extends HBox {
    public ImageView dishImageView;
    public Label dishNameLabel;
    public Label dishPriceLabel;
    public Button dishDetailButton;

    public DishComponent(String dishImage, String dishName, Double dishPrice) {
        dishImageView = new ImageView(new Image(dishImage));
        dishImageView.setFitHeight(50);
        dishImageView.setFitWidth(50);
        dishNameLabel = new Label(dishName);
        dishPriceLabel = new Label(dishPrice.toString());
        dishDetailButton = new Button("Details");

        this.getChildren().addAll(dishImageView, dishNameLabel, dishPriceLabel, dishDetailButton);
        this.setAlignment(javafx.geometry.Pos.CENTER);
        this.setSpacing(100);
    }


}
