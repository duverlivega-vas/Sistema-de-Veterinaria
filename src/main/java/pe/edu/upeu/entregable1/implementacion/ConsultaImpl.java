package pe.edu.upeu.entregable1.implementacion;

import pe.edu.upeu.entregable1.interfaces.IConsulta;
import pe.edu.upeu.entregable1.modelo.*;
import pe.edu.upeu.entregable1.utils.ConexionBD;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.time.LocalDate;

public class ConsultaImpl implements IConsulta {

    @Override
    public boolean insert(Consulta c) {
        boolean flag = false;
        String query = "INSERT INTO consulta(citaId, diagnostico, observaciones) VALUES(?, ?, ?)";
        try (Connection cn = ConexionBD.getConnection();
             PreparedStatement st = cn.prepareStatement(query)) {
            st.setInt(1, c.getCita().getIdCita());
            st.setString(2, c.getDiagnostico());
            st.setString(3, c.getObservaciones());
            flag = st.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error al insertar consulta: " + e.getMessage());
        }
        return flag;
    }

    @Override
    public boolean update(Consulta c) {
        boolean flag = false;
        String query = "UPDATE consulta SET citaId=?, diagnostico=?, observaciones=? WHERE idconsulta=?";
        try (Connection cn = ConexionBD.getConnection();
             PreparedStatement st = cn.prepareStatement(query)) {
            st.setInt(1, c.getCita().getIdCita());
            st.setString(2, c.getDiagnostico());
            st.setString(3, c.getObservaciones());
            st.setInt(4, c.getIdConsulta());
            flag = st.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error al actualizar consulta: " + e.getMessage());
        }
        return flag;
    }

    @Override
    public boolean delete(Consulta c) {
        boolean flag = false;
        String query = "DELETE FROM consulta WHERE idconsulta=?";
        try (Connection cn = ConexionBD.getConnection();
             PreparedStatement st = cn.prepareStatement(query)) {
            st.setInt(1, c.getIdConsulta());
            flag = st.executeUpdate() > 0;
        } catch (SQLException e) {
            if (e.getMessage() != null && e.getMessage().contains("FOREIGN KEY constraint failed")) {
                throw new IllegalStateException("No se puede eliminar esta consulta porque tiene tratamientos registrados. Revise los tratamientos asociados.", e);
            }
            throw new IllegalStateException("No se pudo completar la eliminación: " + e.getMessage(), e);
        }
        return flag;
    }

    @Override
    public Consulta searchById(int id) {
        Consulta c = null;
        String query = "SELECT co.*, ci.mascotaId, ci.fecha, ci.hora, m.nombre FROM consulta " +
                "co, cita ci, mascota m WHERE co.citaId=ci.idcita " +
                "AND ci.mascotaId=m.idmascota AND co.idconsulta=?";
        try (Connection cn = ConexionBD.getConnection();
             PreparedStatement st = cn.prepareStatement(query)) {
            st.setInt(1, id);
            try (ResultSet rs = st.executeQuery()) {
                if (rs.next()) {
                    c = new Consulta();
                    c.setIdConsulta(rs.getInt("idconsulta"));
                    c.setDiagnostico(rs.getString("diagnostico"));
                    c.setObservaciones(rs.getString("observaciones"));
                    Mascota m = new Mascota();
                    m.setIdMascota(rs.getInt("mascotaId"));
                    m.setNombre(rs.getString("nombre"));
                    Cita ci = new Cita();
                    ci.setIdCita(rs.getInt("citaId"));
                    ci.setFecha(LocalDate.parse(rs.getString("fecha")));
                    ci.setHora(rs.getString("hora"));
                    ci.setMascota(m);
                    c.setCita(ci);
                }
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Error al buscar consulta: " + e.getMessage(), e);
        }
        return c;
    }

    @Override
    public List<Consulta> listAll() {
        List<Consulta> lista = new ArrayList<>();
        String query = "SELECT co.*, ci.mascotaId, ci.fecha, ci.hora, m.nombre FROM consulta co, cita ci, mascota m WHERE co.citaId=ci.idcita AND ci.mascotaId=m.idmascota";
        try (Connection cn = ConexionBD.getConnection();
             PreparedStatement st = cn.prepareStatement(query);
             ResultSet rs = st.executeQuery()) {
            while (rs.next()) {
                Consulta c = new Consulta();
                c.setIdConsulta(rs.getInt("idconsulta"));
                c.setDiagnostico(rs.getString("diagnostico"));
                c.setObservaciones(rs.getString("observaciones"));
                Mascota m = new Mascota();
                m.setIdMascota(rs.getInt("mascotaId"));
                m.setNombre(rs.getString("nombre"));
                Cita ci = new Cita();
                ci.setIdCita(rs.getInt("citaId"));
                ci.setFecha(LocalDate.parse(rs.getString("fecha")));
                ci.setHora(rs.getString("hora"));
                ci.setMascota(m);
                c.setCita(ci);
                lista.add(c);
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Error al listar consulta: " + e.getMessage(), e);
        }
        return lista;
    }
}
