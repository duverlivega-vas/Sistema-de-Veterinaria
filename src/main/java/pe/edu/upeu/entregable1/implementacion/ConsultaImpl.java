package pe.edu.upeu.entregable1.implementacion;

import pe.edu.upeu.entregable1.interfaces.IConsulta;
import pe.edu.upeu.entregable1.modelo.Consulta;

import java.util.ArrayList;

public class ConsultaImpl implements IConsulta {
    static ArrayList<Consulta> arrayM=new ArrayList<>();

    @Override
    public void agregar(Consulta m) {
        if (m == null || m.getIdConsulta() <= 0) {
            System.out.println("El ID debe ser mayor que cero.");
            return;
        }
        for (Consulta registrado : arrayM) {
            if (registrado.getIdConsulta() == m.getIdConsulta()) {
                System.out.println("Ya existe un registro con ese ID.");
                return;
            }
        }
        if (m.getCita() == null) {
            System.out.println("Faltan los registros asociados.");
            return;
        }
        arrayM.add(m);
        System.out.println("Registro agregado con el ID: "+m.getIdConsulta());
    }

    @Override
    public Consulta buscarPorId(int id) {
        for (Consulta m:arrayM){
            if(m.getIdConsulta()==id){
                return m;
            }
        }
        return null;
    }

    @Override
    public void listar() {
        if (arrayM.isEmpty()) {
            System.out.println("No hay registros");
        } else {
            System.out.println("\nLISTA DE CONSULTAS");
            System.out.printf("%-6s %-10s %-30s %-35s%n",
                    "ID", "ID CITA", "DIAGNOSTICO", "OBSERVACIONES");
            System.out.println("------------------------------------------------------------------------------------");
            for (Consulta m : arrayM) {
                System.out.printf("%-6s %-10s %-30s %-35s%n",
                        m.getIdConsulta(), m.getCita().getIdCita(), m.getDiagnostico(), m.getObservaciones());
            }
        }
    }
}
