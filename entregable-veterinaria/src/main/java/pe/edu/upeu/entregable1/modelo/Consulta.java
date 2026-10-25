package pe.edu.upeu.entregable1.modelo;

public class Consulta {
    private int idConsulta;
    private Cita cita;
    private String diagnostico;
    private String observaciones;

    public Consulta() {
    }

    public Consulta(int idConsulta, Cita cita, String diagnostico, String observaciones) {
        this.idConsulta = idConsulta;
        this.cita = cita;
        this.diagnostico = diagnostico;
        this.observaciones = observaciones;
    }

    public int getIdConsulta() {
        return idConsulta;
    }

    public void setIdConsulta(int idConsulta) {
        this.idConsulta = idConsulta;
    }

    public Cita getCita() {
        return cita;
    }

    public void setCita(Cita cita) {
        this.cita = cita;
    }

    public String getDiagnostico() {
        return diagnostico;
    }

    public void setDiagnostico(String diagnostico) {
        this.diagnostico = diagnostico;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }
}
