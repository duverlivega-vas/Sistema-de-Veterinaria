package pe.edu.upeu.entregable1.viewcontrol;

import pe.edu.upeu.entregable1.utils.Lectura;
import pe.edu.upeu.entregable1.viewcontrol.ClienteIndex;
import pe.edu.upeu.entregable1.viewcontrol.VeterinarioIndex;
import pe.edu.upeu.entregable1.viewcontrol.ClienteIndex;
import pe.edu.upeu.entregable1.viewcontrol.ClienteIndex;

public class IndexPrincipal {
    static Lectura leer=new Lectura();
    static ClienteIndex cliente=new ClienteIndex();
    static VeterinarioIndex veterinario=new VeterinarioIndex();
    static MascotaIndex mascota=new MascotaIndex();
    static CitaIndex cita=new CitaIndex();
    static ConsultaIndex consulta=new ConsultaIndex();
    static TratamientoIndex tratamiento=new TratamientoIndex();

    public static void menu(){
        System.out.println("***MENU PRINCIPAL***");
        System.out.println("1. Cliente: ");
        System.out.println("2. Veterinario: ");
        System.out.println("3. Mascota: ");
        System.out.println("4. Cita: ");
        System.out.println("5. Consulta: ");
        System.out.println("6. Tratamiento: ");
        System.out.println("7. Salir: ");
    }

    public static void inicio(){
        int op;
        do {
            menu();
            op=leer.entero();
            switch(op){
                case 1 -> cliente.inicioCliente();
                case 2 -> veterinario.inicioVeterinario();
                case 3 -> mascota.inicioMascota();
                case 4 -> cita.inicioCita();
                case 5 -> consulta.inicioConsulta();
                case 6 -> tratamiento.inicioTratamiento();
                case 7 -> System.out.println("Gracias...");
            }
        }while(op!=7);
    }
}
