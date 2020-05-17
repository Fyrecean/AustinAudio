import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Mixer;
import java.io.File;

public class View extends Application {

    private static Stage primaryStage;

    @Override
    public void start(Stage stage) throws Exception {
        primaryStage = stage;
        FXMLLoader loader = new FXMLLoader(View.class.getResource("main.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        primaryStage.setTitle("Austin's Audio");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    @Override
    public void stop() {
        FileManager.getFileManager().stop();
    }

    public static File openFileChooser() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Audio File");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Audio Files", "*.wav"));
        return fileChooser.showOpenDialog(primaryStage);
    }

    public static Mixer getDefaultDevice() {
        for (Mixer.Info info : AudioSystem.getMixerInfo()) {
            if (info.getName().equals("Headset Earphone (Razer Kraken 7.1 V2)")) {
                return AudioSystem.getMixer(info);
            }
        }
        return null;
    }

    public static void main(String[] args){ launch(); }
}
