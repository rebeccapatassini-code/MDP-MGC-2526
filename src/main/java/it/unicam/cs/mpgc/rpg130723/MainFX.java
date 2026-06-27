package it.unicam.cs.mpgc.rpg130723;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

public class MainFX extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        URL fxmlUrl = getClass().getResource("/view/bossmind.fxml");

        if (fxmlUrl == null) {
            throw new IllegalStateException("FXML non trovato nel classpath!");
        }

        FXMLLoader loader = new FXMLLoader(fxmlUrl);
        Scene scene = new Scene(loader.load(), 1100, 700);

        stage.setTitle("BossMind RPG");
        stage.setMinWidth(1100);
        stage.setMinHeight(700);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}