package pe.edu.upeu.entregable1.modelo;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Consulta {

    private final IntegerProperty idConsulta = new SimpleIntegerProperty(this, "idConsulta");
    private final ObjectProperty<Cita> cita = new SimpleObjectProperty<>(this, "cita");
    private final StringProperty diagnostico = new SimpleStringProperty(this, "diagnostico");
    private final StringProperty observaciones = new SimpleStringProperty(this, "observaciones");

    public Consulta() {
    }

    public Consulta(int idConsulta, Cita cita, String diagnostico, String observaciones) {
        setIdConsulta(idConsulta);
        setCita(cita);
        setDiagnostico(diagnostico);
        setObservaciones(observaciones);
    }

    // ---------- idConsulta ----------
    public final int getIdConsulta() {
        return idConsulta.get();
    }

    public final void setIdConsulta(int value) {
        idConsulta.set(value);
    }

    public final IntegerProperty idConsultaProperty() {
        return idConsulta;
    }

    // ---------- cita ----------
    public final Cita getCita() {
        return cita.get();
    }

    public final void setCita(Cita value) {
        cita.set(value);
    }

    public final ObjectProperty<Cita> citaProperty() {
        return cita;
    }

    // ---------- diagnostico ----------
    public final String getDiagnostico() {
        return diagnostico.get();
    }

    public final void setDiagnostico(String value) {
        diagnostico.set(value);
    }

    public final StringProperty diagnosticoProperty() {
        return diagnostico;
    }

    // ---------- observaciones ----------
    public final String getObservaciones() {
        return observaciones.get();
    }

    public final void setObservaciones(String value) {
        observaciones.set(value);
    }

    public final StringProperty observacionesProperty() {
        return observaciones;
    }
}