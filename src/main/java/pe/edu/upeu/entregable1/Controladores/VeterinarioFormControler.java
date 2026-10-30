package pe.edu.upeu.entregable1.Controladores;

import pe.edu.upeu.entregable1.utils.Alerts;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import pe.edu.upeu.entregable1.modelo.*;

public class VeterinarioFormControler {
    @FXML private Label lblTitulo, lblMensaje;
    @FXML private Button btnGuardar;
    private Veterinario v;
    private boolean saved = false;
    @FXML private TextField txtNombres;
    @FXML private TextField txtApellidos;
    @FXML private TextField txtDni;
    @FXML private TextField txtTelefono;
    @FXML private TextField txtEspecialidad;
    @FXML private ComboBox<String> cmbEstado;

    @FXML
    public void initialize() {
        cmbEstado.setValue("Activo");
    }

    public void setVeterinario(Veterinario v) {
        this.v = v;
        if (v == null) return;
        lblTitulo.setText("Editar veterinario");
        txtNombres.setText(v.getNombres());
        txtApellidos.setText(v.getApellidos());
        txtDni.setText(v.getDni());
        txtTelefono.setText(v.getTelefono());
        txtEspecialidad.setText(v.getEspecialidad());
        cmbEstado.setValue(v.getEstado());
    }

    public boolean isSaved() { return saved; }
    public Veterinario getVeterinario() { return v; }

    private void cerrar() {
        Stage stage = (Stage) btnGuardar.getScene().getWindow();
        stage.close();
    }

    private boolean validated() {
        if (txtNombres.getText().isBlank() ||
                txtApellidos.getText().isBlank() ||
                txtDni.getText().isBlank() ||
                cmbEstado.getValue() == null) {
            mostrarMensaje("Complete los campos obligatorios y seleccione las opciones", "WARNING");
            return false;
        }
        if (!txtDni.getText().trim().matches("[0-9]{8}")) {
            mostrarMensaje("El DNI debe tener 8 dígitos", "WARNING");
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
            if (v == null) v = new Veterinario();
            v.setNombres(txtNombres.getText().trim());
            v.setApellidos(txtApellidos.getText().trim());
            v.setDni(txtDni.getText().trim());
            v.setTelefono(txtTelefono.getText().trim());
            v.setEspecialidad(txtEspecialidad.getText().trim());
            v.setEstado(cmbEstado.getValue());
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
