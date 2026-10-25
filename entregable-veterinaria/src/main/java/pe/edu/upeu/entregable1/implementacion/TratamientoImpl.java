package pe.edu.upeu.entregable1.implementacion;

import pe.edu.upeu.entregable1.interfaces.ITratamiento;
import pe.edu.upeu.entregable1.modelo.Tratamiento;

import java.util.ArrayList;

public class TratamientoImpl implements ITratamiento {
    static ArrayList<Tratamiento> arrayM=new ArrayList<>();

    @Override
    public void agregar(Tratamiento m) {
        arrayM.add(m);
        System.out.println("Registro agregado con el ID: "+m.getIdTratamiento());
    }

    @Override
    public void listar() {
        if(arrayM.isEmpty()){
            System.out.println("No hay registros");
        }else{
            int i=1;
            for (Tratamiento m:arrayM){
                System.out.println(i+"-");
                System.out.println("\nIDTRATAMIENTO: "+m.getIdTratamiento()
                        +"\nCONSULTA: "+m.getConsulta().getIdConsulta()
                        +"\nDESCRIPCION: "+m.getDescripcion()
                        +"\nINDICACIONES: "+m.getIndicaciones()
                        +"\nDURACIONDIAS: "+m.getDuracionDias()
                );
                i++;
            }
        }
    }
}
