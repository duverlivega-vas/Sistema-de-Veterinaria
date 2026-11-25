package pe.edu.upeu.entregable1;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.stage.Window;
import java.util.function.Consumer;
import java.util.concurrent.atomic.AtomicReference;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import java.nio.file.*;
import java.util.concurrent.*;
import pe.edu.upeu.entregable1.viewcontrol.VeterinariaApplication;

class JavaFxIntegrationTest {
    @org.junit.jupiter.api.io.TempDir static Path databaseDirectory;
    static String previousDatabase;

    @BeforeAll static void startToolkit() throws Exception {
        previousDatabase = System.getProperty("veterinaria.db.url");
        String url = "jdbc:sqlite:" + databaseDirectory.resolve("vistas.db");
        System.setProperty("veterinaria.db.url", url);
        try (var cn = java.sql.DriverManager.getConnection(url);
             var st = cn.createStatement()) {
            String schema = Files.readString(Path.of("src/test/resources/schema.sql"));
            for (String sql : schema.split(";")) if (!sql.isBlank()) st.execute(sql);
        }
        CountDownLatch ready=new CountDownLatch(1);
        Platform.startup(()->{Platform.setImplicitExit(false);ready.countDown();});
        assertTrue(ready.await(15,TimeUnit.SECONDS));
    }
    @AfterAll static void stopToolkit() throws Exception {
        Platform.exit();
        if (pe.edu.upeu.entregable1.utils.ConexionBD.connection != null)
            pe.edu.upeu.entregable1.utils.ConexionBD.connection.close();
        pe.edu.upeu.entregable1.utils.ConexionBD.connection = null;
        if (previousDatabase == null) System.clearProperty("veterinaria.db.url");
        else System.setProperty("veterinaria.db.url", previousDatabase);
    }
    private final java.util.List<String> avisos = new java.util.ArrayList<>();

    private void onFx(Callable<Void> action) throws Exception {
        FutureTask<Void> task = new FutureTask<>(() -> {
            // Solo en las pruebas: leer y aceptar los avisos para continuar el recorrido.
            javafx.animation.Timeline aceptarAvisos = new javafx.animation.Timeline(
                new javafx.animation.KeyFrame(javafx.util.Duration.millis(30), e -> {
                    for (Window window : java.util.List.copyOf(Window.getWindows())) {
                        if (window instanceof Stage stage && window.isShowing()
                                && stage.getScene().getRoot() instanceof DialogPane pane
                                && !stage.getTitle().startsWith("Eliminar ")) {
                            // Aceptar después de que JavaFX termine de mostrar el diálogo.
                            Platform.runLater(() -> {
                                if (stage.isShowing()) {
                                    avisos.add(pane.getContentText());
                                    ((Button)pane.lookupButton(ButtonType.OK)).fire();
                                }
                            });
                        }
                    }
                }));
            aceptarAvisos.setCycleCount(javafx.animation.Animation.INDEFINITE);
            aceptarAvisos.play();
            try { return action.call(); }
            finally { aceptarAvisos.stop(); }
        });
        Platform.runLater(task);
        task.get(45, TimeUnit.SECONDS);
    }
    private void checkImages(Node node) {
        if(node instanceof ImageView iv) {assertNotNull(iv.getImage());assertFalse(iv.getImage().isError(),iv.getImage().getUrl());}
        if(node instanceof Parent parent)parent.getChildrenUnmodifiable().forEach(this::checkImages);
    }
    @Test void allFxmlAndImagesLoadOnModulePath() throws Exception {
        onFx(()->{
            Path resources=Path.of("src/main/resources/pe/edu/upeu/entregable1");
            try(var files=Files.walk(resources)) {
                for(Path path:files.filter(f->f.toString().endsWith(".fxml")).toList()) {
                    String resource="/pe/edu/upeu/entregable1/"+resources.relativize(path).toString().replace('\\','/');
                    Parent root=FXMLLoader.load(getClass().getResource(resource));
                    new Scene(root);root.applyCss();root.layout();checkImages(root);
                }
            }
            return null;
        });
    }
    @Test void mainMenuLoadsEveryView() throws Exception {
        onFx(()->{
            FXMLLoader loader=new FXMLLoader(getClass().getResource("/pe/edu/upeu/entregable1/VIews/main-view.fxml"));
            Parent root=loader.load();new Scene(root,1000,680);root.applyCss();root.layout();
            StackPane center=(StackPane)loader.getNamespace().get("contenedorVistas");
            String[] ids={"Clientes","Mascotas","Veterinarios","Citas","Consultas","Tratamientos","Usuarios","Inicio"};
            for(String id:ids) {
                Node previous=center.getChildren().getFirst();
                ((Button)loader.getNamespace().get("btn"+id)).fire();
                assertEquals(1,center.getChildren().size());assertNotSame(previous,center.getChildren().getFirst());
            }
            return null;
        });
    }
    @Test void applicationStartsAndCloses() throws Exception {
        onFx(()->{
            Stage stage=new Stage();
            try {new VeterinariaApplication().start(stage);assertTrue(stage.isShowing());assertNotNull(stage.getScene());}
            finally {stage.close();}
            return null;
        });
    }

    private void dialogAction(Stage owner, Button button, Consumer<Stage> action) {
        AtomicReference<Throwable> failure = new AtomicReference<>();
        Platform.runLater(() -> {
            Stage dialog = (Stage) Window.getWindows().stream()
                    .filter(w -> w != owner && w.isShowing()).findFirst().orElseThrow();
            try {
                action.accept(dialog);
                assertFalse(dialog.isShowing(), "El diálogo debe cerrar al completar la acción");
            } catch (Throwable e) {
                failure.set(e);
                dialog.close();
            }
        });
        button.fire();
        if (failure.get() != null) throw new AssertionError("Error en formulario", failure.get());
    }

    private void text(Stage stage, String id, String value) {
        ((TextInputControl) stage.getScene().lookup("#" + id)).setText(value);
    }

    private void click(Stage stage, String id) {
        ((Button) stage.getScene().lookup("#" + id)).fire();
    }

    @Test void loginRejectsWrongPasswordAndAllowsLogout() throws Exception {
        onFx(() -> {
            Stage stage = new Stage();
            try {
                new VeterinariaApplication().start(stage);
                text(stage, "txtUsuario", "admin");
                text(stage, "txtPassword", "incorrecta");
                click(stage, "btnIngresar");
                assertTrue(((Label)stage.getScene().lookup("#lblMensaje")).getText().contains("incorrectos"));
                assertNull(stage.getScene().lookup("#btnCerrarSesion"));
                text(stage, "txtPassword", "admin123");
                click(stage, "btnIngresar");
                assertNotNull(stage.getScene().lookup("#btnCerrarSesion"));
                assertEquals("admin", ((Label)stage.getScene().lookup("#lblUsuario")).getText());
                click(stage, "btnCerrarSesion");
                assertNotNull(stage.getScene().lookup("#btnIngresar"));
                assertEquals("", ((TextField)stage.getScene().lookup("#txtUsuario")).getText());
                assertEquals("", ((PasswordField)stage.getScene().lookup("#txtPassword")).getText());
            } finally { stage.close(); }
            return null;
        });
    }

    @Test void loginAndLogoutKeepWindowSizeAndMaximizedState() throws Exception {
        AtomicReference<Stage> window = new AtomicReference<>();
        try {
            for (boolean maximized : new boolean[] {false, true}) {
                CountDownLatch ready = new CountDownLatch(1);
                onFx(() -> {
                    Stage stage = new Stage();
                    window.set(stage);
                    new VeterinariaApplication().start(stage);
                    stage.setWidth(1100);
                    stage.setHeight(740);
                    stage.setMaximized(maximized);
                    javafx.animation.PauseTransition pause = new javafx.animation.PauseTransition(
                            javafx.util.Duration.millis(250));
                    pause.setOnFinished(e -> ready.countDown());
                    pause.play();
                    return null;
                });
                assertTrue(ready.await(5, TimeUnit.SECONDS));
                onFx(() -> {
                    Stage stage = window.get();
                    Scene scene = stage.getScene();
                    double width = stage.getWidth();
                    double height = stage.getHeight();
                    double x = stage.getX();
                    double y = stage.getY();
                    assertEquals(maximized, stage.isMaximized());
                    text(stage, "txtUsuario", "admin");
                    text(stage, "txtPassword", "admin123");
                    click(stage, "btnIngresar");
                    assertNotNull(scene.lookup("#btnCerrarSesion"));
                    for (int step = 0; step < 2; step++) {
                        assertSame(scene, stage.getScene());
                        assertEquals(width, stage.getWidth(), 1);
                        assertEquals(height, stage.getHeight(), 1);
                        assertEquals(x, stage.getX(), 1);
                        assertEquals(y, stage.getY(), 1);
                        assertEquals(maximized, stage.isMaximized());
                        assertFalse(stage.isIconified());
                        if (step == 0) click(stage, "btnCerrarSesion");
                    }
                    assertNotNull(scene.lookup("#btnIngresar"));
                    stage.close();
                    return null;
                });
            }
        } finally {
            onFx(() -> { if (window.get() != null) window.get().close(); return null; });
        }
    }

    @Test void crudButtonsPersistSearchEditAndDelete() throws Exception {
        onFx(() -> {
            Stage stage = new Stage();
            try {
                {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/pe/edu/upeu/entregable1/VIews/cliente-vista.fxml"));
                    stage.setScene(new Scene(loader.load(), 820, 615)); stage.show();
                    TableView<?> table = (TableView<?>)loader.getNamespace().get("tblClientes");
                    TableColumn<?, ?> id = (TableColumn<?, ?>)loader.getNamespace().get("colId");
                    assertNotNull(id, "La columna ID se conserva");
                    assertFalse(id.isVisible(), "La columna ID debe estar oculta");
                    TableColumn<?, ?> numero = (TableColumn<?, ?>)loader.getNamespace().get("colNumero");
                    assertNotNull(numero);
                    assertTrue(numero.isVisible());
                    assertEquals("N°", numero.getText());
                    assertFalse(numero.isSortable());
                    assertEquals(numero, table.getVisibleLeafColumn(0));
                    assertEquals(0, table.getItems().size());
                    dialogAction(stage, (Button)loader.getNamespace().get("btnNuevo"), form -> {
                        click(form, "btnGuardar");
                        assertTrue(form.isShowing(), "No guardar campos vacíos");
                        text(form, "txtNombres", "Ana");
                        text(form, "txtApellidos", "Vega");
                        text(form, "txtDni", "01234567");
                        text(form, "txtTelefono", "012345678");
                        text(form, "txtDireccion", "Lima");
                        ((ComboBox<?>)form.getScene().lookup("#cmbEstado")).getSelectionModel().selectFirst();
                        click(form, "btnGuardar");
                    });
                    assertEquals(1, table.getItems().size(), "Insertar desde el formulario");
                    table.getSelectionModel().selectFirst();
                    dialogAction(stage, (Button)loader.getNamespace().get("btnEditar"), form -> {
                        assertNotNull(((ComboBox<?>)form.getScene().lookup("#cmbEstado")).getValue(), "Precargar cmbEstado");
                        text(form, "txtNombres", "Maria");
                        click(form, "btnGuardar");
                    });
                    assertEquals(1, table.getItems().size());
                    assertEquals("Buscar por DNI", ((TextField)loader.getNamespace().get("txtBuscar")).getPromptText());
                    assertNull(loader.getNamespace().get("cmbCriterioBusqueda"));
                    text(stage, "txtBuscar", "01234567"); click(stage, "btnBuscar");
                    assertEquals(1, table.getItems().size(), "Buscar DNI");
                    assertTrue(table.getColumns().stream().anyMatch(col ->
                            "Maria".equals(col.getCellData(0))), "Edición persistida");
                    text(stage, "txtBuscar", "99999999"); click(stage, "btnBuscar");
                    assertEquals(0, table.getItems().size());
                    assertTrue(avisos.contains("No se encontró ningún cliente con ese DNI"));
                    click(stage, "btnMostrarTodos");
                    assertEquals(1, table.getItems().size(), "Mostrar todos");
                    dialogAction(stage, (Button)loader.getNamespace().get("btnNuevo"), form -> click(form, "btnCancelarEdicion"));
                    assertEquals(1, table.getItems().size(), "Cancelar no inserta");
                }
                {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/pe/edu/upeu/entregable1/VIews/veterinario-view.fxml"));
                    stage.setScene(new Scene(loader.load(), 820, 615)); stage.show();
                    TableView<?> table = (TableView<?>)loader.getNamespace().get("tblVeterinarios");
                    TableColumn<?, ?> id = (TableColumn<?, ?>)loader.getNamespace().get("colId");
                    assertNotNull(id, "La columna ID se conserva");
                    assertFalse(id.isVisible(), "La columna ID debe estar oculta");
                    TableColumn<?, ?> numero = (TableColumn<?, ?>)loader.getNamespace().get("colNumero");
                    assertNotNull(numero);
                    assertTrue(numero.isVisible());
                    assertEquals("N°", numero.getText());
                    assertFalse(numero.isSortable());
                    assertEquals(numero, table.getVisibleLeafColumn(0));
                    assertEquals(0, table.getItems().size());
                    dialogAction(stage, (Button)loader.getNamespace().get("btnNuevo"), form -> {
                        click(form, "btnGuardar");
                        assertTrue(form.isShowing(), "No guardar campos vacíos");
                        text(form, "txtNombres", "Luis");
                        text(form, "txtApellidos", "Perez");
                        text(form, "txtDni", "02345678");
                        text(form, "txtTelefono", "987654321");
                        text(form, "txtEspecialidad", "General");
                        ((ComboBox<?>)form.getScene().lookup("#cmbEstado")).getSelectionModel().selectFirst();
                        click(form, "btnGuardar");
                    });
                    assertEquals(1, table.getItems().size(), "Insertar desde el formulario");
                    table.getSelectionModel().selectFirst();
                    dialogAction(stage, (Button)loader.getNamespace().get("btnEditar"), form -> {
                        assertNotNull(((ComboBox<?>)form.getScene().lookup("#cmbEstado")).getValue(), "Precargar cmbEstado");
                        text(form, "txtEspecialidad", "Cirugia");
                        click(form, "btnGuardar");
                    });
                    assertEquals(1, table.getItems().size());
                    assertEquals("Buscar por DNI", ((TextField)loader.getNamespace().get("txtBuscar")).getPromptText());
                    text(stage, "txtBuscar", "02345678"); click(stage, "btnBuscar");
                    assertEquals(1, table.getItems().size(), "Buscar DNI");
                    assertTrue(table.getColumns().stream().anyMatch(col ->
                            "Cirugia".equals(col.getCellData(0))), "Edición persistida");
                    text(stage, "txtBuscar", "99999999"); click(stage, "btnBuscar");
                    assertEquals(0, table.getItems().size());
                    assertTrue(avisos.contains("No se encontró ningún veterinario con ese DNI"));
                    click(stage, "btnMostrarTodos");
                    assertEquals(1, table.getItems().size(), "Mostrar todos");
                    dialogAction(stage, (Button)loader.getNamespace().get("btnNuevo"), form -> click(form, "btnCancelarEdicion"));
                    assertEquals(1, table.getItems().size(), "Cancelar no inserta");
                }
                {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/pe/edu/upeu/entregable1/VIews/mascota-view.fxml"));
                    stage.setScene(new Scene(loader.load(), 820, 615)); stage.show();
                    TableView<?> table = (TableView<?>)loader.getNamespace().get("tblMascotas");
                    TableColumn<?, ?> id = (TableColumn<?, ?>)loader.getNamespace().get("colId");
                    assertNotNull(id, "La columna ID se conserva");
                    assertFalse(id.isVisible(), "La columna ID debe estar oculta");
                    TableColumn<?, ?> numero = (TableColumn<?, ?>)loader.getNamespace().get("colNumero");
                    assertNotNull(numero);
                    assertTrue(numero.isVisible());
                    assertEquals("N°", numero.getText());
                    assertFalse(numero.isSortable());
                    assertEquals(numero, table.getVisibleLeafColumn(0));
                    assertEquals(0, table.getItems().size());
                    dialogAction(stage, (Button)loader.getNamespace().get("btnNuevo"), form -> {
                        click(form, "btnGuardar");
                        assertTrue(form.isShowing(), "No guardar campos vacíos");
                        text(form, "txtNombre", "Luna");
                        text(form, "txtRaza", "Mestiza");
                        ((ComboBox<?>)form.getScene().lookup("#cmbEspecie")).getSelectionModel().selectFirst();
                        ((ComboBox<?>)form.getScene().lookup("#cmbCliente")).getSelectionModel().selectFirst();
                        ((ComboBox<?>)form.getScene().lookup("#cmbEstado")).getSelectionModel().selectFirst();
                        click(form, "btnGuardar");
                    });
                    assertEquals(1, table.getItems().size(), "Insertar desde el formulario");
                    table.getSelectionModel().selectFirst();
                    dialogAction(stage, (Button)loader.getNamespace().get("btnEditar"), form -> {
                        assertNotNull(((ComboBox<?>)form.getScene().lookup("#cmbEspecie")).getValue(), "Precargar cmbEspecie");
                        assertNotNull(((ComboBox<?>)form.getScene().lookup("#cmbCliente")).getValue(), "Precargar cmbCliente");
                        assertNotNull(((ComboBox<?>)form.getScene().lookup("#cmbEstado")).getValue(), "Precargar cmbEstado");
                        text(form, "txtNombre", "Luna dos");
                        click(form, "btnGuardar");
                    });
                    assertEquals(1, table.getItems().size());
                    assertEquals("Buscar por DNI del propietario", ((TextField)loader.getNamespace().get("txtBuscar")).getPromptText());
                    text(stage, "txtBuscar", ""); click(stage, "btnBuscar");
                    assertTrue(avisos.contains("Ingrese el DNI del propietario para buscar"));
                    text(stage, "txtBuscar", "ABC12345"); click(stage, "btnBuscar");
                    assertTrue(avisos.contains("El DNI debe tener 8 dígitos numéricos"));
                    text(stage, "txtBuscar", "01234567"); click(stage, "btnBuscar");
                    assertEquals(1, table.getItems().size(), "Buscar DNI del propietario");
                    assertTrue(table.getColumns().stream().anyMatch(col ->
                            "Luna dos".equals(col.getCellData(0))), "Edición persistida");
                    text(stage, "txtBuscar", "99999999"); click(stage, "btnBuscar");
                    assertEquals(0, table.getItems().size());
                    assertTrue(avisos.contains("No se encontraron mascotas con ese DNI del propietario"));
                    click(stage, "btnMostrarTodos");
                    assertEquals(1, table.getItems().size(), "Mostrar todos");
                    dialogAction(stage, (Button)loader.getNamespace().get("btnNuevo"), form -> click(form, "btnCancelarEdicion"));
                    assertEquals(1, table.getItems().size(), "Cancelar no inserta");
                }
                {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/pe/edu/upeu/entregable1/VIews/cita-view.fxml"));
                    stage.setScene(new Scene(loader.load(), 820, 615)); stage.show();
                    TableView<?> table = (TableView<?>)loader.getNamespace().get("tblCitas");
                    TableColumn<?, ?> id = (TableColumn<?, ?>)loader.getNamespace().get("colId");
                    assertNotNull(id, "La columna ID se conserva");
                    assertFalse(id.isVisible(), "La columna ID debe estar oculta");
                    TableColumn<?, ?> numero = (TableColumn<?, ?>)loader.getNamespace().get("colNumero");
                    assertNotNull(numero);
                    assertTrue(numero.isVisible());
                    assertEquals("N°", numero.getText());
                    assertFalse(numero.isSortable());
                    assertEquals(numero, table.getVisibleLeafColumn(0));
                    assertEquals(0, table.getItems().size());
                    dialogAction(stage, (Button)loader.getNamespace().get("btnNuevo"), form -> {
                        click(form, "btnGuardar");
                        assertTrue(form.isShowing(), "No guardar campos vacíos");
                        text(form, "txtHora", "14:30");
                        text(form, "txtMotivo", "Control");
                        ((ComboBox<?>)form.getScene().lookup("#cmbMascota")).getSelectionModel().selectFirst();
                        ((ComboBox<?>)form.getScene().lookup("#cmbVeterinario")).getSelectionModel().selectFirst();
                        ((ComboBox<?>)form.getScene().lookup("#cmbEstado")).getSelectionModel().selectFirst();
                        ((DatePicker)form.getScene().lookup("#dpFecha")).setValue(java.time.LocalDate.of(2026, 10, 30));
                        click(form, "btnGuardar");
                    });
                    assertEquals(1, table.getItems().size(), "Insertar desde el formulario");
                    table.getSelectionModel().selectFirst();
                    dialogAction(stage, (Button)loader.getNamespace().get("btnEditar"), form -> {
                        assertNotNull(((ComboBox<?>)form.getScene().lookup("#cmbMascota")).getValue(), "Precargar cmbMascota");
                        assertNotNull(((ComboBox<?>)form.getScene().lookup("#cmbVeterinario")).getValue(), "Precargar cmbVeterinario");
                        assertNotNull(((ComboBox<?>)form.getScene().lookup("#cmbEstado")).getValue(), "Precargar cmbEstado");
                        text(form, "txtMotivo", "Control anual");
                        click(form, "btnGuardar");
                    });
                    assertEquals(1, table.getItems().size());
                    assertNull(loader.getNamespace().get("txtBuscar"));
                    assertNull(loader.getNamespace().get("btnBuscar"));
                    assertTrue(table.getColumns().stream().anyMatch(col ->
                            "Control anual".equals(col.getCellData(0))), "Edición persistida");
                    click(stage, "btnMostrarTodos");
                    assertEquals(1, table.getItems().size(), "Mostrar todos");
                    dialogAction(stage, (Button)loader.getNamespace().get("btnNuevo"), form -> click(form, "btnCancelarEdicion"));
                    assertEquals(1, table.getItems().size(), "Cancelar no inserta");
                }
                {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/pe/edu/upeu/entregable1/VIews/consulta-view.fxml"));
                    stage.setScene(new Scene(loader.load(), 820, 615)); stage.show();
                    TableView<?> table = (TableView<?>)loader.getNamespace().get("tblConsultas");
                    TableColumn<?, ?> id = (TableColumn<?, ?>)loader.getNamespace().get("colId");
                    assertNotNull(id, "La columna ID se conserva");
                    assertFalse(id.isVisible(), "La columna ID debe estar oculta");
                    TableColumn<?, ?> numero = (TableColumn<?, ?>)loader.getNamespace().get("colNumero");
                    assertNotNull(numero);
                    assertTrue(numero.isVisible());
                    assertEquals("N°", numero.getText());
                    assertFalse(numero.isSortable());
                    assertEquals(numero, table.getVisibleLeafColumn(0));
                    assertEquals(0, table.getItems().size());
                    dialogAction(stage, (Button)loader.getNamespace().get("btnNuevo"), form -> {
                        click(form, "btnGuardar");
                        assertTrue(form.isShowing(), "No guardar campos vacíos");
                        text(form, "txtDiagnostico", "Revision");
                        text(form, "txtObservaciones", "Sin observaciones");
                        ((ComboBox<?>)form.getScene().lookup("#cmbCita")).getSelectionModel().selectFirst();
                        click(form, "btnGuardar");
                    });
                    assertEquals(1, table.getItems().size(), "Insertar desde el formulario");
                    table.getSelectionModel().selectFirst();
                    dialogAction(stage, (Button)loader.getNamespace().get("btnEditar"), form -> {
                        assertNotNull(((ComboBox<?>)form.getScene().lookup("#cmbCita")).getValue(), "Precargar cmbCita");
                        text(form, "txtDiagnostico", "Revision completa");
                        click(form, "btnGuardar");
                    });
                    assertEquals(1, table.getItems().size());
                    assertNull(loader.getNamespace().get("txtBuscar"));
                    assertNull(loader.getNamespace().get("btnBuscar"));
                    assertTrue(table.getColumns().stream().anyMatch(col ->
                            "Revision completa".equals(col.getCellData(0))), "Edición persistida");
                    click(stage, "btnMostrarTodos");
                    assertEquals(1, table.getItems().size(), "Mostrar todos");
                    dialogAction(stage, (Button)loader.getNamespace().get("btnNuevo"), form -> click(form, "btnCancelarEdicion"));
                    assertEquals(1, table.getItems().size(), "Cancelar no inserta");
                }
                {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/pe/edu/upeu/entregable1/VIews/tratamiento-view.fxml"));
                    stage.setScene(new Scene(loader.load(), 820, 615)); stage.show();
                    TableView<?> table = (TableView<?>)loader.getNamespace().get("tblTratamientos");
                    assertEquals(0, table.getItems().size());
                    dialogAction(stage, (Button)loader.getNamespace().get("btnNuevo"), form -> {
                        click(form, "btnGuardar");
                        assertTrue(form.isShowing(), "No guardar campos vacíos");
                        text(form, "txtDescripcion", "Control");
                        text(form, "txtIndicaciones", "Regresar");
                        text(form, "txtDuracionDias", "5");
                        ((ComboBox<?>)form.getScene().lookup("#cmbConsulta")).getSelectionModel().selectFirst();
                        click(form, "btnGuardar");
                    });
                    assertEquals(1, table.getItems().size(), "Insertar desde el formulario");
                    table.getSelectionModel().selectFirst();
                    dialogAction(stage, (Button)loader.getNamespace().get("btnEditar"), form -> {
                        assertNotNull(((ComboBox<?>)form.getScene().lookup("#cmbConsulta")).getValue(), "Precargar cmbConsulta");
                        text(form, "txtDescripcion", "Seguimiento");
                        click(form, "btnGuardar");
                    });
                    assertEquals(1, table.getItems().size());
                    text(stage, "txtBuscar", String.valueOf(table.getColumns().getFirst().getCellData(0))); click(stage, "btnBuscar");
                    assertEquals(1, table.getItems().size(), "Buscar ID");
                    assertTrue(table.getColumns().stream().anyMatch(col ->
                            "Seguimiento".equals(col.getCellData(0))), "Edición persistida");
                    text(stage, "txtBuscar", "99999"); click(stage, "btnBuscar");
                    assertEquals(0, table.getItems().size());
                    click(stage, "btnMostrarTodos");
                    assertEquals(1, table.getItems().size(), "Mostrar todos");
                    dialogAction(stage, (Button)loader.getNamespace().get("btnNuevo"), form -> click(form, "btnCancelarEdicion"));
                    assertEquals(1, table.getItems().size(), "Cancelar no inserta");
                }
                {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/pe/edu/upeu/entregable1/VIews/usuario-view.fxml"));
                    stage.setScene(new Scene(loader.load(), 820, 615)); stage.show();
                    TableView<?> table = (TableView<?>)loader.getNamespace().get("tblUsuarios");
                    TableColumn<?, ?> id = (TableColumn<?, ?>)loader.getNamespace().get("colId");
                    assertNotNull(id, "La columna ID se conserva");
                    assertFalse(id.isVisible(), "La columna ID debe estar oculta");
                    TableColumn<?, ?> numero = (TableColumn<?, ?>)loader.getNamespace().get("colNumero");
                    assertNotNull(numero);
                    assertTrue(numero.isVisible());
                    assertEquals("N°", numero.getText());
                    assertFalse(numero.isSortable());
                    assertEquals(numero, table.getVisibleLeafColumn(0));
                    assertEquals(0, table.getItems().size());
                    dialogAction(stage, (Button)loader.getNamespace().get("btnNuevo"), form -> {
                        click(form, "btnGuardar");
                        assertTrue(form.isShowing(), "No guardar campos vacíos");
                        text(form, "txtUsuario", "prueba");
                        text(form, "txtPassword", "clavePrueba");
                        text(form, "txtNombres", "Juan");
                        text(form, "txtApellidos", "Perez");
                        text(form, "txtDni", "03456789");
                        ((ComboBox<?>)form.getScene().lookup("#cmbTipoUser")).getSelectionModel().selectFirst();
                        click(form, "btnGuardar");
                    });
                    assertEquals(1, table.getItems().size(), "Insertar desde el formulario");
                    table.getSelectionModel().selectFirst();
                    dialogAction(stage, (Button)loader.getNamespace().get("btnEditar"), form -> {
                        assertNotNull(((ComboBox<?>)form.getScene().lookup("#cmbTipoUser")).getValue(), "Precargar cmbTipoUser");
                        text(form, "txtNombres", "Juan dos");
                        click(form, "btnGuardar");
                    });
                    assertEquals(1, table.getItems().size());
                    assertEquals("Buscar por DNI", ((TextField)loader.getNamespace().get("txtBuscar")).getPromptText());
                    text(stage, "txtBuscar", "03456789"); click(stage, "btnBuscar");
                    assertEquals(1, table.getItems().size(), "Buscar DNI");
                    assertEquals("03456789", ((TableColumn<?, ?>)loader.getNamespace().get("colDni")).getCellData(0));
                    assertTrue(table.getColumns().stream().anyMatch(col ->
                            "Juan dos".equals(col.getCellData(0))), "Edición persistida");
                    text(stage, "txtBuscar", "99999999"); click(stage, "btnBuscar");
                    assertEquals(0, table.getItems().size());
                    assertTrue(avisos.contains("No se encontró ningún usuario con ese DNI"));
                    click(stage, "btnMostrarTodos");
                    assertEquals(1, table.getItems().size(), "Mostrar todos");
                    dialogAction(stage, (Button)loader.getNamespace().get("btnNuevo"), form -> click(form, "btnCancelarEdicion"));
                    assertEquals(1, table.getItems().size(), "Cancelar no inserta");
                }
                {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/pe/edu/upeu/entregable1/VIews/usuario-view.fxml"));
                    stage.setScene(new Scene(loader.load(), 820, 615)); stage.show();
                    TableView<?> table = (TableView<?>)loader.getNamespace().get("tblUsuarios");
                    assertEquals(1, table.getItems().size());
                    table.getSelectionModel().selectFirst();
                    dialogAction(stage, (Button)loader.getNamespace().get("btnEliminar"), dialog -> {
                        DialogPane pane = (DialogPane)dialog.getScene().getRoot();
                        ((Button)pane.lookupButton(ButtonType.OK)).fire();
                    });
                    assertEquals(0, table.getItems().size(), "Eliminar Usuario");
                }
                {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/pe/edu/upeu/entregable1/VIews/tratamiento-view.fxml"));
                    stage.setScene(new Scene(loader.load(), 820, 615)); stage.show();
                    TableView<?> table = (TableView<?>)loader.getNamespace().get("tblTratamientos");
                    assertEquals(1, table.getItems().size());
                    table.getSelectionModel().selectFirst();
                    dialogAction(stage, (Button)loader.getNamespace().get("btnEliminar"), dialog -> {
                        DialogPane pane = (DialogPane)dialog.getScene().getRoot();
                        ((Button)pane.lookupButton(ButtonType.OK)).fire();
                    });
                    assertEquals(0, table.getItems().size(), "Eliminar Tratamiento");
                }
                {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/pe/edu/upeu/entregable1/VIews/consulta-view.fxml"));
                    stage.setScene(new Scene(loader.load(), 820, 615)); stage.show();
                    TableView<?> table = (TableView<?>)loader.getNamespace().get("tblConsultas");
                    assertEquals(1, table.getItems().size());
                    table.getSelectionModel().selectFirst();
                    dialogAction(stage, (Button)loader.getNamespace().get("btnEliminar"), dialog -> {
                        DialogPane pane = (DialogPane)dialog.getScene().getRoot();
                        ((Button)pane.lookupButton(ButtonType.OK)).fire();
                    });
                    assertEquals(0, table.getItems().size(), "Eliminar Consulta");
                }
                {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/pe/edu/upeu/entregable1/VIews/cita-view.fxml"));
                    stage.setScene(new Scene(loader.load(), 820, 615)); stage.show();
                    TableView<?> table = (TableView<?>)loader.getNamespace().get("tblCitas");
                    assertEquals(1, table.getItems().size());
                    table.getSelectionModel().selectFirst();
                    dialogAction(stage, (Button)loader.getNamespace().get("btnEliminar"), dialog -> {
                        DialogPane pane = (DialogPane)dialog.getScene().getRoot();
                        ((Button)pane.lookupButton(ButtonType.OK)).fire();
                    });
                    assertEquals(0, table.getItems().size(), "Eliminar Cita");
                }
                {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/pe/edu/upeu/entregable1/VIews/mascota-view.fxml"));
                    stage.setScene(new Scene(loader.load(), 820, 615)); stage.show();
                    TableView<?> table = (TableView<?>)loader.getNamespace().get("tblMascotas");
                    assertEquals(1, table.getItems().size());
                    table.getSelectionModel().selectFirst();
                    dialogAction(stage, (Button)loader.getNamespace().get("btnEliminar"), dialog -> {
                        DialogPane pane = (DialogPane)dialog.getScene().getRoot();
                        ((Button)pane.lookupButton(ButtonType.OK)).fire();
                    });
                    assertEquals(0, table.getItems().size(), "Eliminar Mascota");
                }
                {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/pe/edu/upeu/entregable1/VIews/veterinario-view.fxml"));
                    stage.setScene(new Scene(loader.load(), 820, 615)); stage.show();
                    TableView<?> table = (TableView<?>)loader.getNamespace().get("tblVeterinarios");
                    assertEquals(1, table.getItems().size());
                    table.getSelectionModel().selectFirst();
                    dialogAction(stage, (Button)loader.getNamespace().get("btnEliminar"), dialog -> {
                        DialogPane pane = (DialogPane)dialog.getScene().getRoot();
                        ((Button)pane.lookupButton(ButtonType.OK)).fire();
                    });
                    assertEquals(0, table.getItems().size(), "Eliminar Veterinario");
                }
                {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/pe/edu/upeu/entregable1/VIews/cliente-vista.fxml"));
                    stage.setScene(new Scene(loader.load(), 820, 615)); stage.show();
                    TableView<?> table = (TableView<?>)loader.getNamespace().get("tblClientes");
                    assertEquals(1, table.getItems().size());
                    table.getSelectionModel().selectFirst();
                    dialogAction(stage, (Button)loader.getNamespace().get("btnEliminar"), dialog -> {
                        DialogPane pane = (DialogPane)dialog.getScene().getRoot();
                        ((Button)pane.lookupButton(ButtonType.OK)).fire();
                    });
                    assertEquals(0, table.getItems().size(), "Eliminar Cliente");
                    assertTrue(avisos.contains("Registro guardado correctamente"));
                    assertTrue(avisos.contains("Registro eliminado"));
                    assertTrue(avisos.contains("No se encontraron registros"));
                    assertTrue(avisos.contains("Complete los campos obligatorios y seleccione las opciones"));
                }
            } finally { stage.close(); }
            return null;
        });
    }

    @Test void registeredUserCanLoginAndChangedPasswordIsRespected() throws Exception {
        onFx(() -> {
            var dao = new pe.edu.upeu.entregable1.implementacion.UsuarioImpl();
            var u = new pe.edu.upeu.entregable1.modelo.Usuario();
            u.setUsuario("recepcion_prueba"); u.setPassword("clave1");
            u.setNombres("Ana"); u.setApellidos("Prueba"); u.setDni("88776655");
            u.setTipoUser("Recepcionista");
            assertTrue(dao.insert(u));
            u = dao.searchByUsuario("recepcion_prueba");
            Stage stage = new Stage();
            try {
                new VeterinariaApplication().start(stage);
                text(stage, "txtUsuario", u.getUsuario()); text(stage, "txtPassword", "clave1");
                click(stage, "btnIngresar");
                assertEquals(u.getUsuario(), ((Label)stage.getScene().lookup("#lblUsuario")).getText());
                click(stage, "btnCerrarSesion");
                u.setPassword("clave2"); assertTrue(dao.update(u));
                text(stage, "txtUsuario", u.getUsuario()); text(stage, "txtPassword", "clave1");
                click(stage, "btnIngresar");
                assertTrue(avisos.contains("Usuario o contraseña incorrectos"));
                assertNotNull(stage.getScene().lookup("#btnIngresar"));
                text(stage, "txtPassword", "clave2"); click(stage, "btnIngresar");
                assertNotNull(stage.getScene().lookup("#btnCerrarSesion"));
            } finally { stage.close(); assertTrue(dao.delete(u)); }
            return null;
        });
    }

    @Test void missingSelectionAndInvalidDniShowAlerts() throws Exception {
        onFx(() -> {
            Stage stage = new Stage();
            try {
                Parent root = FXMLLoader.load(getClass().getResource("/pe/edu/upeu/entregable1/VIews/cliente-vista.fxml"));
                stage.setScene(new Scene(root)); stage.show();
                click(stage, "btnEditar");
                assertTrue(avisos.contains("Seleccione un registro para editar"));
                click(stage, "btnEliminar");
                assertTrue(avisos.contains("Seleccione un registro para eliminar"));
                for (String vista : new String[]{"cliente-vista.fxml", "veterinario-view.fxml", "usuario-view.fxml"}) {
                    Parent vistaDni = FXMLLoader.load(getClass().getResource("/pe/edu/upeu/entregable1/VIews/" + vista));
                    stage.setScene(new Scene(vistaDni));
                    avisos.clear();
                    text(stage, "txtBuscar", " "); click(stage, "btnBuscar");
                    assertTrue(avisos.contains("Ingrese un DNI para buscar"));
                    avisos.clear();
                    text(stage, "txtBuscar", "abc"); click(stage, "btnBuscar");
                    assertTrue(avisos.contains("El DNI debe tener 8 dígitos numéricos"));
                    avisos.clear();
                    text(stage, "txtBuscar", "1234567"); click(stage, "btnBuscar");
                    assertTrue(avisos.contains("El DNI debe tener 8 dígitos numéricos"));
                }
            } finally { stage.close(); }
            return null;
        });
    }

    @Test void cancellationDuplicateAndForeignKeyErrorsAreVisible() throws Exception {
        onFx(() -> {
            var clientes = new pe.edu.upeu.entregable1.implementacion.ClienteImpl();
            var mascotas = new pe.edu.upeu.entregable1.implementacion.MascotaImpl();
            var c = new pe.edu.upeu.entregable1.modelo.Cliente();
            c.setNombres("Prueba"); c.setApellidos("Errores"); c.setDni("77665544");
            c.setEstado("Activo"); assertTrue(clientes.agregar(c));
            c = clientes.searchByDni("77665544");
            var m = new pe.edu.upeu.entregable1.modelo.Mascota();
            m.setNombre("Mascota errores"); m.setEspecie("Gato"); m.setEstado("Activo"); m.setCliente(c);
            assertTrue(mascotas.insert(m));
            m = mascotas.listAll().stream().filter(x -> "Mascota errores".equals(x.getNombre())).findFirst().orElseThrow();
            Stage stage = new Stage();
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/pe/edu/upeu/entregable1/VIews/cliente-vista.fxml"));
                stage.setScene(new Scene(loader.load())); stage.show();
                TableView<?> table = (TableView<?>)loader.getNamespace().get("tblClientes");
                table.getSelectionModel().selectFirst();
                dialogAction(stage, (Button)loader.getNamespace().get("btnEliminar"), dialog -> {
                    DialogPane pane = (DialogPane)dialog.getScene().getRoot();
                    ((Button)pane.lookupButton(ButtonType.CANCEL)).fire();
                });
                assertNotNull(clientes.searchById(c.getIdcliente()));
                dialogAction(stage, (Button)loader.getNamespace().get("btnEliminar"), dialog -> {
                    DialogPane pane = (DialogPane)dialog.getScene().getRoot();
                    ((Button)pane.lookupButton(ButtonType.OK)).fire();
                });
                assertTrue(avisos.contains("No se puede eliminar este cliente porque tiene mascotas registradas. Puede marcarlo como Inactivo desde Editar para conservar su historial."));
                assertNotNull(clientes.searchById(c.getIdcliente()));
                dialogAction(stage, (Button)loader.getNamespace().get("btnNuevo"), form -> {
                    text(form, "txtNombres", "Duplicado"); text(form, "txtApellidos", "Prueba");
                    text(form, "txtDni", "77665544"); click(form, "btnGuardar");
                });
                assertTrue(avisos.contains("No se pudo guardar. Revise datos duplicados o relaciones."));
                assertEquals("Prueba", clientes.searchByDni("77665544").getNombres());
                assertEquals(1, table.getItems().size());
            } finally {
                stage.close(); assertTrue(mascotas.delete(m)); assertTrue(clientes.delete(c));
            }
            return null;
        });
    }

    @Test void sqlReadErrorIsNotReportedAsAnEmptyTable() throws Exception {
        String original = System.getProperty("veterinaria.db.url");
        try {
            System.setProperty("veterinaria.db.url", "jdbc:sqlite:" + databaseDirectory.resolve("sin-tablas.db"));
            assertThrows(IllegalStateException.class,
                () -> new pe.edu.upeu.entregable1.implementacion.ClienteImpl().listAll());
        } finally {
            if (pe.edu.upeu.entregable1.utils.ConexionBD.connection != null)
                pe.edu.upeu.entregable1.utils.ConexionBD.connection.close();
            pe.edu.upeu.entregable1.utils.ConexionBD.connection = null;
            System.setProperty("veterinaria.db.url", original);
        }
    }
}
