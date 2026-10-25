package pe.edu.upeu.entregable1.implementacion;

import pe.edu.upeu.entregable1.interfaces.ITratamiento;
import pe.edu.upeu.entregable1.modelo.Tratamiento;

import java.util.ArrayList;

public class TratamientoImpl implements ITratamiento {
    static ArrayList<Tratamiento> arrayM=new ArrayList<>();

    @Override
    public void agregar(Tratamiento m) {
        if (m == null || m.getIdTratamiento() <= 0) {
            System.out.println("El ID debe ser mayor que cero.");
            return;
        }
        for (Tratamiento registrado : arrayM) {
            if (registrado.getIdTratamiento() == m.getIdTratamiento()) {
                System.out.println("Ya existe un registro con ese ID.");
                return;
            }
        }
        if (m.getConsulta() == null) {
            System.out.println("Faltan los registros asociados.");
            return;
        }
        arrayM.add(m);
        System.out.println("Registro agregado con el ID: "+m.getIdTratamiento());
    }

    @Override
    public void listar() {
        if (arrayM.isEmpty()) {
            System.out.println("No hay registros");
        } else {
            System.out.println("\nLISTA DE TRATAMIENTOS");
            System.out.printf("%-6s %-14s %-30s %-35s %-6s%n",
                    "ID", "ID CONSULTA", "DESCRIPCION", "INDICACIONES", "DIAS");
            System.out.println("-----------------------------------------------------------------------------------------------");
            for (Tratamiento m : arrayM) {
                System.out.printf("%-6s %-14s %-30s %-35s %-6s%n",
                        m.getIdTratamiento(), m.getConsulta().getIdConsulta(), m.getDescripcion(), m.getIndicaciones(), m.getDuracionDias());
            }
        }
    }
}
