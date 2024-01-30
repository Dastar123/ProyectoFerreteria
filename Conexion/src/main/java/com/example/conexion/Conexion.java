package com.example.conexion;
import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Clase que gestiona la conexión a la base de datos PostgreSQL.
 */
public class Conexion {


    //Depende del host
    public String url = "jdbc:postgresql://localhost:5432/CytnhiaFerreteria";

    // public String url = "jdbc:postgresql://localhost:5433/CytnhiaFerreteria";


    public String usuario = "postgres";
    public String password = "1234";
    public static Connection con;

    /**
     * Establece la conexión a la base de datos.
     *
     * @return La conexión establecida.
     */
    public Connection conectar() {
        try {
            Class.forName("org.postgresql.Driver");
            con = DriverManager.getConnection(url, usuario, password);
            if (con != null) {
                System.out.println("Conectado");
            }
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(Conexion.class.getName()).log(Level.SEVERE, null, ex);
        }
        return con;
    }

    /**
     * Realiza una prueba simple ejecutando una consulta en la base de datos.
     */
    public void realizarPrueba() {
        try {

            String sql = "SELECT * FROM servicios";
            PreparedStatement statement = con.prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery();


            while (resultSet.next()) {
                System.out.println("Columna1: " + resultSet.getString("columna1"));
                System.out.println("Columna2: " + resultSet.getString("columna2"));

            }

        } catch (SQLException ex) {
            Logger.getLogger(Conexion.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Cierra la conexión a la base de datos.
     */
    public void desconectar() {
        try {
            if (con != null && !con.isClosed()) {
                con.close();
                System.out.println("Desconectado");
            }
        } catch (SQLException ex) {
            Logger.getLogger(Conexion.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}