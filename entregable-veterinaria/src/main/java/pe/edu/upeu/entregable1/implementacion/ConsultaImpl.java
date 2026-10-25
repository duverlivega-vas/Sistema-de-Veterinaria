package pe.edu.upeu.entregable1.implementacion;

import pe.edu.upeu.entregable1.interfaces.IConsulta;
import pe.edu.upeu.entregable1.modelo.Consulta;

import java.util.ArrayList;

public class ConsultaImpl implements IConsulta {
    static ArrayList<Consulta> arrayM=new ArrayList<>();

    @Override
    public void agregar(Consulta m) {
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
        if(arrayM.isEmpty()){
            System.out.println("No hay registros");
        }else{
            int i=1;
            for (Consulta m:arrayM){
                System.out.println(i+"-");
                System.out.println("\nIDCONSULTA: "+m.getIdConsulta()
                        +"\nCITA: "+m.getCita().getIdCita()
                        +"\nDIAGNOSTICO: "+m.getDiagnostico()
                        +"\nOBSERVACIONES: "+m.getObservaciones()
                );
                i++;
            }
        }
    }
}
