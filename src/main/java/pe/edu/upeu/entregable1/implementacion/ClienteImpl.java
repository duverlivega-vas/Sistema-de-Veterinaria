package pe.edu.upeu.entregable1.implementacion;

import pe.edu.upeu.entregable1.interfaces.ICliente;
import pe.edu.upeu.entregable1.modelo.*;
import pe.edu.upeu.entregable1.utils.ConexionBD;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ClienteImpl implements ICliente {

    @Override
    public boolean agregar(Cliente c) {
        boolean flag = false;
        String query = "INSERT INTO cliente(nombres, apellidos, dni, telefono, direccion, estado) VALUES(?, ?, ?, ?, ?, ?)";
        try (Connection cn = ConexionBD.getConnection();
             PreparedStatement st = cn.prepareStatement(query)) {
            st.setString(1, c.getNombres());
            st.setString(2, c.getApellidos());
            st.setString(3, c.getDni());
            st.setString(4, c.getTelefono());
            st.setString(5, c.getDireccion());
            st.setString(6, c.getEstado());
            flag = st.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error al insertar cliente: " + e.getMessage());
        }
        return flag;
    }

    @Override
    public boolean update(Cliente c) {
        boolean flag = false;
        String query = "UPDATE cliente SET nombres=?, apellidos=?, dni=?, telefono=?, direccion=?, estado=? WHERE idcliente=?";
        try (Connection cn = ConexionBD.getConnection();
             PreparedStatement st = cn.prepareStatement(query)) {
            st.setString(1, c.getNombres());
            st.setString(2, c.getApellidos());
            st.setString(3, c.getDni());
            st.setString(4, c.getTelefono());
            st.setString(5, c.getDireccion());
            st.setString(6, c.getEstado());
            st.setInt(7, c.getIdcliente());
            flag = st.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error al actualizar cliente: " + e.getMessage());
        }
        return flag;
    }

    @Override
    public boolean delete(Cliente c) {
        boolean flag = false;
        String query = "DELETE FROM cliente WHERE idcliente=?";
        try (Connection cn = ConexionBD.getConnection();
             PreparedStatement st = cn.prepareStatement(query)) {
            st.setInt(1, c.getIdcliente());
            flag = st.executeUpdate() > 0;
        } catch (SQLException e) {
            if (e.getMessage() != null && e.getMessage().contains("FOREIGN KEY constraint failed")) {
                throw new IllegalStateException("No se puede eliminar este cliente porque tiene mascotas registradas. Puede marcarlo como Inactivo desde Editar para conservar su historial.", e);
            }
            throw new IllegalStateException("No se pudo completar la eliminación: " + e.getMessage(), e);
        }
        return flag;
    }

    @Override
    public Cliente searchById(int id) {
        Cliente c = null;
        String query = "SELECT * FROM cliente WHERE idcliente=?";
        try (Connection cn = ConexionBD.getConnection();
             PreparedStatement st = cn.prepareStatement(query)) {
            st.setInt(1, id);
            try (ResultSet rs = st.executeQuery()) {
                if (rs.next()) {
                    c = new Cliente();
                    c.setIdcliente(rs.getInt("idcliente"));
                    c.setNombres(rs.getString("nombres"));
                    c.setApellidos(rs.getString("apellidos"));
                    c.setDni(rs.getString("dni"));
                    c.setTelefono(rs.getString("telefono"));
                    c.setDireccion(rs.getString("direccion"));
                    c.setEstado(rs.getString("estado"));
                }
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Error al buscar cliente: " + e.getMessage(), e);
        }
        return c;
    }

    @Override
    public Cliente searchByDni(String dni) {
        Cliente c = null;
        String query = "SELECT * FROM cliente WHERE dni=?";
        try (Connection cn = ConexionBD.getConnection();
             PreparedStatement st = cn.prepareStatement(query)) {
            st.setString(1, dni);
            try (ResultSet rs = st.executeQuery()) {
                if (rs.next()) {
                    c = new Cliente();
                    c.setIdcliente(rs.getInt("idcliente"));
                    c.setNombres(rs.getString("nombres"));
                    c.setApellidos(rs.getString("apellidos"));
                    c.setDni(rs.getString("dni"));
                    c.setTelefono(rs.getString("telefono"));
                    c.setDireccion(rs.getString("direccion"));
                    c.setEstado(rs.getString("estado"));
                }
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Error al buscar cliente: " + e.getMessage(), e);
        }
        return c;
    }

    @Override
    public List<Cliente> listAll() {
        List<Cliente> lista = new ArrayList<>();
        String query = "SELECT * FROM cliente";
        try (Connection cn = ConexionBD.getConnection();
             PreparedStatement st = cn.prepareStatement(query);
             ResultSet rs = st.executeQuery()) {
            while (rs.next()) {
                Cliente c = new Cliente();
                c.setIdcliente(rs.getInt("idcliente"));
                c.setNombres(rs.getString("nombres"));
                c.setApellidos(rs.getString("apellidos"));
                c.setDni(rs.getString("dni"));
                c.setTelefono(rs.getString("telefono"));
                c.setDireccion(rs.getString("direccion"));
                c.setEstado(rs.getString("estado"));
                lista.add(c);
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Error al listar cliente: " + e.getMessage(), e);
        }
        return lista;
    }
}
