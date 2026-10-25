package pe.edu.upeu.entregable1.implementacion;

import pe.edu.upeu.entregable1.interfaces.ICita;
import pe.edu.upeu.entregable1.modelo.Cita;

import java.util.ArrayList;

public class CitaImpl implements ICita {
    static ArrayList<Cita> arraycita =new ArrayList<>();

    @Override
    public void agregar(Cita m) {
        if (m == null || m.getIdCita() <= 0) {
            System.out.println("El ID debe ser mayor que cero.");
            return;
        }
        for (Cita registrado : arraycita) {
            if (registrado.getIdCita() == m.getIdCita()) {
                System.out.println("Ya existe un registro con ese ID.");
                return;
            }
        }
        if (m.getMascota() == null || m.getVeterinario() == null) {
            System.out.println("Faltan los registros asociados.");
            return;
        }
        arraycita.add(m);
        System.out.println("Registro agregado con el ID: "+m.getIdCita());
    }

    @Override
    public Cita buscarPorId(int id) {
        for (Cita m: arraycita){
            if(m.getIdCita()==id){
                return m;
            }
        }
        return null;
    }

    @Override
    public void listar() {
        if (arraycita.isEmpty()) {
            System.out.println("No hay registros");
        } else {
            System.out.println("\nLISTA DE CITAS");
            System.out.println("");
            System.out.printf("%-6s %-18s %-20s %-14s %-8s %-25s%n", "ID", "MASCOTA", "VETERINARIO", "FECHA", "HORA", "MOTIVO");
            System.out.println("------------------------------------------------------------------------------------------------");
            for (Cita m : arraycita) {
                System.out.printf("%-6s %-18s %-20s %-14s %-8s %-25s%n",
                        m.getIdCita(),
                        m.getMascota().getNombre(),
                        m.getVeterinario().getNombres(),
                        m.getFecha(),
                        m.getHora(),
                        m.getMotivo());
            }
        }
    }
}
