package com.example.conexion;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

import static java.lang.Integer.parseInt;

/**
 * Controlador para la segunda pantalla de la aplicación, encargado de la inserción de nuevos mecánicos.
 */
public class SegundaPantallaControlador {
    @FXML
    public Button UpdateButton;
    public AnchorPane fondo;
    public Button botonClaroOscuro;

    @FXML
    private TextField rolTextField;

    @FXML
    private TextField conthorasTextField;

    @FXML
    private TextField seguroTextField;

    @FXML
    private TextField nombreTextField;

    @FXML
    private TextField numTextField;

    @FXML
    private TextField calleTextField;

    @FXML
    private TextField ciudadTextField;

    @FXML
    private TextField cpTextField;

    @FXML
    private TextField passTextField;

    @FXML
    private TextField nominaTextField;

    @FXML
    private TextField horarioTextField;

    @FXML
    private TextField nussTextField;

    public static Controlador controladorPantalla2;
    Conexion conexion = new Conexion();
    public static boolean noche;
    public static boolean cambiarEstadoNocheDia(boolean cambio ) {
        noche=cambio;
        return noche;
    }
    @FXML
    protected void initialize() {


        Platform.runLater(() -> {
            actualizarEstiloNocturno();
            actualizarEstiloNocturno();
            Stage stage = (Stage) botonClaroOscuro.getScene().getWindow();
            stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
                @Override
                public void handle(WindowEvent event) {
                    // Llamar al método que quieres ejecutar antes de cerrar la ventana
                    antesDeCerrarVentana();
                }
            });

        });


    }

    /**
     * Método para  insertar un nuevo mecánico.
     */
    @FXML
    protected void insertarMecanico() {
        String rol = rolTextField.getText().trim();
        if (!llamarAlertaSiInvalido(validarRol(rol), "Rol inválido. Los roles válidos son 'chapista' o 'mecánico' o 'pintor'.")) {
            return;
        }
        String contHorasText = conthorasTextField.getText().trim();
        if (!llamarAlertaSiInvalido(!contHorasText.isEmpty(), "Por favor, ingrese un valor válido para 'Horas de trabajo'.")) {
            return;
        }
        String seguro = seguroTextField.getText().trim();
        if (!llamarAlertaSiInvalido(!seguro.isEmpty(), "Por favor, ingrese un valor para 'Seguro'.")) {
            return;
        }
        String nombre = nombreTextField.getText().trim();
        if (!llamarAlertaSiInvalido(!nombre.isEmpty(), "Por favor, ingrese un valor para 'Nombre'.")) {
            return;
        }
        String numText = numTextField.getText().trim();
        if (!llamarAlertaSiInvalido(!numText.isEmpty() || numText.matches("\\d+"), "Por favor, ingrese un valor válido para 'Número'.")) {
            return;
        }
        String calle = calleTextField.getText().trim();
        if (!llamarAlertaSiInvalido(!calle.isEmpty(), "Por favor, ingrese un valor para 'Calle'.")) {
            return;
        }
        String ciudad = ciudadTextField.getText().trim();
        if (!llamarAlertaSiInvalido(!ciudad.isEmpty(), "Por favor, ingrese un valor para 'Ciudad'.")) {
            return;
        }
        String cp = cpTextField.getText().trim();
        if (!llamarAlertaSiInvalido(!cp.isEmpty(), "Por favor, ingrese un valor para 'Código Postal'.")) {
            return;
        }
        String pass = passTextField.getText().trim();
        if (!llamarAlertaSiInvalido(!pass.isEmpty(), "Por favor, ingrese un valor para 'Contraseña'.")) {
            return;
        }
        String nussText = nussTextField.getText().trim();
        if (!llamarAlertaSiInvalido(!nussText.isEmpty() || nussText.matches("\\d+"), "Por favor, ingrese un valor válido para 'NUSS'.")) {
            return;
        }
        String nomina = nominaTextField.getText().trim();
        if (!llamarAlertaSiInvalido(!nomina.isEmpty(), "Por favor, ingrese un valor para 'Nómina'.")) {
            return;
        }
        String fecha = horarioTextField.getText().trim();
        if (!llamarAlertaSiInvalido(!fecha.isEmpty(), "Por favor, ingrese un valor para 'Fecha'.") || !validarFecha(fecha)) {
            return;
        }

        try (Connection con = conexion.conectar()) {
            if (con != null) {
                String sql = "INSERT INTO mecanico (rol, contHoras, seguro, empleados)\n" +
                        "VALUES (?::especialidad,?,?, ROW(Row( ?, ROW(?, ?, ?, ?)),?,?,?::date,?)::empleados_type);";

                try (PreparedStatement statement = con.prepareStatement(sql)) {
                    /**
                     *  Asigna valores a los parámetros
                      */

                    statement.setObject(1, rol, Types.VARCHAR);
                    statement.setInt(2, parseInt(conthorasTextField.getText()));
                    statement.setString(3, seguro);
                    statement.setString(4, nombre);
                    statement.setInt(5, parseInt(numText));
                    statement.setString(6, calle);
                    statement.setString(7, ciudad);
                    statement.setString(8, cp);
                    statement.setString(9, pass);
                    statement.setString(10, nomina);
                    statement.setObject(11, LocalDate.parse(fecha));
                    statement.setLong(12, Long.parseLong(nussText));


                    int filasAfectadas = statement.executeUpdate();

                    if (filasAfectadas > 0) {
                        mostrarAlerta("Éxito", "Mecánico insertado exitosamente.");

                       controladorPantalla2.cargarDatos();
                        conexion.desconectar();
                        cerrarVentana();
                    } else {
                        mostrarAlerta("Error", "No se pudo insertar el mecánico.");
                    }
                } catch (SQLException e) {
                    mostrarAlerta("Error", "No se pudo insertar el mecánico. Verifica los datos e inténtalo nuevamente.");
                    e.printStackTrace();
                }
            }
        } catch (SQLException e) {
            mostrarAlerta("Error", "No se pudo conectar con la base de datos. Verifica la conexión e inténtalo nuevamente.");
            e.printStackTrace();
        }
    }


    /**
     * Valida si el rol ingresado es válido.
     * @param rol El rol a validar.
     * @return true si el rol es válido, false de lo contrario.
     */
    private boolean validarRol(String rol) {
        return rol.equalsIgnoreCase("chapista") || rol.equalsIgnoreCase("soldador") || rol.equalsIgnoreCase("pintor");
    }

    /**
     * Valida si la fecha ingresada es válida.
     *
     * @param fecha La fecha a validar.
     * @return true si la fecha es válida, false de lo contrario.
     */

    private boolean validarFecha(String fecha) {
        try {
            LocalDate.parse(fecha);
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }

    /**
     * Muestra una alerta con el título y mensaje proporcionados.
     *
     * @param titulo  El título de la alerta.
     * @param mensaje El mensaje de la alerta.
     */
    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    /**
     * Llama a una alerta si la condición es falsa y devuelve el resultado de la condición.
     *
     * @param condicion La condición a evaluar.
     * @param mensaje   El mensaje a mostrar en la alerta si la condición es falsa.
     * @return true si la condición es verdadera, false de lo contrario.
     */
    private boolean llamarAlertaSiInvalido(boolean condicion, String mensaje) {
        if (!condicion) {
            mostrarAlerta("Error", mensaje);
            return false;
        }
        return true;
    }

    /**
     * Cierra la ventana actual.
     */
    private void cerrarVentana() {

        Stage stage = (Stage) nominaTextField.getScene().getWindow();


        stage.close();
    }
    /**
     * Cambia el tema claro/oscuro de la aplicación.
     *
     * @param actionEvent Evento que desencadena el cambio de tema.
     */
    public void llamarcambiarClaroOscuro(ActionEvent actionEvent){
        actualizarEstiloNocturno();
    }
    private void actualizarEstiloNocturno() {
        Mecanico.crearImagenes(botonClaroOscuro, fondo, Iniciador.isModoNocturno());
        // Aquí puedes realizar otras actualizaciones de estilo según el modo nocturno
    }
    private void antesDeCerrarVentana() {
        controladorPantalla2.cargarDatos();
        conexion.desconectar();

    }

    public void VolverMec(ActionEvent actionEvent) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("PrimeraPantalla.fxml"));
        Parent root = fxmlLoader.load();
        Stage stage = new Stage();

        stage.setTitle("Panel mecanicos");
        controladorPantalla2.cargarDatos();
        conexion.desconectar();
        cerrarVentana();

        stage.setScene(new Scene(root));
        stage.show();


    }
}
