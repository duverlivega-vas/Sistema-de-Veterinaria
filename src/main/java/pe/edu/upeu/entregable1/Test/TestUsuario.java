package pe.edu.upeu.entregable1.Test;

import pe.edu.upeu.entregable1.implementacion.UsuarioImpl;
import pe.edu.upeu.entregable1.interfaces.IUsuario;
import pe.edu.upeu.entregable1.modelo.Usuario;
import java.util.List;

public class TestUsuario {
    private static final IUsuario dao = new UsuarioImpl();

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

    public void update() {
        List<Usuario> usuarios = dao.listAll();
        if (usuarios.isEmpty()) {
            System.out.println("No hay usuarios para actualizar");
            return;
        }
        Usuario usuario = usuarios.get(0);
        usuario.setNombres(usuario.getNombres() + "*");
        System.out.println(dao.update(usuario)
                ? "Usuario actualizado correctamente"
                : "No se pudo actualizar el usuario");
    }

    public void delete() {
        List<Usuario> usuarios = dao.listAll();
        if (usuarios.isEmpty()) {
            System.out.println("No hay usuarios para eliminar");
            return;
        }
        Usuario usuario = usuarios.get(usuarios.size() - 1);
        System.out.println(dao.delete(usuario)
                ? "Usuario eliminado correctamente"
                : "No se pudo eliminar el usuario");
    }

    public void listarUsuario() {
        List<Usuario> usuarios = dao.listAll();
        if (usuarios.isEmpty()) {
            System.out.println("No hay usuarios registrados");
            return;
        }
        for (Usuario usuario : usuarios) {
            System.out.println(usuario.getUsuario() + " | " + usuario.getDni()
                    + " | " + usuario.getTipoUser());
        }
    }

    public void searchDni() {
        List<Usuario> usuarios = dao.listAll();
        if (usuarios.isEmpty()) {
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
        prueba.searchDni();
        // Ejecutar insertar, update o delete manualmente para modificar datos de prueba.
    }
}
