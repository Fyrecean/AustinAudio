package austinaudio;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.File;

public class LoopManager  {
    private static LoopManager loopManager = new LoopManager();
    protected ObservableList<Track> tracks = FXCollections.observableArrayList();
    private int playingIndex = 0;
    protected static Controller controller;

    private LoopManager() { }

    public static LoopManager getLoopManager() {
        return loopManager;
    }

    public static LoopManager getLoopManager(Controller controller) {
        LoopManager.controller = controller;
        return loopManager;
    }

    public void addTrack(File file, int fadeDuration) {
        tracks.add(new Track(file, fadeDuration));
        if (tracks.size() == 1) {
            controller.populateListView();
        }
    }

    public boolean play(int selected) {
        if (tracks.isEmpty()) {
            return false;
        }
        if (selected != -1) {
            playingIndex = selected;
        }
        pause();
        return tracks.get(playingIndex).play();
    }

    public boolean pause() {
        boolean rv = false;
        if (tracks.isEmpty()) {
            return false;
        }
        for (Track t : tracks) {
            rv |= t.pause();
        }
        return rv;
    }

    public void nextTrack() {
        if (tracks.isEmpty())
            return;
        Track currentlyPlaying = tracks.get(playingIndex);
        if (!currentlyPlaying.isPlaying())
            return;
        if (playingIndex + 1 >= tracks.size()) {
            currentlyPlaying.stopFade();
            playingIndex = 0;
            controller.switchLoopPlayButton(Controller.playMode.PLAY);
        } else {
            currentlyPlaying.stopFade();
            playingIndex++;
        }
        tracks.get(playingIndex).play();
    }

    public void removeTrack(int selected) {
        if (tracks.isEmpty() || selected < 0) {
            return;
        }
        tracks.remove(selected);
    }

    public void shiftTrack(int selected, int direction) {
        if (tracks.isEmpty() || selected + direction < 0 || selected + direction >= tracks.size()) {
            return;
        }
        Track hold = tracks.get(selected);
        tracks.set(selected, tracks.get(selected + direction));
        tracks.set(selected + direction, hold);
    }

    public String[] getFileNames() {
        String[] trackNames = new String[tracks.size()];
        int i = 0;
        for(Track t : tracks) {
            trackNames[i++] = t.toString();
        }
        return trackNames;
    }
}