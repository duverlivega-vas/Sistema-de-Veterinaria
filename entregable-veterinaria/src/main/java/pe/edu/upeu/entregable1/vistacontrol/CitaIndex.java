package pe.edu.upeu.entregable1.viewcontrol;

import pe.edu.upeu.entregable1.utils.CitaLectura;
import pe.edu.upeu.entregable1.utils.Lectura;

public class CitaIndex {
    static Lectura leer=new Lectura();
    static CitaLectura cl=new CitaLectura();
    public static void menuCita(){
        System.out.println("***MENU DE CITA***");
        System.out.println("1. Agregar: ");
        System.out.println("2. Listar: ");
        System.out.println("3. Volver: ");
    }

    public static void inicioCita(){
        int op;
        do {
            menuCita();
            op=leer.entero();
            switch(op){
                case 1 -> cl.leerAgregarCita();
                case 2 -> cl.leerListarCita();
                case 3 -> System.out.println("Regresar al menu principal");
            }
        }while(op!=3);
    }
}
