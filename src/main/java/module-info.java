module me.loule.hipopothalous {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.ikonli.javafx;

    opens me.loule.hipopothalous to javafx.fxml;
    exports me.loule.hipopothalous;
}
