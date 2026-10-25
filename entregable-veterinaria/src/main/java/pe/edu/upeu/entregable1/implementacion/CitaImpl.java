package pe.edu.upeu.entregable1.implementacion;

import pe.edu.upeu.entregable1.interfaces.ICita;
import pe.edu.upeu.entregable1.modelo.Cita;

import java.util.ArrayList;

public class CitaImpl implements ICita {
    static ArrayList<Cita> arrayM=new ArrayList<>();

    @Override
    public void agregar(Cita m) {
        arrayM.add(m);
        System.out.println("Registro agregado con el ID: "+m.getIdCita());
    }

    @Override
    public Cita buscarPorId(int id) {
        for (Cita m:arrayM){
            if(m.getIdCita()==id){
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
            for (Cita m:arrayM){
                System.out.println(i+"-");
                System.out.println("\nIDCITA: "+m.getIdCita()
                        +"\nMASCOTA: "+m.getMascota().getNombre()
                        +"\nVETERINARIO: "+m.getVeterinario().getNombres()
                        +"\nFECHA: "+m.getFecha()
                        +"\nHORA: "+m.getHora()
                        +"\nMOTIVO: "+m.getMotivo()
                );
                i++;
            }
        }
    }
}
