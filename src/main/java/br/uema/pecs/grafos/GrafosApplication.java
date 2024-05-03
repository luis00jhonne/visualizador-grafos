package br.uema.pecs.grafos;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class GrafosApplication extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(GrafosApplication.class.getResource("complex-application.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 980, 820);
        stage.setTitle("Visualização de Grafos");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}