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
    private final ImageView dishImageView;
    private final Label dishNameLabel;
    private final Label dishPriceLabel;
    private final Label dishDescriptionLabel;

    /**
     * @param dishImage
     * @param dishName
     * @param dishPrice
     * @param dishDescription
     */
    public DishComponent(String dishImage, String dishName, Double dishPrice , String dishDescription) {
        dishImageView = new ImageView(new Image(dishImage));
        dishImageView.setFitHeight(50);
        dishImageView.setFitWidth(50);
        dishNameLabel = new Label(dishName);
        dishPriceLabel = new Label(dishPrice.toString());
        dishDescriptionLabel = new Label(dishDescription);
        Button dishDetailButton = new Button("Details");

        this.getChildren().addAll(dishImageView, dishNameLabel, dishPriceLabel, dishDetailButton);
        this.setAlignment(javafx.geometry.Pos.CENTER);
        this.setSpacing(100);

        dishDetailButton.setOnAction(event -> onDishDetailButtonClick());
    }

    /**
     * This function show a dialog with all information of the dishe
     */
    //Sout when we click on the dishDetailButton
    public void onDishDetailButtonClick() {
        Dialog<Object> dialog = new Dialog<>();
        VBox dialogVBox = new VBox();
        dialog.setTitle("Dish details");
        dialog.setHeight(600);
        dialog.setWidth(600);

        if(this.dishImageView.getImage() == null) {
            ImageView imageView = new ImageView(new Image("https://upload.wikimedia.org/wikipedia/commons/thumb/6/65/No-Image-Placeholder.svg/1665px-No-Image-Placeholder.svg.png"));
            imageView.setFitHeight(100);
            imageView.setFitWidth(100);
            dialogVBox.getChildren().add(imageView);
        }else{
            ImageView imageView = new ImageView(this.dishImageView.getImage());
            imageView.setFitHeight(100);
            imageView.setFitWidth(100);
            dialogVBox.getChildren().add(imageView);
        }

        Label nameLabel = new Label("Dishes Name: " + this.dishNameLabel.getText());

        Label priceLabel = new Label("Dishes Price : " + this.dishPriceLabel.getText() + " €");

        Label descriptionLabel = new Label("Dishes Description : " + this.dishDescriptionLabel.getText());

        dialogVBox.getChildren().add(nameLabel);
        dialogVBox.getChildren().add(priceLabel);
        dialogVBox.getChildren().add(descriptionLabel);

        dialog.getDialogPane().setContent(dialogVBox);

        ButtonType closeButton = new ButtonType("Close");
        dialog.getDialogPane().getButtonTypes().add(closeButton);
        dialog.show();
    }


}
