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
import pe.edu.upeu.entregable1.modelo.Veterinario;
import pe.edu.upeu.entregable1.interfaces.IVeterinario;
import pe.edu.upeu.entregable1.implementacion.VeterinarioImpl;
import pe.edu.upeu.entregable1.utils.Alerts;

public class VeterinarioControler {
    @FXML private TableView<Veterinario> tblVeterinarios;
    @FXML private TextField txtBuscar;
    @FXML private Label lblMensaje;
    @FXML private TableColumn<Veterinario, Number> colNumero;
    @FXML private TableColumn<Veterinario, Integer> colId;
    @FXML private TableColumn<Veterinario, String> colNombres;
    @FXML private TableColumn<Veterinario, String> colApellidos;
    @FXML private TableColumn<Veterinario, String> colDni;
    @FXML private TableColumn<Veterinario, String> colTelefono;
    @FXML private TableColumn<Veterinario, String> colEspecialidad;
    @FXML private TableColumn<Veterinario, String> colEstado;
    private final IVeterinario veterinarioDao = new VeterinarioImpl();
    private ObservableList<Veterinario> listaVeterinario;

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
        colId.setCellValueFactory(column -> column.getValue().idveterinarioProperty().asObject());
        colNombres.setCellValueFactory(column -> column.getValue().nombresProperty());
        colApellidos.setCellValueFactory(column -> column.getValue().apellidosProperty());
        colDni.setCellValueFactory(column -> column.getValue().dniProperty());
        colTelefono.setCellValueFactory(column -> column.getValue().telefonoProperty());
        colEspecialidad.setCellValueFactory(column -> column.getValue().especialidadProperty());
        colEstado.setCellValueFactory(column -> column.getValue().estadoProperty());
        txtBuscar.setOnAction(e -> handleSearch());
        txtBuscar.textProperty().addListener((observable, anterior, actual) -> {
            if (actual != null && actual.trim().matches("[0-9]{8}")) handleSearch();
        });
        cargardatos();
    }

    private void cargardatos() {
        try {
            listaVeterinario = FXCollections.observableArrayList(veterinarioDao.listAll());
            tblVeterinarios.setItems(listaVeterinario);
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
        String dni = txtBuscar.getText().trim();
        if (dni.isEmpty()) {
            mostrarMensaje("Ingrese un DNI para buscar", "WARNING");
            return;
        }
        if (!dni.matches("[0-9]{8}")) {
            mostrarMensaje("El DNI debe tener 8 dígitos numéricos", "WARNING");
            return;
        }
        try {
            Veterinario v = veterinarioDao.searchByDni(dni);
            tblVeterinarios.getItems().clear();
            if (v == null) {
                mostrarMensaje("No se encontró ningún veterinario con ese DNI", "WARNING");
            } else {
                tblVeterinarios.getItems().add(v);
                lblMensaje.setText("");
            }
        } catch (Exception e) {
            mostrarMensaje("Error al buscar: " + e.getMessage(), "ERROR");
        }
    }

    @FXML
    private void handleNew() { showForm(null); }

    @FXML
    private void handleEdit() {
        Veterinario v = tblVeterinarios.getSelectionModel().getSelectedItem();
        if (v == null) {
            mostrarMensaje("Seleccione un registro para editar", "WARNING");
            return;
        }
        try {
            v = veterinarioDao.searchById(v.getIdveterinario());
            if (v != null) showForm(v);
            else mostrarMensaje("El registro ya no existe", "WARNING");
        } catch (Exception e) {
            mostrarMensaje("No se pudo abrir el registro: " + e.getMessage(), "ERROR");
        }
    }

    @FXML
    private void handleDelete() {
        Veterinario v = tblVeterinarios.getSelectionModel().getSelectedItem();
        if (v == null) {
            mostrarMensaje("Seleccione un registro para eliminar", "WARNING");
            return;
        }
        Alert alerta = Alerts.viewAlerts("Eliminar veterinario",
                "¿Desea eliminar el registro seleccionado?", "CONFIRMATION");
        alerta.initOwner(tblVeterinarios.getScene().getWindow());
        Optional<ButtonType> result = alerta.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                if (veterinarioDao.delete(v)) {
                    listaVeterinario.remove(v);
                    mostrarMensaje("Registro eliminado", "CONFIRMATION");
                } else {
                    mostrarMensaje("El registro ya no existe. Pulse Mostrar todos para actualizar la tabla.", "WARNING");
                }
            } catch (Exception e) {
                mostrarMensaje(e.getMessage(), "ERROR");
            }
        }
    }

    private void showForm(Veterinario v) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(
                    "/pe/edu/upeu/entregable1/VIews/veterinario-form.fxml"));
            Parent root = loader.load();
            VeterinarioFormControler controller = loader.getController();
            controller.setVeterinario(v);
            Stage stage = new Stage();
            stage.setTitle(v == null ? "Nuevo veterinario" : "Editar veterinario");
            stage.initOwner(tblVeterinarios.getScene().getWindow());
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(new Scene(root));
            stage.setResizable(false);
            stage.showAndWait();
            if (controller.isSaved()) {
                Veterinario veterinarioProcesado = controller.getVeterinario();
                if (v == null) {
                    boolean result = veterinarioDao.insert(veterinarioProcesado);
                    if (result) {
                        mostrarMensaje("Registro guardado correctamente", "CONFIRMATION");
                    } else {
                        mostrarMensaje("No se pudo guardar. Revise datos duplicados o relaciones.", "ERROR");
                    }
                } else {
                    boolean result = veterinarioDao.update(veterinarioProcesado);
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
