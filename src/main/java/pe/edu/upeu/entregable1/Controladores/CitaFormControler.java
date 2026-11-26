package pe.edu.upeu.entregable1.Controladores;

import pe.edu.upeu.entregable1.utils.Alerts;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.collections.FXCollections;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import java.time.LocalDate;
import java.time.LocalTime;
import pe.edu.upeu.entregable1.modelo.*;
import pe.edu.upeu.entregable1.implementacion.*;

public class CitaFormControler {
    @FXML private Label lblTitulo, lblMensaje;
    @FXML private Label lblEstado, lblNombreCliente;
    @FXML private Button btnGuardar;
    private Cita c;
    private Cliente clienteEncontrado;
    private boolean saved = false;
    @FXML private ComboBox<Cliente> cmbDniCliente;
    @FXML private ComboBox<Mascota> cmbMascota;
    @FXML private ComboBox<Veterinario> cmbVeterinario;
    @FXML private DatePicker dpFecha;
    @FXML private ComboBox<String> cmbHora;
    @FXML private TextArea txtMotivo;

    @FXML
    public void initialize() {
        try {
            cmbDniCliente.setItems(FXCollections.observableArrayList(new ClienteImpl().listAll()));
            cmbDniCliente.setConverter(new StringConverter<>() {
                @Override
                public String toString(Cliente cliente) {
                    return cliente == null ? "" : cliente.getDni();
                }

                @Override
                public Cliente fromString(String texto) {
                    return null;
                }
            });
            cmbDniCliente.setCellFactory(combo -> new ListCell<>() {
                @Override
                protected void updateItem(Cliente cliente, boolean empty) {
                    super.updateItem(cliente, empty);
                    setText(empty || cliente == null ? null : cliente.getDni());
                }
            });
            cmbDniCliente.setButtonCell(new ListCell<>() {
                @Override
                protected void updateItem(Cliente cliente, boolean empty) {
                    super.updateItem(cliente, empty);
                    setText(empty || cliente == null ? null : cliente.getDni());
                }
            });
            cmbVeterinario.setItems(FXCollections.observableArrayList(new VeterinarioImpl().listAll()));
            cmbMascota.setItems(FXCollections.observableArrayList());
            cmbMascota.setDisable(true);
            cmbMascota.setConverter(new StringConverter<>() {
                @Override
                public String toString(Mascota mascota) {
                    return mascota == null ? "" : mascota.getNombre() + " (" + mascota.getEspecie() + ")";
                }

                @Override
                public Mascota fromString(String texto) {
                    return null;
                }
            });
            cmbMascota.setCellFactory(combo -> new ListCell<>() {
                @Override
                protected void updateItem(Mascota mascota, boolean empty) {
                    super.updateItem(mascota, empty);
                    setText(empty || mascota == null ? null
                            : mascota.getNombre() + " (" + mascota.getEspecie() + ")");
                }
            });
            cmbVeterinario.setConverter(new StringConverter<>() {
                @Override
                public String toString(Veterinario veterinario) {
                    if (veterinario == null) return "";
                    String nombre = veterinario.getNombres() + " " + veterinario.getApellidos();
                    return veterinario.getEspecialidad() == null || veterinario.getEspecialidad().isBlank()
                            ? nombre : nombre + " — " + veterinario.getEspecialidad();
                }

                @Override
                public Veterinario fromString(String texto) {
                    return null;
                }
            });
            cargarHorarios();
            dpFecha.setValue(LocalDate.now());
            lblEstado.setText("Pendiente");
            cmbDniCliente.valueProperty().addListener((observable, anterior, seleccionado) ->
                    handleBuscarCliente(seleccionado));
        } catch (Exception e) {
            mostrarMensaje("No se pudieron cargar las opciones: " + e.getMessage(), "ERROR");
            btnGuardar.setDisable(true);
        }
    }

    private void cargarHorarios() {
        for (LocalTime hora = LocalTime.of(8, 0);
             !hora.isAfter(LocalTime.of(18, 0));
             hora = hora.plusMinutes(30)) {
            cmbHora.getItems().add(hora.toString());
        }
    }

    public void setCita(Cita c) {
        this.c = c;
        if (c == null) return;
        lblTitulo.setText("Editar cita");
        if (c.getMascota() != null) {
            Mascota mascotaCompleta = new MascotaImpl().searchById(c.getMascota().getIdMascota());
            if (mascotaCompleta != null && mascotaCompleta.getCliente() != null) {
                for (Cliente propietario : cmbDniCliente.getItems()) {
                    if (propietario.getDni().equals(mascotaCompleta.getCliente().getDni())) {
                        cmbDniCliente.setValue(propietario);
                        break;
                    }
                }
                for (Mascota opcion : cmbMascota.getItems()) {
                    if (opcion.getIdMascota() == mascotaCompleta.getIdMascota()) {
                        cmbMascota.setValue(opcion);
                        break;
                    }
                }
            }
        }
        for (Veterinario opcion : cmbVeterinario.getItems()) {
            if (c.getVeterinario() != null && opcion.getIdveterinario() == c.getVeterinario().getIdveterinario()) {
                cmbVeterinario.setValue(opcion);
                break;
            }
        }
        dpFecha.setValue(c.getFecha());
        if (c.getHora() != null && !cmbHora.getItems().contains(c.getHora())) {
            cmbHora.getItems().add(c.getHora());
            FXCollections.sort(cmbHora.getItems());
        }
        cmbHora.setValue(c.getHora());
        txtMotivo.setText(c.getMotivo());
        lblEstado.setText(c.getEstado());
    }

    private void handleBuscarCliente(Cliente clienteSeleccionado) {
        clienteEncontrado = clienteSeleccionado;
        cmbMascota.getItems().clear();
        cmbMascota.setValue(null);
        cmbMascota.setDisable(true);
        lblMensaje.setText("");
        if (clienteSeleccionado == null) {
            lblNombreCliente.setText("Seleccione un DNI para cargar sus mascotas");
            lblNombreCliente.setStyle("-fx-text-fill: #718391;");
            cmbMascota.setPromptText("Seleccione primero el DNI del propietario");
            return;
        }
        try {
            lblNombreCliente.setText(clienteSeleccionado.getNombres() + " " + clienteSeleccionado.getApellidos());
            lblNombreCliente.setStyle("-fx-text-fill: #1B5E20; -fx-font-weight: bold;");
            cmbMascota.setItems(FXCollections.observableArrayList(
                    new MascotaImpl().searchByDni(clienteSeleccionado.getDni())));
            if (cmbMascota.getItems().isEmpty()) {
                cmbMascota.setPromptText("Este propietario no tiene mascotas");
                lblMensaje.setText("Este propietario todavía no tiene mascotas registradas.");
            } else {
                cmbMascota.setPromptText("Seleccione una mascota");
                cmbMascota.setDisable(false);
            }
        } catch (Exception e) {
            clienteEncontrado = null;
            cmbMascota.setPromptText("No se pudieron cargar las mascotas");
            lblNombreCliente.setText("No se pudo cargar el propietario");
            lblNombreCliente.setStyle("-fx-text-fill: #B71C1C;");
            mostrarMensaje("No se pudo buscar al cliente: " + e.getMessage(), "ERROR");
        }
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
        if (clienteEncontrado == null ||
                cmbMascota.getValue() == null ||
                cmbVeterinario.getValue() == null ||
                dpFecha.getValue() == null ||
                cmbHora.getValue() == null || cmbHora.getValue().isBlank()) {
            mostrarMensaje("Complete los campos obligatorios y seleccione las opciones", "WARNING");
            return false;
        }
        if (dpFecha.getValue().isBefore(LocalDate.now())) {
            mostrarMensaje("La cita debe programarse para hoy o una fecha futura", "WARNING");
            return false;
        }

        Cita propuesta = new Cita();
        if (c != null) propuesta.setIdCita(c.getIdCita());
        propuesta.setMascota(cmbMascota.getValue());
        propuesta.setVeterinario(cmbVeterinario.getValue());
        propuesta.setFecha(dpFecha.getValue());
        propuesta.setHora(cmbHora.getValue());
        try {
            if (new CitaImpl().verificarConflictoHorario(propuesta)) {
                mostrarMensaje("Ese horario ya está reservado para la mascota o el veterinario seleccionado", "WARNING");
                return false;
            }
        } catch (Exception e) {
            mostrarMensaje("No se pudo comprobar la disponibilidad: " + e.getMessage(), "ERROR");
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
            c.setHora(cmbHora.getValue());
            c.setMotivo(txtMotivo.getText().trim());
            if (c.getEstado() == null || c.getEstado().isBlank()) {
                c.setEstado("Pendiente");
            }
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
