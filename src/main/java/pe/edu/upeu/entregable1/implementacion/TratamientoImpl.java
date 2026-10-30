package pe.edu.upeu.entregable1.implementacion;

import pe.edu.upeu.entregable1.interfaces.ITratamiento;
import pe.edu.upeu.entregable1.modelo.*;
import pe.edu.upeu.entregable1.utils.ConexionBD;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TratamientoImpl implements ITratamiento {

    @Override
    public boolean insert(Tratamiento t) {
        boolean flag = false;
        String query = "INSERT INTO tratamiento(consultaId, descripcion, indicaciones, duracionDias) VALUES(?, ?, ?, ?)";
        try (Connection cn = ConexionBD.getConnection();
             PreparedStatement st = cn.prepareStatement(query)) {
            st.setInt(1, t.getConsulta().getIdConsulta());
            st.setString(2, t.getDescripcion());
            st.setString(3, t.getIndicaciones());
            st.setInt(4, t.getDuracionDias());
            flag = st.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error al insertar tratamiento: " + e.getMessage());
        }
        return flag;
    }

    @Override
    public boolean update(Tratamiento t) {
        boolean flag = false;
        String query = "UPDATE tratamiento SET consultaId=?, descripcion=?, indicaciones=?, duracionDias=? WHERE idtratamiento=?";
        try (Connection cn = ConexionBD.getConnection();
             PreparedStatement st = cn.prepareStatement(query)) {
            st.setInt(1, t.getConsulta().getIdConsulta());
            st.setString(2, t.getDescripcion());
            st.setString(3, t.getIndicaciones());
            st.setInt(4, t.getDuracionDias());
            st.setInt(5, t.getIdTratamiento());
            flag = st.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error al actualizar tratamiento: " + e.getMessage());
        }
        return flag;
    }

    @Override
    public boolean delete(Tratamiento t) {
        boolean flag = false;
        String query = "DELETE FROM tratamiento WHERE idtratamiento=?";
        try (Connection cn = ConexionBD.getConnection();
             PreparedStatement st = cn.prepareStatement(query)) {
            st.setInt(1, t.getIdTratamiento());
            flag = st.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new IllegalStateException("No se pudo completar la eliminación: " + e.getMessage(), e);
        }
        return flag;
    }

    @Override
    public Tratamiento searchById(int id) {
        Tratamiento t = null;
        String query = "SELECT t.*, co.diagnostico FROM tratamiento t, consulta co WHERE t.consultaId=co.idconsulta AND t.idtratamiento=?";
        try (Connection cn = ConexionBD.getConnection();
             PreparedStatement st = cn.prepareStatement(query)) {
            st.setInt(1, id);
            try (ResultSet rs = st.executeQuery()) {
                if (rs.next()) {
                    t = new Tratamiento();
                    t.setIdTratamiento(rs.getInt("idtratamiento"));
                    t.setDescripcion(rs.getString("descripcion"));
                    t.setIndicaciones(rs.getString("indicaciones"));
                    t.setDuracionDias(rs.getInt("duracionDias"));
                    Consulta co = new Consulta();
                    co.setIdConsulta(rs.getInt("consultaId"));
                    co.setDiagnostico(rs.getString("diagnostico"));
                    t.setConsulta(co);
                }
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Error al buscar tratamiento: " + e.getMessage(), e);
        }
        return t;
    }

    @Override
    public List<Tratamiento> listAll() {
        List<Tratamiento> lista = new ArrayList<>();
        String query = "SELECT t.*, co.diagnostico FROM tratamiento t, consulta co WHERE t.consultaId=co.idconsulta";
        try (Connection cn = ConexionBD.getConnection();
             PreparedStatement st = cn.prepareStatement(query);
             ResultSet rs = st.executeQuery()) {
            while (rs.next()) {
                Tratamiento t = new Tratamiento();
                t.setIdTratamiento(rs.getInt("idtratamiento"));
                t.setDescripcion(rs.getString("descripcion"));
                t.setIndicaciones(rs.getString("indicaciones"));
                t.setDuracionDias(rs.getInt("duracionDias"));
                Consulta co = new Consulta();
                co.setIdConsulta(rs.getInt("consultaId"));
                co.setDiagnostico(rs.getString("diagnostico"));
                t.setConsulta(co);
                lista.add(t);
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Error al listar tratamiento: " + e.getMessage(), e);
        }
        return lista;
    }
}
