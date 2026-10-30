package pe.edu.upeu.entregable1.Controladores;

import pe.edu.upeu.entregable1.utils.Alerts;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.collections.FXCollections;
import javafx.stage.Stage;
import pe.edu.upeu.entregable1.modelo.*;
import pe.edu.upeu.entregable1.implementacion.*;

public class TratamientoFormControler {
    @FXML private Label lblTitulo, lblMensaje;
    @FXML private Button btnGuardar;
    private Tratamiento t;
    private boolean saved = false;
    @FXML private ComboBox<Consulta> cmbConsulta;
    @FXML private TextArea txtDescripcion;
    @FXML private TextArea txtIndicaciones;
    @FXML private TextField txtDuracionDias;

    @FXML
    public void initialize() {
        try {
            cmbConsulta.setItems(FXCollections.observableArrayList(new ConsultaImpl().listAll()));
        } catch (Exception e) {
            mostrarMensaje("No se pudieron cargar las opciones: " + e.getMessage(), "ERROR");
            btnGuardar.setDisable(true);
        }
    }

    public void setTratamiento(Tratamiento t) {
        this.t = t;
        if (t == null) return;
        lblTitulo.setText("Editar tratamiento");
        for (Consulta opcion : cmbConsulta.getItems()) {
            if (opcion.getIdConsulta() == t.getConsulta().getIdConsulta()) {
                cmbConsulta.setValue(opcion);
                break;
            }
        }
        txtDescripcion.setText(t.getDescripcion());
        txtIndicaciones.setText(t.getIndicaciones());
        txtDuracionDias.setText(String.valueOf(t.getDuracionDias()));
    }

    public boolean isSaved() { return saved; }
    public Tratamiento getTratamiento() { return t; }

    private void cerrar() {
        Stage stage = (Stage) btnGuardar.getScene().getWindow();
        stage.close();
    }

    private boolean validated() {
        if (cmbConsulta.getValue() == null ||
                txtDescripcion.getText().isBlank() ||
                txtDuracionDias.getText().isBlank()) {
            mostrarMensaje("Complete los campos obligatorios y seleccione las opciones", "WARNING");
            return false;
        }
        int dias;
        try {
            dias = Integer.parseInt(txtDuracionDias.getText().trim());
            if (dias <= 0) throw new NumberFormatException();
        } catch (NumberFormatException e) {
            mostrarMensaje("La duración debe ser un número entero mayor que cero", "WARNING");
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
            if (t == null) t = new Tratamiento();
            t.setConsulta(cmbConsulta.getValue());
            t.setDescripcion(txtDescripcion.getText().trim());
            t.setIndicaciones(txtIndicaciones.getText().trim());
            t.setDuracionDias(Integer.parseInt(txtDuracionDias.getText().trim()));
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
