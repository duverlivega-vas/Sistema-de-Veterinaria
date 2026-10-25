package pe.edu.upeu.entregable1.utils;

import pe.edu.upeu.entregable1.implementacion.ConsultaImpl;
import pe.edu.upeu.entregable1.interfaces.IConsulta;
import pe.edu.upeu.entregable1.modelo.Consulta;
import pe.edu.upeu.entregable1.modelo.Mascota;
import pe.edu.upeu.entregable1.implementacion.CitaImpl;
import pe.edu.upeu.entregable1.interfaces.ICita;
import pe.edu.upeu.entregable1.modelo.Cita;

public class ConsultaLectura {
    static Lectura leer=new Lectura();
    static IConsulta crud=new ConsultaImpl();
    static ICita citaCrud=new CitaImpl();

    public void leerAgregarConsulta(){
        Consulta m=new Consulta();
        System.out.println("***REGISTRAR CONSULTA***");

        System.out.println("Ingrese el ID de la consulta:");
        m.setIdConsulta(leer.id());

        System.out.println("Asignar cita a la consulta por ID:");
        int id_cita=leer.id();
        Cita cita=citaCrud.buscarPorId(id_cita);
        if(cita!=null){
            m.setCita(cita);
            System.out.println("Cita encontrada.");
            Mascota mascota = cita.getMascota();
            System.out.println("Mascota: " + mascota.getNombre() + " | ID: " + mascota.getIdMascota());
            System.out.println("Especie: " + mascota.getEspecie() + " | Raza: " + mascota.getRaza());
            System.out.println("Dueño: " + mascota.getCliente().getNombres()
                    + " " + mascota.getCliente().getApellidos()
                    + " | ID: " + mascota.getCliente().getIdPersona());
            System.out.println("Veterinario: " + cita.getVeterinario().getNombres()
                    + " " + cita.getVeterinario().getApellidos());
            System.out.println("Fecha de la cita: " + cita.getFecha() + " | Hora: " + cita.getHora());
        }else{
            System.out.println("No existe una cita con ese ID. Registre la cita primero.");
            return;
        }

        System.out.println("Ingrese el diagnostico de la mascota:");
        m.setDiagnostico(leer.cadena());

        System.out.println("Ingrese las observaciones de la consulta:");
        m.setObservaciones(leer.cadena());

        crud.agregar(m);
    }

    public void leerListarConsulta(){
        crud.listar();
    }
}
