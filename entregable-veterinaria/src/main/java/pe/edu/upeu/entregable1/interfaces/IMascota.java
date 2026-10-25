package pe.edu.upeu.entregable1.interfaces;

import pe.edu.upeu.entregable1.modelo.Mascota;

public interface IMascota {
    public void agregar(Mascota m);
    public Mascota buscarPorId(int id);
}
