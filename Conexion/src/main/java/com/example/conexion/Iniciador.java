package com.example.conexion;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Iniciador extends Application {

    public static boolean nochedia=true;

    @Override
    public void start(Stage stage) throws IOException {


        FXMLLoader fxmlLoader = new FXMLLoader(Iniciador.class.getResource("PrimeraPantalla.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("MecanicoPanel");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {

        launch();
    }
}