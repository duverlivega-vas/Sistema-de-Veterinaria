package pe.edu.upeu.entregable1.viewcontrol;

import pe.edu.upeu.entregable1.utils.Lectura;
import pe.edu.upeu.entregable1.utils.CitaLectura;

public class CitaIndex {
    static Lectura leer=new Lectura();
    static CitaLectura cl=new CitaLectura();
    public static void menuCita(){
        System.out.println("=====MENU DEL CITA=====");
        System.out.println("1. Agregar: ");
        System.out.println("2. Listar: ");
        System.out.println("3. Volver: ");
        System.out.println("-------------------------");
    }
    public static void inicioCita(){
        int op;
        do {
            menuCita();
            op= leer.entero();
            switch (op){
                case 1 -> cl.leerAgregarCita();
                case 2 -> cl.leerListarCita();
                case 3 -> System.out.println("Regresar al menu principal");

                default -> System.out.println("Opcion invalida. Intente nuevamente.");

            }

        }while (op!=3);
    }
}
