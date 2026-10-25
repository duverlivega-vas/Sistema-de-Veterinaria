package pe.edu.upeu.entregable1.interfaces;

import pe.edu.upeu.entregable1.modelo.Cita;

public interface ICita {
    public void agregar(Cita m);
    public void listar();
    public Cita buscarPorId(int id);
}
