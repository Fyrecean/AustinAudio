import java.io.File;

public class FileManager {
    private static FileManager fileManager = new FileManager();
    private Player[] players = new Player[2];
    private Loop loop;
    private boolean isPlaying;

    private FileManager() {
    }

    public static FileManager getFileManager() {
        return fileManager;
    }

    public void loadFile(File file) {
        if (isPlaying) {
            players[0].kill();
            players[1].kill();
        }
        players[0] = new Player();
        players[1] = new Player();
        players[0].makeClip(file, 0, 6, 2);
        players[1].makeClip(file, 0, 6, 2);
    }

    public void play() {
        if (players[0] != null) {
            players[0].play();
            isPlaying = true;
        }
    }

    public void pause() {
        if (isPlaying) {
            players[0].pause();
            players[1].pause();
            isPlaying = false;
        }
    }

    public void stop() {
        for (Player player : players) {
            if (player != null)
                player.kill();
        }
    }

    public void triggerLoop() {
        players[1].play();
        Player hold = players[0];
        players[0] = players[1];
        players[1] = hold;
    }

    public boolean isReady() {
        return players[0] != null;
    }
}
