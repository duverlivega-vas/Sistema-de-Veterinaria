package pe.edu.upeu.entregable1.modelo;

// imports de las properties de JavaFX
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Cita {

    // CAMBIO: atributos simples -> properties de JavaFX
    private final IntegerProperty idCita = new SimpleIntegerProperty(this, "idCita");
    private final ObjectProperty<Mascota> mascota = new SimpleObjectProperty<>(this, "mascota");
    private final ObjectProperty<Veterinario> veterinario = new SimpleObjectProperty<>(this, "veterinario");
    // fecha y hora siguen como texto, igual que en la base (2026-10-30 y 14:30)
    private final StringProperty fecha = new SimpleStringProperty(this, "fecha");
    private final StringProperty hora = new SimpleStringProperty(this, "hora");
    private final StringProperty motivo = new SimpleStringProperty(this, "motivo");
    // NUEVO: la tabla cita tiene la columna estado (Pendiente / Atendida / Cancelada)
    private final StringProperty estado = new SimpleStringProperty(this, "estado", "Pendiente");

    public Cita() {
    }

    // ---------- idCita ----------
    public final int getIdCita() {
        return idCita.get();
    }

    public final void setIdCita(int value) {
        idCita.set(value);
    }

    public final IntegerProperty idCitaProperty() {
        return idCita;
    }

    // ---------- mascota ----------
    public final Mascota getMascota() {
        return mascota.get();
    }

    public final void setMascota(Mascota value) {
        mascota.set(value);
    }

    public final ObjectProperty<Mascota> mascotaProperty() {
        return mascota;
    }

    // ---------- veterinario ----------
    public final Veterinario getVeterinario() {
        return veterinario.get();
    }

    public final void setVeterinario(Veterinario value) {
        veterinario.set(value);
    }

    public final ObjectProperty<Veterinario> veterinarioProperty() {
        return veterinario;
    }

    // ---------- fecha ----------
    public final String getFecha() {
        return fecha.get();
    }

    public final void setFecha(String value) {
        fecha.set(value);
    }

    public final StringProperty fechaProperty() {
        return fecha;
    }

    // ---------- hora ----------
    public final String getHora() {
        return hora.get();
    }

    public final void setHora(String value) {
        hora.set(value);
    }

    public final StringProperty horaProperty() {
        return hora;
    }

    // ---------- motivo ----------
    public final String getMotivo() {
        return motivo.get();
    }

    public final void setMotivo(String value) {
        motivo.set(value);
    }

    public final StringProperty motivoProperty() {
        return motivo;
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
}