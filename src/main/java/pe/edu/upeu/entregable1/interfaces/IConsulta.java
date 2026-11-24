package pe.edu.upeu.entregable1.interfaces;

import pe.edu.upeu.entregable1.modelo.Consulta;
import java.util.List;

public interface IConsulta {
    public boolean insert(Consulta c);
    public boolean update(Consulta c);
    public boolean delete(Consulta c);
    public Consulta searchById(int id);
    public List<Consulta> listAll();
}
