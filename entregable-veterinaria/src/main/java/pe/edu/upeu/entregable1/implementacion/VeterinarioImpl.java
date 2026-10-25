package pe.edu.upeu.entregable1.implementacion;

import pe.edu.upeu.entregable1.interfaces.IVeterinario;
import pe.edu.upeu.entregable1.modelo.Veterinario;

import java.util.ArrayList;

public class VeterinarioImpl implements IVeterinario {
    static ArrayList<Veterinario> arrayD = new ArrayList<>();

    @Override
    public void agregar(Veterinario d) {
        arrayD.add(d);
        System.out.println("Register Success!!!!");
    }

    @Override
    public Veterinario buscarPorId(int id) {
        for (Veterinario d:arrayD) {
            if (d.getIdPersona()==id) {
                return d;
            }
        }
        return null;
    }
}
