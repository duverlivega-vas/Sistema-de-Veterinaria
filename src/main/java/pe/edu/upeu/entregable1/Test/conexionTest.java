package pe.edu.upeu.entregable1.Test;

import pe.edu.upeu.entregable1.utils.ConexionBD;

import java.sql.Connection;
import java.sql.SQLException;

public class conexionTest {
 private static void testDeConexion(){
     try {
         Connection cn = ConexionBD.getConnection();
         if (cn != null && !cn.isClosed()) {
             System.out.println("conexion exitosa");
         }

     } catch (SQLException e) {
         throw new RuntimeException("ERROR DE CONEXION A LA BASE DE DATOS"+ e.getMessage());
     }

        }
    public static void main(String[] args) {
     testDeConexion();
    }
}
