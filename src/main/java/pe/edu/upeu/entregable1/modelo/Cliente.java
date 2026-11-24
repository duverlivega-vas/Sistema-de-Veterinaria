package pe.edu.upeu.entregable1.modelo;

import javafx.beans.property.*;

public class Cliente extends Persona {
    private final IntegerProperty idcliente = new SimpleIntegerProperty();

    private final StringProperty direccion = new SimpleStringProperty();

    private final StringProperty estado = new SimpleStringProperty();

    public Cliente(int idPersona, String nombres, String apellidos, String dni,
                   int telefono, String direccion) {
        this(idPersona, nombres, apellidos, dni, String.valueOf(telefono), direccion);
    }

    public Cliente() {
    }

    public Cliente(int idPersona, String nombres, String apellidos, String dni,
                   String telefono, String direccion) {
        super(idPersona, nombres, apellidos, dni, telefono);
        setIdcliente(idPersona);
        setDireccion(direccion);
    }

    public final String getDireccion() {
        return direccion.get();
    }

    public final void setDireccion(String value) {
        direccion.set(value);
    }

    public final StringProperty direccionProperty() {
        return direccion;
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
     public final int getIdcliente() {
        return idcliente.get();
    }

    public final void setIdcliente(int value) {
        idcliente.set(value);
    }

    public final IntegerProperty idclienteProperty() {
        return idcliente;
    }

    @Override
    public String toString() {
        return getIdcliente() + " - " + getNombres() + " " + getApellidos();
    }
}
