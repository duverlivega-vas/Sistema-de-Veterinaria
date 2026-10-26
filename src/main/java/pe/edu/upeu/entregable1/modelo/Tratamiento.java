package pe.edu.upeu.entregable1.modelo;

// CAMBIO: imports de las properties de JavaFX
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Tratamiento {

    // CAMBIO: atributos simples -> properties de JavaFX
    private final IntegerProperty idTratamiento = new SimpleIntegerProperty(this, "idTratamiento");
    private final ObjectProperty<Consulta> consulta = new SimpleObjectProperty<>(this, "consulta");
    private final StringProperty descripcion = new SimpleStringProperty(this, "descripcion");
    private final StringProperty indicaciones = new SimpleStringProperty(this, "indicaciones");
    private final IntegerProperty duracionDias = new SimpleIntegerProperty(this, "duracionDias");

    public Tratamiento() {
    }

    public Tratamiento(int idTratamiento, Consulta consulta, String descripcion, String indicaciones, int duracionDias) {
        setIdTratamiento(idTratamiento);
        setConsulta(consulta);
        setDescripcion(descripcion);
        setIndicaciones(indicaciones);
        setDuracionDias(duracionDias);
    }

    // ---------- idTratamiento ----------
    public final int getIdTratamiento() {
        return idTratamiento.get();
    }

    public final void setIdTratamiento(int value) {
        idTratamiento.set(value);
    }

    public final IntegerProperty idTratamientoProperty() {
        return idTratamiento;
    }

    // ---------- consulta ----------
    public final Consulta getConsulta() {
        return consulta.get();
    }

    public final void setConsulta(Consulta value) {
        consulta.set(value);
    }

    public final ObjectProperty<Consulta> consultaProperty() {
        return consulta;
    }

    // ---------- descripcion ----------
    public final String getDescripcion() {
        return descripcion.get();
    }

    public final void setDescripcion(String value) {
        descripcion.set(value);
    }

    public final StringProperty descripcionProperty() {
        return descripcion;
    }

    // ---------- indicaciones ----------
    public final String getIndicaciones() {
        return indicaciones.get();
    }

    public final void setIndicaciones(String value) {
        indicaciones.set(value);
    }

    public final StringProperty indicacionesProperty() {
        return indicaciones;
    }

    // ---------- duracionDias ----------
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