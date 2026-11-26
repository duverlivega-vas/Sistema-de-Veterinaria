package pe.edu.upeu.entregable1.viewcontrol;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class VeterinariaApplication extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(
                "/pe/edu/upeu/entregable1/VIews/login-view.fxml"));
        stage.setScene(new Scene(loader.load(), 820, 600));

        var logo = getClass().getResourceAsStream(
                "/pe/edu/upeu/entregable1/img/logo veterinaria.png");
        if (logo != null) {
            stage.getIcons().add(new Image(logo));
        }

        stage.setTitle("Sistema de Veterinaria");
        stage.setMinWidth(820);
        stage.setMinHeight(600);
        stage.setResizable(false);
        stage.centerOnScreen();
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
