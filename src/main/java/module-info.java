module com.sims5_1.sims {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires com.almasb.fxgl.all;
    requires java.desktop;

    opens com.sims5_1.sims to javafx.fxml;
    exports com.sims5_1.sims;
    exports SIMS5.gui;
    opens SIMS5.gui to javafx.fxml;
    exports SIMS5.gui.Screen;
    opens SIMS5.gui.Screen to javafx.fxml;
}