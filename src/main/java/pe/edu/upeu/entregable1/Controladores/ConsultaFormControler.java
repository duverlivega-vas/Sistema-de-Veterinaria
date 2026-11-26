package pe.edu.upeu.entregable1.Controladores;

import pe.edu.upeu.entregable1.utils.Alerts;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.collections.FXCollections;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import java.util.HashSet;
import java.util.Set;
import pe.edu.upeu.entregable1.modelo.*;
import pe.edu.upeu.entregable1.implementacion.*;

public class ConsultaFormControler {
    @FXML private Label lblTitulo, lblMensaje;
    @FXML private Button btnGuardar;
    private Consulta c;
    private boolean saved = false;
    @FXML private ComboBox<Cita> cmbCita;
    @FXML private TextArea txtDiagnostico;
    @FXML private TextArea txtObservaciones;

    @FXML
    public void initialize() {
        try {
            Set<Integer> citasConConsulta = new HashSet<>();
            for (Consulta consulta : new ConsultaImpl().listAll()) {
                if (consulta.getCita() != null) citasConConsulta.add(consulta.getCita().getIdCita());
            }
            var citasDisponibles = new CitaImpl().listAll().stream()
                    .filter(cita -> "Atendida".equalsIgnoreCase(cita.getEstado()))
                    .filter(cita -> !citasConConsulta.contains(cita.getIdCita()))
                    .toList();
            cmbCita.setItems(FXCollections.observableArrayList(citasDisponibles));
            cmbCita.setConverter(new StringConverter<>() {
                @Override
                public String toString(Cita cita) {
                    if (cita == null) return "";
                    String mascota = cita.getMascota() == null ? "Mascota" : cita.getMascota().getNombre();
                    String veterinario = cita.getVeterinario() == null ? "" :
                            " — Dr. " + cita.getVeterinario().getNombres() + " " + cita.getVeterinario().getApellidos();
                    return cita.getFecha() + " " + cita.getHora() + " — " + mascota + veterinario;
                }

                @Override
                public Cita fromString(String texto) {
                    return null;
                }
            });
            if (citasDisponibles.isEmpty()) {
                lblMensaje.setText("No hay citas atendidas disponibles para registrar una consulta.");
                btnGuardar.setDisable(true);
            }
        } catch (Exception e) {
            mostrarMensaje("No se pudieron cargar las opciones: " + e.getMessage(), "ERROR");
            btnGuardar.setDisable(true);
        }
    }

    public void setConsulta(Consulta c) {
        this.c = c;
        if (c == null) return;
        lblTitulo.setText("Editar consulta");
        if (c.getCita() != null) {
            Cita citaActual = new CitaImpl().searchById(c.getCita().getIdCita());
            if (citaActual != null) {
                boolean disponible = cmbCita.getItems().stream()
                        .anyMatch(opcion -> opcion.getIdCita() == citaActual.getIdCita());
                if (!disponible) cmbCita.getItems().add(citaActual);
                cmbCita.setValue(citaActual);
                btnGuardar.setDisable(false);
                lblMensaje.setText("");
            }
        }
        txtDiagnostico.setText(c.getDiagnostico());
        txtObservaciones.setText(c.getObservaciones());
    }

    public boolean isSaved() { return saved; }
    public Consulta getConsulta() { return c; }

    private void cerrar() {
        Stage stage = (Stage) btnGuardar.getScene().getWindow();
        stage.close();
    }

    private boolean validated() {
        if (cmbCita.getValue() == null ||
                txtDiagnostico.getText().isBlank()) {
            mostrarMensaje("Complete los campos obligatorios y seleccione las opciones", "WARNING");
            return false;
        }
        if (c == null && !"Atendida".equalsIgnoreCase(cmbCita.getValue().getEstado())) {
            mostrarMensaje("Solo se puede registrar una consulta para una cita atendida", "WARNING");
            return false;
        }
        return true;
    }

    @FXML
    private void handleCancel() {
        cerrar();
    }

    @FXML
    private void handleSaved() {
        if (validated()) {
            if (c == null) c = new Consulta();
            c.setCita(cmbCita.getValue());
            c.setDiagnostico(txtDiagnostico.getText().trim());
            c.setObservaciones(txtObservaciones.getText().trim());
            saved = true;
            cerrar();
        }
    }

    private void mostrarMensaje(String mensaje, String tipo) {
        lblMensaje.setText(mensaje);
        Alert alerta = Alerts.viewAlerts("Sistema de Veterinaria", mensaje, tipo);
        if (lblMensaje.getScene() != null && lblMensaje.getScene().getWindow() != null) {
            alerta.initOwner(lblMensaje.getScene().getWindow());
        }
        alerta.getButtonTypes().setAll(ButtonType.OK);
        alerta.showAndWait();
    }
}
