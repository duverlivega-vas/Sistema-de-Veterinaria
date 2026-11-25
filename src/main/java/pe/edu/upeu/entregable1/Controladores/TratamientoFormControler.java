package pe.edu.upeu.entregable1.Controladores;

import pe.edu.upeu.entregable1.utils.Alerts;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.collections.FXCollections;
import javafx.stage.Stage;
import pe.edu.upeu.entregable1.modelo.*;
import pe.edu.upeu.entregable1.implementacion.*;
import java.time.format.DateTimeFormatter;

public class TratamientoFormControler {
    @FXML private Label lblTitulo, lblMensaje;
    @FXML private Button btnGuardar;
    private Tratamiento t;
    private boolean saved = false;
    @FXML private ComboBox<Consulta> cmbConsulta;
    @FXML private TextArea txtDescripcion;
    @FXML private TextArea txtIndicaciones;
    @FXML private Spinner<Integer> spnDuracionDias;

    @FXML
    public void initialize() {
        spnDuracionDias.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 365, 1));
        spnDuracionDias.setEditable(false);
        cmbConsulta.setCellFactory(lista -> new ListCell<>() {
            @Override
            protected void updateItem(Consulta consulta, boolean empty) {
                super.updateItem(consulta, empty);
                setText(empty || consulta == null ? null : mostrarConsulta(consulta));
            }
        });
        cmbConsulta.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(Consulta consulta, boolean empty) {
                super.updateItem(consulta, empty);
                setText(empty || consulta == null ? null : mostrarConsulta(consulta));
            }
        });
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
        spnDuracionDias.getValueFactory().setValue(Math.max(1, Math.min(365, t.getDuracionDias())));
    }

    public boolean isSaved() { return saved; }
    public Tratamiento getTratamiento() { return t; }

    private void cerrar() {
        Stage stage = (Stage) btnGuardar.getScene().getWindow();
        stage.close();
    }

    private boolean validated() {
        if (cmbConsulta.getValue() == null ||
                txtDescripcion.getText().isBlank() || spnDuracionDias.getValue() == null) {
            mostrarMensaje("Seleccione una consulta, escriba la descripción y elija la duración", "WARNING");
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
            t.setDuracionDias(spnDuracionDias.getValue());
            saved = true;
            cerrar();
        }
    }

    private String mostrarConsulta(Consulta consulta) {
        String diagnostico = consulta.getDiagnostico() == null || consulta.getDiagnostico().isBlank()
                ? "Sin diagnóstico" : consulta.getDiagnostico().trim();
        if (consulta.getCita() == null) {
            return diagnostico;
        }

        StringBuilder texto = new StringBuilder();
        if (consulta.getCita().getMascota() != null && consulta.getCita().getMascota().getNombre() != null) {
            texto.append(consulta.getCita().getMascota().getNombre()).append(" | ");
        }
        if (consulta.getCita().getFecha() != null) {
            texto.append(consulta.getCita().getFecha().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
            if (consulta.getCita().getHora() != null && !consulta.getCita().getHora().isBlank()) {
                texto.append(" ").append(consulta.getCita().getHora());
            }
            texto.append(" | ");
        }
        texto.append(diagnostico);
        return texto.toString();
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
