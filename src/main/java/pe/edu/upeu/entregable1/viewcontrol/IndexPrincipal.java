package pe.edu.upeu.entregable1.viewcontrol;

import jdk.swing.interop.SwingInterOpUtils;
import pe.edu.upeu.entregable1.utils.Lectura;

public class IndexPrincipal {
    static Lectura leer = new Lectura();
    static ClienteIndex cl = new ClienteIndex();
    static MascotaIndex mi = new MascotaIndex();
    static VeterinarioIndex vi = new VeterinarioIndex();
    static CitaIndex ci = new CitaIndex();

    static ConsultaIndex coi = new ConsultaIndex();
    static TratamientoIndex ti = new TratamientoIndex();

    public static void menu() {
        System.out.println("====SISTEMA DE GESTION DE VETERINARIA====");
        System.out.println("1. Cliente: ");
        System.out.println("2. Veterinario: ");
        System.out.println("3. Mascota: ");
        System.out.println("4. Cita: ");
        System.out.println("5. Consulta: ");
        System.out.println("6. Tratamiento: ");
        System.out.println("7. Salir: ");
        System.out.println("______________________________________________");
        System.out.println("Seleccione una opcion entre [1 - 7]");
        System.out.println("");

    }

    public static void inicio() {
        int op;
        do {
            menu();
            op = leer.entero();
            switch (op) {
                case 1 -> cl.inicioCliente();
                case 2 -> vi.inicioVeterinario();
                case 3 -> mi.inicioMascota();
                case 4 -> ci.inicioCita();
                case 5 -> coi.inicioConsulta();
                case 6 -> ti.inicioTratamiento();
                case 7 -> System.out.println("Gracias por usar el sistema...");
                default -> System.out.println("Opcion invalida. Intente nuevamente.");

            }

        } while (op != 7);
    }
}
