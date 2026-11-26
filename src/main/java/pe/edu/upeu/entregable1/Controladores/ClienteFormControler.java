package pe.edu.upeu.entregable1.Controladores;

import pe.edu.upeu.entregable1.utils.Alerts;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import pe.edu.upeu.entregable1.modelo.*;

public class ClienteFormControler {
    @FXML private Label lblTitulo, lblMensaje;
    @FXML private Button btnGuardar;
    private Cliente c;
    private boolean saved = false;
    @FXML private TextField txtNombres;
    @FXML private TextField txtApellidos;
    @FXML private TextField txtDni;
    @FXML private TextField txtTelefono;
    @FXML private TextField txtDireccion;
    @FXML private ComboBox<String> cmbEstado;

    @FXML
    public void initialize() {
        cmbEstado.setValue("Activo");
        txtTelefono.setTextFormatter(new TextFormatter<String>(change ->
                change.getControlNewText().matches("[0-9]{0,9}") ? change : null));
    }

    public void setCliente(Cliente c) {
        this.c = c;
        if (c == null) return;
        lblTitulo.setText("Editar cliente");
        txtNombres.setText(c.getNombres());
        txtApellidos.setText(c.getApellidos());
        txtDni.setText(c.getDni());
        txtTelefono.setText(c.getTelefono());
        txtDireccion.setText(c.getDireccion());
        cmbEstado.setValue(c.getEstado());
    }

    public boolean isSaved() { return saved; }
    public Cliente getCliente() { return c; }

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
        String telefono = txtTelefono.getText().trim();
        if (!telefono.isEmpty() && !telefono.matches("[0-9]{9}")) {
            mostrarMensaje("El teléfono debe contener exactamente 9 números", "WARNING");
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
            if (c == null) c = new Cliente();
            c.setNombres(txtNombres.getText().trim());
            c.setApellidos(txtApellidos.getText().trim());
            c.setDni(txtDni.getText().trim());
            c.setTelefono(txtTelefono.getText().trim());
            c.setDireccion(txtDireccion.getText().trim());
            c.setEstado(cmbEstado.getValue());
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
