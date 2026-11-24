package pe.edu.upeu.entregable1.interfaces;

import pe.edu.upeu.entregable1.modelo.Tratamiento;
import java.util.List;

public interface ITratamiento {
    public boolean insert(Tratamiento t);
    public boolean update(Tratamiento t);
    public boolean delete(Tratamiento t);
    public Tratamiento searchById(int id);
    public List<Tratamiento> listAll();
}
