package me.loule.hipopothalous.controller;

import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class DishComponent extends HBox {
    public ImageView dishImageView;
    public Label dishNameLabel;
    public Label dishPriceLabel;
    public Label dishDescriptionLabel;
    public Button dishDetailButton;

    public DishComponent(String dishImage, String dishName, Double dishPrice , String dishDescription) {
        dishImageView = new ImageView(new Image(dishImage));
        dishImageView.setFitHeight(50);
        dishImageView.setFitWidth(50);
        dishNameLabel = new Label(dishName);
        dishPriceLabel = new Label(dishPrice.toString());
        dishDescriptionLabel = new Label(dishDescription);
        dishDetailButton = new Button("Details");

        this.getChildren().addAll(dishImageView, dishNameLabel, dishPriceLabel, dishDetailButton);
        this.setAlignment(javafx.geometry.Pos.CENTER);
        this.setSpacing(100);

        dishDetailButton.setOnAction(event -> onDishDetailButtonClick());
    }

    //Sout when we click on the dishDetailButton
    public void onDishDetailButtonClick() {
        Dialog dialog = new Dialog<>();
        VBox dialogVBox = new VBox();
        dialog.setTitle("Dish details");
        dialog.setHeight(600);
        dialog.setWidth(600);

        ImageView imageView = new ImageView(this.dishImageView.getImage());
        imageView.setFitHeight(100);
        imageView.setFitWidth(100);

        Label nameLabel = new Label("Dishes Name: " + this.dishNameLabel.getText());

        Label priceLabel = new Label("Dishes Price : " + this.dishPriceLabel.getText() + " €");

        Label descriptionLabel = new Label("Dishes Description : " + this.dishDescriptionLabel.getText());

        dialogVBox.getChildren().add(imageView);
        dialogVBox.getChildren().add(nameLabel);
        dialogVBox.getChildren().add(priceLabel);
        dialogVBox.getChildren().add(descriptionLabel);

        dialog.getDialogPane().setContent(dialogVBox);

        ButtonType closeButton = new ButtonType("Close");
        dialog.getDialogPane().getButtonTypes().add(closeButton);
        dialog.show();
    }


}
