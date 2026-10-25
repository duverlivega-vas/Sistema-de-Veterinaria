package pe.edu.upeu.entregable1.utils;

import pe.edu.upeu.entregable1.implementacion.ConsultaImpl;
import pe.edu.upeu.entregable1.interfaces.IConsulta;
import pe.edu.upeu.entregable1.modelo.Consulta;
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

        System.out.println("IDCONSULTA:");
        m.setIdConsulta(leer.entero());

        System.out.println("Buscar cita por ID:");
        int id_cita=leer.entero();
        Cita cita=citaCrud.buscarPorId(id_cita);
        if(cita!=null){
            m.setCita(cita);
        }else{
            System.out.println("No existe el registro");
            return;
        }

        System.out.println("DIAGNOSTICO:");
        m.setDiagnostico(leer.cadena());

        System.out.println("OBSERVACIONES:");
        m.setObservaciones(leer.cadena());

        crud.agregar(m);
    }

    public void leerListarConsulta(){
        crud.listar();
    }
}
