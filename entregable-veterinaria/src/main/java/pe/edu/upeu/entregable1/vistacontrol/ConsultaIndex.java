package pe.edu.upeu.entregable1.viewcontrol;

import pe.edu.upeu.entregable1.utils.ConsultaLectura;
import pe.edu.upeu.entregable1.utils.Lectura;

public class ConsultaIndex {
    static Lectura leer=new Lectura();
    static ConsultaLectura cl=new ConsultaLectura();
    public static void menuConsulta(){
        System.out.println("***MENU DE CONSULTA***");
        System.out.println("1. Agregar: ");
        System.out.println("2. Listar: ");
        System.out.println("3. Volver: ");
    }

    public static void inicioConsulta(){
        int op;
        do {
            menuConsulta();
            op=leer.entero();
            switch(op){
                case 1 -> cl.leerAgregarConsulta();
                case 2 -> cl.leerListarConsulta();
                case 3 -> System.out.println("Regresar al menu principal");
            }
        }while(op!=3);
    }
}
