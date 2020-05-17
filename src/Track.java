import java.io.File;
import java.io.Serializable;

public class Track extends CanLoop implements Serializable {
    private int fadeDuration;
    private File file;

    public Track(File file, int fadeDuration) {
        this.fadeDuration = fadeDuration;
        this.file = file;
        players[0] = new AudioPlayer(this, file, fadeDuration);
    }

    public void play() {
        players[0].play();
    }

    public void pause() {
        for (AudioPlayer player : players) {
            if (player != null)
                player.pause();
        }
    }

    public void stopFade() {
        if (players[0] != null)
            players[0].triggerFade();
        if (players[1] != null)
            players[1].interrupt();
    }

    public String getName() {
        return file.getName();
    }

    public void triggerLoop() {
        players[1] = new AudioPlayer(this, file, 0, -1, fadeDuration);
        super.triggerLoop();
    }
}
