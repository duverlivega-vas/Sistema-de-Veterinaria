package pe.edu.upeu.entregable1.Test;

import pe.edu.upeu.entregable1.implementacion.CitaImpl;
import pe.edu.upeu.entregable1.interfaces.ICita;
import pe.edu.upeu.entregable1.modelo.*;
import java.util.List;
import java.time.LocalDate;

public class TestCita {
    static ICita dao = new CitaImpl();

    public void insertar() {
        Cita c = new Cita();
        Mascota m = new Mascota();
        m.setIdMascota(1); // Usar una mascota existente.
        Veterinario v = new Veterinario();
        v.setIdveterinario(1); // Usar un veterinario existente.
        c.setMascota(m);
        c.setVeterinario(v);
        c.setFecha(LocalDate.of(2026, 10, 30));
        c.setHora("14:30");
        c.setMotivo("Control general");
        c.setEstado("Pendiente");
        boolean resultado = dao.insert(c);
        if (resultado) {
            System.out.println("Registro insertado correctamente");
        } else {
            System.out.println("Error al insertar");
        }
    }

    public void update() {
        Cita c = new Cita();
        Mascota m = new Mascota();
        m.setIdMascota(1); // Usar una mascota existente.
        Veterinario v = new Veterinario();
        v.setIdveterinario(1); // Usar un veterinario existente.
        c.setMascota(m);
        c.setVeterinario(v);
        c.setFecha(LocalDate.of(2026, 10, 30));
        c.setHora("14:30");
        c.setMotivo("Control general");
        c.setEstado("Pendiente");
        c.setIdCita(1); // Cambiar por el ID que se desea editar.
        boolean resultado = dao.update(c);
        if (resultado) {
            System.out.println("Registro actualizado correctamente");
        } else {
            System.out.println("No se pudo actualizar el registro");
        }
    }

    public void delete() {
        Cita c = new Cita();
        c.setIdCita(1); // Cambiar por el ID que se desea eliminar.
        boolean resultado = dao.delete(c);
        if (resultado) {
            System.out.println("Registro eliminado correctamente");
        } else {
            System.out.println("No se pudo eliminar el registro");
        }
    }

    public void listarCita() {
        List<Cita> lista = dao.listAll();
        if (lista != null && !lista.isEmpty()) {
            for (Cita c : lista) {
                System.out.println("ID: " + c.getIdCita());
                System.out.println("MASCOTAID: " + c.getMascota().getIdMascota());
                System.out.println("VETERINARIOID: " + c.getVeterinario().getIdveterinario());
                System.out.println("FECHA: " + c.getFecha());
                System.out.println("HORA: " + c.getHora());
                System.out.println("MOTIVO: " + c.getMotivo());
                System.out.println("ESTADO: " + c.getEstado());
            }
        } else {
            System.out.println("No hay registros; si hubo un error SQL, revisar el mensaje anterior");
        }
    }

    public void searchId() {
        Cita c = dao.searchById(1); // Cambiar por el ID que se desea buscar.
        if (c != null) {
            System.out.println("ID: " + c.getIdCita());
        } else {
            System.out.println("No hay registros");
        }
    }

    public static void main(String[] args) {
        TestCita t = new TestCita();
        // Ejecutar primero el listado. Para modificar datos, ajustar los ejemplos e IDs.
        // t.searchId();
        // t.insertar();
        // t.update();
        // t.delete();
        t.listarCita();
    }
}
