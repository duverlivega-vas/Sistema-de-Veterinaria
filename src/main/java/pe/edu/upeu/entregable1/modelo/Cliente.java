package pe.edu.upeu.entregable1.modelo;

// CAMBIO: imports de las properties de JavaFX
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Cliente extends Persona {

    private final StringProperty direccion = new SimpleStringProperty(this, "direccion");

    private final StringProperty estado = new SimpleStringProperty(this, "estado", "Activo");

    public Cliente() {
    }

    // telefono ahora es String
    public Cliente(int idPersona, String nombres, String apellidos, String dni,
                   String telefono, String direccion) {
        super(idPersona, nombres, apellidos, dni, telefono);
        setDireccion(direccion);
    }

    // ---------- direccion ----------
    public final String getDireccion() {
        return direccion.get();
    }

    public final void setDireccion(String value) {
        direccion.set(value);
    }

    public final StringProperty direccionProperty() {
        return direccion;
    }

    // ---------- estado ----------
    public final String getEstado() {
        return estado.get();
    }

    public final void setEstado(String value) {
        estado.set(value);
    }

    public final StringProperty estadoProperty() {
        return estado;
    }

    @Override
    public void mostrarInfo(Persona p) {
        // CAMBIO: Telefono ahora se imprime con %s (es texto)
        System.out.printf("ID: %d | %s %s | DNI: %s | Telefono: %s%n",
                p.getIdPersona(), p.getNombres(), p.getApellidos(), p.getDni(), p.getTelefono());
    }
}

//cambiar todo el modelo de property (hecho)