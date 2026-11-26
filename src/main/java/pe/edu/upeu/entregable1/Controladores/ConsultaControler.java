package pe.edu.upeu.entregable1.Controladores;

import javafx.fxml.*;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.util.Optional;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.stage.Modality;
import pe.edu.upeu.entregable1.modelo.Consulta;
import pe.edu.upeu.entregable1.interfaces.IConsulta;
import pe.edu.upeu.entregable1.implementacion.ConsultaImpl;
import pe.edu.upeu.entregable1.utils.Alerts;

public class ConsultaControler {
    @FXML private TableView<Consulta> tblConsultas;
    @FXML private Label lblMensaje;
    @FXML private TableColumn<Consulta, Number> colNumero;
    @FXML private TableColumn<Consulta, Integer> colId;
    @FXML private TableColumn<Consulta, String> colCita;
    @FXML private TableColumn<Consulta, String> colMascota;
    @FXML private TableColumn<Consulta, String> colDiagnostico;
    @FXML private TableColumn<Consulta, String> colObservaciones;
    private final IConsulta consultaDao = new ConsultaImpl();
    private ObservableList<Consulta> listaConsulta;

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
        colId.setCellValueFactory(column -> column.getValue().idConsultaProperty().asObject());
        colCita.setCellValueFactory(column -> new SimpleStringProperty(column.getValue().getCita().getFecha() + " " + column.getValue().getCita().getHora()));
        colMascota.setCellValueFactory(column -> column.getValue().getCita().getMascota().nombreProperty());
        colDiagnostico.setCellValueFactory(column -> column.getValue().diagnosticoProperty());
        colObservaciones.setCellValueFactory(column -> column.getValue().observacionesProperty());
        cargardatos();
    }

    private void cargardatos() {
        try {
            listaConsulta = FXCollections.observableArrayList(consultaDao.listAll());
            tblConsultas.setItems(listaConsulta);
        } catch (Exception e) {
            mostrarMensaje("No se pudieron cargar los datos: " + e.getMessage(), "ERROR");
        }
    }


    @FXML
    private void handleNew() { showForm(null); }

    @FXML
    private void handleEdit() {
        Consulta c = tblConsultas.getSelectionModel().getSelectedItem();
        if (c == null) {
            mostrarMensaje("Seleccione un registro para editar", "WARNING");
            return;
        }
        try {
            c = consultaDao.searchById(c.getIdConsulta());
            if (c != null) showForm(c);
            else mostrarMensaje("El registro ya no existe", "WARNING");
        } catch (Exception e) {
            mostrarMensaje("No se pudo abrir el registro: " + e.getMessage(), "ERROR");
        }
    }

    @FXML
    private void handleDelete() {
        Consulta c = tblConsultas.getSelectionModel().getSelectedItem();
        if (c == null) {
            mostrarMensaje("Seleccione un registro para eliminar", "WARNING");
            return;
        }
        Alert alerta = Alerts.viewAlerts("Eliminar consulta",
                "¿Desea eliminar el registro seleccionado?", "CONFIRMATION");
        alerta.initOwner(tblConsultas.getScene().getWindow());
        Optional<ButtonType> result = alerta.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                if (consultaDao.delete(c)) {
                    listaConsulta.remove(c);
                    mostrarMensaje("Registro eliminado", "CONFIRMATION");
                } else {
                    mostrarMensaje("El registro ya no existe. Pulse Mostrar todos para actualizar la tabla.", "WARNING");
                }
            } catch (Exception e) {
                mostrarMensaje(e.getMessage(), "ERROR");
            }
        }
    }

    private void showForm(Consulta c) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(
                    "/pe/edu/upeu/entregable1/VIews/consulta-form.fxml"));
            Parent root = loader.load();
            ConsultaFormControler controller = loader.getController();
            controller.setConsulta(c);
            Stage stage = new Stage();
            stage.setTitle(c == null ? "Nueva consulta" : "Editar consulta");
            stage.initOwner(tblConsultas.getScene().getWindow());
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(new Scene(root));
            stage.setResizable(false);
            stage.showAndWait();
            if (controller.isSaved()) {
                Consulta consultaProcesado = controller.getConsulta();
                if (c == null) {
                    boolean result = consultaDao.insert(consultaProcesado);
                    if (result) {
                        mostrarMensaje("Registro guardado correctamente", "CONFIRMATION");
                    } else {
                        mostrarMensaje("No se pudo guardar. Revise datos duplicados o relaciones.", "ERROR");
                    }
                } else {
                    boolean result = consultaDao.update(consultaProcesado);
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
