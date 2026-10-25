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

public class CitaLectura {
    static Lectura leer=new Lectura();
    static ICita crud=new CitaImpl();
    static IMascota mascotaCrud=new MascotaImpl();
    static IVeterinario veterinarioCrud=new VeterinarioImpl();

    public void leerAgregarCita(){
        Cita m=new Cita();
        System.out.println("***REGISTRAR CITA***");

        System.out.println("IDCITA:");
        m.setIdCita(leer.entero());

        System.out.println("Buscar mascota por ID:");
        int id_mascota=leer.entero();
        Mascota mascota=mascotaCrud.buscarPorId(id_mascota);
        if(mascota!=null){
            m.setMascota(mascota);
        }else{
            System.out.println("No existe el registro");
            return;
        }

        System.out.println("Buscar veterinario por ID:");
        int id_veterinario=leer.entero();
        Veterinario veterinario=veterinarioCrud.buscarPorId(id_veterinario);
        if(veterinario!=null){
            m.setVeterinario(veterinario);
        }else{
            System.out.println("No existe el registro");
            return;
        }

        System.out.println("FECHA:");
        m.setFecha(leer.cadena());

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
