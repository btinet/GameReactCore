package org.gamereact.gamereactcore;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.engine.KeyPolling;

import java.io.IOException;
import java.util.Objects;

public class CoreApplication extends Application {

    public static Stage stage;
    public static boolean fullscreen = false;
    public static boolean resizable = false;
    public static int width = 1280;
    public static int height = 720;
    public static String title = "GameReact Core";
    public static boolean verbose = false;

    @Override
    public void start(Stage stage) throws IOException {
        CoreApplication.stage = stage;
        FXMLLoader fxmlLoader = new FXMLLoader(CoreApplication.class.getResource("index.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), width, height);
        scene.getStylesheets().add(Objects.requireNonNull(this.getClass().getResource("/css/chart.css")).toExternalForm());

        KeyPolling.getInstance().pollScene(scene);

        stage.setOnCloseRequest(event -> {
            Platform.exit();
            System.exit(0);
        });

        stage.setFullScreen(false);
        stage.initStyle(StageStyle.UNDECORATED);
        stage.getIcons().add(new Image(Objects.requireNonNull(CoreApplication.class.getResource("/org/icons/tk.png")).openStream()));
        stage.setTitle(title);
        stage.setScene(scene);
        stage.setResizable(resizable);
        stage.setFullScreen(fullscreen);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }


}