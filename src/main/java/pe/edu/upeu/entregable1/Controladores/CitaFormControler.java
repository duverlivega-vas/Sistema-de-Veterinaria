package pe.edu.upeu.entregable1.Controladores;

import pe.edu.upeu.entregable1.utils.Alerts;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.collections.FXCollections;
import javafx.stage.Stage;
import java.time.LocalDate;
import pe.edu.upeu.entregable1.modelo.*;
import pe.edu.upeu.entregable1.implementacion.*;

public class CitaFormControler {
    @FXML private Label lblTitulo, lblMensaje;
    @FXML private Button btnGuardar;
    private Cita c;
    private boolean saved = false;
    @FXML private ComboBox<Mascota> cmbMascota;
    @FXML private ComboBox<Veterinario> cmbVeterinario;
    @FXML private DatePicker dpFecha;
    @FXML private TextField txtHora;
    @FXML private TextArea txtMotivo;
    @FXML private ComboBox<String> cmbEstado;

    @FXML
    public void initialize() {
        try {
            cmbMascota.setItems(FXCollections.observableArrayList(new MascotaImpl().listAll()));
            cmbVeterinario.setItems(FXCollections.observableArrayList(new VeterinarioImpl().listAll()));
            dpFecha.setValue(LocalDate.now());
            cmbEstado.setValue("Pendiente");
        } catch (Exception e) {
            mostrarMensaje("No se pudieron cargar las opciones: " + e.getMessage(), "ERROR");
            btnGuardar.setDisable(true);
        }
    }

    public void setCita(Cita c) {
        this.c = c;
        if (c == null) return;
        lblTitulo.setText("Editar cita");
        for (Mascota opcion : cmbMascota.getItems()) {
            if (opcion.getIdMascota() == c.getMascota().getIdMascota()) {
                cmbMascota.setValue(opcion);
                break;
            }
        }
        for (Veterinario opcion : cmbVeterinario.getItems()) {
            if (opcion.getIdveterinario() == c.getVeterinario().getIdveterinario()) {
                cmbVeterinario.setValue(opcion);
                break;
            }
        }
        dpFecha.setValue(c.getFecha());
        txtHora.setText(c.getHora());
        txtMotivo.setText(c.getMotivo());
        cmbEstado.setValue(c.getEstado());
    }

    public boolean isSaved() { return saved; }
    public Cita getCita() { return c; }

    private void cerrar() {
        Stage stage = (Stage) btnGuardar.getScene().getWindow();
        stage.close();
    }

    private boolean validated() {
        try {
            dpFecha.setValue(dpFecha.getConverter().fromString(dpFecha.getEditor().getText()));
        } catch (Exception e) {
            mostrarMensaje("Ingrese una fecha válida usando el calendario", "WARNING");
            return false;
        }
        if (cmbMascota.getValue() == null ||
                cmbVeterinario.getValue() == null ||
                dpFecha.getValue() == null ||
                txtHora.getText().isBlank() ||
                cmbEstado.getValue() == null) {
            mostrarMensaje("Complete los campos obligatorios y seleccione las opciones", "WARNING");
            return false;
        }
        if (!txtHora.getText().trim().matches("([01][0-9]|2[0-3]):[0-5][0-9]")) {
            mostrarMensaje("Ingrese la hora en formato HH:mm, por ejemplo 14:30", "WARNING");
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
            if (c == null) c = new Cita();
            c.setMascota(cmbMascota.getValue());
            c.setVeterinario(cmbVeterinario.getValue());
            c.setFecha(dpFecha.getValue());
            c.setHora(txtHora.getText().trim());
            c.setMotivo(txtMotivo.getText().trim());
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
