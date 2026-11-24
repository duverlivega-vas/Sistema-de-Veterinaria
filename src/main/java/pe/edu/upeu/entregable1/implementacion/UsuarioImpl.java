package pe.edu.upeu.entregable1.implementacion;

import pe.edu.upeu.entregable1.interfaces.IUsuario;
import pe.edu.upeu.entregable1.modelo.*;
import pe.edu.upeu.entregable1.utils.ConexionBD;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UsuarioImpl implements IUsuario {

    @Override
    public boolean insert(Usuario u) {
        boolean flag = false;
        String query = "INSERT INTO usuario(user, password, tipoUser, nombres, apellidos, dni) VALUES(?,?,?,?,?,?)";
        try (Connection cn = ConexionBD.getConnection();
             PreparedStatement st = cn.prepareStatement(query)) {
            st.setString(1, u.getUsuario());
            st.setString(2, u.getPassword());
            st.setString(3, u.getTipoUser());
            st.setString(4, u.getNombres());
            st.setString(5, u.getApellidos());
            st.setString(6, u.getDni());
            flag = st.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error al insertar usuario: " + e.getMessage());
        }
        return flag;
    }

    @Override
    public boolean update(Usuario u) {
        boolean flag = false;
        String query = "UPDATE usuario SET user=?, password=?, tipoUser=?, nombres=?, apellidos=?, dni=? WHERE idusuario=?";
        try (Connection cn = ConexionBD.getConnection();
             PreparedStatement st = cn.prepareStatement(query)) {
            st.setString(1, u.getUsuario());
            st.setString(2, u.getPassword());
            st.setString(3, u.getTipoUser());
            st.setString(4, u.getNombres());
            st.setString(5, u.getApellidos());
            st.setString(6, u.getDni());
            st.setInt(7, u.getIdusuario());
            flag = st.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error al actualizar usuario: " + e.getMessage());
        }
        return flag;
    }

    @Override
    public boolean delete(Usuario u) {
        boolean flag = false;
        String query = "DELETE FROM usuario WHERE idusuario=?";
        try (Connection cn = ConexionBD.getConnection();
             PreparedStatement st = cn.prepareStatement(query)) {
            st.setInt(1, u.getIdusuario());
            flag = st.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new IllegalStateException("No se pudo completar la eliminación: " + e.getMessage(), e);
        }
        return flag;
    }

    @Override
    public Usuario searchById(int id) {
        Usuario u = null;
        String query = "SELECT * FROM usuario WHERE idusuario=?";
        try (Connection cn = ConexionBD.getConnection();
             PreparedStatement st = cn.prepareStatement(query)) {
            st.setInt(1, id);
            try (ResultSet rs = st.executeQuery()) {
                if (rs.next()) {
                    u = new Usuario();
                    u.setIdusuario(rs.getInt("idusuario"));
                    u.setUsuario(rs.getString("user"));
                    u.setPassword(rs.getString("password"));
                    u.setTipoUser(rs.getString("tipoUser"));
                    u.setNombres(rs.getString("nombres"));
                    u.setApellidos(rs.getString("apellidos"));
                    u.setDni(rs.getString("dni"));
                }
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Error al buscar usuario: " + e.getMessage(), e);
        }
        return u;
    }

    @Override
    public Usuario searchByDni(String dni) {
        Usuario u = null;
        String query = "SELECT * FROM usuario WHERE dni=?";
        try (Connection cn = ConexionBD.getConnection();
             PreparedStatement st = cn.prepareStatement(query)) {
            st.setString(1, dni);
            try (ResultSet rs = st.executeQuery()) {
                if (rs.next()) {
                    u = new Usuario();
                    u.setIdusuario(rs.getInt("idusuario"));
                    u.setUsuario(rs.getString("user"));
                    u.setPassword(rs.getString("password"));
                    u.setTipoUser(rs.getString("tipoUser"));
                    u.setNombres(rs.getString("nombres"));
                    u.setApellidos(rs.getString("apellidos"));
                    u.setDni(rs.getString("dni"));
                }
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Error al buscar usuario: " + e.getMessage(), e);
        }
        return u;
    }

    @Override
    public List<Usuario> listAll() {
        List<Usuario> lista = new ArrayList<>();
        String query = "SELECT * FROM usuario";
        try (Connection cn = ConexionBD.getConnection();
             PreparedStatement st = cn.prepareStatement(query);
             ResultSet rs = st.executeQuery()) {
            while (rs.next()) {
                Usuario u = new Usuario();
                u.setIdusuario(rs.getInt("idusuario"));
                u.setUsuario(rs.getString("user"));
                u.setPassword(rs.getString("password"));
                u.setTipoUser(rs.getString("tipoUser"));
                u.setNombres(rs.getString("nombres"));
                u.setApellidos(rs.getString("apellidos"));
                u.setDni(rs.getString("dni"));
                lista.add(u);
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Error al listar usuario: " + e.getMessage(), e);
        }
        return lista;
    }
    @Override
    public Usuario searchByUsuario(String usuario) {
        Usuario u = null;
        String query = "SELECT * FROM usuario WHERE user=?";
        try (Connection cn = ConexionBD.getConnection();
             PreparedStatement st = cn.prepareStatement(query)) {
            st.setString(1, usuario);
            try (ResultSet rs = st.executeQuery()) {
                if (rs.next()) {
                    u = new Usuario();
                    u.setIdusuario(rs.getInt("idusuario"));
                    u.setUsuario(rs.getString("user"));
                    u.setPassword(rs.getString("password"));
                    u.setTipoUser(rs.getString("tipoUser"));
                    u.setNombres(rs.getString("nombres"));
                    u.setApellidos(rs.getString("apellidos"));
                    u.setDni(rs.getString("dni"));
                }
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Error al buscar usuario: " + e.getMessage(), e);
        }
        return u;
    }

}
