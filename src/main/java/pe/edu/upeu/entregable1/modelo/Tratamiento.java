package pe.edu.upeu.entregable1.modelo;

import javafx.beans.property.*;

public class Tratamiento {

    private final IntegerProperty idTratamiento = new SimpleIntegerProperty();
    private final ObjectProperty<Consulta> consulta = new SimpleObjectProperty<>();
    private final StringProperty descripcion = new SimpleStringProperty();
    private final StringProperty indicaciones = new SimpleStringProperty();
    private final IntegerProperty duracionDias = new SimpleIntegerProperty();

    public Tratamiento() {
    }

    public Tratamiento(int idTratamiento, Consulta consulta, String descripcion, String indicaciones, int duracionDias) {
        setIdTratamiento(idTratamiento);
        setConsulta(consulta);
        setDescripcion(descripcion);
        setIndicaciones(indicaciones);
        setDuracionDias(duracionDias);
    }

    public final int getIdTratamiento() {
        return idTratamiento.get();
    }

    public final void setIdTratamiento(int value) {
        idTratamiento.set(value);
    }

    public final IntegerProperty idTratamientoProperty() {
        return idTratamiento;
    }

    public final Consulta getConsulta() {
        return consulta.get();
    }

    public final void setConsulta(Consulta value) {
        consulta.set(value);
    }

    public final ObjectProperty<Consulta> consultaProperty() {
        return consulta;
    }

    public final String getDescripcion() {
        return descripcion.get();
    }

    public final void setDescripcion(String value) {
        descripcion.set(value);
    }

    public final StringProperty descripcionProperty() {
        return descripcion;
    }

    public final String getIndicaciones() {
        return indicaciones.get();
    }

    public final void setIndicaciones(String value) {
        indicaciones.set(value);
    }

    public final StringProperty indicacionesProperty() {
        return indicaciones;
    }

    public final int getDuracionDias() {
        return duracionDias.get();
    }

    public final void setDuracionDias(int value) {
        duracionDias.set(value);
    }

    public final IntegerProperty duracionDiasProperty() {
        return duracionDias;
    }
}