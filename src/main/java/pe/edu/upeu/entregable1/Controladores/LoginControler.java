package pe.edu.upeu.entregable1.Controladores;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import pe.edu.upeu.entregable1.implementacion.UsuarioImpl;
import pe.edu.upeu.entregable1.modelo.Usuario;
import pe.edu.upeu.entregable1.utils.Alerts;

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

        Usuario usuarioEncontrado;

        try {
            usuarioEncontrado = new UsuarioImpl().searchByUsuario(usuario);
        } catch (RuntimeException e) {
            mostrarMensaje("Error de conexión con la base de datos", "ERROR");
            return;
        }

        if (usuarioEncontrado == null
                || !password.equals(usuarioEncontrado.getPassword())) {
            mostrarMensaje("Usuario o contraseña incorrectos", "WARNING");
            txtPassword.clear();
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(
                    "/pe/edu/upeu/entregable1/VIews/main-view.fxml"));

            Parent root = loader.load();

            MainControler controller = loader.getController();
            controller.setUsuario(usuario);

            Stage stage = (Stage) txtUsuario.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Sistema de Veterinaria - " + usuario);
            stage.show();

        } catch (IOException e) {
            mostrarMensaje("No se pudo abrir la pantalla principal", "ERROR");
        }
    }

    private void mostrarMensaje(String mensaje, String tipo) {
        lblMensaje.setText(mensaje);

        Alert alerta = Alerts.viewAlerts(
                "Sistema de Veterinaria",
                mensaje,
                tipo
        );

        if (lblMensaje.getScene() != null
                && lblMensaje.getScene().getWindow() != null) {
            alerta.initOwner(lblMensaje.getScene().getWindow());
        }

        alerta.getButtonTypes().setAll(ButtonType.OK);
        alerta.showAndWait();
    }
}