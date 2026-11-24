package pe.edu.upeu.entregable1.Controladores;

import javafx.fxml.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.util.Optional;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.stage.Modality;
import pe.edu.upeu.entregable1.modelo.Tratamiento;
import pe.edu.upeu.entregable1.interfaces.ITratamiento;
import pe.edu.upeu.entregable1.implementacion.TratamientoImpl;
import pe.edu.upeu.entregable1.utils.Alerts;

public class TratamientoControler {
    @FXML private TableView<Tratamiento> tblTratamientos;
    @FXML private TextField txtBuscar;
    @FXML private Label lblMensaje;
    @FXML private TableColumn<Tratamiento, Integer> colId;
    @FXML private TableColumn<Tratamiento, Integer> colConsulta;
    @FXML private TableColumn<Tratamiento, String> colDescripcion;
    @FXML private TableColumn<Tratamiento, String> colIndicaciones;
    @FXML private TableColumn<Tratamiento, Integer> colDias;
    private final ITratamiento tratamientoDao = new TratamientoImpl();
    private ObservableList<Tratamiento> listaTratamiento;

    @FXML
    public void initialize() {
        colId.setCellValueFactory(column -> column.getValue().idTratamientoProperty().asObject());
        colConsulta.setCellValueFactory(column -> column.getValue().getConsulta().idConsultaProperty().asObject());
        colDescripcion.setCellValueFactory(column -> column.getValue().descripcionProperty());
        colIndicaciones.setCellValueFactory(column -> column.getValue().indicacionesProperty());
        colDias.setCellValueFactory(column -> column.getValue().duracionDiasProperty().asObject());
        txtBuscar.setOnAction(e -> handleSearch());
        cargardatos();
    }

    private void cargardatos() {
        try {
            listaTratamiento = FXCollections.observableArrayList(tratamientoDao.listAll());
            tblTratamientos.setItems(listaTratamiento);
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
            Tratamiento t;
            t = tratamientoDao.searchById(Integer.parseInt(txtBuscar.getText().trim()));
            tblTratamientos.getItems().clear();
            if (t != null) tblTratamientos.getItems().add(t);
            if (t == null) mostrarMensaje("No se encontraron registros", "WARNING");
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
        Tratamiento t = tblTratamientos.getSelectionModel().getSelectedItem();
        if (t == null) {
            mostrarMensaje("Seleccione un registro para editar", "WARNING");
            return;
        }
        try {
            t = tratamientoDao.searchById(t.getIdTratamiento());
            if (t != null) showForm(t);
            else mostrarMensaje("El registro ya no existe", "WARNING");
        } catch (Exception e) {
            mostrarMensaje("No se pudo abrir el registro: " + e.getMessage(), "ERROR");
        }
    }

    @FXML
    private void handleDelete() {
        Tratamiento t = tblTratamientos.getSelectionModel().getSelectedItem();
        if (t == null) {
            mostrarMensaje("Seleccione un registro para eliminar", "WARNING");
            return;
        }
        Alert alerta = Alerts.viewAlerts("Eliminar tratamiento",
                "¿Desea eliminar el registro seleccionado?", "CONFIRMATION");
        alerta.initOwner(tblTratamientos.getScene().getWindow());
        Optional<ButtonType> result = alerta.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                if (tratamientoDao.delete(t)) {
                    listaTratamiento.remove(t);
                    mostrarMensaje("Registro eliminado", "CONFIRMATION");
                } else {
                    mostrarMensaje("El registro ya no existe. Pulse Mostrar todos para actualizar la tabla.", "WARNING");
                }
            } catch (Exception e) {
                mostrarMensaje(e.getMessage(), "ERROR");
            }
        }
    }

    private void showForm(Tratamiento t) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(
                    "/pe/edu/upeu/entregable1/VIews/tratamiento-form.fxml"));
            Parent root = loader.load();
            TratamientoFormControler controller = loader.getController();
            controller.setTratamiento(t);
            Stage stage = new Stage();
            stage.setTitle(t == null ? "Nuevo tratamiento" : "Editar tratamiento");
            stage.initOwner(tblTratamientos.getScene().getWindow());
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(new Scene(root));
            stage.setResizable(false);
            stage.showAndWait();
            if (controller.isSaved()) {
                Tratamiento tratamientoProcesado = controller.getTratamiento();
                if (t == null) {
                    boolean result = tratamientoDao.insert(tratamientoProcesado);
                    if (result) {
                        mostrarMensaje("Registro guardado correctamente", "CONFIRMATION");
                    } else {
                        mostrarMensaje("No se pudo guardar. Revise datos duplicados o relaciones.", "ERROR");
                    }
                } else {
                    boolean result = tratamientoDao.update(tratamientoProcesado);
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
