package pe.edu.upeu.entregable1.interfaces;

import pe.edu.upeu.entregable1.modelo.Cliente;
import java.util.List;

public interface ICliente {
    public boolean agregar(Cliente c);
    public boolean update(Cliente c);
    public boolean delete(Cliente c);
    public Cliente searchById(int id);
    public Cliente searchByDni(String dni);
    public List<Cliente> listAll();
}
