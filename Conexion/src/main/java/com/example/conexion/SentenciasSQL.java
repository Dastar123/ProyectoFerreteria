package com.example.conexion;

public class SentenciasSQL {

    public static String buscarAdmin="SELECT * FROM buscar_controlador(?)";
    public static String crearAdmin="INSERT INTO controlador (nombreUsuario, contraseña) VALUES (?, ?)";
    public static String comprobarCuenta="SELECT * FROM buscar_controlador(?)";

    public static String insertarMecanico= "INSERT INTO mecanico (rol, contHoras, seguro, empleados)\n" +
            "VALUES (?::especialidad,?,?, ROW(Row( ?, ROW(?, ?, ?, ?)),?,?,?::date,?)::empleados_type);";

    public static  String mecanicoParametros= "SELECT " +
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
}
