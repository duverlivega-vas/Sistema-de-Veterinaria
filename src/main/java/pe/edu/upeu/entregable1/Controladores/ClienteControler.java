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
import pe.edu.upeu.entregable1.modelo.Cliente;
import pe.edu.upeu.entregable1.interfaces.ICliente;
import pe.edu.upeu.entregable1.implementacion.ClienteImpl;
import pe.edu.upeu.entregable1.utils.Alerts;

public class ClienteControler {
    @FXML private TableView<Cliente> tblClientes;
    @FXML private TextField txtBuscar;
    @FXML private Label lblMensaje;
    @FXML private TableColumn<Cliente, Number> colNumero;
    @FXML private TableColumn<Cliente, Integer> colId;
    @FXML private TableColumn<Cliente, String> colNombres;
    @FXML private TableColumn<Cliente, String> colApellidos;
    @FXML private TableColumn<Cliente, String> colDni;
    @FXML private TableColumn<Cliente, String> colTelefono;
    @FXML private TableColumn<Cliente, String> colDireccion;
    @FXML private TableColumn<Cliente, String> colEstado;
    private final ICliente clienteDao = new ClienteImpl();
    private ObservableList<Cliente> listaCliente;

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
        colId.setCellValueFactory(column -> column.getValue().idclienteProperty().asObject());
        colNombres.setCellValueFactory(column -> column.getValue().nombresProperty());
        colApellidos.setCellValueFactory(column -> column.getValue().apellidosProperty());
        colDni.setCellValueFactory(column -> column.getValue().dniProperty());
        colTelefono.setCellValueFactory(column -> column.getValue().telefonoProperty());
        colDireccion.setCellValueFactory(column -> column.getValue().direccionProperty());
        colEstado.setCellValueFactory(column -> column.getValue().estadoProperty());
        txtBuscar.setOnAction(e -> handleSearch());
        txtBuscar.textProperty().addListener((observable, anterior, actual) -> {
            if (actual != null && actual.trim().matches("[0-9]{8}")) handleSearch();
        });
        cargardatos();
    }

    private void cargardatos() {
        try {
            listaCliente = FXCollections.observableArrayList(clienteDao.listAll());
            tblClientes.setItems(listaCliente);
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
            Cliente c = clienteDao.searchByDni(dni);
            tblClientes.getItems().clear();
            if (c == null) {
                mostrarMensaje("No se encontró ningún cliente con ese DNI", "WARNING");
            } else {
                tblClientes.getItems().add(c);
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
        Cliente c = tblClientes.getSelectionModel().getSelectedItem();
        if (c == null) {
            mostrarMensaje("Seleccione un registro para editar", "WARNING");
            return;
        }
        try {
            c = clienteDao.searchById(c.getIdcliente());
            if (c != null) showForm(c);
            else mostrarMensaje("El registro ya no existe", "WARNING");
        } catch (Exception e) {
            mostrarMensaje("No se pudo abrir el registro: " + e.getMessage(), "ERROR");
        }
    }

    @FXML
    private void handleDelete() {
        Cliente c = tblClientes.getSelectionModel().getSelectedItem();
        if (c == null) {
            mostrarMensaje("Seleccione un registro para eliminar", "WARNING");
            return;
        }
        Alert alerta = Alerts.viewAlerts("Eliminar cliente",
                "¿Desea eliminar el registro seleccionado?", "CONFIRMATION");
        alerta.initOwner(tblClientes.getScene().getWindow());
        Optional<ButtonType> result = alerta.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                if (clienteDao.delete(c)) {
                    listaCliente.remove(c);
                    mostrarMensaje("Registro eliminado", "CONFIRMATION");
                } else {
                    mostrarMensaje("El registro ya no existe. Pulse Mostrar todos para actualizar la tabla.", "WARNING");
                }
            } catch (Exception e) {
                mostrarMensaje(e.getMessage(), "ERROR");
            }
        }
    }

    private void showForm(Cliente c) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(
                    "/pe/edu/upeu/entregable1/VIews/cliente-form.fxml"));
            Parent root = loader.load();
            ClienteFormControler controller = loader.getController();
            controller.setCliente(c);
            Stage stage = new Stage();
            stage.setTitle(c == null ? "Nuevo cliente" : "Editar cliente");
            stage.initOwner(tblClientes.getScene().getWindow());
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(new Scene(root));
            stage.setResizable(false);
            stage.showAndWait();
            if (controller.isSaved()) {
                Cliente clienteProcesado = controller.getCliente();
                if (c == null) {
                    boolean result = clienteDao.agregar(clienteProcesado);
                    if (result) {
                        mostrarMensaje("Registro guardado correctamente", "CONFIRMATION");
                    } else {
                        mostrarMensaje("No se pudo guardar. Revise datos duplicados o relaciones.", "ERROR");
                    }
                } else {
                    boolean result = clienteDao.update(clienteProcesado);
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
