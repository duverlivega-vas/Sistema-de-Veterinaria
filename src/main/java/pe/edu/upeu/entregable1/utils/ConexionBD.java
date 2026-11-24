package pe.edu.upeu.entregable1.utils;

import java.sql.*;

public class ConexionBD {
public static Connection connection;
public static Connection getConnection(){

    try {
        if (connection == null || connection.isClosed()) {
           Class.forName("org.sqlite.JDBC");
           String url = System.getProperty("veterinaria.db.url", "jdbc:sqlite:C:/sqlite/db/veterinaria.db");
           connection= DriverManager.getConnection(url);
           try(Statement st = connection.createStatement()){
               st.execute("PRAGMA foreign_keys = ON;");
               st.execute("PRAGMA busy_timeout = 5000;");
               st.execute("PRAGMA journal_mode = WAL;");

           }
        }
        return connection;
    } catch (Exception e) {
        throw new IllegalArgumentException("Error de conexión a la base de datos", e);
    }


}

}
