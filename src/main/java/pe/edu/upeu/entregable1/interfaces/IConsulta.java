package pe.edu.upeu.entregable1.interfaces;

import pe.edu.upeu.entregable1.modelo.Consulta;

public interface IConsulta {
    public void agregar(Consulta m);
    public void listar();
    public Consulta buscarPorId(int id);
}
