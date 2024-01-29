module com.example.conexion {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires org.postgresql.jdbc;


    opens com.example.conexion to javafx.fxml;
    exports com.example.conexion;
}