package pe.edu.upeu.entregable1.Controladores;

import javafx.fxml.*;
import java.time.LocalDate;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.util.Optional;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.stage.*;
import pe.edu.upeu.entregable1.modelo.Cita;
import pe.edu.upeu.entregable1.interfaces.ICita;
import pe.edu.upeu.entregable1.implementacion.CitaImpl;
import pe.edu.upeu.entregable1.utils.Alerts;

public class CItaControler {
    @FXML private TableView<Cita> tblCitas;
    @FXML private Label lblMensaje;
    @FXML private TableColumn<Cita, Number> colNumero;
    @FXML private TableColumn<Cita, Integer> colId;
    @FXML private TableColumn<Cita, String> colMascota;
    @FXML private TableColumn<Cita, String> colVeterinario;
    @FXML private TableColumn<Cita, LocalDate> colFecha;
    @FXML private TableColumn<Cita, String> colHora;
    @FXML private TableColumn<Cita, String> colMotivo;
    @FXML private TableColumn<Cita, String> colEstado;
    private final ICita citaDao = new CitaImpl();
    private ObservableList<Cita> listaCita;

    @FXML
    public void initialize() {
        colNumero.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(Number item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setText(null);
                } else {
                    setText(String.valueOf(getIndex() + 1));
                }
            }
        });
        colId.setCellValueFactory(column -> column.getValue().idCitaProperty().asObject());
        colMascota.setCellValueFactory(column -> column.getValue().getMascota().nombreProperty());
        colVeterinario.setCellValueFactory(column -> new SimpleStringProperty(column.getValue().getVeterinario().getNombres() + " " + column.getValue().getVeterinario().getApellidos()));
        colFecha.setCellValueFactory(column -> column.getValue().fechaProperty());
        colHora.setCellValueFactory(column -> column.getValue().horaProperty());
        colMotivo.setCellValueFactory(column -> column.getValue().motivoProperty());
        colEstado.setCellValueFactory(column -> column.getValue().estadoProperty());
        colEstado.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(String estado, boolean empty) {
                super.updateItem(estado, empty);
                if (empty || estado == null) {
                    setText(null);
                    setStyle("");
                    return;
                }
                setText(estado);
                switch (estado) {
                    case "Pendiente" -> setStyle("-fx-text-fill: #9A6700; -fx-font-weight: bold;");
                    case "En atención" -> setStyle("-fx-text-fill: #0E596B; -fx-font-weight: bold;");
                    case "Atendida" -> setStyle("-fx-text-fill: #1B5E20; -fx-font-weight: bold;");
                    case "Cancelada" -> setStyle("-fx-text-fill: #B71C1C; -fx-font-weight: bold;");
                    default -> setStyle("");
                }
            }
        });
        cargardatos();
    }

    private void cargardatos() {
        try {
            listaCita = FXCollections.observableArrayList(citaDao.listAll());
            tblCitas.setItems(listaCita);
        } catch (Exception e) {
            mostrarMensaje("No se pudieron cargar los datos: " + e.getMessage(), "ERROR");
        }
    }


    @FXML
    private void handleNew() { showForm(null); }

    @FXML
    private void handleEdit() {
        Cita c = tblCitas.getSelectionModel().getSelectedItem();
        if (c == null) {
            mostrarMensaje("Seleccione un registro para editar", "WARNING");
            return;
        }
        if ("Atendida".equals(c.getEstado()) || "Cancelada".equals(c.getEstado())) {
            mostrarMensaje("No se puede editar una cita atendida o cancelada", "WARNING");
            return;
        }
        try {
            c = citaDao.searchById(c.getIdCita());
            if (c != null) showForm(c);
            else mostrarMensaje("El registro ya no existe", "WARNING");
        } catch (Exception e) {
            mostrarMensaje("No se pudo abrir el registro: " + e.getMessage(), "ERROR");
        }
    }

    @FXML
    private void handleDelete() {
        Cita c = tblCitas.getSelectionModel().getSelectedItem();
        if (c == null) {
            mostrarMensaje("Seleccione un registro para eliminar", "WARNING");
            return;
        }
        String mensaje = "¿Desea eliminar la cita seleccionada?";
        if ("Atendida".equals(c.getEstado())) {
            mensaje += " Si tiene consulta o tratamiento asociado, se conservará el historial clínico y no podrá eliminarse.";
        }
        Alert alerta = Alerts.viewAlerts("Eliminar cita", mensaje, "CONFIRMATION");
        alerta.initOwner(tblCitas.getScene().getWindow());
        Optional<ButtonType> result = alerta.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                if (citaDao.delete(c)) {
                    listaCita.remove(c);
                    mostrarMensaje("Registro eliminado", "CONFIRMATION");
                } else {
                    mostrarMensaje("El registro ya no existe. Pulse Mostrar todos para actualizar la tabla.", "WARNING");
                }
            } catch (Exception e) {
                mostrarMensaje(e.getMessage(), "ERROR");
            }
        }
    }

    @FXML
    private void handleIniciarAtencion() {
        cambiarEstado("En atención", "Pendiente", "Iniciar atención",
                "¿Desea iniciar la atención de esta cita?");
    }

    @FXML
    private void handleMarcarAtendida() {
        cambiarEstado("Atendida", "En atención", "Finalizar atención",
                "¿Confirma que la atención de esta cita terminó?");
    }

    @FXML
    private void handleCancelarCita() {
        cambiarEstado("Cancelada", "Pendiente", "Cancelar cita",
                "¿Desea cancelar esta cita?");
    }

    private void cambiarEstado(String nuevoEstado, String estadoRequerido,
                               String titulo, String mensaje) {
        Cita seleccionada = tblCitas.getSelectionModel().getSelectedItem();
        if (seleccionada == null) {
            mostrarMensaje("Seleccione una cita", "WARNING");
            return;
        }
        if (!estadoRequerido.equals(seleccionada.getEstado())) {
            mostrarMensaje("Esta acción requiere que la cita esté en estado " + estadoRequerido + ".", "WARNING");
            return;
        }

        Alert confirmacion = Alerts.viewAlerts(titulo, mensaje, "CONFIRMATION");
        confirmacion.initOwner(tblCitas.getScene().getWindow());
        Optional<ButtonType> respuesta = confirmacion.showAndWait();
        if (respuesta.isPresent() && respuesta.get() == ButtonType.OK) {
            String estadoAnterior = seleccionada.getEstado();
            try {
                seleccionada.setEstado(nuevoEstado);
                if (citaDao.updateStatus(seleccionada)) {
                    cargardatos();
                    mostrarMensaje("Estado actualizado: " + nuevoEstado, "CONFIRMATION");
                } else {
                    seleccionada.setEstado(estadoAnterior);
                    mostrarMensaje("No se pudo actualizar el estado de la cita", "ERROR");
                }
            } catch (Exception e) {
                seleccionada.setEstado(estadoAnterior);
                mostrarMensaje(e.getMessage(), "ERROR");
            }
        }
    }

    private void showForm(Cita c) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(
                    "/pe/edu/upeu/entregable1/VIews/cita-form.fxml"));
            Parent root = loader.load();
            CitaFormControler controller = loader.getController();
            controller.setCita(c);
            Stage stage = new Stage();
            stage.setTitle(c == null ? "Nueva cita" : "Editar cita");
            stage.initOwner(tblCitas.getScene().getWindow());
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(new Scene(root));
            stage.setResizable(false);
            stage.showAndWait();
            if (controller.isSaved()) {
                Cita citaProcesado = controller.getCita();
                if (c == null) {
                    boolean result = citaDao.insert(citaProcesado);
                    if (result) {
                        mostrarMensaje("Registro guardado correctamente", "CONFIRMATION");
                    } else {
                        mostrarMensaje("No se pudo guardar. Revise datos duplicados o relaciones.", "ERROR");
                    }
                } else {
                    boolean result = citaDao.update(citaProcesado);
                    if (result) {
                        mostrarMensaje("Registro actualizado correctamente", "CONFIRMATION");
                    } else {
                        mostrarMensaje("No se pudo actualizar. Revise datos duplicados o relaciones.", "ERROR");
                    }
                }
                cargardatos();
            }
        } catch (Exception e) {
            mostrarMensaje("No se pudo guardar: " + e.getMessage(), "ERROR");
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
