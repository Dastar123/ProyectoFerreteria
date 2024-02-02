package com.example.conexion;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.collections.ObservableList;

import java.io.IOException;
import java.sql.*;




/**
 * Controlador es la clase que gestiona la lógica de la interfaz gráfica y la interacción con la base de datos
 *
 */

public class MecanicoPanelControlador {

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
    private MecanicoPanelControlador controlador = this;
    @FXML
    public TableColumn<MecanicoObjeto, String> idJava;
    public TableColumn<MecanicoObjeto, String> RolJava;
    public TableColumn<MecanicoObjeto, String> horasJava;
    public TableColumn<MecanicoObjeto, String> SeguroJava;
    public TableView<MecanicoObjeto> EmpleadosContendor;
    public TableColumn<MecanicoObjeto, String> nombreJava;
    public TableColumn<MecanicoObjeto, String> NumeroJava;
    public TableColumn<MecanicoObjeto, String> CalleJava;
    public TableColumn<MecanicoObjeto, String> CiudadJava;
    public TableColumn<MecanicoObjeto, String> PassJava;
    public TableColumn<MecanicoObjeto, String> CpJava;
    public TableColumn<MecanicoObjeto, String> nussJava;
    public TableColumn<MecanicoObjeto, String> NominaJava;

    public Button botonClaroOscuro;
    Conexion conexion = new Conexion();
    public static boolean noche;

    /**
     * Inicializa el controlador, cargando datos desde la base de datos y configurando la interfaz gráfica.
     * Además, se establece una relación bidireccional con el controlador de la segunda pantalla.(Aunque solo se usa de forma unidericcional)
     */
    public static boolean cambiarEstadoNocheDia(boolean cambio ) {
        noche=cambio;
        return noche;
    }

    @FXML
    protected void initialize() {
        // Cargar datos desde la base de datos
        cargarDatos();
        InsertMecanicoControlador.controladorPantalla2 = controlador;
        cambiarEstadoNocheDia(Iniciador.nochedia);

        Platform.runLater(() -> {
            actualizarEstiloNocturno();
            actualizarEstiloNocturno();
        });
    }


    /**
     * Carga datos desde la base de datos y los muestra en el TableView.
     */
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
                /**
                 *  Realizar la consulta
                  */

                String sql =SentenciasSQL.mecanicoParametros;

                try (PreparedStatement statement = con.prepareStatement(sql);
                     ResultSet resultSet = statement.executeQuery()) {


                    ObservableList<MecanicoObjeto> listaMecanicos = FXCollections.observableArrayList();

                    /**
                     *   Recorre los resultados y agregarlos a la lista
                      */

                    while (resultSet.next()) {
                        MecanicoObjeto mecanico = new MecanicoObjeto(
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
                        actualizarEstiloNocturno();
                        actualizarEstiloNocturno();
                    }


                    EmpleadosContendor.setItems(listaMecanicos);
                } catch (SQLException ex) {

                    ex.printStackTrace();
                } finally {

                    conexion.desconectar();
                }
            }
        } catch (SQLException ex) {

            ex.printStackTrace();
        }
    }

    /**
     * Cambia a la segunda pantalla cuando se activa el evento correspondiente.
     *
     */
    @FXML
    protected void CambiarPantalla() throws IOException {
        conexion.desconectar();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("MecanicoInsert.fxml"));
        Parent root = fxmlLoader.load();
        Stage stage = new Stage();
        stage.setTitle("Insertar mecanicos");


        stage.setScene(new Scene(root));
        stage.show();
    }

    /**
     * Actualiza la información de un mecánico en la base de datos y en el TableView.
     * Muestra mensajes de advertencia si no se selecciona un mecánico o si no se actualiza ningún campo.
     */
    @FXML
    protected void ActualizarMecanico() {
        MecanicoObjeto mecanicoSeleccionado = EmpleadosContendor.getSelectionModel().getSelectedItem();

        /**
         *  Muestra un mensaje de advertencia si no se ha seleccionado ningún mecanico
         */
        if (mecanicoSeleccionado == null) {

            mostrarMensaje("Advertencia", "No se ha seleccionado ningún mecanico", "Por favor, selecciona un mecanico antes de intentar actualizar.");
            return;
        }


        /**
         * Aquí construyo la consulta de actualización base
         */

        StringBuilder sqlBuilder = new StringBuilder("UPDATE mecanico SET ");
        boolean algunValorActualizado = false;


        if (!RolActualizar.getText().isEmpty()) {
            String nuevoRol = RolActualizar.getText().trim();
            if (validarRol(nuevoRol)) {
                sqlBuilder.append("rol = CAST(? AS especialidad), ");
                algunValorActualizado = true;
            } else {
                mostrarMensaje("Error", "Rol inválido", "Los roles válidos son 'chapista', 'soldador' o 'pintor'.");
                return;
            }
        }

        if (!HorarioActualizar.getText().isEmpty()) {
            sqlBuilder.append("conthoras = CAST(? AS integer), ");
            algunValorActualizado = true;
        }


        if (!SeguroActualizar.getText().isEmpty()) {
            sqlBuilder.append("seguro = ?, ");
            algunValorActualizado = true;
        }

        if (!NombreActualizar.getText().isEmpty()) {
            sqlBuilder.append("empleados.persona.nombre = ?, ");
            algunValorActualizado = true;
        }

        if (!NumeroActualizar.getText().isEmpty()) {
            sqlBuilder.append("empleados.persona.direccion.num = CAST(? AS integer ) , ");
            algunValorActualizado = true;
        }

        if (!CalleActualizar.getText().isEmpty()) {
            sqlBuilder.append("empleados.persona.direccion.calle = ?, ");
            algunValorActualizado = true;
        }

        if (!CiudadActualizar.getText().isEmpty()) {
            sqlBuilder.append("empleados.persona.direccion.ciudad = ?, ");
            algunValorActualizado = true;
        }

        if (!NussActualizar.getText().isEmpty()) {
            sqlBuilder.append("empleados.nuss = CAST(? AS bigint ) , ");


            algunValorActualizado = true;
        }

        if (!PassActualizar.getText().isEmpty()) {
            sqlBuilder.append("empleados.pass = ?, ");
            algunValorActualizado = true;
        }

        if (!CpActualizar.getText().isEmpty()) {
            sqlBuilder.append("empleados.persona.direccion.cp = ?, ");
            algunValorActualizado = true;
        }

        if (!NominaActualizar.getText().isEmpty()) {
            sqlBuilder.append("empleados.nomina = ?, ");
            algunValorActualizado = true;
        }

        if (!algunValorActualizado) {
            mostrarMensaje("Advertencia", "Ningún campo actualizado", "Por favor, escribe al menos en un campo antes de intentar actualizar.");
            return;
        }
        if (algunValorActualizado) {
            sqlBuilder.deleteCharAt(sqlBuilder.length() - 2);
        }


        sqlBuilder.append("WHERE idMecanico = ?");


        try (Connection con = conexion.conectar();
             PreparedStatement statement = con.prepareStatement(sqlBuilder.toString())) {

            int parametroIndex = 1;


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


            statement.setInt(parametroIndex, Integer.parseInt(mecanicoSeleccionado.getID()));

            /**
             *  Ejecuta la actualización
             */

            statement.executeUpdate();


            EmpleadosContendor.refresh();


            cargarDatos();
            mostrarMensaje("Éxito", "Actualización exitosa", "Los datos del mecanico se actualizaron correctamente.");

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }


    /**
     * Muestra un mensaje de alerta con los parámetros proporcionados.
     *
     * @param titulo     El título del mensaje de alerta.
     * @param encabezado El encabezado del mensaje de alerta.
     * @param contenido  El contenido o cuerpo del mensaje de alerta.
     */
    private void mostrarMensaje(String titulo, String encabezado, String contenido) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(titulo);
        alert.setHeaderText(encabezado);
        alert.setContentText(contenido);
        alert.showAndWait();
    }
    /**
     * Elimina un mecánico seleccionado de la base de datos y del TableView.
     * Muestra mensajes de advertencia si no se selecciona un mecánico.
     */
    @FXML
    protected void eliminarMecanico() {

        MecanicoObjeto mecanicoSeleccionado = EmpleadosContendor.getSelectionModel().getSelectedItem();

        if (mecanicoSeleccionado != null) {

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmación");
            alert.setHeaderText("¿Estás seguro de que quieres eliminar a " + mecanicoSeleccionado.getNombre() + "?");
            alert.setContentText("Esta acción no se puede deshacer.");


            ButtonType result = alert.showAndWait().orElse(ButtonType.CANCEL);
            /**
             *Si el usuario confirma, eliminar el mecanico
             */
            if (result == ButtonType.OK) {

                eliminarMecanicoDeBaseDeDatos(mecanicoSeleccionado);
            }
        } else {

            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Advertencia");
            alert.setHeaderText("No se ha seleccionado ningún mecanico");
            alert.setContentText("Por favor, selecciona un mecanico antes de intentar eliminar.");

            alert.showAndWait();
        }
    }
    /**
     * Elimina un registro de mecánico de la base de datos y actualiza la interfaz gráfica al removerlo del TableView.
     *
     * @param mecanico El objeto Mecanico que se eliminará de la base de datos y del TableView.
     */
    private void eliminarMecanicoDeBaseDeDatos(MecanicoObjeto mecanico) {
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

    /**
     * Método auxiliar para cambiar entre el tema claro y oscuro.
     *
     */
    public void llamarcambiarClaroOscuro(ActionEvent actionEvent){
        MecanicoObjeto.crearImagenes(botonClaroOscuro,fondo, cambiarEstadoNocheDia(Iniciador.nochedia));
    }

    /**
     * Método auxiliar que llama al método de actualización de mecánico.

     */
    public void llamarActualizar(ActionEvent actionEvent) {
        ActualizarMecanico();
    }
    /**
     * Valida si un rol proporcionado es válido.
     * @param rol Rol a validar.
     * @return true si el rol es válido, false en caso contrario.
     */
    private boolean validarRol(String rol) {
        return rol.equalsIgnoreCase("chapista") || rol.equalsIgnoreCase("soldador") || rol.equalsIgnoreCase("pintor");
    }


    private void actualizarEstiloNocturno() {
        MecanicoObjeto.crearImagenes(botonClaroOscuro, fondo, Iniciador.isModoNocturno());

    }
    /**
     * Carga los datos del mecánico seleccionado en los campos de actualización.
     *
     * @param mecanico El mecánico seleccionado.
     */
    private void cargarDatosDeMecanicoSeleccionado(MecanicoObjeto mecanico) {
        if(mecanico!=null){
            RolActualizar.setText(mecanico.getRol());
            HorarioActualizar.setText(String.valueOf(mecanico.getHoras()));
            SeguroActualizar.setText(mecanico.getSeguro());
            NombreActualizar.setText(mecanico.getNombre());
            NumeroActualizar.setText(String.valueOf(mecanico.getNumero()));
            CalleActualizar.setText(mecanico.getCalle());
            CiudadActualizar.setText(mecanico.getCiudad());
            NussActualizar.setText(String.valueOf(mecanico.getNuss()));
            PassActualizar.setText(mecanico.getContrasena());
            CpActualizar.setText(mecanico.getCodigoPostal());
            NominaActualizar.setText(mecanico.getNomina());
        }


    }

    @FXML
    private void llamarDatosMecanico(){
        MecanicoObjeto mecanicoSeleccionado = EmpleadosContendor.getSelectionModel().getSelectedItem();
        cargarDatosDeMecanicoSeleccionado(mecanicoSeleccionado);
    }
}