import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.File;

public class LoopManager  {
    private static LoopManager loopManager = new LoopManager();
    protected ObservableList<Track> tracks = FXCollections.observableArrayList();
    private int playingIndex = 0;
    private int selectedIndex = 0;
    private static Controller controller;

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

    public void removeTrack() {
        tracks.remove(selectedIndex);
    }

    public void play() {
        tracks.get(playingIndex).play();
    }

    public void pause() {
        for (Track t : tracks) {
            t.pause();
        }
    }

    public void nextTrack() {
        tracks.get(playingIndex).stopFade();
        playingIndex++;
        tracks.get(playingIndex).play();
    }

    public void selectTrack(int index) {
        selectedIndex = index;
    }

    public void moveTrack(int selected, int destination) {
        Track hold = tracks.get(selected);
        int dir = (selected < destination ? 1 : -1);
        for (int i = selected; i != destination; i += dir) {
            tracks.set(i, tracks.get(i - dir));
        }
        tracks.set(destination, hold);
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