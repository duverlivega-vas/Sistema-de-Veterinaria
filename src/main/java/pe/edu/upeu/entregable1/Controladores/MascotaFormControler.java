package pe.edu.upeu.entregable1.Controladores;

import pe.edu.upeu.entregable1.utils.Alerts;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.collections.FXCollections;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import pe.edu.upeu.entregable1.modelo.*;
import pe.edu.upeu.entregable1.implementacion.*;

public class MascotaFormControler {
    @FXML private Label lblTitulo, lblMensaje;
    @FXML private Button btnGuardar;
    private Mascota m;
    private boolean saved = false;
    @FXML private TextField txtNombre;
    @FXML private ComboBox<String> cmbEspecie;
    @FXML private TextField txtRaza;
    @FXML private ComboBox<Cliente> cmbCliente;
    @FXML private ComboBox<String> cmbEstado;

    @FXML
    public void initialize() {
        try {
            cmbCliente.setItems(FXCollections.observableArrayList(new ClienteImpl().listAll()));
            cmbCliente.setConverter(new StringConverter<>() {
                @Override
                public String toString(Cliente cliente) {
                    if (cliente == null) return "";
                    return cliente.getDni() + " — " + cliente.getNombres() + " " + cliente.getApellidos();
                }

                @Override
                public Cliente fromString(String texto) {
                    return null;
                }
            });
            cmbEstado.setValue("Activo");
            if (cmbCliente.getItems().isEmpty()) {
                lblMensaje.setText("Primero registre un propietario en la sección Clientes.");
                btnGuardar.setDisable(true);
            }
        } catch (Exception e) {
            mostrarMensaje("No se pudieron cargar las opciones: " + e.getMessage(), "ERROR");
            btnGuardar.setDisable(true);
        }
    }

    public void setMascota(Mascota m) {
        this.m = m;
        if (m == null) return;
        lblTitulo.setText("Editar mascota");
        txtNombre.setText(m.getNombre());
        cmbEspecie.setValue(m.getEspecie());
        txtRaza.setText(m.getRaza());
        boolean propietarioDisponible = false;
        if (m.getCliente() != null) {
            for (Cliente opcion : cmbCliente.getItems()) {
                if (opcion.getIdcliente() == m.getCliente().getIdcliente()) {
                    cmbCliente.setValue(opcion);
                    propietarioDisponible = true;
                    break;
                }
            }
        }
        if (!propietarioDisponible && m.getCliente() != null) {
            cmbCliente.getItems().add(m.getCliente());
            cmbCliente.setValue(m.getCliente());
            btnGuardar.setDisable(false);
        }
        cmbEstado.setValue(m.getEstado());
    }

    public boolean isSaved() { return saved; }
    public Mascota getMascota() { return m; }

    private void cerrar() {
        Stage stage = (Stage) btnGuardar.getScene().getWindow();
        stage.close();
    }

    private boolean validated() {
        if (txtNombre.getText().isBlank() ||
                cmbEspecie.getValue() == null ||
                cmbCliente.getValue() == null ||
                cmbEstado.getValue() == null) {
            mostrarMensaje("Complete los campos obligatorios y seleccione las opciones", "WARNING");
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
            if (m == null) m = new Mascota();
            m.setNombre(txtNombre.getText().trim());
            m.setEspecie(cmbEspecie.getValue());
            m.setRaza(txtRaza.getText().trim());
            m.setCliente(cmbCliente.getValue());
            m.setEstado(cmbEstado.getValue());
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
