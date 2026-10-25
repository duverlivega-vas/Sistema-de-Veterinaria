package pe.edu.upeu.entregable1.implementacion;


import pe.edu.upeu.entregable1.interfaces.ICliente;

import pe.edu.upeu.entregable1.modelo.Cliente;
import pe.edu.upeu.entregable1.modelo.Mascota;


import java.util.ArrayList;


public class ClienteImpl implements ICliente {

    static ArrayList<Cliente> ArrayCliente = new ArrayList<>();

    @Override

    public void agregar(Cliente e) {

        if (e == null || e.getIdPersona() <= 0) {
            System.out.println("El ID debe ser mayor que cero.");
            return;
        }
        for (Cliente registrado : ArrayCliente) {
            if (registrado.getIdPersona() == e.getIdPersona()) {
                System.out.println("Ya existe un registro con ese ID.");
                return;
            }
        }
        ArrayCliente.add(e);

        System.out.println("<<Register Success>>");


    }


    @Override

    public void listar() {
        if (ArrayCliente.isEmpty()) {
            System.out.println("No hay registros");
        } else {
            System.out.println("\nLISTA DE CLIENTES");
            System.out.println("");
            System.out.printf("%-6s %-18s %-18s %-12s %-12s %-25s%n",
                    "ID", "NOMBRES", "APELLIDOS", "DNI", "TELEFONO", "DIRECCION");
            System.out.println("------------------------------------------------------------------------------------------------");
            for (Cliente es : ArrayCliente) {
                System.out.printf("%-6s %-18s %-18s %-12s %-12s %-25s%n",
                        es.getIdPersona(), es.getNombres(), es.getApellidos(), es.getDni(), es.getTelefono(), es.getDireccion());
            }
        }
    }


    @Override

    public void eliminar(int id) {

        Cliente e = buscarPorId(id);

        if (e != null) {

            for (Mascota mascota : MascotaImpl.arrayM) {
                if (mascota.getCliente() != null && mascota.getCliente().getIdPersona() == id) {
                    System.out.println("No se puede eliminar: el cliente tiene mascotas registradas.");
                    return;
                }
            }
            ArrayCliente.remove(e);

            System.out.println("<<Cliente Eliminado>>");

        } else {

            System.out.println("No existe el cliente con este ID");

        }

    }


    @Override

    public void editar(int id, Cliente e) {

        Cliente est = buscarPorId(id);
        if (est == null) {
            System.out.println("El ID no existe");
            return;
        }

        est.setNombres(e.getNombres());

        est.setApellidos(e.getApellidos());

        est.setDni(e.getDni());
        est.setTelefono(e.getTelefono());

        est.setDireccion(e.getDireccion());

        est.setIdPersona(id);

        System.out.println("<<Cliente editado success");

    }


    @Override

    public Cliente buscarPorId(int id) {

        for (Cliente e : ArrayCliente) {

            if (e.getIdPersona() == id) {

                return e;

            }

        }

        return null;

    }

}

