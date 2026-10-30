package pe.edu.upeu.entregable1.Test;

import pe.edu.upeu.entregable1.implementacion.ClienteImpl;
import pe.edu.upeu.entregable1.interfaces.ICliente;
import pe.edu.upeu.entregable1.modelo.*;
import java.util.List;

public class TestCliente {
    static ICliente dao = new ClienteImpl();

    public void insertar() {
        Cliente c = new Cliente();
        c.setNombres("Juan");
        c.setApellidos("Perez");
        c.setDni("98765433");
        c.setTelefono("987654321");
        c.setDireccion("Moyobamba");
        c.setEstado("Activo");
        boolean resultado = dao.agregar(c);
        if (resultado) {
            System.out.println("Registro insertado correctamente");
        } else {
            System.out.println("Error al insertar");
        }
    }

    public void update() {
        Cliente c = new Cliente();
        c.setNombres("Juan");
        c.setApellidos("Perez");
        c.setDni("98765433");
        c.setTelefono("987654321");
        c.setDireccion("Moyobamba");
        c.setEstado("Activo");
        c.setIdcliente(1); // Cambiar por el ID que se desea editar.
        boolean resultado = dao.update(c);
        if (resultado) {
            System.out.println("Registro actualizado correctamente");
        } else {
            System.out.println("No se pudo actualizar el registro");
        }
    }

    public void delete() {
        Cliente c = new Cliente();
        c.setIdcliente(1); // Cambiar por el ID que se desea eliminar.
        boolean resultado = dao.delete(c);
        if (resultado) {
            System.out.println("Registro eliminado correctamente");
        } else {
            System.out.println("No se pudo eliminar el registro");
        }
    }

    public void listarCliente() {
        List<Cliente> lista = dao.listAll();
        if (lista != null && !lista.isEmpty()) {
            for (Cliente c : lista) {
                System.out.println("ID: " + c.getIdcliente());
                System.out.println("NOMBRES: " + c.getNombres());
                System.out.println("APELLIDOS: " + c.getApellidos());
                System.out.println("DNI: " + c.getDni());
                System.out.println("TELEFONO: " + c.getTelefono());
                System.out.println("DIRECCION: " + c.getDireccion());
                System.out.println("ESTADO: " + c.getEstado());
            }
        } else {
            System.out.println("No hay registros; si hubo un error SQL, revisar el mensaje anterior");
        }
    }

    public void searchId() {
        Cliente c = dao.searchById(1); // Cambiar por el ID que se desea buscar.
        if (c != null) {
            System.out.println("ID: " + c.getIdcliente());
        } else {
            System.out.println("No hay registros");
        }
    }

    public void searchDni() {
        Cliente c = dao.searchByDni("98765433");
        if (c != null) {
            System.out.println("ID: " + c.getIdcliente());
            System.out.println("NOMBRES: " + c.getNombres());
            System.out.println("DNI: " + c.getDni());
        } else {
            System.out.println("No hay registros");
        }
    }

    public static void main(String[] args) {
        TestCliente t = new TestCliente();
        // Ejecutar primero el listado. Para modificar datos, ajustar los ejemplos e IDs.
        // t.searchId();
        // t.searchDni();
        // t.insertar();
        // t.update();
        // t.delete();
        t.listarCliente();
    }
}
