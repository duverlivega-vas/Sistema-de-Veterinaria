package pe.edu.upeu.entregable1.implementacion;

import pe.edu.upeu.entregable1.interfaces.ICita;
import pe.edu.upeu.entregable1.modelo.*;
import pe.edu.upeu.entregable1.utils.ConexionBD;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.time.LocalDate;

public class CitaImpl implements ICita {

    @Override
    public boolean insert(Cita c) {
        boolean flag = false;
        String query = "INSERT INTO cita(mascotaId, veterinarioId, fecha, hora, motivo, estado) VALUES(?, ?, ?, ?, ?, ?)";
        try (Connection cn = ConexionBD.getConnection();
             PreparedStatement st = cn.prepareStatement(query)) {
            st.setInt(1, c.getMascota().getIdMascota());
            st.setInt(2, c.getVeterinario().getIdveterinario());
            st.setString(3, c.getFecha().toString());
            st.setString(4, c.getHora());
            st.setString(5, c.getMotivo());
            st.setString(6, c.getEstado());
            flag = st.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error al insertar cita: " + e.getMessage());
        }
        return flag;
    }

    @Override
    public boolean update(Cita c) {
        boolean flag = false;
        String query = "UPDATE cita SET mascotaId=?, veterinarioId=?, fecha=?, hora=?, motivo=?, estado=? WHERE idcita=?";
        try (Connection cn = ConexionBD.getConnection();
             PreparedStatement st = cn.prepareStatement(query)) {
            st.setInt(1, c.getMascota().getIdMascota());
            st.setInt(2, c.getVeterinario().getIdveterinario());
            st.setString(3, c.getFecha().toString());
            st.setString(4, c.getHora());
            st.setString(5, c.getMotivo());
            st.setString(6, c.getEstado());
            st.setInt(7, c.getIdCita());
            flag = st.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error al actualizar cita: " + e.getMessage());
        }
        return flag;
    }

    @Override
    public boolean delete(Cita c) {
        boolean flag = false;
        String query = "DELETE FROM cita WHERE idcita=?";
        try (Connection cn = ConexionBD.getConnection();
             PreparedStatement st = cn.prepareStatement(query)) {
            st.setInt(1, c.getIdCita());
            flag = st.executeUpdate() > 0;
        } catch (SQLException e) {
            if (e.getMessage() != null && e.getMessage().contains("FOREIGN KEY constraint failed")) {
                throw new IllegalStateException("No se puede eliminar esta cita porque tiene una consulta registrada. Revise la consulta asociada.", e);
            }
            throw new IllegalStateException("No se pudo completar la eliminación: " + e.getMessage(), e);
        }
        return flag;
    }

    @Override
    public boolean updateStatus(Cita c) {
        String query = "UPDATE cita SET estado=? WHERE idcita=?";
        try (Connection cn = ConexionBD.getConnection();
             PreparedStatement st = cn.prepareStatement(query)) {
            st.setString(1, c.getEstado());
            st.setInt(2, c.getIdCita());
            return st.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new IllegalStateException("No se pudo actualizar el estado de la cita: " + e.getMessage(), e);
        }
    }

    @Override
    public boolean verificarConflictoHorario(Cita c) {
        String query = "SELECT COUNT(*) FROM cita " +
                "WHERE fecha=? AND hora=? AND estado IN ('Pendiente','En atención') " +
                "AND (veterinarioId=? OR mascotaId=?) AND idcita<>?";
        try (Connection cn = ConexionBD.getConnection();
             PreparedStatement st = cn.prepareStatement(query)) {
            st.setString(1, c.getFecha().toString());
            st.setString(2, c.getHora());
            st.setInt(3, c.getVeterinario().getIdveterinario());
            st.setInt(4, c.getMascota().getIdMascota());
            st.setInt(5, c.getIdCita());
            try (ResultSet rs = st.executeQuery()) {
                return rs.next() && rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            throw new IllegalStateException("No se pudo verificar el horario: " + e.getMessage(), e);
        }
    }

    @Override
    public Cita searchById(int id) {
        Cita c = null;
        String query = "SELECT c.*, m.nombre, v.nombres, v.apellidos FROM cita c, mascota m, veterinario v WHERE c.mascotaId=m.idmascota AND c.veterinarioId=v.idveterinario AND c.idcita=?";
        try (Connection cn = ConexionBD.getConnection();
             PreparedStatement st = cn.prepareStatement(query)) {
            st.setInt(1, id);
            try (ResultSet rs = st.executeQuery()) {
                if (rs.next()) {
                    c = new Cita();
                    c.setIdCita(rs.getInt("idcita"));
                    c.setFecha(LocalDate.parse(rs.getString("fecha")));
                    c.setHora(rs.getString("hora"));
                    c.setMotivo(rs.getString("motivo"));
                    c.setEstado(rs.getString("estado"));
                    Mascota m = new Mascota();
                    m.setIdMascota(rs.getInt("mascotaId"));
                    m.setNombre(rs.getString("nombre"));
                    Veterinario v = new Veterinario();
                    v.setIdveterinario(rs.getInt("veterinarioId"));
                    v.setNombres(rs.getString("nombres"));
                    v.setApellidos(rs.getString("apellidos"));
                    c.setMascota(m);
                    c.setVeterinario(v);
                }
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Error al buscar cita: " + e.getMessage(), e);
        }
        return c;
    }

    @Override
    public List<Cita> listAll() {
        List<Cita> lista = new ArrayList<>();
        String query = "SELECT c.*, m.nombre, v.nombres, v.apellidos FROM cita c, mascota m, veterinario v WHERE c.mascotaId=m.idmascota AND c.veterinarioId=v.idveterinario";
        try (Connection cn = ConexionBD.getConnection();
             PreparedStatement st = cn.prepareStatement(query);
             ResultSet rs = st.executeQuery()) {
            while (rs.next()) {
                Cita c = new Cita();
                c.setIdCita(rs.getInt("idcita"));
                c.setFecha(LocalDate.parse(rs.getString("fecha")));
                c.setHora(rs.getString("hora"));
                c.setMotivo(rs.getString("motivo"));
                c.setEstado(rs.getString("estado"));
                Mascota m = new Mascota();
                m.setIdMascota(rs.getInt("mascotaId"));
                m.setNombre(rs.getString("nombre"));
                Veterinario v = new Veterinario();
                v.setIdveterinario(rs.getInt("veterinarioId"));
                v.setNombres(rs.getString("nombres"));
                v.setApellidos(rs.getString("apellidos"));
                c.setMascota(m);
                c.setVeterinario(v);
                lista.add(c);
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Error al listar cita: " + e.getMessage(), e);
        }
        return lista;
    }
}
