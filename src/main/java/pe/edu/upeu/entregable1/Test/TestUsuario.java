package pe.edu.upeu.entregable1.Test;

import pe.edu.upeu.entregable1.implementacion.UsuarioImpl;
import pe.edu.upeu.entregable1.interfaces.IUsuario;
import pe.edu.upeu.entregable1.modelo.Usuario;

import java.util.List;

public class TestUsuario {
    private static final IUsuario dao = new UsuarioImpl();

    public void buscarExistente() {
        List<Usuario> usuarios = dao.listAll();
        if (usuarios == null || usuarios.isEmpty()) {
            System.out.println("No hay usuarios registrados para probar la búsqueda");
            return;
        }

        String nombreUsuario = usuarios.get(0).getUsuario();
        Usuario encontrado = dao.searchByUsuario(nombreUsuario);
        if (encontrado != null) {
            System.out.println("Usuario encontrado: " + encontrado.getUsuario());
            System.out.println("Rol: " + encontrado.getTipoUser());
        } else {
            System.out.println("Error: no se encontró el usuario existente");
        }
    }

    public void buscarInexistente() {
        Usuario usuario = dao.searchByUsuario("usuario_que_no_existe");
        System.out.println(usuario == null
                ? "Correcto: el usuario inexistente devuelve null"
                : "Error: no debería encontrar ese usuario");
    }

    public void listarUsuarios() {
        List<Usuario> usuarios = dao.listAll();
        if (usuarios == null || usuarios.isEmpty()) {
            System.out.println("No hay usuarios registrados");
            return;
        }

        for (Usuario usuario : usuarios) {
            System.out.println("USUARIO: " + usuario.getUsuario());
            System.out.println("ROL: " + usuario.getTipoUser());
            System.out.println("-----------------------");
        }
    }

    public void insertar() {
        Usuario usuario = new Usuario();
        usuario.setUsuario("usuario_prueba");
        usuario.setPassword("clave123");
        usuario.setTipoUser("Recepcionista");
        usuario.setNombres("Usuario");
        usuario.setApellidos("Prueba");
        usuario.setDni("99999998");
        System.out.println(dao.insert(usuario)
                ? "Usuario insertado correctamente"
                : "No se insertó. Revise si el usuario o DNI ya existe.");
    }

    public void actualizar() {
        List<Usuario> usuarios = dao.listAll();
        if (usuarios == null || usuarios.isEmpty()) {
            System.out.println("No hay usuarios para actualizar");
            return;
        }
        Usuario usuario = usuarios.get(0);
        usuario.setNombres(usuario.getNombres() + "*");
        System.out.println(dao.update(usuario)
                ? "Usuario actualizado correctamente"
                : "No se pudo actualizar el usuario");
    }

    public void eliminar() {
        List<Usuario> usuarios = dao.listAll();
        if (usuarios == null || usuarios.isEmpty()) {
            System.out.println("No hay usuarios para eliminar");
            return;
        }
        Usuario usuario = usuarios.get(usuarios.size() - 1);
        System.out.println(dao.delete(usuario)
                ? "Usuario eliminado correctamente"
                : "No se pudo eliminar el usuario");
    }

    public void buscarPorDni() {
        List<Usuario> usuarios = dao.listAll();
        if (usuarios == null || usuarios.isEmpty()) {
            System.out.println("No hay usuarios para probar la búsqueda por DNI");
            return;
        }
        String dni = usuarios.get(0).getDni();
        Usuario encontrado = dao.searchByDni(dni);
        System.out.println(encontrado != null && dni.equals(encontrado.getDni())
                ? "Búsqueda de usuario por DNI correcta: " + dni
                : "Error en la búsqueda de usuario por DNI: " + dni);
    }

    public static void main(String[] args) {
        TestUsuario prueba = new TestUsuario();
        prueba.buscarExistente();
        prueba.buscarInexistente();
        prueba.buscarPorDni();
        prueba.listarUsuarios();
        // insertar, actualizar y eliminar se ejecutan manualmente para no modificar la BD.
    }
}
