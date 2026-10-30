package pe.edu.upeu.entregable1.Test;

import pe.edu.upeu.entregable1.implementacion.MascotaImpl;
import pe.edu.upeu.entregable1.interfaces.IMascota;
import pe.edu.upeu.entregable1.modelo.*;
import java.util.List;

public class TestMascota {
    static IMascota dao = new MascotaImpl();

    public void insertar() {
        Mascota m = new Mascota();
        Cliente cl = new Cliente();
        cl.setIdcliente(1);
        m.setCliente(cl);
        m.setNombre("Luna");
        m.setEspecie("Gato");
        m.setRaza("Mestiza");
        m.setEstado("Activo");
        boolean resultado = dao.insert(m);
        if (resultado) {
            System.out.println("Registro insertado correctamente");
        } else {
            System.out.println("Error al insertar");
        }
    }

    public void update() {
        Mascota m = new Mascota();
        Cliente cl = new Cliente();
        cl.setIdcliente(1);
        m.setCliente(cl);
        m.setNombre("Luna");
        m.setEspecie("Gato");
        m.setRaza("Mestiza");
        m.setEstado("Activo");
        m.setIdMascota(1);
        boolean resultado = dao.update(m);
        if (resultado) {
            System.out.println("Registro actualizado correctamente");
        } else {
            System.out.println("No se pudo actualizar el registro");
        }
    }

    public void delete() {
        Mascota m = new Mascota();
        m.setIdMascota(1);
        boolean resultado = dao.delete(m);
        if (resultado) {
            System.out.println("Registro eliminado correctamente");
        } else {
            System.out.println("No se pudo eliminar el registro");
        }
    }

    public void listarMascota() {
        List<Mascota> lista = dao.listAll();
        if (lista != null && !lista.isEmpty()) {
            for (Mascota m : lista) {
                System.out.println("ID: " + m.getIdMascota());
                System.out.println("NOMBRE: " + m.getNombre());
                System.out.println("ESPECIE: " + m.getEspecie());
                System.out.println("RAZA: " + m.getRaza());
                System.out.println("CLIENTEID: " + m.getCliente().getIdcliente());
                System.out.println("ESTADO: " + m.getEstado());
            }
        } else {
            System.out.println("No hay registros; si hubo un error SQL, revisar el mensaje anterior");
        }
    }

    public void searchId() {
        Mascota m = dao.searchById(1); // Cambiar por el ID que se desea buscar.
        if (m != null) {
            System.out.println("ID: " + m.getIdMascota());
        } else {
            System.out.println("No hay registros");
        }
    }

    public static void main(String[] args) {
        TestMascota t = new TestMascota();
        // t.searchId();
        // t.insertar();
        // t.update();
        // t.delete();
        t.listarMascota();
    }
}
