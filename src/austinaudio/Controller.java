package austinaudio;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.io.File;

public class Controller {
    FileManager fileManager = FileManager.getFileManager(this);
    LoopManager loopManager = LoopManager.getLoopManager(this);

    @FXML public Button filePlayButton;
    @FXML public Button loopPlayButton;
    @FXML public Button deleteButton;
    @FXML public Button upButton;
    @FXML public Button downButton;

    @FXML public Label filePathLabel;
    @FXML public Label currentTimeLabel;
    @FXML public TextField startTimeField;
    @FXML public TextField endTimeField;
    @FXML public CheckBox loopCheckBox;
    @FXML public ListView<Track> tracksListView;

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
            switchFilePlayButton(playMode.PAUSE);
    }

    @FXML
    public void loopPlayButtonListener(ActionEvent event) {
        if (loopManager.play(tracksListView.getSelectionModel().getSelectedIndex()))
            switchLoopPlayButton(playMode.PAUSE);
    }

    @FXML
    public void filePauseButtonListener(ActionEvent event) {
        if (fileManager.pause())
            switchFilePlayButton(playMode.PLAY);
    }

    @FXML
    public void loopPauseButtonListener(ActionEvent event) {
        if (loopManager.pause()) {
            switchLoopPlayButton(playMode.PLAY);
        }
    }

    @FXML
    public void importButtonListener() {
        File file = View.openFileChooser();
        if (file != null) {
            loopManager.addTrack(file, 5_000);
        }
    }

    @FXML
    public void deleteButtonListener() {
        loopManager.removeTrack(tracksListView.getSelectionModel().getSelectedIndex());
    }

    @FXML
    public void upButtonListener() {
        loopManager.shiftTrack(tracksListView.getSelectionModel().getSelectedIndex(), -1);
    }

    @FXML
    public void downButtonListener() {
        loopManager.shiftTrack(tracksListView.getSelectionModel().getSelectedIndex(), 1);
    }

    @FXML
    public void nextButtonListener() {
        loopManager.nextTrack();
    }


    @FXML
    public void openButtonListener() {
        File file = View.openFileChooser();
        if (file == null) {
            return;
        }
        filePathLabel.setText(file.getAbsolutePath());
        fileManager.loadFile(file);
        switchFilePlayButton(playMode.PLAY);
    }

    @FXML
    public void enterTimesButtonListener() {
        int startTime = getMillisFromTimeString(startTimeField.getText());
        int endTime = getMillisFromTimeString(endTimeField.getText());
        fileManager.setBounds(startTime, endTime, 5_000);
        switchFilePlayButton(playMode.PLAY);
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
        if (destinationFile != null) {
            fileManager.saveLoop(destinationFile);
        }
    }

    protected enum playMode { PLAY, PAUSE }
    private void switchFilePlayButton(playMode mode) {
        switch (mode) {
            case PLAY:
                filePlayButton.setText("Play");
                filePlayButton.setOnAction(this::filePlayButtonListener);
                break;
            case PAUSE:
                filePlayButton.setText("Pause");
                filePlayButton.setOnAction(this::filePauseButtonListener);
        }
    }

    protected void switchLoopPlayButton(playMode mode) {
        switch (mode) {
            case PLAY:
                loopPlayButton.setText("Play");
                loopPlayButton.setOnAction(this::loopPlayButtonListener);
                deleteButton.setDisable(false);
                upButton.setDisable(false);
                downButton.setDisable(false);
                break;
            case PAUSE:
                loopPlayButton.setText("Pause");
                loopPlayButton.setOnAction(this::loopPauseButtonListener);
                deleteButton.setDisable(true);
                upButton.setDisable(true);
                downButton.setDisable(true);
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
