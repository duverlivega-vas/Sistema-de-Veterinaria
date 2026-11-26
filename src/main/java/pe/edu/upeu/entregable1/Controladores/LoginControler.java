package pe.edu.upeu.entregable1.Controladores;

import pe.edu.upeu.entregable1.utils.Alerts;
import javafx.fxml.FXML;
import pe.edu.upeu.entregable1.modelo.Usuario;
import pe.edu.upeu.entregable1.implementacion.UsuarioImpl;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class LoginControler {
    @FXML private TextField txtUsuario;
    @FXML private PasswordField txtPassword;
    @FXML private Label lblMensaje;

    @FXML
    private void handleLogin() {
        String usuario = txtUsuario.getText().trim();
        String password = txtPassword.getText();

        if (usuario.isBlank() || password.isBlank()) {
            mostrarMensaje("Ingrese usuario y contraseña", "WARNING");
            return;
        }

        Usuario u;
        try {
            u = new UsuarioImpl().searchByUsuario(usuario);
        } catch (RuntimeException e) {
            mostrarMensaje("Error de conexión con la base de datos", "ERROR");
            return;
        }

        if (u == null || !password.equals(u.getPassword())) {
            mostrarMensaje("Usuario o contraseña incorrectos", "WARNING");
            txtPassword.clear();
            return;
        }

        String tipoUsuario = u.getTipoUser() == null ? "" : u.getTipoUser().trim();
        if (!"Administrador".equalsIgnoreCase(tipoUsuario)
                && !"Recepcionista".equalsIgnoreCase(tipoUsuario)) {
            mostrarMensaje("El usuario no tiene un tipo asignado válido. Contacte al administrador.", "ERROR");
            return;
        }


        try {
            Stage stage = (Stage) txtUsuario.getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(getClass().getResource(
                    "/pe/edu/upeu/entregable1/VIews/main-view.fxml"));
            Parent root = loader.load();
            MainControler controller = loader.getController();
            controller.setUsuario(u);
            stage.getScene().setRoot(root);
            stage.setResizable(true);
            stage.setMaximized(true);
            stage.setTitle("Sistema de Veterinaria - " + usuario);
        } catch (Exception e) {
            mostrarMensaje("No se pudo abrir el sistema: " + e.getMessage(), "ERROR");
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
