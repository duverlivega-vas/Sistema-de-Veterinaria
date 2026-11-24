package pe.edu.upeu.entregable1.interfaces;

import pe.edu.upeu.entregable1.modelo.Veterinario;
import java.util.List;

public interface IVeterinario {
    public boolean insert(Veterinario v);
    public boolean update(Veterinario v);
    public boolean delete(Veterinario v);
    public Veterinario searchById(int id);
    public List<Veterinario> listAll();
}
