import javax.sound.sampled.Clip;
import java.io.File;

public class FileManager {
    private static FileManager fileManager = new FileManager();
    private File audioFile;
    private Player player;
    private Loop loop;
    private boolean isPlaying;

    private FileManager() {
        player = new Player();
    }

    public static FileManager getFileManager() {
        return fileManager;
    }

    public File load(String path) {
        if (!isPlaying) {
            audioFile = new File(path);
            return audioFile;
        } else {
            return null;
        }
    }

    public void play() {
        player.play(audioFile);
        isPlaying = true;
    }

    public void pause() throws InterruptedException {
        player.pause();
        isPlaying = false;
    }
}
