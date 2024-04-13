package esprit.monstergym.demo.Utils;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

public class MainFx extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/esprit/monstergym/demo/ajoutannonce.fxml"));
        primaryStage.setTitle("Your Application Title");
        primaryStage.setScene(new Scene(root, 1000, 800));
        primaryStage.show();
    }




}
