import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Orientation;
import javafx.scene.control.*;

import java.io.File;

public class Controller {
    FileManager fileManager = FileManager.getFileManager(this);
    LoopManager loopManager = LoopManager.getLoopManager(this);

    @FXML private Button filePlayButton;
    @FXML private Button loopPlayButton;

    @FXML private Label filePathLabel;
    @FXML private Label currentTimeLabel;
    @FXML private TextField startTimeField;
    @FXML private TextField endTimeField;
    @FXML private TextField fadeDurationField;
    @FXML private CheckBox loopCheckBox;
    @FXML private ListView<Track> tracksListView;

    protected void populateListView() {
        tracksListView.setItems(loopManager.tracks);
    }

    @FXML
    public void setLoopCheckBoxListener() {
        fileManager.setLooping(loopCheckBox.isSelected());
    }

    @FXML
    public void filePlayButtonListener(ActionEvent event) {
        if (fileManager.play())
            switchPlayButton(playMode.PAUSE);
    }

    @FXML
    public void loopPlayButtonListener(ActionEvent event) {

    }

    @FXML
    public void pauseButtonListener(ActionEvent event) {
        if (fileManager.pause())
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
        switchPlayButton(playMode.PLAY);
    }

    @FXML
    public void enterTimesButtonListener() {
        int startTime = getMillisFromTimeString(startTimeField.getText());
        int endTime = getMillisFromTimeString(endTimeField.getText());
        int fadeDuration = getMillisFromTimeString(fadeDurationField.getText());
        fileManager.setBounds(startTime, endTime, fadeDuration);
        switchPlayButton(playMode.PLAY);
    }

    private int getMillisFromTimeString(String timeString) {
        String[] pieces = timeString.split(":");
        if (pieces.length != 3) {
            System.err.println("Bad input");
            return -1;
        }
        int millis = Integer.parseInt(pieces[0]) * 60_000;
        millis += Integer.parseInt(pieces[1]) * 1_000;
        millis += Integer.parseInt(pieces[2]);
        return millis;
    }

    @FXML
    public void saveTrackButtonListener() {
        File destinationFile = View.openFileSaver();
        fileManager.saveLoop(destinationFile);
    }

    private enum playMode { PLAY, PAUSE }
    private void switchPlayButton(playMode mode) {
        switch (mode) {
            case PLAY:
                filePlayButton.setText("Play");
                filePlayButton.setOnAction(this::filePlayButtonListener);
                break;
            case PAUSE:
                filePlayButton.setText("Pause");
                filePlayButton.setOnAction(this::pauseButtonListener);
        }
    }

    protected void updateTime(int currentTime) {
        int minutes = currentTime / 60_000;
        currentTime %= 60_000;
        int seconds = currentTime / 1_000;
        currentTime %= 1_000;
        String timeString = String.format("%02d:%02d:%03d", minutes, seconds, currentTime);
        currentTimeLabel.setText(timeString);
    }
}
