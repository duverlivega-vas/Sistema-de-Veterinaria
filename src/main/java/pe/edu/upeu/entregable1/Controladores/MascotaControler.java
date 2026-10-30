package pe.edu.upeu.entregable1.Controladores;

import javafx.fxml.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.util.Optional;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.stage.Modality;
import pe.edu.upeu.entregable1.modelo.Mascota;
import pe.edu.upeu.entregable1.interfaces.IMascota;
import pe.edu.upeu.entregable1.implementacion.MascotaImpl;
import pe.edu.upeu.entregable1.utils.Alerts;

public class MascotaControler {
    @FXML private TableView<Mascota> tblMascotas;
    @FXML private TextField txtBuscar;
    @FXML private Label lblMensaje;
    @FXML private TableColumn<Mascota, Integer> colId;
    @FXML private TableColumn<Mascota, String> colNombre;
    @FXML private TableColumn<Mascota, String> colEspecie;
    @FXML private TableColumn<Mascota, String> colRaza;
    @FXML private TableColumn<Mascota, String> colCliente;
    @FXML private TableColumn<Mascota, String> colEstado;
    private final IMascota mascotaDao = new MascotaImpl();
    private ObservableList<Mascota> listaMascota;

    @FXML
    public void initialize() {
        colId.setCellValueFactory(column -> column.getValue().idMascotaProperty().asObject());
        colNombre.setCellValueFactory(column -> column.getValue().nombreProperty());
        colEspecie.setCellValueFactory(column -> column.getValue().especieProperty());
        colRaza.setCellValueFactory(column -> column.getValue().razaProperty());
        colCliente.setCellValueFactory(column -> new SimpleStringProperty(column.getValue().getCliente().toString()));
        colEstado.setCellValueFactory(column -> column.getValue().estadoProperty());
        txtBuscar.setOnAction(e -> handleSearch());
        cargardatos();
    }

    private void cargardatos() {
        try {
            listaMascota = FXCollections.observableArrayList(mascotaDao.listAll());
            tblMascotas.setItems(listaMascota);
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
            Mascota m;
            m = mascotaDao.searchById(Integer.parseInt(txtBuscar.getText().trim()));
            tblMascotas.getItems().clear();
            if (m != null) tblMascotas.getItems().add(m);
            if (m == null) mostrarMensaje("No se encontraron registros", "WARNING");
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
        Mascota m = tblMascotas.getSelectionModel().getSelectedItem();
        if (m == null) {
            mostrarMensaje("Seleccione un registro para editar", "WARNING");
            return;
        }
        try {
            m = mascotaDao.searchById(m.getIdMascota());
            if (m != null) showForm(m);
            else mostrarMensaje("El registro ya no existe", "WARNING");
        } catch (Exception e) {
            mostrarMensaje("No se pudo abrir el registro: " + e.getMessage(), "ERROR");
        }
    }

    @FXML
    private void handleDelete() {
        Mascota m = tblMascotas.getSelectionModel().getSelectedItem();
        if (m == null) {
            mostrarMensaje("Seleccione un registro para eliminar", "WARNING");
            return;
        }
        Alert alerta = Alerts.viewAlerts("Eliminar mascota",
                "¿Desea eliminar el registro seleccionado?", "CONFIRMATION");
        alerta.initOwner(tblMascotas.getScene().getWindow());
        Optional<ButtonType> result = alerta.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                if (mascotaDao.delete(m)) {
                    listaMascota.remove(m);
                    mostrarMensaje("Registro eliminado", "CONFIRMATION");
                } else {
                    mostrarMensaje("El registro ya no existe. Pulse Mostrar todos para actualizar la tabla.", "WARNING");
                }
            } catch (Exception e) {
                mostrarMensaje(e.getMessage(), "ERROR");
            }
        }
    }

    private void showForm(Mascota m) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(
                    "/pe/edu/upeu/entregable1/VIews/mascota-form.fxml"));
            Parent root = loader.load();
            MascotaFormControler controller = loader.getController();
            controller.setMascota(m);
            Stage stage = new Stage();
            stage.setTitle(m == null ? "Nueva mascota" : "Editar mascota");
            stage.initOwner(tblMascotas.getScene().getWindow());
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(new Scene(root));
            stage.setResizable(false);
            stage.showAndWait();
            if (controller.isSaved()) {
                Mascota mascotaProcesado = controller.getMascota();
                if (m == null) {
                    boolean result = mascotaDao.insert(mascotaProcesado);
                    if (result) {
                        mostrarMensaje("Registro guardado correctamente", "CONFIRMATION");
                    } else {
                        mostrarMensaje("No se pudo guardar. Revise datos duplicados o relaciones.", "ERROR");
                    }
                } else {
                    boolean result = mascotaDao.update(mascotaProcesado);
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
