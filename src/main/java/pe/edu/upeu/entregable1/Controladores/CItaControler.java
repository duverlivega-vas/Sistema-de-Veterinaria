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
    @FXML private TextField txtBuscar;
    @FXML private Label lblMensaje;
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
        colId.setCellValueFactory(column -> column.getValue().idCitaProperty().asObject());
        colMascota.setCellValueFactory(column -> new SimpleStringProperty(column.getValue().getMascota().toString()));
        colVeterinario.setCellValueFactory(column -> new SimpleStringProperty(column.getValue().getVeterinario().toString()));
        colFecha.setCellValueFactory(column -> column.getValue().fechaProperty());
        colHora.setCellValueFactory(column -> column.getValue().horaProperty());
        colMotivo.setCellValueFactory(column -> column.getValue().motivoProperty());
        colEstado.setCellValueFactory(column -> column.getValue().estadoProperty());
        txtBuscar.setOnAction(e -> handleSearch());
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
    private void handleListAll() {
        txtBuscar.clear();
        lblMensaje.setText("");
        cargardatos();
    }

    @FXML
    private void handleSearch() {
        if (txtBuscar.getText().isBlank()) {
            handleListAll();
            return;
        }
        try {
            Cita c;
            c = citaDao.searchById(Integer.parseInt(txtBuscar.getText().trim()));
            tblCitas.getItems().clear();
            if (c != null) tblCitas.getItems().add(c);
            if (c == null) mostrarMensaje("No se encontraron registros", "WARNING");
            else lblMensaje.setText("");
        } catch (NumberFormatException e) {
            mostrarMensaje("Ingrese un ID numérico válido", "WARNING");
        } catch (Exception e) {
            mostrarMensaje("Error al buscar: " + e.getMessage(), "ERROR");
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
        Alert alerta = Alerts.viewAlerts("Eliminar cita",
                "¿Desea eliminar el registro seleccionado?", "CONFIRMATION");
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
