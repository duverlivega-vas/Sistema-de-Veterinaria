package pe.edu.upeu.entregable1.interfaces;

import pe.edu.upeu.entregable1.modelo.Mascota;
import java.util.List;

public interface IMascota {
    public boolean insert(Mascota m);
    public boolean update(Mascota m);
    public boolean delete(Mascota m);
    public Mascota searchById(int id);
    public List<Mascota> searchByDni(String dni);
    public List<Mascota> listAll();
}
