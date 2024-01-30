package com.example.conexion;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

public class CrearCuentaAdminControlador {
    Conexion conexion = new Conexion();
    private static boolean noche;
    public Button botonClaroOscuro;
    public AnchorPane fondo;
    public static InicioSesionControlador controlador2;
    public static boolean cambiarEstadoNocheDia(boolean cambio ) {
        noche=cambio;
        return noche;
    }
    @FXML
    protected void initialize() {


        Platform.runLater(() -> {
            actualizarEstiloNocturno();
        });
    }

    public void llamarcambiarClaroOscuro(ActionEvent actionEvent) {
        Mecanico.crearImagenes(botonClaroOscuro,fondo, cambiarEstadoNocheDia(Iniciador.nochedia));
    }
    private void actualizarEstiloNocturno() {
        Mecanico.crearImagenes(botonClaroOscuro, fondo, Iniciador.isModoNocturno());

    }

    @FXML
    protected void CambiarPantalla() throws IOException {

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("crearCuentaAdmin.fxml"));
        Parent root = fxmlLoader.load();
        Stage stage = new Stage();
        stage.setTitle("Insertar mecanicos");


        stage.setScene(new Scene(root));
        stage.show();

    }
    private void cerrarVentana() {

        Stage stage = (Stage) botonClaroOscuro.getScene().getWindow();


        stage.close();
    }
    private void antesDeCerrarVentana() {
        controlador2.actualizarEstiloNocturno();
        conexion.desconectar();

    }
}
