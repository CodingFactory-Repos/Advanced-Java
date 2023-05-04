package me.loule.hipopothalous;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import me.loule.hipopothalous.model.DatabaseConnection;

import java.io.IOException;
import java.sql.Connection;
import java.util.logging.Logger;

public class MainApplication extends Application {
    public void initialize() {
        Connection connection = DatabaseConnection.getConnection();
        if (connection != null) {
            Logger.getLogger("Connected to database!");
        } else {
            Logger.getLogger("Failed to connect to database!");
        }
    }

    @Override
    public void start(Stage stage) throws IOException {
        initialize();

        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}