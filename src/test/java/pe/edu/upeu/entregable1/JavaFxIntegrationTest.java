package pe.edu.upeu.entregable1;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.*;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import java.nio.file.*;
import java.util.concurrent.*;
import pe.edu.upeu.entregable1.viewcontrol.VeterinariaApplication;

class JavaFxIntegrationTest {
    @BeforeAll static void startToolkit() throws Exception {
        CountDownLatch ready=new CountDownLatch(1);
        Platform.startup(()->{Platform.setImplicitExit(false);ready.countDown();});
        assertTrue(ready.await(15,TimeUnit.SECONDS));
    }
    @AfterAll static void stopToolkit(){Platform.exit();}
    private void onFx(Callable<Void> action) throws Exception {
        FutureTask<Void> task=new FutureTask<>(action);Platform.runLater(task);task.get(30,TimeUnit.SECONDS);
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
            String[] ids={"Clientes","Mascotas","Veterinarios","Citas","Consultas","Tratamientos","Usuarios","Vacunacion","Inicio"};
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
}
