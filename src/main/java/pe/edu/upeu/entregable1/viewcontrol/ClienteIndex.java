package pe.edu.upeu.entregable1.viewcontrol;

import pe.edu.upeu.entregable1.utils.ClienteLectura;
import pe.edu.upeu.entregable1.utils.Lectura;

public class ClienteIndex {
    static Lectura leer=new Lectura();
    static ClienteLectura cl=new ClienteLectura();
    public static void menuCliente(){
        System.out.println("=====MENU DEL CLIENTE=====");
        System.out.println("1. Agregar: ");
        System.out.println("2. Listar: ");
        System.out.println("3. Eliminar: ");
        System.out.println("4. Editar: ");
        System.out.println("5. Volver: ");
        System.out.println("---------------------------");
        System.out.println("Seleccione una opcion entre [1 - 5]");

    }
    public static void inicioCliente(){
        int op;
        do {
            menuCliente();
            op= leer.entero();
            switch (op){
                case 1 -> cl.leerAgregarCliente();
                case 2 -> cl.leerListarCliente();
                case 3 -> cl.leerEliminarCliente();
                case 4 -> cl.leerEditarCliente();
                case 5 -> System.out.println("Regresar al menu principal");
                default -> System.out.println("Opcion invalida. Intente nuevamente.");

            }

        }while (op!=5);
    }
}
