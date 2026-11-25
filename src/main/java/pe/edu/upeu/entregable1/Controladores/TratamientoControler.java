package pe.edu.upeu.entregable1.Controladores;

import javafx.fxml.*;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.File;
import java.util.Optional;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.Modality;
import pe.edu.upeu.entregable1.modelo.Tratamiento;
import pe.edu.upeu.entregable1.interfaces.ITratamiento;
import pe.edu.upeu.entregable1.implementacion.TratamientoImpl;
import pe.edu.upeu.entregable1.utils.Alerts;
import pe.edu.upeu.entregable1.utils.PdfExporter;

public class TratamientoControler {
    @FXML private Button btnDescargar;
    @FXML private TableView<Tratamiento> tblTratamientos;
    @FXML private TextField txtBuscar;
    @FXML private Label lblMensaje;
    @FXML private TableColumn<Tratamiento, Number> celNumero;
    @FXML private TableColumn<Tratamiento, Integer> colId;
    @FXML private TableColumn<Tratamiento, String> colMascota;
    @FXML private TableColumn<Tratamiento, String> colDiagnostico;
    @FXML private TableColumn<Tratamiento, String> colDescripcion;
    @FXML private TableColumn<Tratamiento, Integer> colDias;
    private final ITratamiento tratamientoDao = new TratamientoImpl();
    private ObservableList<Tratamiento> listaTratamiento;

    @FXML
    public void initialize() {
        celNumero.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(Number item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty ? null : String.valueOf(getIndex() + 1));
            }
        });
        colId.setCellValueFactory(column -> column.getValue().idTratamientoProperty().asObject());
        colMascota.setCellValueFactory(column -> {
            Tratamiento tratamiento = column.getValue();
            if (tratamiento.getConsulta() == null || tratamiento.getConsulta().getCita() == null ||
                    tratamiento.getConsulta().getCita().getMascota() == null) {
                return new SimpleStringProperty("-");
            }
            return tratamiento.getConsulta().getCita().getMascota().nombreProperty();
        });
        colDiagnostico.setCellValueFactory(column -> {
            Tratamiento tratamiento = column.getValue();
            if (tratamiento.getConsulta() == null || tratamiento.getConsulta().getDiagnostico() == null) {
                return new SimpleStringProperty("-");
            }
            return tratamiento.getConsulta().diagnosticoProperty();
        });
        colDescripcion.setCellValueFactory(column -> column.getValue().descripcionProperty());
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
        String nombre = txtBuscar.getText().trim();
        if (nombre.isBlank()) {
            mostrarMensaje("Ingrese el nombre de la mascota para buscar", "WARNING");
            return;
        }
        try {
            ObservableList<Tratamiento> encontrados = FXCollections.observableArrayList(
                    tratamientoDao.searchByNombreMascota(nombre));
            tblTratamientos.getItems().clear();
            tblTratamientos.getItems().addAll(encontrados);
            if (encontrados.isEmpty()) mostrarMensaje("No se encontraron tratamientos para esa mascota", "WARNING");
            else lblMensaje.setText("");
        } catch (Exception e) {
            mostrarMensaje("Error al buscar: " + e.getMessage(), "ERROR");
        }
    }

    @FXML
    private void handleNew() { showForm(null); }

    @FXML
    private void handleDescargar(){
        Tratamiento tratamiento = tblTratamientos.getSelectionModel().getSelectedItem();
        if (tratamiento == null) {
            mostrarMensaje("Seleccione un tratamiento para descargar su ficha", "WARNING");
            return;
        }
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Guardar ficha del tratamiento");
        chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF", "*.pdf"));
        chooser.setInitialFileName("ficha-tratamiento-" + tratamiento.getIdTratamiento() + ".pdf");
        File archivo = chooser.showSaveDialog(btnDescargar.getScene().getWindow());
        if (archivo == null) {
            return;
        }
        try {
            PdfExporter.exportaTratamiento(tratamiento, archivo);
            mostrarMensaje("Ficha guardada en: " + archivo.getAbsolutePath(), "CONFIRMATION");
        } catch (Exception e) {
            e.printStackTrace();
            mostrarMensaje("Error al generar el documento: " + e.getMessage(), "ERROR");
        }
    }
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
