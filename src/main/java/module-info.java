module com.example.psd {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    //requires validatorfx;
    requires org.kordamp.bootstrapfx.core;
    requires java.desktop;

    opens com.example.psd to javafx.fxml;
    exports com.example.psd;
}