package pe.edu.upeu.entregable1.utils;

import pe.edu.upeu.entregable1.implementacion.VeterinarioImpl;
import pe.edu.upeu.entregable1.interfaces.IVeterinario;
import pe.edu.upeu.entregable1.modelo.Veterinario;

public class VeterinarioLectura {
    static Lectura leer=new Lectura();
    static IVeterinario crud =new VeterinarioImpl();
    public void leerAgregarVeterinario(){
        Veterinario d=new Veterinario();
        System.out.println("***REGISTRAR VETERINARIO****");

        System.out.println("ID: ");
        d.setIdPersona(leer.entero());

        System.out.println("NOMBRE: ");
        d.setNombres(leer.cadena());

        System.out.println("APELLIDOS: ");
        d.setApellidos(leer.cadena());

        System.out.println("DNI: ");
        d.setDni(leer.cadena());

        System.out.println("TELEFONO: ");
        d.setTelefono(leer.entero());

        System.out.println("NUMERO DE COLEGIATURA: ");
        d.setNumeroColegiatura(leer.cadena());

        System.out.println("ESP: ");
        d.setEspecialidad(leer.cadena());

        crud.agregar(d);
    }
}
