package pe.edu.upeu.entregable1.viewcontrol;

import pe.edu.upeu.entregable1.utils.VeterinarioLectura;
import pe.edu.upeu.entregable1.utils.Lectura;

public class VeterinarioIndex {
    static Lectura leer=new Lectura();
    static VeterinarioLectura cl=new VeterinarioLectura();
    public static void menuVeterinario(){
        System.out.println("***MENU DEL VETERINARIO***");
        System.out.println("1. Agregar: ");
        System.out.println("2. Volver: ");

    }
    public static void inicioVeterinario(){
        int op;
        do {
            menuVeterinario();
            op= leer.entero();
            switch (op){
                case 1 -> cl.leerAgregarVeterinario();
                case 2 -> System.out.println("Regresar al menu principal");
                default -> System.out.println("Opcion invalida. Intente nuevamente.");

            }

        }while (op!=2);
    }
}
