package pe.edu.upeu.entregable1.Test;

import pe.edu.upeu.entregable1.implementacion.VeterinarioImpl;
import pe.edu.upeu.entregable1.interfaces.IVeterinario;
import pe.edu.upeu.entregable1.modelo.*;
import java.util.List;

public class TestVeterinario {
    static IVeterinario dao = new VeterinarioImpl();

    public void insertar() {
        Veterinario v = new Veterinario();
        v.setNombres("Ana");
        v.setApellidos("Vega");
        v.setDni("87654322");
        v.setTelefono("912345678");
        v.setEspecialidad("General");
        v.setEstado("Activo");
        boolean resultado = dao.insert(v);
        if (resultado) {
            System.out.println("Registro insertado correctamente");
        } else {
            System.out.println("Error al insertar");
        }
    }

    public void update() {
        Veterinario v = new Veterinario();
        v.setNombres("Ana");
        v.setApellidos("Vega");
        v.setDni("87654322");
        v.setTelefono("912345678");
        v.setEspecialidad("General");
        v.setEstado("Activo");
        v.setIdveterinario(1); // Cambiar por el ID que se desea editar.
        boolean resultado = dao.update(v);
        if (resultado) {
            System.out.println("Registro actualizado correctamente");
        } else {
            System.out.println("No se pudo actualizar el registro");
        }
    }

    public void delete() {
        Veterinario v = new Veterinario();
        v.setIdveterinario(1); // Cambiar por el ID que se desea eliminar.
        boolean resultado = dao.delete(v);
        if (resultado) {
            System.out.println("Registro eliminado correctamente");
        } else {
            System.out.println("No se pudo eliminar el registro");
        }
    }

    public void listarVeterinario() {
        List<Veterinario> lista = dao.listAll();
        if (lista != null && !lista.isEmpty()) {
            for (Veterinario v : lista) {
                System.out.println("ID: " + v.getIdveterinario());
                System.out.println("NOMBRES: " + v.getNombres());
                System.out.println("APELLIDOS: " + v.getApellidos());
                System.out.println("DNI: " + v.getDni());
                System.out.println("TELEFONO: " + v.getTelefono());
                System.out.println("ESPECIALIDAD: " + v.getEspecialidad());
                System.out.println("ESTADO: " + v.getEstado());
            }
        } else {
            System.out.println("No hay registros; si hubo un error SQL, revisar el mensaje anterior");
        }
    }

    public void searchId() {
        Veterinario v = dao.searchById(1); // Cambiar por el ID que se desea buscar.
        if (v != null) {
            System.out.println("ID: " + v.getIdveterinario());
        } else {
            System.out.println("No hay registros");
        }
    }

    public static void main(String[] args) {
        TestVeterinario t = new TestVeterinario();
        // Ejecutar primero el listado. Para modificar datos, ajustar los ejemplos e IDs.
        // t.searchId();
        // t.insertar();
        // t.update();
        // t.delete();
        t.listarVeterinario();
    }
}
