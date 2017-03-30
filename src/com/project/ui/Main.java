package com.project.ui;

import com.project.gethumbnails.ThumbUtilities;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("MainScreenGUI.fxml"));
        primaryStage.setTitle(ThumbUtilities.WINDOW_TITLE_NAME);
        Scene scene = new Scene(root, 760, 470);
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.getIcons().add(new Image("assets/download_icon.png"));
        scene.getStylesheets().add("assets/pagination.css");
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
