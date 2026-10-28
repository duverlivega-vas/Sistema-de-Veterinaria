package pe.edu.upeu.entregable1.modelo;

import javafx.beans.property.*;

public class Consulta {

    private final IntegerProperty idConsulta = new SimpleIntegerProperty();
    private final ObjectProperty<Cita> cita = new SimpleObjectProperty<>();
    private final StringProperty diagnostico = new SimpleStringProperty();
    private final StringProperty observaciones = new SimpleStringProperty();

    public Consulta() {
    }

    public Consulta(int idConsulta, Cita cita, String diagnostico, String observaciones) {
        setIdConsulta(idConsulta);
        setCita(cita);
        setDiagnostico(diagnostico);
        setObservaciones(observaciones);
    }

    public final int getIdConsulta() {
        return idConsulta.get();
    }

    public final void setIdConsulta(int value) {
        idConsulta.set(value);
    }

    public final IntegerProperty idConsultaProperty() {
        return idConsulta;
    }

    public final Cita getCita() {
        return cita.get();
    }

    public final void setCita(Cita value) {
        cita.set(value);
    }

    public final ObjectProperty<Cita> citaProperty() {
        return cita;
    }

    public final String getDiagnostico() {
        return diagnostico.get();
    }

    public final void setDiagnostico(String value) {
        diagnostico.set(value);
    }

    public final StringProperty diagnosticoProperty() {
        return diagnostico;
    }

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