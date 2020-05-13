import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import java.io.File;

public class MainController {
    private FileManager fileManager = FileManager.getFileManager();

    @FXML private Button playButton;
    @FXML private Label filePathLabel;

    @FXML
    public void playButtonListener(ActionEvent event) {
        if (FileManager.getFileManager().isReady()) {
            fileManager.play();
            switchPlayButton(playMode.PAUSE);
        }
    }

    @FXML
    public void pauseButtonListener(ActionEvent event) {
        fileManager.pause();
        switchPlayButton(playMode.PLAY);
    }

    @FXML
    public void openButtonListener() {
        File file = View.openFileChooser();
        if (file == null) {
            return;
        }
        filePathLabel.setText(file.getAbsolutePath());
        fileManager.loadFile(file);
        fileManager.stop();
        switchPlayButton(playMode.PLAY);
    }

    private enum playMode { PLAY, PAUSE }
    private void switchPlayButton(playMode mode) {
        switch(mode) {
            case PLAY:
                playButton.setText("Play");
                playButton.setOnAction(this::playButtonListener);
                break;
            case PAUSE:
                playButton.setText("Pause");
                playButton.setOnAction(this::pauseButtonListener);
        }
    }
}
