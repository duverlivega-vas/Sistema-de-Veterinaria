package pe.edu.upeu.entregable1.modelo;

import javafx.beans.property.*;

public class Veterinario extends Persona {

    private final StringProperty especialidad = new SimpleStringProperty();
    private final StringProperty estado = new SimpleStringProperty();
    private final IntegerProperty idveterinario = new SimpleIntegerProperty(this, "idveterinario");




    public Veterinario(int idPersona, String nombres, String apellidos, String dni,
                       int telefono, String especialidad) {
        this(idPersona, nombres, apellidos, dni, String.valueOf(telefono), especialidad);
    }

    public Veterinario() {
    }

    public Veterinario(int idPersona, String nombres, String apellidos, String dni,
                       String telefono, String especialidad) {
        super(idPersona, nombres, apellidos, dni, telefono);
        setIdveterinario(idPersona);
        setEspecialidad(especialidad);
    }

    public final String getEspecialidad() {
        return especialidad.get();
    }

    public final void setEspecialidad(String value) {
        especialidad.set(value);
    }

    public final StringProperty especialidadProperty() {
        return especialidad;
    }

    public final String getEstado() {
        return estado.get();
    }

    public final void setEstado(String value) {
        estado.set(value);
    }

    public final StringProperty estadoProperty() {
        return estado;
    }

 public final int getIdveterinario() {
        return idveterinario.get();
    }

    public final void setIdveterinario(int value) {
        idveterinario.set(value);
    }

    public final IntegerProperty idveterinarioProperty() {
        return idveterinario;
    }
    @Override
    public String toString() {
        return getIdveterinario() + " - " + getNombres() + " " + getApellidos();
    }
}
