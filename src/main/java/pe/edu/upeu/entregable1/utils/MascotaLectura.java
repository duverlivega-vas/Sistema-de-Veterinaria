package pe.edu.upeu.entregable1.utils;

import pe.edu.upeu.entregable1.implementacion.MascotaImpl;
import pe.edu.upeu.entregable1.interfaces.IMascota;
import pe.edu.upeu.entregable1.modelo.Mascota;
import pe.edu.upeu.entregable1.implementacion.ClienteImpl;
import pe.edu.upeu.entregable1.interfaces.ICliente;
import pe.edu.upeu.entregable1.modelo.Cliente;

public class MascotaLectura {
    static Lectura leer=new Lectura();
    static IMascota crud=new MascotaImpl();
    static ICliente clienteCrud=new ClienteImpl();

    public void leerAgregarMascota(){
        Mascota m=new Mascota();
        System.out.println("***REGISTRAR MASCOTA***");

        System.out.println("IDMASCOTA:");
        m.setIdMascota(leer.id());

        System.out.println("NOMBRE:");
        m.setNombre(leer.cadena());

        System.out.println("ESPECIE:");
        m.setEspecie(leer.cadena());

        System.out.println("RAZA:");
        m.setRaza(leer.cadena());

        System.out.println("Asignar dueño por el ID:");
        int id_cliente=leer.id();
        Cliente cliente=clienteCrud.buscarPorId(id_cliente);
        if(cliente!=null){
            m.setCliente(cliente);
        }else{
            System.out.println("No existe el registro");
            return;
        }

        crud.agregar(m);
    }
}
