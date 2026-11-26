package pe.edu.upeu.entregable1.interfaces;

import pe.edu.upeu.entregable1.modelo.Cita;
import java.util.List;

public interface ICita {
    public boolean insert(Cita c);
    public boolean update(Cita c);
    public boolean delete(Cita c);
    public Cita searchById(int id);
    public List<Cita> listAll();
    public boolean updateStatus(Cita cita);
    public boolean verificarConflictoHorario(Cita cita);
}
