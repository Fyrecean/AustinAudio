import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import java.io.File;

public class MainController {
    private FileManager fileManager = FileManager.getFileManager();

    @FXML private Button playButton;
    @FXML private Button pauseButton;
    @FXML private TextField pathTextField;

    @FXML
    public void playButtonListener(ActionEvent event) {
        File file = fileManager.load(pathTextField.getText());
        if (file != null) {
            fileManager.play();
        }
    }

    @FXML
    public void pauseButtonListener(ActionEvent event) throws InterruptedException {
        fileManager.pause();
    }
}
