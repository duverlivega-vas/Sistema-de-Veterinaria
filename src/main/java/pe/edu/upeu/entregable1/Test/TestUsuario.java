package pe.edu.upeu.entregable1.Test;

import pe.edu.upeu.entregable1.implementacion.UsuarioImpl;
import pe.edu.upeu.entregable1.interfaces.IUsuario;
import pe.edu.upeu.entregable1.modelo.Usuario;
import java.util.List;

public class TestUsuario {
    static IUsuario dao = new UsuarioImpl();


    public void buscarExistente() {
        Usuario u = dao.searchByUsuario("wilson");
        if (u != null) {
            System.out.println("Usuario encontrado: " + u.getUsuario());
            System.out.println("Rol: " + u.getTipoUser());
        } else {
            System.out.println("No se encontró el usuario");
        }
    }


    public void buscarInexistente() {
        Usuario u = dao.searchByUsuario("noexiste");
        if (u == null) {
            System.out.println("Correcto: el usuario inexistente devuelve null");
        } else {
            System.out.println("Error: no debería encontrar ese usuario");
        }
    }


    public void listarUsuarios() {
        List<Usuario> lista = dao.listAll();
        if (lista != null && !lista.isEmpty()) {
            for (Usuario u : lista) {
                System.out.println("ID: " + u.getIdusuario());
                System.out.println("USUARIO: " + u.getUsuario());
                System.out.println("ROL: " + u.getTipoUser());
                System.out.println("-----------------------");
            }
        } else {
            System.out.println("No hay usuarios registrados");
        }
    }

    public static void main(String[] args) {
        TestUsuario test = new TestUsuario();
        test.buscarExistente();
        test.buscarInexistente();
        test.listarUsuarios();
    }
}