module me.loule.hipopothalous {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.ikonli.javafx;
    requires java.sql;

    opens me.loule.hipopothalous to javafx.fxml;
    exports me.loule.hipopothalous;
    exports me.loule.hipopothalous.controller;
    opens me.loule.hipopothalous.controller to javafx.fxml;
    exports me.loule.hipopothalous.model;
    opens me.loule.hipopothalous.model to javafx.fxml;
}
