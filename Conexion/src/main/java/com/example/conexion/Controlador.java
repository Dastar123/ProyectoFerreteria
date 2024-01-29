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
    public TextField RolActualizar;
    public TextField HorarioActualizar;
    public TextField SeguroActualizar;
    public TextField NombreActualizar;
    public TextField NumeroActualizar;
    public TextField CalleActualizar;
    public TextField CiudadActualizar;
    public TextField NussActualizar;
    public TextField PassActualizar;
    public TextField CpActualizar;
    public TextField NominaActualizar;
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
    protected void ActualizarMecanico() {
        // Obtener el mecanico seleccionado en el TableView
        Mecanico mecanicoSeleccionado = EmpleadosContendor.getSelectionModel().getSelectedItem();

        if (mecanicoSeleccionado == null) {
            // Mostrar mensaje de advertencia si no se ha seleccionado ningún mecanico
            mostrarMensaje("Advertencia", "No se ha seleccionado ningún mecanico", "Por favor, selecciona un mecanico antes de intentar actualizar.");
            return;
        }

        // Construir la consulta de actualización base
        StringBuilder sqlBuilder = new StringBuilder("UPDATE mecanico SET ");
        boolean algunValorActualizado = false;

        // Actualizar el rol si hay un valor proporcionado

        if (!RolActualizar.getText().isEmpty()) {
            String nuevoRol = RolActualizar.getText().trim();
            if (validarRol(nuevoRol)) {
                sqlBuilder.append("rol = CAST(? AS especialidad), ");
                algunValorActualizado = true;
            } else {
                mostrarMensaje("Error", "Rol inválido", "Los roles válidos son 'chapista', 'soldador' o 'pintor'.");
                return; // Detener la actualización si el rol no es válido
            }
        }

        // Actualizar las horas si hay un valor proporcionado
        if (!HorarioActualizar.getText().isEmpty()) {
            sqlBuilder.append("conthoras = CAST(? AS integer), ");
            algunValorActualizado = true;
        }

        // Actualizar el seguro si hay un valor proporcionado
        if (!SeguroActualizar.getText().isEmpty()) {
            sqlBuilder.append("seguro = ?, ");
            algunValorActualizado = true;
        }


        // Actualizar el nombre si hay un valor proporcionado
        if (!NombreActualizar.getText().isEmpty()) {
            sqlBuilder.append("empleados.persona.nombre = ?, ");
            algunValorActualizado = true;
        }

        // Actualizar el número si hay un valor proporcionado
        if (!NumeroActualizar.getText().isEmpty()) {
            sqlBuilder.append("empleados.persona.direccion.num = CAST(? AS integer ) , ");
            algunValorActualizado = true;
        }
         // Actualizar la calle si hay un valor proporcionado
        if (!CalleActualizar.getText().isEmpty()) {
            sqlBuilder.append("empleados.persona.direccion.calle = ?, ");
            algunValorActualizado = true;
        }
        // Actualizar la ciudad si hay un valor proporcionado
        if (!CiudadActualizar.getText().isEmpty()) {
            sqlBuilder.append("empleados.persona.direccion.ciudad = ?, ");
            algunValorActualizado = true;
        }
        // Actualizar el Nuss si hay un valor proporcionado bigint
        if (!NussActualizar.getText().isEmpty()) {
            sqlBuilder.append("empleados.nuss = CAST(? AS bigint ) , ");


            algunValorActualizado = true;
        }
        // Actualizar la contraseña si hay un valor proporcionado
        if (!PassActualizar.getText().isEmpty()) {
            sqlBuilder.append("empleados.pass = ?, ");
            algunValorActualizado = true;
        }
        // Actualizar el código postal si hay un valor proporcionado
        if (!CpActualizar.getText().isEmpty()) {
            sqlBuilder.append("empleados.persona.direccion.cp = ?, ");
            algunValorActualizado = true;
        }
        // Actualizar la nómina si hay un valor proporcionado
        if (!NominaActualizar.getText().isEmpty()) {
            sqlBuilder.append("empleados.nomina = ?, ");
            algunValorActualizado = true;
        }

        // Eliminar la última coma si algún valor fue actualizado
        if (!algunValorActualizado) {
            mostrarMensaje("Advertencia", "Ningún campo actualizado", "Por favor, escribe al menos un campo antes de intentar actualizar.");
            return;
        }
        if (algunValorActualizado) {
            sqlBuilder.deleteCharAt(sqlBuilder.length() - 2);
        }

        // Agregar la condición WHERE
        sqlBuilder.append("WHERE idMecanico = ?");

        // Actualizar los datos del mecanico con los valores proporcionados
        try (Connection con = conexion.conectar();
             PreparedStatement statement = con.prepareStatement(sqlBuilder.toString())) {

            int parametroIndex = 1;

            // Setear los valores si fueron proporcionados
            if (!RolActualizar.getText().isEmpty()) {
                statement.setString(parametroIndex++, RolActualizar.getText());
            }

            if (!HorarioActualizar.getText().isEmpty()) {
                statement.setInt(parametroIndex++, Integer.parseInt(HorarioActualizar.getText()));
            }

            if (!SeguroActualizar.getText().isEmpty()) {
                statement.setString(parametroIndex++, SeguroActualizar.getText());
            }

            if (!NombreActualizar.getText().isEmpty()) {
                statement.setString(parametroIndex++, NombreActualizar.getText());
            }
            if (!NumeroActualizar.getText().isEmpty()) {
                statement.setString(parametroIndex++, NumeroActualizar.getText());
            }
            if (!CalleActualizar.getText().isEmpty()) {
                statement.setString(parametroIndex++, CalleActualizar.getText());
            }
            if (!CiudadActualizar.getText().isEmpty()) {
                statement.setString(parametroIndex++, CiudadActualizar.getText());
            }
            if (!NussActualizar.getText().isEmpty()) {
                statement.setString(parametroIndex++, NussActualizar.getText());
            }
            if (!PassActualizar.getText().isEmpty()) {
                statement.setString(parametroIndex++, PassActualizar.getText());
            }
            if (!CpActualizar.getText().isEmpty()) {
                statement.setString(parametroIndex++, CpActualizar.getText());
            }
            if (!NominaActualizar.getText().isEmpty()) {
                statement.setString(parametroIndex++, NominaActualizar.getText());
            }

            // Setear el último parámetro WHERE
            statement.setInt(parametroIndex, Integer.parseInt(mecanicoSeleccionado.getID()));

            // Ejecutar la actualización
            statement.executeUpdate();

            // Refrescar el TableView después de la actualización
            EmpleadosContendor.refresh();

            // Mostrar mensaje de éxito
            cargarDatos();
            mostrarMensaje("Éxito", "Actualización exitosa", "Los datos del mecanico se actualizaron correctamente.");

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }




    // Método auxiliar para verificar si todos los campos están vacíos
    private boolean camposVacios(String... campos) {
        for (String campo : campos) {
            if (!campo.isEmpty()) {
                return false; // Al menos un campo no está vacío
            }
        }
        return true; // Todos los campos están vacíos
    }

    // Método auxiliar para mostrar mensajes
    private void mostrarMensaje(String titulo, String encabezado, String contenido) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(titulo);
        alert.setHeaderText(encabezado);
        alert.setContentText(contenido);
        alert.showAndWait();
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


    public void llamarActualizar(ActionEvent actionEvent) {
        ActualizarMecanico();
    }
    private boolean validarRol(String rol) {
        return rol.equalsIgnoreCase("chapista") || rol.equalsIgnoreCase("soldador") || rol.equalsIgnoreCase("pintor");
    }

}