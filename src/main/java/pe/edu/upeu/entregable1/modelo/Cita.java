package pe.edu.upeu.entregable1.modelo;

import javafx.beans.property.*;
import java.time.LocalDate;

public class Cita {

    private final IntegerProperty idCita = new SimpleIntegerProperty();
    private final ObjectProperty<Mascota> mascota = new SimpleObjectProperty<>();
    private final ObjectProperty<Veterinario> veterinario = new SimpleObjectProperty<>();
    private final ObjectProperty<LocalDate> fecha = new SimpleObjectProperty<>();
    private final StringProperty hora = new SimpleStringProperty();
    private final StringProperty motivo = new SimpleStringProperty();
    private final StringProperty estado = new SimpleStringProperty();

    public Cita() {
    }

    public final int getIdCita() {
        return idCita.get();
    }

    public final void setIdCita(int value) {
        idCita.set(value);
    }

    public final IntegerProperty idCitaProperty() {
        return idCita;
    }

    public final Mascota getMascota() {
        return mascota.get();
    }

    public final void setMascota(Mascota value) {
        mascota.set(value);
    }

    public final ObjectProperty<Mascota> mascotaProperty() {
        return mascota;
    }

    public final Veterinario getVeterinario() {
        return veterinario.get();
    }

    public final void setVeterinario(Veterinario value) {
        veterinario.set(value);
    }

    public final ObjectProperty<Veterinario> veterinarioProperty() {
        return veterinario;
    }

    public final LocalDate getFecha() {
        return fecha.get();
    }

    public final void setFecha(LocalDate value) {
        fecha.set(value);
    }

    public final ObjectProperty<LocalDate> fechaProperty() {
        return fecha;
    }

    public final String getHora() {
        return hora.get();
    }

    public final void setHora(String value) {
        hora.set(value);
    }

    public final StringProperty horaProperty() {
        return hora;
    }

    public final String getMotivo() {
        return motivo.get();
    }

    public final void setMotivo(String value) {
        motivo.set(value);
    }

    public final StringProperty motivoProperty() {
        return motivo;
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
}