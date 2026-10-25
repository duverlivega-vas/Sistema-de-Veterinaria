package pe.edu.upeu.entregable1.modelo;

public class Tratamiento {
    private int idTratamiento;
    private Consulta consulta;
    private String descripcion;
    private String indicaciones;
    private int duracionDias;

    public Tratamiento() {
    }

    public Tratamiento(int idTratamiento, Consulta consulta, String descripcion, String indicaciones, int duracionDias) {
        this.idTratamiento = idTratamiento;
        this.consulta = consulta;
        this.descripcion = descripcion;
        this.indicaciones = indicaciones;
        this.duracionDias = duracionDias;
    }

    public int getIdTratamiento() {

        return idTratamiento;
    }

    public void setIdTratamiento(int idTratamiento) {
        this.idTratamiento = idTratamiento;
    }

    public Consulta getConsulta() {
        return consulta;
    }

    public void setConsulta(Consulta consulta) {
        this.consulta = consulta;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getIndicaciones() {
        return indicaciones;
    }

    public void setIndicaciones(String indicaciones) {
        this.indicaciones = indicaciones;
    }

    public int getDuracionDias() {
        return duracionDias;
    }

    public void setDuracionDias(int duracionDias) {
        this.duracionDias = duracionDias;
    }
}
