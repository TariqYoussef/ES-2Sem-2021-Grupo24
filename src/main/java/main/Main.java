package main;

import gui.GuiController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

/**
 *<p>Main class for starting the javafx app</p>
 */
public class Main extends Application {

    /**
     * @param primaryStage necessary for starting the javafx app
     */
    public void start(Stage primaryStage) {
        try {

            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("main.fxml")));
            primaryStage.setTitle("Painel de utilização");
            Scene scene = new Scene(root, 1000, 650);
            primaryStage.setScene(scene);
            primaryStage.show();

        } catch (IOException e) {
            GuiController.throwErroInesperado(e);
        }
    }

    /**
     * @param args for main method
     */
    public static void main(String[] args) {
        launch(args);
    }

}

