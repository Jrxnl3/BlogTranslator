package de.jinx.blogtranslator;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class UI_Main extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/interfaces/main.fxml"));
        primaryStage.setTitle("Blog Translator");
        primaryStage.setScene(new Scene(root, 300, 275));
        primaryStage.show();
    }
}
