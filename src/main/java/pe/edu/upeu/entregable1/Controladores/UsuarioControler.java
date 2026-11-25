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
import pe.edu.upeu.entregable1.modelo.Usuario;
import pe.edu.upeu.entregable1.interfaces.IUsuario;
import pe.edu.upeu.entregable1.implementacion.UsuarioImpl;
import pe.edu.upeu.entregable1.utils.Alerts;

public class UsuarioControler {
    @FXML private TableView<Usuario> tblUsuarios;
    @FXML private TextField txtBuscar;
    @FXML private Label lblMensaje;
    @FXML private TableColumn<Usuario, Number> colNumero;
    @FXML private TableColumn<Usuario, Integer> colId;
    @FXML private TableColumn<Usuario, String> colUsuario;
    @FXML private TableColumn<Usuario, String> colNombres;
    @FXML private TableColumn<Usuario, String> colApellidos;
    @FXML private TableColumn<Usuario, String> colDni;
    @FXML private TableColumn<Usuario, String> colTipo;
    private final IUsuario usuarioDao = new UsuarioImpl();
    private ObservableList<Usuario> listaUsuario;

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
        colId.setCellValueFactory(column -> column.getValue().idusuarioProperty().asObject());
        colUsuario.setCellValueFactory(column -> column.getValue().usuarioProperty());
        colNombres.setCellValueFactory(column -> column.getValue().nombresProperty());
        colApellidos.setCellValueFactory(column -> column.getValue().apellidosProperty());
        colDni.setCellValueFactory(column -> column.getValue().dniProperty());
        colTipo.setCellValueFactory(column -> column.getValue().tipoUserProperty());
        txtBuscar.setOnAction(e -> handleSearch());
        cargardatos();
    }

    private void cargardatos() {
        try {
            listaUsuario = FXCollections.observableArrayList(usuarioDao.listAll());
            tblUsuarios.setItems(listaUsuario);
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
            Usuario u = usuarioDao.searchByDni(dni);
            tblUsuarios.getItems().clear();
            if (u == null) {
                mostrarMensaje("No se encontró ningún usuario con ese DNI", "WARNING");
            } else {
                tblUsuarios.getItems().add(u);
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
        Usuario u = tblUsuarios.getSelectionModel().getSelectedItem();
        if (u == null) {
            mostrarMensaje("Seleccione un registro para editar", "WARNING");
            return;
        }
        try {
            u = usuarioDao.searchById(u.getIdusuario());
            if (u != null) showForm(u);
            else mostrarMensaje("El registro ya no existe", "WARNING");
        } catch (Exception e) {
            mostrarMensaje("No se pudo abrir el registro: " + e.getMessage(), "ERROR");
        }
    }

    @FXML
    private void handleDelete() {
        Usuario u = tblUsuarios.getSelectionModel().getSelectedItem();
        if (u == null) {
            mostrarMensaje("Seleccione un registro para eliminar", "WARNING");
            return;
        }
        Alert alerta = Alerts.viewAlerts("Eliminar usuario",
                "¿Desea eliminar el registro seleccionado?", "CONFIRMATION");
        alerta.initOwner(tblUsuarios.getScene().getWindow());
        Optional<ButtonType> result = alerta.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                if (usuarioDao.delete(u)) {
                    listaUsuario.remove(u);
                    mostrarMensaje("Registro eliminado", "CONFIRMATION");
                } else {
                    mostrarMensaje("El registro ya no existe. Pulse Mostrar todos para actualizar la tabla.", "WARNING");
                }
            } catch (Exception e) {
                mostrarMensaje(e.getMessage(), "ERROR");
            }
        }
    }

    private void showForm(Usuario u) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(
                    "/pe/edu/upeu/entregable1/VIews/usuario-form.fxml"));
            Parent root = loader.load();
            UsuarioFormControler controller = loader.getController();
            controller.setUsuario(u);
            Stage stage = new Stage();
            stage.setTitle(u == null ? "Nuevo usuario" : "Editar usuario");
            stage.initOwner(tblUsuarios.getScene().getWindow());
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(new Scene(root));
            stage.setResizable(false);
            stage.showAndWait();
            if (controller.isSaved()) {
                Usuario usuarioProcesado = controller.getUsuario();
                if (u == null) {
                    boolean result = usuarioDao.insert(usuarioProcesado);
                    if (result) {
                        mostrarMensaje("Registro guardado correctamente", "CONFIRMATION");
                    } else {
                        mostrarMensaje("No se pudo guardar. Revise datos duplicados o relaciones.", "ERROR");
                    }
                } else {
                    boolean result = usuarioDao.update(usuarioProcesado);
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
