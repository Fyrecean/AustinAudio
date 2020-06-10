package austinaudio;

import java.io.File;
import java.io.Serializable;

public class Track extends CanLoop implements Serializable {
    private int fadeDuration;
    private File file;

    public Track(File file, int fadeDuration) {
        this.fadeDuration = fadeDuration;
        this.file = file;
        players[0] = new AudioPlayer(this, file, fadeDuration);
        players[0].start();
    }

    public boolean play() {
        if (players[0] == null || players[0].isInterrupted()) {
            players[0] = new AudioPlayer(this, file, fadeDuration);
            players[0].start();
        }
        players[0].play();
        return true;
    }

    public boolean pause() {
        boolean rv = false;
        for (AudioPlayer player : players) {
            if (player != null)
                player.interrupt();
                rv = true;
        }
        players[0] = null;
        players[1] = null;
        return rv;
    }

    public void stopFade() {
        if (players[0] != null)
            players[0].triggerFade();
        if (players[1] != null)
            players[1].interrupt();
    }

    public boolean isPlaying() {
        return (players[0] != null && players[0].isAlive());
    }

    public String toString() {
        return file.getName();
    }

    public void triggerLoop() {
        players[1] = new AudioPlayer(this, file, 0, -1, fadeDuration);
        super.triggerLoop();
    }
}
