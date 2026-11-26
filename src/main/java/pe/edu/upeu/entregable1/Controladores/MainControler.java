package pe.edu.upeu.entregable1.Controladores;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.stage.Stage;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import pe.edu.upeu.entregable1.utils.Alerts;
import javafx.scene.layout.StackPane;
import pe.edu.upeu.entregable1.modelo.Usuario;

public class MainControler {
    @FXML private Label lblUsuario;
    @FXML private StackPane contenedorVistas;
    @FXML private Button btnVeterinarios;
    @FXML private Button btnConsultas;
    @FXML private Button btnTratamientos;
    @FXML private Button btnUsuarios;

    private Usuario usuarioActual;

    @FXML
    private void showHome() {
        mostrarVista("home-view.fxml");
    }

    @FXML
    private void showCliente() {
        mostrarVista("cliente-vista.fxml");
    }

    @FXML
    private void showMascota() {
        mostrarVista("mascota-view.fxml");
    }

    @FXML
    private void showVeterinario() {
        mostrarVista("veterinario-view.fxml");
    }

    @FXML
    private void showCita() {
        mostrarVista("cita-view.fxml");
    }

    @FXML
    private void showConsulta() {
        mostrarVista("consulta-view.fxml");
    }

    @FXML
    private void showTratamiento() {
        mostrarVista("tratamiento-view.fxml");
    }

    @FXML
    private void showUsuario() {
        mostrarVista("usuario-view.fxml");
    }

    private void mostrarVista(String archivo) {
        if (!tieneAcceso(archivo)) {
            Alerts.viewAlerts("Acceso restringido",
                    "Tu tipo de usuario no tiene permiso para abrir esta sección.", "WARNING").showAndWait();
            return;
        }
        try {
            Parent vista = FXMLLoader.load(getClass().getResource("/pe/edu/upeu/entregable1/VIews/" + archivo));
            contenedorVistas.getChildren().setAll(vista);
        } catch (IOException e) {
            Alerts.viewAlerts("Error", "No se pudo abrir " + archivo + ": " + e.getMessage(), "ERROR").showAndWait();
        }
    }
    @FXML
    private void cerrarSesion() {
        try {
            Stage stage = (Stage) lblUsuario.getScene().getWindow();
            Parent login = FXMLLoader.load(getClass().getResource("/pe/edu/upeu/entregable1/VIews/login-view.fxml"));
            stage.setMaximized(false);
            stage.setResizable(false);
            stage.getScene().setRoot(login);
            stage.setTitle("Sistema de Veterinaria");
            stage.setWidth(820);
            stage.setHeight(600);
            stage.centerOnScreen();
        } catch (IOException e) {
            Alerts.viewAlerts("Error", "No se pudo cerrar sesión: " + e.getMessage(), "ERROR").showAndWait();
        }
    }
    public void setUsuario(Usuario usuario) {
        this.usuarioActual = usuario;
        if (usuario == null) {
            lblUsuario.setText("Sin usuario conectado");
            actualizarPermisos(false);
            return;
        }

        String tipo = usuario.getTipoUser() == null ? "" : usuario.getTipoUser().trim();
        lblUsuario.setText(usuario.getUsuario() + " | " + tipo);
        actualizarPermisos(esAdministrador());
    }

    private boolean esAdministrador() {
        return usuarioActual != null && usuarioActual.getTipoUser() != null
                && "Administrador".equalsIgnoreCase(usuarioActual.getTipoUser().trim());
    }

    private void actualizarPermisos(boolean administrador) {
        configurarAcceso(btnVeterinarios, administrador);
        configurarAcceso(btnConsultas, administrador);
        configurarAcceso(btnTratamientos, administrador);
        configurarAcceso(btnUsuarios, administrador);
    }

    private void configurarAcceso(Button boton, boolean permitido) {
        if (boton != null) {
            boton.setVisible(permitido);
            boton.setManaged(permitido);
        }
    }

    private boolean tieneAcceso(String archivo) {
        if (esAdministrador()) return true;
        if (usuarioActual == null || usuarioActual.getTipoUser() == null
                || !"Recepcionista".equalsIgnoreCase(usuarioActual.getTipoUser().trim())) {
            return false;
        }

        return "home-view.fxml".equals(archivo)
                || "cliente-vista.fxml".equals(archivo)
                || "mascota-view.fxml".equals(archivo)
                || "cita-view.fxml".equals(archivo);
    }
}
