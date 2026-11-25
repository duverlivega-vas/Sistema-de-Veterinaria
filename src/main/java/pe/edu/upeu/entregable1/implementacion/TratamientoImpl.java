package pe.edu.upeu.entregable1.implementacion;

import pe.edu.upeu.entregable1.interfaces.ITratamiento;
import pe.edu.upeu.entregable1.modelo.*;
import pe.edu.upeu.entregable1.utils.ConexionBD;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class TratamientoImpl implements ITratamiento {

    private static final String CONSULTA_BASE =
            "SELECT t.idtratamiento, t.consultaId, t.descripcion, t.indicaciones, t.duracionDias, " +
            "co.diagnostico, co.observaciones, " +
            "ci.idcita, ci.fecha, ci.hora, ci.motivo, ci.estado AS estadoCita, " +
            "m.idmascota, m.nombre AS nombreMascota, m.especie, m.raza, m.estado AS estadoMascota, " +
            "cl.idcliente, cl.nombres AS nombresCliente, cl.apellidos AS apellidosCliente, " +
            "cl.dni AS dniCliente, cl.telefono AS telefonoCliente, cl.direccion, cl.estado AS estadoCliente, " +
            "v.idveterinario, v.nombres AS nombresVeterinario, v.apellidos AS apellidosVeterinario, " +
            "v.dni AS dniVeterinario, v.telefono AS telefonoVeterinario, " +
            "v.especialidad, v.estado AS estadoVeterinario " +
            "FROM tratamiento t " +
            "JOIN consulta co ON t.consultaId = co.idconsulta " +
            "JOIN cita ci ON co.citaId = ci.idcita " +
            "JOIN mascota m ON ci.mascotaId = m.idmascota " +
            "JOIN cliente cl ON m.clienteId = cl.idcliente " +
            "JOIN veterinario v ON ci.veterinarioId = v.idveterinario ";

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
        String query = CONSULTA_BASE + "WHERE t.idtratamiento=?";
        try (Connection cn = ConexionBD.getConnection();
             PreparedStatement st = cn.prepareStatement(query)) {
            st.setInt(1, id);
            try (ResultSet rs = st.executeQuery()) {
                if (rs.next()) {
                    return crearTratamiento(rs);
                }
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Error al buscar tratamiento: " + e.getMessage(), e);
        }
        return null;
    }

    @Override
    public List<Tratamiento> searchByNombreMascota(String nombre) {
        List<Tratamiento> lista = new ArrayList<>();
        String query = CONSULTA_BASE + "WHERE LOWER(m.nombre) LIKE LOWER(?) ORDER BY ci.fecha DESC, ci.hora DESC, t.idtratamiento DESC";
        try (Connection cn = ConexionBD.getConnection();
             PreparedStatement st = cn.prepareStatement(query)) {
            st.setString(1, "%" + nombre.trim() + "%");
            try (ResultSet rs = st.executeQuery()) {
                while (rs.next()) {
                    lista.add(crearTratamiento(rs));
                }
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Error al buscar tratamientos por mascota: " + e.getMessage(), e);
        }
        return lista;
    }

    @Override
    public List<Tratamiento> listAll() {
        List<Tratamiento> lista = new ArrayList<>();
        String query = CONSULTA_BASE + "ORDER BY ci.fecha DESC, ci.hora DESC, t.idtratamiento DESC";
        try (Connection cn = ConexionBD.getConnection();
             PreparedStatement st = cn.prepareStatement(query);
             ResultSet rs = st.executeQuery()) {
            while (rs.next()) {
                lista.add(crearTratamiento(rs));
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Error al listar tratamientos: " + e.getMessage(), e);
        }
        return lista;
    }

    private Tratamiento crearTratamiento(ResultSet rs) throws SQLException {
        Tratamiento tratamiento = new Tratamiento();
        tratamiento.setIdTratamiento(rs.getInt("idtratamiento"));
        tratamiento.setDescripcion(rs.getString("descripcion"));
        tratamiento.setIndicaciones(rs.getString("indicaciones"));
        tratamiento.setDuracionDias(rs.getInt("duracionDias"));

        Cliente cliente = new Cliente();
        cliente.setIdcliente(rs.getInt("idcliente"));
        cliente.setNombres(rs.getString("nombresCliente"));
        cliente.setApellidos(rs.getString("apellidosCliente"));
        cliente.setDni(rs.getString("dniCliente"));
        cliente.setTelefono(rs.getString("telefonoCliente"));
        cliente.setDireccion(rs.getString("direccion"));
        cliente.setEstado(rs.getString("estadoCliente"));

        Mascota mascota = new Mascota();
        mascota.setIdMascota(rs.getInt("idmascota"));
        mascota.setNombre(rs.getString("nombreMascota"));
        mascota.setEspecie(rs.getString("especie"));
        mascota.setRaza(rs.getString("raza"));
        mascota.setEstado(rs.getString("estadoMascota"));
        mascota.setCliente(cliente);

        Veterinario veterinario = new Veterinario();
        veterinario.setIdveterinario(rs.getInt("idveterinario"));
        veterinario.setNombres(rs.getString("nombresVeterinario"));
        veterinario.setApellidos(rs.getString("apellidosVeterinario"));
        veterinario.setDni(rs.getString("dniVeterinario"));
        veterinario.setTelefono(rs.getString("telefonoVeterinario"));
        veterinario.setEspecialidad(rs.getString("especialidad"));
        veterinario.setEstado(rs.getString("estadoVeterinario"));

        Cita cita = new Cita();
        cita.setIdCita(rs.getInt("idcita"));
        String fecha = rs.getString("fecha");
        if (fecha != null && !fecha.isBlank()) {
            cita.setFecha(LocalDate.parse(fecha));
        }
        cita.setHora(rs.getString("hora"));
        cita.setMotivo(rs.getString("motivo"));
        cita.setEstado(rs.getString("estadoCita"));
        cita.setMascota(mascota);
        cita.setVeterinario(veterinario);

        Consulta consulta = new Consulta();
        consulta.setIdConsulta(rs.getInt("consultaId"));
        consulta.setDiagnostico(rs.getString("diagnostico"));
        consulta.setObservaciones(rs.getString("observaciones"));
        consulta.setCita(cita);

        tratamiento.setConsulta(consulta);
        return tratamiento;
    }
}
