module org.gamereact.gamereactcore {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.kordamp.ikonli.core;
    requires org.kordamp.ikonli.javafx;
    requires com.almasb.fxgl.all;

    opens org.gamereact.gamereactcore to javafx.fxml;
    exports org.gamereact.gamereactcore;
    exports org.gamereact.controller;
    exports com.tuio;
    opens org.gamereact.controller to javafx.fxml;
}