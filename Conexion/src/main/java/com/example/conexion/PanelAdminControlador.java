package com.example.conexion;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

public class PanelAdminControlador {
    public Button botonClaroOscuro;
    public AnchorPane fondo;
    private static boolean noche;
    public Button clientesButton;
    public Button MecanicoButton;

    public static boolean cambiarEstadoNocheDia(boolean cambio ) {
        noche=cambio;
        return noche;
    }
    @FXML
    protected void initialize() {

        ImagenesPanel(clientesButton,MecanicoButton);
        Platform.runLater(() -> {
            actualizarEstiloNocturno();
            actualizarEstiloNocturno();
        });
    }
    public void llamarcambiarClaroOscuro(ActionEvent actionEvent) {
        MecanicoObjeto.crearImagenes(botonClaroOscuro,fondo, cambiarEstadoNocheDia(Iniciador.nochedia));
        ImagenesPanel(clientesButton,MecanicoButton);
    }
    private void actualizarEstiloNocturno() {
        MecanicoObjeto.crearImagenes(botonClaroOscuro, fondo, Iniciador.isModoNocturno());

    }

    @FXML
    protected void CambiarPantalla() throws IOException {


        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("MecanicoPanel.fxml"));
        Parent root = fxmlLoader.load();
        Stage stage = new Stage();
        stage.setTitle("Insertar mecanicos");


        stage.setScene(new Scene(root));
        stage.show();
        cerrarVentana();

    }

    @FXML
    protected void InicioSesion() throws IOException {


        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("InicioSesion.fxml"));
        Parent root = fxmlLoader.load();
        Stage stage = new Stage();
        stage.setTitle("Inicio de Sesión");


        stage.setScene(new Scene(root));
        stage.show();
        cerrarVentana();

    }

    private void cerrarVentana() {

        Stage stage = (Stage) botonClaroOscuro.getScene().getWindow();
        stage.close();
    }


    private void ImagenesPanel (Button button,Button button2) {
        if ( Iniciador.nochedia) {
        Image ClienteImage = new Image(MecanicoPanelControlador.class.getResourceAsStream("/imgs/cliente_blanco.png"));
        ImageView ClientesView = new ImageView(ClienteImage);
        ClientesView.setFitWidth(120);
        ClientesView.setFitHeight(117);
        button.setGraphic(ClientesView);
        button.setStyle("-fx-border-color: white; -fx-background-color: transparent; -fx-border-radius: 20px; -fx-border-width: 3px; ");

        Image MecanicoImage = new Image(MecanicoPanelControlador.class.getResourceAsStream("/imgs/mecanico_blanco.png"));
        ImageView MecanicoView = new ImageView(MecanicoImage);
        MecanicoView.setFitWidth(120);
        MecanicoView.setFitHeight(117);
        button2.setGraphic(MecanicoView);
        button2.setStyle("-fx-border-color: white; -fx-background-color: transparent; -fx-border-radius: 20px; -fx-border-width: 3px; ");

        }
        else {
            Image ClienteImage1 = new Image(MecanicoPanelControlador.class.getResourceAsStream("/imgs/cliente_negro.png"));
            ImageView ClientesView1 = new ImageView(ClienteImage1);
            ClientesView1.setFitWidth(120);
            ClientesView1.setFitHeight(117);
            button.setGraphic(ClientesView1);
            button.setStyle("-fx-border-color: black; -fx-background-color: transparent; -fx-border-radius: 20px; -fx-border-width: 3px; ");

            Image MecanicoImage1 = new Image(MecanicoPanelControlador.class.getResourceAsStream("/imgs/mecanico_negro.png"));
            ImageView MecanicoView1 = new ImageView(MecanicoImage1);
            MecanicoView1.setFitWidth(120);
            MecanicoView1.setFitHeight(117);
            button2.setGraphic(MecanicoView1);
            button2.setStyle("-fx-border-color: black; -fx-background-color: transparent; -fx-border-radius: 20px; -fx-border-width: 3px; ");
        }
    }



}
