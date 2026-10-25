package pe.edu.upeu.entregable1.modelo;



public class Veterinario extends Persona{

    private String especialidad;



    public Veterinario() {

    }



    public Veterinario(int idPersona, String nombres, String apellidos, String dni, int telefono, String especialidad) {

        super(idPersona, nombres, apellidos, dni, telefono);

        this.especialidad = especialidad;

    }



    public String getEspecialidad() {

        return especialidad;

    }



    public void setEspecialidad(String especialidad) {

        this.especialidad = especialidad;

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

