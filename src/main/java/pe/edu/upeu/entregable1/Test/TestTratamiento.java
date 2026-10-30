package pe.edu.upeu.entregable1.Test;

import pe.edu.upeu.entregable1.implementacion.TratamientoImpl;
import pe.edu.upeu.entregable1.interfaces.ITratamiento;
import pe.edu.upeu.entregable1.modelo.*;
import java.util.List;

public class TestTratamiento {
    static ITratamiento dao = new TratamientoImpl();

    public void insertar() {
        Tratamiento t = new Tratamiento();
        Consulta co = new Consulta();
        co.setIdConsulta(1); // Usar una consulta existente.
        t.setConsulta(co);
        t.setDescripcion("Seguimiento");
        t.setIndicaciones("Regresar a control");
        t.setDuracionDias(5);
        boolean resultado = dao.insert(t);
        if (resultado) {
            System.out.println("Registro insertado correctamente");
        } else {
            System.out.println("Error al insertar");
        }
    }

    public void update() {
        Tratamiento t = new Tratamiento();
        Consulta co = new Consulta();
        co.setIdConsulta(1); // Usar una consulta existente.
        t.setConsulta(co);
        t.setDescripcion("Seguimiento");
        t.setIndicaciones("Regresar a control");
        t.setDuracionDias(5);
        t.setIdTratamiento(1); // Cambiar por el ID que se desea editar.
        boolean resultado = dao.update(t);
        if (resultado) {
            System.out.println("Registro actualizado correctamente");
        } else {
            System.out.println("No se pudo actualizar el registro");
        }
    }

    public void delete() {
        Tratamiento t = new Tratamiento();
        t.setIdTratamiento(1); // Cambiar por el ID que se desea eliminar.
        boolean resultado = dao.delete(t);
        if (resultado) {
            System.out.println("Registro eliminado correctamente");
        } else {
            System.out.println("No se pudo eliminar el registro");
        }
    }

    public void listarTratamiento() {
        List<Tratamiento> lista = dao.listAll();
        if (lista != null && !lista.isEmpty()) {
            for (Tratamiento t : lista) {
                System.out.println("ID: " + t.getIdTratamiento());
                System.out.println("CONSULTAID: " + t.getConsulta().getIdConsulta());
                System.out.println("DESCRIPCION: " + t.getDescripcion());
                System.out.println("INDICACIONES: " + t.getIndicaciones());
                System.out.println("DURACIONDIAS: " + t.getDuracionDias());
            }
        } else {
            System.out.println("No hay registros; si hubo un error SQL, revisar el mensaje anterior");
        }
    }

    public void searchId() {
        Tratamiento t = dao.searchById(1); // Cambiar por el ID que se desea buscar.
        if (t != null) {
            System.out.println("ID: " + t.getIdTratamiento());
        } else {
            System.out.println("No hay registros");
        }
    }

    public static void main(String[] args) {
        TestTratamiento t = new TestTratamiento();
        // Ejecutar primero el listado. Para modificar datos, ajustar los ejemplos e IDs.
        // t.searchId();
        // t.insertar();
        // t.update();
        // t.delete();
        t.listarTratamiento();
    }
}
