package com.example.conexion;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CrearCuentaAdminControlador {
    public TextField CrearPass;
    public TextField CrearUsuario;
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


        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("InicioSesion.fxml"));
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
    public  void setControlador2() {
        this.actualizarEstiloNocturno();


    }

    @FXML
    protected void crear(ActionEvent actionEvent) throws IOException {
        String nuevoUsuario = CrearUsuario.getText();
        String nuevaContraseña = CrearPass.getText();

        if (nuevoUsuario.isEmpty() || nuevaContraseña.isEmpty()) {
            mostrarAlerta("Error", "Campos Vacíos", "Se le ha olvidado rellenar los campos.");
        } else {
            // Verificar si el nombre de usuario ya existe
            if (verificarExistenciaUsuario(nuevoUsuario)) {
                mostrarAlerta("Error", "Nombre de Usuario Existente", "El nombre de usuario ya existe.");
            } else {
                // Intentar crear el nuevo controlador
                if (crearNuevoControlador(nuevoUsuario, nuevaContraseña)) {
                    // Éxito al crear el controlador
                    // Puedes agregar lógica adicional aquí si es necesario
                    mostrarAlerta("Éxito", "Creación Exitosa", "El controlador se ha creado exitosamente.");
                    // Cerrar la ventana después de la creación exitosa
                    CambiarPantalla();
                    cerrarVentana();
                } else {
                    // Ocurrió un error al crear el controlador
                    mostrarAlerta("Error", "Error al Crear el Controlador", "Hubo un problema al crear el controlador.");
                }
            }
        }
    }

    private boolean verificarExistenciaUsuario(String nombreUsuario) {
        conexion.conectar();
        try (Connection con = conexion.conectar()) {
            if (con != null) {
                String sql = "SELECT * FROM buscar_controlador(?)";
                try (PreparedStatement statement = con.prepareStatement(sql)) {
                    statement.setString(1, nombreUsuario);
                    try (ResultSet resultado = statement.executeQuery()) {
                        return resultado.next(); // Devuelve true si el usuario existe, false si no existe
                    }
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return false; // Error al verificar la existencia del usuario
    }

    private boolean crearNuevoControlador(String nombreUsuario, String contraseña) {
        conexion.conectar();
        try (Connection con = conexion.conectar()) {
            if (con != null) {
                String sql = "INSERT INTO controlador (nombreUsuario, contraseña) VALUES (?, ?)";
                try (PreparedStatement statement = con.prepareStatement(sql)) {
                    statement.setString(1, nombreUsuario);
                    statement.setString(2, contraseña);
                    statement.executeUpdate();
                    return true; // Éxito al insertar el nuevo controlador
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return false; // Error al insertar el nuevo controlador
    }
    private void mostrarAlerta(String tipo, String titulo, String contenido) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(tipo);
        alert.setHeaderText(titulo);
        alert.setContentText(contenido);
        alert.showAndWait();
    }

}
