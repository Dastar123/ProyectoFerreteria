package com.example.conexion;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.collections.ObservableList;

import java.io.IOException;
import java.sql.*;


public class Controlador {

    public AnchorPane fondo;
    private Controlador controlador = this;
    @FXML
    public TableColumn<Mecanico, String> idJava;
    public TableColumn<Mecanico, String> RolJava;
    public TableColumn<Mecanico, String> horasJava;
    public TableColumn<Mecanico, String> SeguroJava;
    public TableView<Mecanico> EmpleadosContendor;
    public TableColumn<Mecanico, String> nombreJava;
    public TableColumn<Mecanico, String> NumeroJava;
    public TableColumn<Mecanico, String> CalleJava;
    public TableColumn<Mecanico, String> CiudadJava;
    public TableColumn<Mecanico, String> PassJava;
    public TableColumn<Mecanico, String> CpJava;
    public TableColumn<Mecanico, String> nussJava;
    public TableColumn<Mecanico, String> NominaJava;
    public Button botonClaroOscuro;
    Conexion conexion = new Conexion();



    @FXML
    protected void initialize() {
        // Cargar datos desde la base de datos
        cargarDatos();
        SegundaPantallaControlador.controladorPantalla2=controlador;

        Mecanico.crearImagenes(botonClaroOscuro,fondo);




    }

    public void cargarDatos() {
        idJava.setCellValueFactory(cellData -> cellData.getValue().idProperty());
        RolJava.setCellValueFactory(cellData -> cellData.getValue().rolProperty());
        horasJava.setCellValueFactory(cellData -> cellData.getValue().horasProperty());
        SeguroJava.setCellValueFactory(cellData -> cellData.getValue().seguroProperty());
        nombreJava.setCellValueFactory(cellData -> cellData.getValue().nombreProperty());
        NumeroJava.setCellValueFactory(cellData -> cellData.getValue().numeroProperty());
        CalleJava.setCellValueFactory(cellData -> cellData.getValue().calleProperty());
        CiudadJava.setCellValueFactory(cellData -> cellData.getValue().ciudadProperty());
        PassJava.setCellValueFactory(cellData -> cellData.getValue().contrasenaProperty());
        CpJava.setCellValueFactory(cellData -> cellData.getValue().codigoPostalProperty());
        NominaJava.setCellValueFactory(cellData -> cellData.getValue().nominaProperty());
        nussJava.setCellValueFactory(cellData -> cellData.getValue().nussProperty());

        try (Connection con = conexion.conectar()) {
            if (con != null) {
                // Realizar la consulta
                String sql = "SELECT " +
                        "mecanico.idmecanico, " +
                        "mecanico.rol, " +
                        "mecanico.conthoras, " +
                        "mecanico.seguro, " +
                        "((mecanico.empleados).persona).nombre AS nombre, " +
                        "(((mecanico.empleados).persona).direccion).num AS num, " +
                        "(((mecanico.empleados).persona).direccion).calle AS calle, " +
                        "(((mecanico.empleados).persona).direccion).ciudad AS ciudad, " +
                        "(((mecanico.empleados).persona).direccion).cp AS cp, " +
                        "((mecanico.empleados).pass) AS pass, " +
                        "((mecanico.empleados).nomina) AS nomina, " +
                        "((mecanico.empleados).horario) AS horario, " +
                        "((mecanico.empleados).nuss) AS nuss " +
                        "FROM mecanico";

                try (PreparedStatement statement = con.prepareStatement(sql);
                     ResultSet resultSet = statement.executeQuery()) {

                    // Crear una lista observable para almacenar los datos
                    ObservableList<Mecanico> listaMecanicos = FXCollections.observableArrayList();

                    // Recorrer los resultados y agregarlos a la lista
                    while (resultSet.next()) {
                        Mecanico mecanico = new Mecanico(
                                resultSet.getString("idmecanico"),
                                resultSet.getString("rol"),
                                resultSet.getString("conthoras"),
                                resultSet.getString("seguro"),
                                resultSet.getString("nombre"),
                                resultSet.getString("num"),
                                resultSet.getString("calle"),
                                resultSet.getString("ciudad"),
                                resultSet.getString("cp"),
                                resultSet.getString("pass"),
                                resultSet.getString("nomina"),
                                resultSet.getString("horario"),
                                resultSet.getString("nuss")
                        );
                        listaMecanicos.add(mecanico);
                    }

                    // Asignar la lista al TableView
                    EmpleadosContendor.setItems(listaMecanicos);
                } catch (SQLException ex) {
                    // Manejar la excepción de manera más específica o lanzarla
                    ex.printStackTrace();
                } finally {
                    // Cerrar la conexión en el bloque finally
                    conexion.desconectar();
                }
            }
        } catch (SQLException ex) {
            // Manejar la excepción de manera más específica o lanzarla
            ex.printStackTrace();
        }
    }

    @FXML
    protected void CambiarPantalla() throws IOException {
        conexion.desconectar();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("segundaPantalla.fxml"));
        Parent root = fxmlLoader.load();
        Stage stage = new Stage();
        stage.setTitle("Insertar mecanicos");


        stage.setScene(new Scene(root));
        stage.show();
    }

    @FXML
    protected void eliminarMecanico() {
        // Obtener el mecanico seleccionado en el TableView
        Mecanico mecanicoSeleccionado = EmpleadosContendor.getSelectionModel().getSelectedItem();

        if (mecanicoSeleccionado != null) {
            // Mostrar un mensaje de confirmación
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmación");
            alert.setHeaderText("¿Estás seguro de que quieres eliminar a " + mecanicoSeleccionado.getNombre() + "?");
            alert.setContentText("Esta acción no se puede deshacer.");

            // Obtener la respuesta del usuario
            ButtonType result = alert.showAndWait().orElse(ButtonType.CANCEL);

            if (result == ButtonType.OK) {
                // Si el usuario confirma, eliminar el mecanico
                eliminarMecanicoDeBaseDeDatos(mecanicoSeleccionado);
            }
        } else {
            // Si no se ha seleccionado ningún mecanico, mostrar un mensaje de advertencia
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Advertencia");
            alert.setHeaderText("No se ha seleccionado ningún mecanico");
            alert.setContentText("Por favor, selecciona un mecanico antes de intentar eliminar.");

            alert.showAndWait();
        }
    }

    private void eliminarMecanicoDeBaseDeDatos(Mecanico mecanico) {
        // Aquí deberías implementar la lógica para eliminar el mecanico de tu base de datos
        // Puedes usar mecanico.getId() para obtener el identificador del mecanico a eliminar
        // Después de eliminarlo de la base de datos, también puedes eliminarlo de la lista del TableView

        // Ejemplo ficticio:
        Conexion conexion = new Conexion();
        try (Connection con = conexion.conectar()) {
            if (con != null) {
                String sql = "DELETE FROM mecanico WHERE idmecanico = ?";
                try (PreparedStatement statement = con.prepareStatement(sql)) {
                    statement.setInt(1, Integer.parseInt(mecanico.getID()));
                    statement.executeUpdate();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        // Después de eliminar de la base de datos, también elimina de la lista del TableView
        EmpleadosContendor.getItems().remove(mecanico);
    }
    public void llamarcambiarClaroOscuro(ActionEvent actionEvent){
        Mecanico.crearImagenes(botonClaroOscuro,fondo);
    }



}