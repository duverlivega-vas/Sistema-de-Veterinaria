package pe.edu.upeu.entregable1.implementacion;

import pe.edu.upeu.entregable1.interfaces.IMascota;
import pe.edu.upeu.entregable1.modelo.*;
import pe.edu.upeu.entregable1.utils.ConexionBD;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MascotaImpl implements IMascota {

    @Override
    public boolean insert(Mascota m) {
        boolean flag = false;
        String query = "INSERT INTO mascota(nombre, especie, raza, clienteId, estado) VALUES(?, ?, ?, ?, ?)";
        try (Connection cn = ConexionBD.getConnection();
             PreparedStatement st = cn.prepareStatement(query)) {
            st.setString(1, m.getNombre());
            st.setString(2, m.getEspecie());
            st.setString(3, m.getRaza());
            st.setInt(4, m.getCliente().getIdcliente());
            st.setString(5, m.getEstado());
            flag = st.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error al insertar mascota: " + e.getMessage());
        }
        return flag;
    }

    @Override
    public boolean update(Mascota m) {
        boolean flag = false;
        String query = "UPDATE mascota SET nombre=?, especie=?, raza=?, clienteId=?, estado=? WHERE idmascota=?";
        try (Connection cn = ConexionBD.getConnection();
             PreparedStatement st = cn.prepareStatement(query)) {
            st.setString(1, m.getNombre());
            st.setString(2, m.getEspecie());
            st.setString(3, m.getRaza());
            st.setInt(4, m.getCliente().getIdcliente());
            st.setString(5, m.getEstado());
            st.setInt(6, m.getIdMascota());
            flag = st.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error al actualizar mascota: " + e.getMessage());
        }
        return flag;
    }

    @Override
    public boolean delete(Mascota m) {
        boolean flag = false;
        String query = "DELETE FROM mascota WHERE idmascota=?";
        try (Connection cn = ConexionBD.getConnection();
             PreparedStatement st = cn.prepareStatement(query)) {
            st.setInt(1, m.getIdMascota());
            flag = st.executeUpdate() > 0;
        } catch (SQLException e) {
            if (e.getMessage() != null && e.getMessage().contains("FOREIGN KEY constraint failed")) {
                throw new IllegalStateException("No se puede eliminar esta mascota porque tiene citas registradas. Puede marcarla como Inactiva desde Editar para conservar su historial.", e);
            }
            throw new IllegalStateException("No se pudo completar la eliminación: " + e.getMessage(), e);
        }
        return flag;
    }

    @Override
    public Mascota searchById(int id) {
        Mascota m = null;
        String query = "SELECT m.*, c.nombres, c.apellidos, c.dni FROM mascota m, cliente c WHERE m.clienteId=c.idcliente AND m.idmascota=?";
        try (Connection cn = ConexionBD.getConnection();
             PreparedStatement st = cn.prepareStatement(query)) {
            st.setInt(1, id);
            try (ResultSet rs = st.executeQuery()) {
                if (rs.next()) {
                    m = new Mascota();
                    m.setIdMascota(rs.getInt("idmascota"));
                    m.setNombre(rs.getString("nombre"));
                    m.setEspecie(rs.getString("especie"));
                    m.setRaza(rs.getString("raza"));
                    m.setEstado(rs.getString("estado"));
                    Cliente cl = new Cliente();
                    cl.setIdcliente(rs.getInt("clienteId"));
                    cl.setNombres(rs.getString("nombres"));
                    cl.setApellidos(rs.getString("apellidos"));
                    cl.setDni(rs.getString("dni"));
                    m.setCliente(cl);
                }
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Error al buscar mascota: " + e.getMessage(), e);
        }
        return m;
    }

    @Override
    public List<Mascota> searchByDni(String dni) {
        List<Mascota> lista = new ArrayList<>();
        String query = "SELECT m.*, c.nombres, c.apellidos, c.dni FROM mascota m, cliente c WHERE m.clienteId=c.idcliente AND c.dni=?";
        try (Connection cn = ConexionBD.getConnection();
             PreparedStatement st = cn.prepareStatement(query)) {
            st.setString(1, dni);
            try (ResultSet rs = st.executeQuery()) {
                while (rs.next()) {
                    Mascota m = new Mascota();
                    m.setIdMascota(rs.getInt("idmascota"));
                    m.setNombre(rs.getString("nombre"));
                    m.setEspecie(rs.getString("especie"));
                    m.setRaza(rs.getString("raza"));
                    m.setEstado(rs.getString("estado"));
                    Cliente cl = new Cliente();
                    cl.setIdcliente(rs.getInt("clienteId"));
                    cl.setNombres(rs.getString("nombres"));
                    cl.setApellidos(rs.getString("apellidos"));
                    cl.setDni(rs.getString("dni"));
                    m.setCliente(cl);
                    lista.add(m);
                }
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Error al buscar mascotas por DNI: " + e.getMessage(), e);
        }
        return lista;
    }

    @Override
    public List<Mascota> listAll() {
        List<Mascota> lista = new ArrayList<>();
        String query = "SELECT m.*, c.nombres, c.apellidos, c.dni FROM mascota m, cliente c WHERE m.clienteId=c.idcliente";
        try (Connection cn = ConexionBD.getConnection();
             PreparedStatement st = cn.prepareStatement(query);
             ResultSet rs = st.executeQuery()) {
            while (rs.next()) {
                Mascota m = new Mascota();
                m.setIdMascota(rs.getInt("idmascota"));
                m.setNombre(rs.getString("nombre"));
                m.setEspecie(rs.getString("especie"));
                m.setRaza(rs.getString("raza"));
                m.setEstado(rs.getString("estado"));
                Cliente cl = new Cliente();
                cl.setIdcliente(rs.getInt("clienteId"));
                cl.setNombres(rs.getString("nombres"));
                cl.setApellidos(rs.getString("apellidos"));
                cl.setDni(rs.getString("dni"));
                m.setCliente(cl);
                lista.add(m);
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Error al listar mascota: " + e.getMessage(), e);
        }
        return lista;
    }
}
