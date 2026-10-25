package pe.edu.upeu.entregable1.utils;

import pe.edu.upeu.entregable1.implementacion.TratamientoImpl;
import pe.edu.upeu.entregable1.interfaces.ITratamiento;
import pe.edu.upeu.entregable1.modelo.Tratamiento;
import pe.edu.upeu.entregable1.implementacion.ConsultaImpl;
import pe.edu.upeu.entregable1.interfaces.IConsulta;
import pe.edu.upeu.entregable1.modelo.Consulta;

public class TratamientoLectura {
    static Lectura leer=new Lectura();
    static ITratamiento crud=new TratamientoImpl();
    static IConsulta consultaCrud=new ConsultaImpl();

    public void leerAgregarTratamiento(){
        Tratamiento m=new Tratamiento();
        System.out.println("***REGISTRAR TRATAMIENTO***");

        System.out.println("IDTRATAMIENTO:");
        m.setIdTratamiento(leer.entero());

        System.out.println("Buscar consulta por ID:");
        int id_consulta=leer.entero();
        Consulta consulta=consultaCrud.buscarPorId(id_consulta);
        if(consulta!=null){
            m.setConsulta(consulta);
        }else{
            System.out.println("No existe el registro");
            return;
        }

        System.out.println("DESCRIPCION:");
        m.setDescripcion(leer.cadena());

        System.out.println("INDICACIONES:");
        m.setIndicaciones(leer.cadena());

        System.out.println("DURACIONDIAS:");
        m.setDuracionDias(leer.entero());

        crud.agregar(m);
    }

    public void leerListarTratamiento(){
        crud.listar();
    }
}
