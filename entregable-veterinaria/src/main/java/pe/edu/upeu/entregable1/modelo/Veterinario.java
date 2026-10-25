package pe.edu.upeu.entregable1.modelo;

public class Veterinario extends Persona{
    private String especialidad;
    private String numeroColegiatura;

    public Veterinario() {
    }

    public Veterinario(int idPersona, String nombres, String apellidos, String dni, int telefono, String especialidad, String numeroColegiatura) {
        super(idPersona, nombres, apellidos, dni, telefono);
        this.especialidad = especialidad;
        this.numeroColegiatura = numeroColegiatura;
    }

    public String getEspecialidad() {
        return especialidad;
    }

    public void setEspecialidad(String especialidad) {
        this.especialidad = especialidad;
    }

    public String getNumeroColegiatura() {
        return numeroColegiatura;
    }

    public void setNumeroColegiatura(String numeroColegiatura) {
        this.numeroColegiatura = numeroColegiatura;
    }

    @Override
    public void mostrarInfo(Persona p) {
        super.mostrarInfo(p);
        System.out.println("\nID VETERINARIO: "+p.getIdPersona()
                +"\nNOMBRES: "+p.getNombres()
                +"\nAPELLIDOS: "+p.getApellidos()
                +"\nDNI: "+p.getDni()
                +"\nTELEFONO: "+p.getTelefono());
    }
}
