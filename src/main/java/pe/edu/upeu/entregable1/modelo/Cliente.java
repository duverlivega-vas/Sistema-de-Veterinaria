package pe.edu.upeu.entregable1.modelo;

public class Cliente extends Persona {
    private String direccion;

    public Cliente() {
    }

    public Cliente(int idPersona, String nombres, String apellidos, String dni,
                   int telefono, String direccion) {
        super(idPersona, nombres, apellidos, dni, telefono);
        this.direccion = direccion;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    @Override
    public void mostrarInfo(Persona p) {
        System.out.printf("ID: %d | %s %s | DNI: %s | Telefono: %d%n",
                p.getIdPersona(), p.getNombres(), p.getApellidos(), p.getDni(), p.getTelefono());
    }
}
