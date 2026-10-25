package pe.edu.upeu.entregable1.viewcontrol;

import pe.edu.upeu.entregable1.utils.TratamientoLectura;
import pe.edu.upeu.entregable1.utils.Lectura;

public class TratamientoIndex {
    static Lectura leer=new Lectura();
    static TratamientoLectura cl=new TratamientoLectura();
    public static void menuTratamiento(){
        System.out.println("***MENU DE TRATAMIENTO***");
        System.out.println("1. Agregar: ");
        System.out.println("2. Listar: ");
        System.out.println("3. Volver: ");
    }

    public static void inicioTratamiento(){
        int op;
        do {
            menuTratamiento();
            op=leer.entero();
            switch(op){
                case 1 -> cl.leerAgregarTratamiento();
                case 2 -> cl.leerListarTratamiento();
                case 3 -> System.out.println("Regresar al menu principal");
            }
        }while(op!=3);
    }
}
