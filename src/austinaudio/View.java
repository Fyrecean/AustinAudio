package austinaudio;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;
import java.io.File;

public class View extends Application {

    private static Stage primaryStage;
    private static Controller controller;
    private static File lastFile;

    @Override
    public void start(Stage stage) throws Exception {
        primaryStage = stage;
        FXMLLoader loader = new FXMLLoader(View.class.getResource("main.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        primaryStage.setTitle("Austin's Audio");
        primaryStage.setScene(scene);
        primaryStage.show();
        controller = loader.getController();
        Timeline updateTimer = new Timeline(new KeyFrame(Duration.millis(10), actionEvent -> controller.updateTime(FileManager.getFileManager().getCurrentTime())));
        updateTimer.setCycleCount(Timeline.INDEFINITE);
        updateTimer.play();
    }

    @Override
    public void stop() {
        FileManager.getFileManager().stop();
        LoopManager.getLoopManager().pause();
        System.exit(0);
    }

    public static File openFileChooser() {
        FileChooser fileChooser = new FileChooser();
        if (lastFile != null) {
            fileChooser.setInitialDirectory(lastFile.getParentFile());
        }
        fileChooser.setTitle("Open Audio File");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Audio Files", "*.wav"));
        lastFile = fileChooser.showOpenDialog(primaryStage);
        return  lastFile;
    }

    public static File openFileSaver() {
        FileChooser fileChooser = new FileChooser();
        if (lastFile != null) {
            fileChooser.setInitialDirectory(lastFile.getParentFile());
        }
        fileChooser.setTitle("Save Audio File");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Audio Files", "*.wav"));
        lastFile = fileChooser.showSaveDialog(primaryStage);
        return lastFile;
    }

    public static void main(String[] args){ launch(); }
}
