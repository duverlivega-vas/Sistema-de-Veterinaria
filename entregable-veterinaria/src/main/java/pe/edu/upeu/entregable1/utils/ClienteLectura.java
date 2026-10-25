package pe.edu.upeu.entregable1.utils;

import pe.edu.upeu.entregable1.implementacion.ClienteImpl;
import pe.edu.upeu.entregable1.interfaces.ICliente;
import pe.edu.upeu.entregable1.modelo.Cliente;

public class ClienteLectura {
    static Lectura leer=new Lectura();
    static ICliente crud =new ClienteImpl();
    public void leerAgregarCliente(){
        Cliente e=new Cliente();
        System.out.println("***REGISTRAR CLIENTE****");

        System.out.println("ID: ");
        e.setIdPersona(leer.entero());

        System.out.println("CODIGO: ");
        e.setCodigo_cliente(leer.entero());

        System.out.println("NOMBRE: ");
        e.setNombres(leer.cadena());

        System.out.println("APELLIDOS: ");
        e.setApellidos(leer.cadena());

        System.out.println("DNI: ");
        e.setDni(leer.cadena());

        System.out.println("TELEFONO: ");
        e.setTelefono(leer.entero());

        System.out.println("DIRECCION: ");
        e.setDireccion(leer.cadena());

        crud.agregar(e);
    }
    public void leerListarCliente(){
        crud.listar();
    }
    public void leerEliminarCliente(){
        System.out.println("ingrese ID a eliminar: ");
        int id=leer.entero();
        crud.eliminar(id);
    }
    public void leerEditarCliente(){
        Cliente e=new Cliente();
        System.out.println("***EDITAR UN CLIENTE***");
        System.out.println("Ingrese Id a editar: ");
        int id= leer.entero();
        e=crud.buscarPorId(id);
        if(e!=null){
            System.out.println("CODIGO: ");
            e.setCodigo_cliente(leer.entero());

            System.out.println("NOMBRE: ");
            e.setNombres(leer.cadena());

            System.out.println("APELLIDOS: ");
            e.setApellidos(leer.cadena());

            System.out.println("DNI: ");
            e.setDni(leer.cadena());

            System.out.println("TELEFONO: ");
            e.setTelefono(leer.entero());

            System.out.println("DIRECCION: ");
            e.setDireccion(leer.cadena());

            crud.editar(id,e);
        }else{
            System.out.println("El ID no existe");
        }
    }
}
