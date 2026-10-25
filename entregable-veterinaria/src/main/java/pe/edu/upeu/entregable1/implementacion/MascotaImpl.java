package pe.edu.upeu.entregable1.implementacion;

import pe.edu.upeu.entregable1.interfaces.IMascota;
import pe.edu.upeu.entregable1.modelo.Mascota;

import java.util.ArrayList;

public class MascotaImpl implements IMascota {
    static ArrayList<Mascota> arrayM=new ArrayList<>();

    @Override
    public void agregar(Mascota m) {
        arrayM.add(m);
        System.out.println("Registro agregado con el ID: "+m.getIdMascota());
    }

    @Override
    public Mascota buscarPorId(int id) {
        for (Mascota m:arrayM){
            if(m.getIdMascota()==id){
                return m;
            }
        }
        return null;
    }
}
