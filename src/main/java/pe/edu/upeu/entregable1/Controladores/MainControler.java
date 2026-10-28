package pe.edu.upeu.entregable1.Controladores;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;

public class MainControler {
    @FXML private StackPane contenedorVistas;
    @FXML private Button btnInicio, btnClientes, btnMascotas, btnVeterinarios;
    @FXML private Button btnCitas, btnConsultas, btnTratamientos, btnUsuarios, btnVacunacion;

    @FXML
    private void initialize() {
        btnInicio.setOnAction(e -> mostrarVista("home-view.fxml"));
        btnClientes.setOnAction(e -> mostrarVista("cliente-vista.fxml"));
        btnMascotas.setOnAction(e -> mostrarVista("mascota-view.fxml"));
        btnVeterinarios.setOnAction(e -> mostrarVista("veterinario-view.fxml"));
        btnCitas.setOnAction(e -> mostrarVista("cita-view.fxml"));
        btnConsultas.setOnAction(e -> mostrarVista("consulta-view.fxml"));
        btnTratamientos.setOnAction(e -> mostrarVista("tratamiento-view.fxml"));
        btnUsuarios.setOnAction(e -> mostrarVista("usuario-view.fxml"));
        btnVacunacion.setOnAction(e -> mostrarVista("vacunacion-view.fxml"));
    }

    private void mostrarVista(String archivo) {
        try {
            Parent vista = FXMLLoader.load(getClass().getResource(
                    "/pe/edu/upeu/entregable1/VIews/" + archivo));
            contenedorVistas.getChildren().setAll(vista);
        } catch (IOException e) {
            new Alert(Alert.AlertType.ERROR, "No se pudo abrir " + archivo + ": " + e.getMessage()).showAndWait();
        }
    }
}
