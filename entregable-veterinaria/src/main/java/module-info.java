module pe.edu.upeu.entregable1 {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.bootstrapfx.core;
    requires jdk.unsupported.desktop;

    opens pe.edu.upeu.entregable1 to javafx.fxml;
    exports pe.edu.upeu.entregable1;
    exports pe.edu.upeu.entregable1.viewcontrol;
}
