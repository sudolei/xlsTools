module com.sunlei.xlstools {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires com.almasb.fxgl.all;
    requires poi;
    requires poi.ooxml;
    opens com.sunlei.xlstools to javafx.fxml;
    exports com.sunlei.xlstools;
}