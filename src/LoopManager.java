import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class LoopManager  {
    private static LoopManager loopManager = new LoopManager();
    private List<Track> tracks = new ArrayList<>();
    private int playingIndex = 0;
    private int selectedIndex = 0;
    private boolean playing;
    private Track undoHold = null;

    private LoopManager() { }

    public static LoopManager getLoopManager() {
        return loopManager;
    }

    public void addTrack(File file, int startTime, int endTime, int fadeDuration) {
        tracks.add(new Track(file, fadeDuration));
    }

    public void removeTrack() {
        undoHold = tracks.remove(selectedIndex);
    }

    public void restoreTrack() {
        tracks.add(undoHold);
    }

    public void play() {
        tracks.get(playingIndex).play();
    }

    public void pause() {
        tracks.get(playingIndex).pause();
        tracks.get(playingIndex + 1).pause();
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
            trackNames[i++] = t.getName();
        }
        return trackNames;
    }

    public boolean isPlaying() {
        return playing;
    }
}