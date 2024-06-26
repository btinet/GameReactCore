module org.gamereact.gamereactcore {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.kordamp.ikonli.core;
    requires org.kordamp.ikonli.javafx;
    requires com.almasb.fxgl.all;
    requires javafx.media;
    requires java.xml;
    requires jSerialComm;
    requires org.knowhowlab.comm.rxtx.patched;
    requires java.desktop;

    opens org.gamereact.gamereactcore to javafx.fxml;
    exports org.gamereact.gamereactcore;
    exports org.gamereact.controller;
    exports org.gamereact.component;
    exports org.gamereact.module.electronic;
    exports org.engine;
    exports com.tuio;
    opens org.gamereact.controller to javafx.fxml;
    exports org.gamereact.module;
}