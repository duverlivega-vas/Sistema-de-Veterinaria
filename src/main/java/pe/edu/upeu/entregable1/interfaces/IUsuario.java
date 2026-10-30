package pe.edu.upeu.entregable1.interfaces;

import pe.edu.upeu.entregable1.modelo.Usuario;
import java.util.List;

public interface IUsuario {
    public boolean insert(Usuario u);
    public boolean update(Usuario u);
    public boolean delete(Usuario u);
    public Usuario searchById(int id);
    public Usuario searchByDni(String dni);
    public Usuario searchByUsuario(String usuario);
    public List<Usuario> listAll();
}
