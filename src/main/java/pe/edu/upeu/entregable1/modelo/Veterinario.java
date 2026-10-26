package pe.edu.upeu.entregable1.modelo;

//las properties de JavaFX
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Veterinario extends Persona {


    private final StringProperty especialidad = new SimpleStringProperty(this, "especialidad");
    // NUEVO: la tabla veterinario tiene la columna estado (Activo / Inactivo)
    private final StringProperty estado = new SimpleStringProperty(this, "estado", "Activo");

    public Veterinario() {

    }

    public Veterinario(int idPersona, String nombres, String apellidos, String dni,
                       String telefono, String especialidad) {
        super(idPersona, nombres, apellidos, dni, telefono);
        setEspecialidad(especialidad);
    }

    // ---------- especialidad ----------
    public final String getEspecialidad() {
        return especialidad.get();
    }

    public final void setEspecialidad(String value) {
        especialidad.set(value);
    }

    public final StringProperty especialidadProperty() {
        return especialidad;
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
        super.mostrarInfo(p);
        System.out.println("\nID VETERINARIO: " + p.getIdPersona()
                + "\nNOMBRES: " + p.getNombres()
                + "\nAPELLIDOS: " + p.getApellidos()
                + "\nDNI: " + p.getDni()
                + "\nTELEFONO: " + p.getTelefono());
    }

}
