package pe.edu.upeu.entregable1.utils;

import pe.edu.upeu.entregable1.implementacion.TratamientoImpl;
import pe.edu.upeu.entregable1.interfaces.ITratamiento;
import pe.edu.upeu.entregable1.modelo.Tratamiento;
import pe.edu.upeu.entregable1.modelo.Mascota;
import pe.edu.upeu.entregable1.implementacion.ConsultaImpl;
import pe.edu.upeu.entregable1.interfaces.IConsulta;
import pe.edu.upeu.entregable1.modelo.Consulta;

public class TratamientoLectura {
    static Lectura leer = new Lectura();
    static ITratamiento crud = new TratamientoImpl();
    static IConsulta consultaCrud = new ConsultaImpl();

    public void leerAgregarTratamiento() {
        Tratamiento m = new Tratamiento();
        System.out.println("===REGISTRAR TRATAMIENTO===");

        System.out.println("Ingrese el ID del tratamiento:");
        m.setIdTratamiento(leer.id());

        System.out.println("Asignar consulta al tratamiento por ID:");
        int id_consulta = leer.id();
        Consulta consulta = consultaCrud.buscarPorId(id_consulta);
        if (consulta != null) {
            m.setConsulta(consulta);
            System.out.println("Consulta encontrada.");
            Mascota mascota = consulta.getCita().getMascota();
            System.out.println("Mascota: " + mascota.getNombre() + " | ID: " + mascota.getIdMascota());
            System.out.println("Especie: " + mascota.getEspecie() + " | Raza: " + mascota.getRaza());
            System.out.println("Dueño: " + mascota.getCliente().getNombres()
                    + " " + mascota.getCliente().getApellidos()
                    + " | ID: " + mascota.getCliente().getIdPersona());
            System.out.println("Veterinario: " + consulta.getCita().getVeterinario().getNombres()
                    + " " + consulta.getCita().getVeterinario().getApellidos());
            System.out.println("Diagnostico: " + consulta.getDiagnostico());
        } else {
            System.out.println("No existe una consulta con ese ID. Registre la consulta primero.");
            return;
        }

        System.out.println("Ingrese la descripcion del tratamiento:");
        m.setDescripcion(leer.cadena());

        System.out.println("Ingrese las indicaciones del tratamiento:");
        m.setIndicaciones(leer.cadena());

        System.out.println("Ingrese la duracion del tratamiento en dias:");
        m.setDuracionDias(leer.entero());

        crud.agregar(m);
    }

    public void leerListarTratamiento() {
        crud.listar();
    }
}
