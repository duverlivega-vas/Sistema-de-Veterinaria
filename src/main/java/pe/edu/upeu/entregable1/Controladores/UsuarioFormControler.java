package pe.edu.upeu.entregable1.Controladores;

import pe.edu.upeu.entregable1.utils.Alerts;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import pe.edu.upeu.entregable1.modelo.*;

public class UsuarioFormControler {
    @FXML private Label lblTitulo, lblMensaje;
    @FXML private Button btnGuardar;
    private Usuario u;
    private boolean saved = false;
    @FXML private TextField txtUsuario;
    @FXML private PasswordField txtPassword;
    @FXML private ComboBox<String> cmbTipoUser;
    @FXML private TextField txtNombres;
    @FXML private TextField txtApellidos;
    @FXML private TextField txtDni;

    @FXML
    public void initialize() {
        cmbTipoUser.setValue("Recepcionista");
    }

    public void setUsuario(Usuario u) {
        this.u = u;
        if (u == null) return;
        lblTitulo.setText("Editar usuario");
        txtUsuario.setText(u.getUsuario());
        txtPassword.setText(u.getPassword());
        cmbTipoUser.setValue(u.getTipoUser());
        txtNombres.setText(u.getNombres());
        txtApellidos.setText(u.getApellidos());
        txtDni.setText(u.getDni());
    }

    public boolean isSaved() { return saved; }
    public Usuario getUsuario() { return u; }

    private void cerrar() {
        Stage stage = (Stage) btnGuardar.getScene().getWindow();
        stage.close();
    }

    private boolean validated() {
        if (txtUsuario.getText().isBlank() ||
                txtPassword.getText().isBlank() ||
                cmbTipoUser.getValue() == null ||
                txtNombres.getText().isBlank() ||
                txtApellidos.getText().isBlank() ||
                txtDni.getText().isBlank()) {
            mostrarMensaje("Complete los campos obligatorios y seleccione las opciones", "WARNING");
            return false;
        }
        if (!txtDni.getText().trim().matches("[0-9]{8}")) {
            mostrarMensaje("El DNI debe tener 8 dígitos", "WARNING");
            return false;
        }
        if ("admin".equals(txtUsuario.getText().trim())) {
            mostrarMensaje("admin está reservado para el acceso de demostración. Use otro nombre.", "WARNING");
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
            if (u == null) u = new Usuario();
            u.setUsuario(txtUsuario.getText().trim());
            u.setPassword(txtPassword.getText());
            u.setTipoUser(cmbTipoUser.getValue());
            u.setNombres(txtNombres.getText().trim());
            u.setApellidos(txtApellidos.getText().trim());
            u.setDni(txtDni.getText().trim());
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
