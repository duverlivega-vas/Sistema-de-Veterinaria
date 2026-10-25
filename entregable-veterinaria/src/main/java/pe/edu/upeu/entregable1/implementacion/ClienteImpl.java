package pe.edu.upeu.entregable1.implementacion;


import pe.edu.upeu.entregable1.interfaces.ICliente;

import pe.edu.upeu.entregable1.modelo.Cliente;


import java.util.ArrayList;


public class ClienteImpl implements ICliente {

    static ArrayList<Cliente> ArrayCliente = new ArrayList<>();

    @Override

    public void agregar(Cliente e) {

        ArrayCliente.add(e);

        System.out.println("<<Register Success>>");


    }


    @Override

    public void listar() {

        if (ArrayCliente.isEmpty()) {

            System.out.println("No hay registros");

        } else {

            int i = 1;

            for (Cliente es : ArrayCliente) {

                System.out.println(i + "-");

                //llamada al metodo poliformico

                es.mostrarInfo(es);

                System.out.println("\nCODIGO: " + es.getCodigo_cliente()

                        + "\nDIRECCION:" + es.getDireccion());

            }

        }

    }


    @Override

    public void eliminar(int id) {

        Cliente e = buscarPorId(id);

        if (e != null) {

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

        est.setCodigo_cliente(e.getCodigo_cliente());

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

