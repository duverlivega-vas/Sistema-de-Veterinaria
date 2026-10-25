package pe.edu.upeu.entregable1.viewcontrol;

import pe.edu.upeu.entregable1.utils.MascotaLectura;
import pe.edu.upeu.entregable1.utils.Lectura;

public class MascotaIndex {
    static Lectura leer=new Lectura();
    static MascotaLectura cl=new MascotaLectura();
    public static void menuMascota(){
        System.out.println("***MENU DE MASCOTA***");
        System.out.println("1. Agregar: ");
        System.out.println("2. Volver: ");
    }

    public static void inicioMascota(){
        int op;
        do {
            menuMascota();
            op=leer.entero();
            switch(op){
                case 1 -> cl.leerAgregarMascota();
                case 2 -> System.out.println("Regresar al menu principal");
            }
        }while(op!=2);
    }
}
