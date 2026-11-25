package pe.edu.upeu.entregable1.implementacion;

import pe.edu.upeu.entregable1.interfaces.IVeterinario;
import pe.edu.upeu.entregable1.modelo.*;
import pe.edu.upeu.entregable1.utils.ConexionBD;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class VeterinarioImpl implements IVeterinario {

    @Override
    public boolean insert(Veterinario v) {
        boolean flag = false;
        String query = "INSERT INTO veterinario(nombres, apellidos, dni, telefono, especialidad, estado) VALUES(?, ?, ?, ?, ?, ?)";
        try (Connection cn = ConexionBD.getConnection();
             PreparedStatement st = cn.prepareStatement(query)) {
            st.setString(1, v.getNombres());
            st.setString(2, v.getApellidos());
            st.setString(3, v.getDni());
            st.setString(4, v.getTelefono());
            st.setString(5, v.getEspecialidad());
            st.setString(6, v.getEstado());
            flag = st.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error al insertar veterinario: " + e.getMessage());
        }
        return flag;
    }

    @Override
    public boolean update(Veterinario v) {
        boolean flag = false;
        String query = "UPDATE veterinario SET nombres=?, apellidos=?, dni=?, telefono=?, especialidad=?, estado=? WHERE idveterinario=?";
        try (Connection cn = ConexionBD.getConnection();
             PreparedStatement st = cn.prepareStatement(query)) {
            st.setString(1, v.getNombres());
            st.setString(2, v.getApellidos());
            st.setString(3, v.getDni());
            st.setString(4, v.getTelefono());
            st.setString(5, v.getEspecialidad());
            st.setString(6, v.getEstado());
            st.setInt(7, v.getIdveterinario());
            flag = st.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error al actualizar veterinario: " + e.getMessage());
        }
        return flag;
    }

    @Override
    public boolean delete(Veterinario v) {
        boolean flag = false;
        String query = "DELETE FROM veterinario WHERE idveterinario=?";
        try (Connection cn = ConexionBD.getConnection();
             PreparedStatement st = cn.prepareStatement(query)) {
            st.setInt(1, v.getIdveterinario());
            flag = st.executeUpdate() > 0;
        } catch (SQLException e) {
            if (e.getMessage() != null && e.getMessage().contains("FOREIGN KEY constraint failed")) {
                throw new IllegalStateException("No se puede eliminar este veterinario porque tiene citas registradas. Puede marcarlo como Inactivo desde Editar para conservar su historial.", e);
            }
            throw new IllegalStateException("No se pudo completar la eliminación: " + e.getMessage(), e);
        }
        return flag;
    }

    @Override
    public Veterinario searchById(int id) {
        Veterinario v = null;
        String query = "SELECT * FROM veterinario WHERE idveterinario=?";
        try (Connection cn = ConexionBD.getConnection();
             PreparedStatement st = cn.prepareStatement(query)) {
            st.setInt(1, id);
            try (ResultSet rs = st.executeQuery()) {
                if (rs.next()) {
                    v = new Veterinario();
                    v.setIdveterinario(rs.getInt("idveterinario"));
                    v.setNombres(rs.getString("nombres"));
                    v.setApellidos(rs.getString("apellidos"));
                    v.setDni(rs.getString("dni"));
                    v.setTelefono(rs.getString("telefono"));
                    v.setEspecialidad(rs.getString("especialidad"));
                    v.setEstado(rs.getString("estado"));
                }
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Error al buscar veterinario: " + e.getMessage(), e);
        }
        return v;
    }

    @Override
    public Veterinario searchByDni(String dni) {
        Veterinario v = null;
        String query = "SELECT * FROM veterinario WHERE dni=?";
        try (Connection cn = ConexionBD.getConnection();
             PreparedStatement st = cn.prepareStatement(query)) {
            st.setString(1, dni);
            try (ResultSet rs = st.executeQuery()) {
                if (rs.next()) {
                    v = new Veterinario();
                    v.setIdveterinario(rs.getInt("idveterinario"));
                    v.setNombres(rs.getString("nombres"));
                    v.setApellidos(rs.getString("apellidos"));
                    v.setDni(rs.getString("dni"));
                    v.setTelefono(rs.getString("telefono"));
                    v.setEspecialidad(rs.getString("especialidad"));
                    v.setEstado(rs.getString("estado"));
                }
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Error al buscar veterinario: " + e.getMessage(), e);
        }
        return v;
    }

    @Override
    public List<Veterinario> listAll() {
        List<Veterinario> lista = new ArrayList<>();
        String query = "SELECT * FROM veterinario";
        try (Connection cn = ConexionBD.getConnection();
             PreparedStatement st = cn.prepareStatement(query);
             ResultSet rs = st.executeQuery()) {
            while (rs.next()) {
                Veterinario v = new Veterinario();
                v.setIdveterinario(rs.getInt("idveterinario"));
                v.setNombres(rs.getString("nombres"));
                v.setApellidos(rs.getString("apellidos"));
                v.setDni(rs.getString("dni"));
                v.setTelefono(rs.getString("telefono"));
                v.setEspecialidad(rs.getString("especialidad"));
                v.setEstado(rs.getString("estado"));
                lista.add(v);
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Error al listar veterinario: " + e.getMessage(), e);
        }
        return lista;
    }
}
