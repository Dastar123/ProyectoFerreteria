package com.example.conexion;
import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Conexion {


    //Depende del host
    //public String url = "jdbc:postgresql://localhost:5432/CytnhiaFerreteria";

    public String url = "jdbc:postgresql://localhost:5433/CytnhiaFerreteria";


    public String usuario = "postgres";
    public String password = "1234";
    public static Connection con;

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
    public void realizarPrueba() {
        try {
            // Realiza una consulta simple
            String sql = "SELECT * FROM servicios";  // Reemplaza 'tu_tabla' con el nombre de tu tabla
            PreparedStatement statement = con.prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery();

            // Muestra los resultados
            while (resultSet.next()) {
                System.out.println("Columna1: " + resultSet.getString("columna1"));
                System.out.println("Columna2: " + resultSet.getString("columna2"));
                // Agrega más columnas según sea necesario
            }

        } catch (SQLException ex) {
            Logger.getLogger(Conexion.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
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