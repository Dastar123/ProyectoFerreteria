package com.example.conexion;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

public class Mecanico {
    private StringProperty id;
    private StringProperty rol;
    private StringProperty horas;
    private StringProperty seguro;
    private StringProperty nombre;
    private StringProperty numero;
    private StringProperty calle;
    private StringProperty ciudad;
    private StringProperty codigoPostal;
    private StringProperty contrasena;
    private StringProperty nomina;
    private StringProperty fechaContratacion;
    private StringProperty nuss; // Número de la Seguridad Social

    public Mecanico(String id, String rol, String horas, String seguro, String nombre, String numero,
                    String calle, String ciudad, String codigoPostal, String contrasena, String nomina,
                    String fechaContratacion, String nuss) {
        this.id = new SimpleStringProperty(id);
        this.rol = new SimpleStringProperty(rol);
        this.horas = new SimpleStringProperty(horas);
        this.seguro = new SimpleStringProperty(seguro);
        this.nombre = new SimpleStringProperty(nombre);
        this.numero = new SimpleStringProperty(numero);
        this.calle = new SimpleStringProperty(calle);
        this.ciudad = new SimpleStringProperty(ciudad);
        this.codigoPostal = new SimpleStringProperty(codigoPostal);
        this.contrasena = new SimpleStringProperty(contrasena);
        this.nomina = new SimpleStringProperty(nomina);
        this.fechaContratacion = new SimpleStringProperty(fechaContratacion);
        this.nuss = new SimpleStringProperty(nuss);
    }

    // Resto de los métodos getter y property...

    // Métodos getter y property para los nuevos campos


    public String getID() {
        return id.get();
    }

    public String getNombre() {
        return nombre.get();
    }

    public StringProperty nombreProperty() {
        return nombre;
    }

    public String getNumero() {
        return numero.get();
    }

    public StringProperty numeroProperty() {
        return numero;
    }

    public String getCalle() {
        return calle.get();
    }

    public StringProperty calleProperty() {
        return calle;
    }

    public String getCiudad() {
        return ciudad.get();
    }

    public StringProperty ciudadProperty() {
        return ciudad;
    }

    public String getCodigoPostal() {
        return codigoPostal.get();
    }

    public StringProperty codigoPostalProperty() {
        return codigoPostal;
    }

    public String getContrasena() {
        return contrasena.get();
    }

    public StringProperty contrasenaProperty() {
        return contrasena;
    }

    public String getNomina() {
        return nomina.get();
    }

    public StringProperty nominaProperty() {
        return nomina;
    }

    public String getFechaContratacion() {
        return fechaContratacion.get();
    }

    public StringProperty fechaContratacionProperty() {
        return fechaContratacion;
    }

    public String getNuss() {
        return nuss.get();
    }

    public StringProperty nussProperty() {
        return nuss;
    }

    public StringProperty idProperty() {
        return id;
    }

    public String getRol() {
        return rol.get();
    }

    public StringProperty rolProperty() {
        return rol;
    }

    public String getHoras() {
        return horas.get();
    }

    public StringProperty horasProperty() {
        return horas;
    }

    public String getSeguro() {
        return seguro.get();
    }

    public StringProperty seguroProperty() {
        return seguro;
    }

    public static void crearImagenes(Button button,  AnchorPane fondo) {

        if ( Iniciador.nochedia) {
            Image lunaImage = new Image(Controlador.class.getResourceAsStream("/imgs/luna.png"));
            ImageView lunaImageView = new ImageView(lunaImage);
            lunaImageView.setFitWidth(30);
            lunaImageView.setFitHeight(30);
            button.setGraphic(lunaImageView);
            cambiarClaroOscuro(fondo);
        } else {
            Image solImage = new Image(Controlador.class.getResourceAsStream("/imgs/sol.png"));
            ImageView solImageView = new ImageView(solImage);
            solImageView.setFitWidth(30);
            solImageView.setFitHeight(30);
            button.setGraphic(solImageView);
            cambiarClaroOscuro(fondo);
        }
    }
    public static void cambiarClaroOscuro( AnchorPane fondo) {
        try {
            Scene scene = fondo.getScene();

            if (scene != null) {
                String cssOscuro = SegundaPantallaControlador.class.getResource("/styles/style_night.css").toExternalForm();
                String cssClaro = SegundaPantallaControlador.class.getResource("/styles/style_day.css").toExternalForm();

                if (Iniciador.nochedia) {
                    scene.getStylesheets().clear();
                    scene.getStylesheets().add(cssClaro);
                    Iniciador.nochedia = false;

                } else {
                    scene.getStylesheets().clear();
                    scene.getStylesheets().add(cssOscuro);

                    Iniciador.nochedia = true;

                }
            } else {
                System.out.println("La escena es nula. Asegúrate de que la escena esté asignada antes de intentar acceder a ella.");
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }



}
