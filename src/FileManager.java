import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

public class FileManager extends CanLoop {
    private static FileManager fileManager = new FileManager();;
    private int startTime, endTime, fadeDuration;
    private boolean isPlaying;
    private boolean looping;
    private File file;

    private FileManager() {
        startTime = 0;
        endTime = -1;
        fadeDuration = 2_500;
    }

    public static FileManager getFileManager() {
        return fileManager;
    }

    public void loadFile(File file) {
        stop();
        this.file = file;
        players[0] = new AudioPlayer(this, file, startTime, endTime, fadeDuration);
        players[0].start();
    }

    public boolean setPosition(int position) {
        if(position > startTime + fadeDuration && position < endTime - fadeDuration) {
            players[0].setPositionMillis(position);
            return true;
        }
        return false;
    }

    public boolean play() {
        if (players[0] != null) {
            players[0].play();
            isPlaying = true;
            return true;
        } else if (file != null) {
            players[0] = new AudioPlayer(this, file, startTime, endTime, fadeDuration);
        }
        return false;
    }

    public boolean pause() {
        if (isPlaying) {
            for (AudioPlayer player : players) {
                if (player != null)
                    player.pause();
            }
            isPlaying = false;
            return true;
        }
        return false;
    }

    public void setBounds(int newStartTime, int newEndTime, int newFadeDuration) {
        stop();
        this.startTime = newStartTime;
        this.endTime = newEndTime;
        this.fadeDuration = newFadeDuration;
        if (file != null)
            players[0] = new AudioPlayer(this, file, startTime, endTime, fadeDuration);
    }

    public void stop() {
        for (AudioPlayer player : players) {
            if (player != null && player.isAlive())
                player.interrupt();
        }
    }

    public void saveLoop(String name) {
        AudioInputStream inputStream = null;
        AudioInputStream newStream = null;
        try {
            AudioFileFormat fileFormat = AudioSystem.getAudioFileFormat(file);
            AudioFormat format = fileFormat.getFormat();
            inputStream = AudioSystem.getAudioInputStream(file);
            int frameRate = (int)(format.getFrameRate() / 1000);
            int bytesPerMilliSecond = format.getFrameSize() * frameRate;
            inputStream.skip(startTime * bytesPerMilliSecond);
            long framesOfAudioToCopy;
            if (endTime > 0) {
                framesOfAudioToCopy = (endTime - startTime) * frameRate;
            } else {
                framesOfAudioToCopy = inputStream.getFrameLength() - (startTime * frameRate);
            }
            newStream = new AudioInputStream(inputStream, format, framesOfAudioToCopy);
            File destinationFile = new File("res/" + name + ".wav");
            AudioSystem.write(newStream, fileFormat.getType(), destinationFile);
            LoopManager.getLoopManager().addTrack(destinationFile, fadeDuration);
        } catch (UnsupportedAudioFileException | IOException e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null) try { inputStream.close(); } catch (Exception ignore) {}
            if (newStream != null) try { newStream.close(); } catch (Exception ignore) {}
        }
    }

    public void setLooping(boolean looping) {
        this.looping = looping;
    }

    public void triggerLoop() {
        if (looping) {
            players[1] = new AudioPlayer(this, file, startTime, endTime, fadeDuration);
            super.triggerLoop();
        }
    }
}
