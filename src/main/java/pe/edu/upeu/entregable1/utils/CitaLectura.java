package pe.edu.upeu.entregable1.utils;

import pe.edu.upeu.entregable1.implementacion.CitaImpl;
import pe.edu.upeu.entregable1.interfaces.ICita;
import pe.edu.upeu.entregable1.modelo.Cita;
import pe.edu.upeu.entregable1.implementacion.MascotaImpl;
import pe.edu.upeu.entregable1.interfaces.IMascota;
import pe.edu.upeu.entregable1.modelo.Mascota;
import pe.edu.upeu.entregable1.implementacion.VeterinarioImpl;
import pe.edu.upeu.entregable1.interfaces.IVeterinario;
import pe.edu.upeu.entregable1.modelo.Veterinario;

import java.time.LocalDate;

public class CitaLectura {
    static Lectura leer=new Lectura();
    static ICita crud=new CitaImpl();
    static IMascota mascotaCrud=new MascotaImpl();
    static IVeterinario veterinarioCrud=new VeterinarioImpl();

    public void leerAgregarCita(){
        Cita m=new Cita();
        System.out.println("***REGISTRAR CITA***");

        System.out.println("IDCITA:");
        m.setIdCita(leer.id());

        System.out.println("Agregar mascota por ID:");
        int id_mascota=leer.id();
        Mascota mascota=mascotaCrud.buscarPorId(id_mascota);
        if(mascota!=null){
            m.setMascota(mascota);
            System.out.println("Mascota encontrada: " + mascota.getNombre());
            if (mascota.getCliente() != null) {
                System.out.println("Dueño: " + mascota.getCliente().getNombres()
                        + " " + mascota.getCliente().getApellidos()
                        + " | ID: " + mascota.getCliente().getIdPersona());
            } else {
                System.out.println("La mascota no tiene dueño asignado.");
                return;
            }
        }else{
            System.out.println("No existe el registro");
            return;
        }

        System.out.println("Asignar el veterinario a la cita por ID:");
        int id_veterinario=leer.id();
        Veterinario veterinario=veterinarioCrud.buscarPorId(id_veterinario);
        if(veterinario!=null){
            m.setVeterinario(veterinario);
        }else{
            System.out.println("No existe el registro");
            return;
        }

        System.out.println("FECHA:");
        m.setFecha(LocalDate.parse(leer.cadena()));

        System.out.println("HORA:");
        m.setHora(leer.cadena());

        System.out.println("MOTIVO:");
        m.setMotivo(leer.cadena());

        crud.agregar(m);
    }

    public void leerListarCita(){
        crud.listar();
    }
}
