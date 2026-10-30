package pe.edu.upeu.entregable1.Controladores;

import pe.edu.upeu.entregable1.utils.Alerts;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.collections.FXCollections;
import javafx.stage.Stage;
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
            cmbCita.setItems(FXCollections.observableArrayList(new CitaImpl().listAll()));
        } catch (Exception e) {
            mostrarMensaje("No se pudieron cargar las opciones: " + e.getMessage(), "ERROR");
            btnGuardar.setDisable(true);
        }
    }

    public void setConsulta(Consulta c) {
        this.c = c;
        if (c == null) return;
        lblTitulo.setText("Editar consulta");
        for (Cita opcion : cmbCita.getItems()) {
            if (opcion.getIdCita() == c.getCita().getIdCita()) {
                cmbCita.setValue(opcion);
                break;
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
