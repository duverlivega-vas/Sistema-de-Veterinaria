package pe.edu.upeu.entregable1.utils;



import pe.edu.upeu.entregable1.implementacion.ClienteImpl;

import pe.edu.upeu.entregable1.interfaces.ICliente;

import pe.edu.upeu.entregable1.modelo.Cliente;



public class ClienteLectura {

    static Lectura leer=new Lectura();

    static ICliente crud =new ClienteImpl();

    public void leerAgregarCliente(){

        Cliente e=new Cliente();

        System.out.println("====REGISTRAR CLIENTE====");



        System.out.println("ID: ");

        e.setIdPersona(leer.id());



        System.out.println("NOMBRE: ");

        e.setNombres(leer.cadena());



        System.out.println("APELLIDOS: ");

        e.setApellidos(leer.cadena());



        System.out.println("DNI: ");

        e.setDni(leer.cadena());



        System.out.println("TELEFONO: ");

        e.setTelefono(String.valueOf(leer.celular()));



        System.out.println("DIRECCION: ");

        e.setDireccion(leer.cadena());



        crud.agregar(e);

    }

    public void leerListarCliente(){

        crud.listar() ;
    }

    public void leerEliminarCliente(){

        System.out.println("ingrese ID a eliminar: ");

        int id=leer.id();

        crud.eliminar(id);

    }

    public void leerEditarCliente(){

        Cliente e=new Cliente();

        System.out.println("***EDITAR UN CLIENTE***");

        System.out.println("Ingresa el ID del cliente: ");

        int id= leer.id();

        Cliente actual=crud.buscarPorId(id);

        if(actual!=null){
            System.out.println("Cliente encontrado.");



            System.out.println("Nuevo nombre: ");

            e.setNombres(leer.cadena());



            System.out.println("Nuevos apellidos: ");

            e.setApellidos(leer.cadena());



            System.out.println("Nuevo DNI: ");

            e.setDni(leer.cadena());



            System.out.println("Nuevo telefono: ");

            e.setTelefono(String.valueOf(leer.celular()));



            System.out.println("Nueva direccion: ");

            e.setDireccion(leer.cadena());



            crud.editar(id,e);

        }else{

            System.out.println("El ID no existe");

        }

    }

}

