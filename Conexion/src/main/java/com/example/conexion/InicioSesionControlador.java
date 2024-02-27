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

public class InicioSesionControlador {
    public TextField NombreField;
    public TextField contraField;
    public Button EntrarButton;
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
        MecanicoObjeto.crearImagenes(botonClaroOscuro,fondo, cambiarEstadoNocheDia(Iniciador.nochedia));
    }
    public void actualizarEstiloNocturno() {
        MecanicoObjeto.crearImagenes(botonClaroOscuro, fondo, Iniciador.isModoNocturno());

    }

    @FXML
    protected void CambiarPantalla() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("crearCuentaAdmin.fxml"));
        Parent root = fxmlLoader.load();
        Stage stage = new Stage();
        stage.setTitle("Crear Cuenta");
        cerrarVentana();

        stage.setScene(new Scene(root));
        stage.show();

    }

    @FXML
    protected void EntrarMecanico() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("PanelAdmin.fxml"));
        Parent root = fxmlLoader.load();
        Stage stage = new Stage();

        stage.setTitle("Panel Admin");
        cerrarVentana();

        stage.setScene(new Scene(root));
        stage.show();

    }
    private void cerrarVentana() {

        Stage stage = (Stage) botonClaroOscuro.getScene().getWindow();


        stage.close();
    }

    public void Comprobador(ActionEvent actionEvent) throws IOException {
        String nombreUsuario = NombreField.getText();
        String contraseña = contraField.getText();

        if (nombreUsuario.isEmpty() || contraseña.isEmpty()) {
            mostrarAlerta("Error", "Campos Vacíos", "Se le ha olvidado rellenar los campos.");
        } else {
            // Realizar la comprobación del usuario y la contraseña
            boolean usuarioExistente = verificarUsuario(nombreUsuario, contraseña);

            if (usuarioExistente) {
                // Realizar la acción correspondiente si el usuario existe y la contraseña es correcta
                // En este caso, dejaremos los campos en blanco (puedes agregar tu lógica aquí)
                NombreField.clear();
                contraField.clear();
                EntrarMecanico();
                cerrarVentana();
            } else {
                // Mostrar mensaje de error si el usuario no existe o la contraseña no coincide
                mostrarAlerta("Error", "Credenciales Incorrectas", "El nombre de usuario o contraseña son incorrectas.");
            }
        }
    }

    /**
     * Como se usa la funcion
     * @param nombreUsuario
     * @param password
     * @return
     */
    private boolean verificarUsuario(String nombreUsuario, String password) {
        conexion.conectar();
        try (Connection con = conexion.conectar()) {
            if (con != null) {
                String sql = SentenciasSQL.comprobarCuenta;
                try (PreparedStatement statement = con.prepareStatement(sql)) {
                    statement.setString(1, nombreUsuario);
                    try (ResultSet resultado = statement.executeQuery()) {
                        if (resultado.next()) {
                            // Usuario encontrado, ahora verificamos la contraseña
                            String pass = resultado.getString("contraseña");

                            // Comparar la contraseña proporcionada con la almacenada
                            return password.equals(pass);
                        }
                    }
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        // Usuario no encontrado o contraseña incorrecta
        return false;
    }


    private void mostrarAlerta(String tipo, String titulo, String contenido) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(tipo);
        alert.setHeaderText(titulo);
        alert.setContentText(contenido);
        alert.showAndWait();
    }
}
