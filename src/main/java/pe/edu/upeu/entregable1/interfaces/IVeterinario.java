package pe.edu.upeu.entregable1.interfaces;

import pe.edu.upeu.entregable1.modelo.Cliente;
import pe.edu.upeu.entregable1.modelo.Veterinario;

public interface IVeterinario {
    public void agregar(Veterinario d);
    public Veterinario buscarPorId(int id);
}
