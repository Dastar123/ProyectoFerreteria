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

public class InicioSesionControlador {
    Conexion conexion = new Conexion();
    private static boolean noche;
    public Button botonClaroOscuro;
    public AnchorPane fondo;
    public static boolean cambiarEstadoNocheDia(boolean cambio ) {
        noche=cambio;
        return noche;
    }
    @FXML
    protected void initialize() {

    CrearCuentaAdminControlador.controlador2=this;
        Platform.runLater(() -> {
            actualizarEstiloNocturno();
            actualizarEstiloNocturno();
        });
    }

    public void llamarcambiarClaroOscuro(ActionEvent actionEvent) {
        Mecanico.crearImagenes(botonClaroOscuro,fondo, cambiarEstadoNocheDia(Iniciador.nochedia));
    }
    public void actualizarEstiloNocturno() {
        Mecanico.crearImagenes(botonClaroOscuro, fondo, Iniciador.isModoNocturno());

    }

    @FXML
    protected void CambiarPantalla() throws IOException {
        actualizarEstiloNocturno();
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
}
