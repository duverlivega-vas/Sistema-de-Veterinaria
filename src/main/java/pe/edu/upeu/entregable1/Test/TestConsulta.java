package pe.edu.upeu.entregable1.Test;

import pe.edu.upeu.entregable1.implementacion.ConsultaImpl;
import pe.edu.upeu.entregable1.interfaces.IConsulta;
import pe.edu.upeu.entregable1.modelo.*;
import java.util.List;

public class TestConsulta {
    static IConsulta dao = new ConsultaImpl();

    public void insertar() {
        Consulta c = new Consulta();
        Cita ci = new Cita();
        ci.setIdCita(1); // Usar una cita existente sin co.
        c.setCita(ci);
        c.setDiagnostico("Control general");
        c.setObservaciones("Sin observaciones");
        boolean resultado = dao.insert(c);
        if (resultado) {
            System.out.println("Registro insertado correctamente");
        } else {
            System.out.println("Error al insertar");
        }
    }

    public void update() {
        Consulta c = new Consulta();
        Cita ci = new Cita();
        ci.setIdCita(1); // Usar una cita existente sin co.
        c.setCita(ci);
        c.setDiagnostico("Control general");
        c.setObservaciones("Sin observaciones");
        c.setIdConsulta(1); // Cambiar por el ID que se desea editar.
        boolean resultado = dao.update(c);
        if (resultado) {
            System.out.println("Registro actualizado correctamente");
        } else {
            System.out.println("No se pudo actualizar el registro");
        }
    }

    public void delete() {
        Consulta c = new Consulta();
        c.setIdConsulta(1); // Cambiar por el ID que se desea eliminar.
        boolean resultado = dao.delete(c);
        if (resultado) {
            System.out.println("Registro eliminado correctamente");
        } else {
            System.out.println("No se pudo eliminar el registro");
        }
    }

    public void listarConsulta() {
        List<Consulta> lista = dao.listAll();
        if (lista != null && !lista.isEmpty()) {
            for (Consulta c : lista) {
                System.out.println("ID: " + c.getIdConsulta());
                System.out.println("CITAID: " + c.getCita().getIdCita());
                System.out.println("DIAGNOSTICO: " + c.getDiagnostico());
                System.out.println("OBSERVACIONES: " + c.getObservaciones());
            }
        } else {
            System.out.println("No hay registros; si hubo un error SQL, revisar el mensaje anterior");
        }
    }

    public void searchId() {
        Consulta c = dao.searchById(1); // Cambiar por el ID que se desea buscar.
        if (c != null) {
            System.out.println("ID: " + c.getIdConsulta());
        } else {
            System.out.println("No hay registros");
        }
    }

    public static void main(String[] args) {
        TestConsulta t = new TestConsulta();
        // Ejecutar primero el listado. Para modificar datos, ajustar los ejemplos e IDs.
        // t.searchId();
        // t.insertar();
        // t.update();
        // t.delete();
        t.listarConsulta();
    }
}
