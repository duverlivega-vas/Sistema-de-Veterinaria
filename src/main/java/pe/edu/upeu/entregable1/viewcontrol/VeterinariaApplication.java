package pe.edu.upeu.entregable1.viewcontrol;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class VeterinariaApplication extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(
                "/pe/edu/upeu/entregable1/VIews/login-view.fxml"));
        stage.setScene(new Scene(loader.load(), 900, 640));
        stage.setTitle("Sistema de Veterinaria");
        stage.setMinWidth(900);
        stage.setMinHeight(600);
        stage.show();
    }

    public static void main(String[] args){
        launch(args);
    }
}
