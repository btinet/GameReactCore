module org.gamereact.gamereactcore {
    requires javafx.controls;
    requires javafx.fxml;
            
                    requires org.kordamp.ikonli.javafx;
                    requires com.almasb.fxgl.all;
    
    opens org.gamereact.gamereactcore to javafx.fxml;
    exports org.gamereact.gamereactcore;
}