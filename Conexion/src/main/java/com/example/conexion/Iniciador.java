package com.example.conexion;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Clase principal que inicia la aplicación JavaFX para el sistema MecanicoPanel.
 */
public class Iniciador extends Application {

    /**
     * Variable que indica si es de día o de noche.
     * Si es verdadero, representa el modo día; si es falso, representa el modo noche.
     */
    public static boolean nochedia=false;




    /**
     * Método principal que inicia la aplicación JavaFX.
     *
     * @param stage El objeto Stage principal de la aplicación.
     */
    @Override
    public void start(Stage stage) throws IOException {


        FXMLLoader fxmlLoader = new FXMLLoader(Iniciador.class.getResource("PrimeraPantalla.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Mecánico Panel");
        stage.setScene(scene);
        stage.show();
    }
    public static boolean isModoNocturno() {
        return nochedia;
    }

    public static void setNochedia(boolean nochedia) {
        Iniciador.nochedia = nochedia;
    }

    public static void main(String[] args) {

        launch();
    }


}