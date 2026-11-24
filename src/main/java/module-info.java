module pe.edu.upeu.entregable1 {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.bootstrapfx.core;
    requires jdk.unsupported.desktop;
    requires java.sql;
    requires org.xerial.sqlitejdbc;


    opens pe.edu.upeu.entregable1 to javafx.fxml;
    opens pe.edu.upeu.entregable1.Controladores to javafx.fxml;
    opens pe.edu.upeu.entregable1.modelo to javafx.base;

    exports pe.edu.upeu.entregable1.viewcontrol;
}
