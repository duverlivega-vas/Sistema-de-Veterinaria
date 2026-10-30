package pe.edu.upeu.entregable1.Controladores;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.stage.Stage;
import javafx.scene.control.Label;
import pe.edu.upeu.entregable1.utils.Alerts;
import javafx.scene.layout.StackPane;

public class MainControler {
    @FXML private Label lblUsuario;
    @FXML private StackPane contenedorVistas;

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
            stage.getScene().setRoot(login);
            stage.setTitle("Sistema de Veterinaria");
        } catch (IOException e) {
            Alerts.viewAlerts("Error", "No se pudo cerrar sesión: " + e.getMessage(), "ERROR").showAndWait();
        }
    }
    public void setUsuario(String usuario) {
        lblUsuario.setText(usuario);
    }
}
