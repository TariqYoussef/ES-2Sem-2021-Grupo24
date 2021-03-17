import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import readers.ExelReader;
import readers.JavaReader;

public class Main extends Application {

    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("main.fxml"));
        primaryStage.setTitle("Painel de utilização");
        Scene scene =new Scene(root, 700, 800);
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    public static void main(String[] args) {
        new JavaReader("/home/main/IdeaProjects/ES-2Sem-2021-Grupo24/src/main/java/Main.java").read();
        new ExelReader("/home/main/Downloads/testeES.xlsx").read();

        launch(args);
    }

}

