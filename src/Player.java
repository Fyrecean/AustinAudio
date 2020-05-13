import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

public class Player extends Thread {
    private Clip line;
    private static Mixer mixer;
    private int start, end;
    private int fadeLength;


    public Player() {
        if (mixer == null)
            mixer = getDefaultDevice();
    }

    public void makeClip(File file, int start, int end, int fadeLength) {
        this.end = end;
        this.start = start;
        this.fadeLength = fadeLength;
        AudioFormat format;
        AudioInputStream stream;
        try {
            format = AudioSystem.getAudioFileFormat(file).getFormat();
            stream = AudioSystem.getAudioInputStream(file);
        } catch (UnsupportedAudioFileException | IOException e) {
            e.printStackTrace();
            return;
        }
        Clip.Info info = new DataLine.Info(Clip.class, format);

        if (!AudioSystem.isLineSupported(info)) {
            System.err.println("Line Not supported");
            return;
        }
        try {
            line = (Clip) mixer.getLine(info);
            line.open(stream);
            line.setMicrosecondPosition(start * 1_000_000);
        } catch (IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }

    public void play() {
        if (!isAlive()) {
            start();
        } else {
            if (line.isOpen() && !line.isRunning()) {
                rewind();
                setVolume(0);
                line.start();
                double fadeIncrement = (double)fadeLength / 100;
                double currentFade = 0;
                try {
                    while (currentFade < fadeLength) {
                        currentFade += fadeIncrement;
                        setVolume(Utils.invlerp(0, fadeLength, currentFade));
                        Thread.sleep(100);
                    }
                    setVolume(1);
                    long endTime = end > 0 ? (end - fadeLength) * 1_000_000 : line.getMicrosecondLength() - (1_000_000 * fadeLength);
                    while (line.isActive()) {
                        if (line.getMicrosecondPosition() >= endTime) {
                            FileManager.getFileManager().triggerLoop();
                            currentFade = fadeLength;
                            while (currentFade > 0) {
                                currentFade -= fadeIncrement;
                                setVolume(1 - Utils.invlerp(fadeLength, 0, currentFade));
                                Thread.sleep(100);
                            }
                            setVolume(0);
                            break;
                        }
                    }
                } catch (InterruptedException e) {
                    interrupt();
                    return;
                }
                setVolume(0);
                line.stop();
                rewind();
            }
        }
    }

    public void pause() {
        if (isAlive()) {
            line.stop();
        }
    }

    public void kill() {
       if (isAlive()) {
           interrupt();
       }
    }

    public void run() {
        while (!isInterrupted()) {
            play();
            System.err.println("This is happening");
        }
    }

    private Mixer getDefaultDevice() {
        for (Mixer.Info info : AudioSystem.getMixerInfo()) {
            if (info.getName().equals("Headset Earphone (Razer Kraken 7.1 V2)")) {
                return AudioSystem.getMixer(info);
            }
        }
        return null;
    }

    public void setVolume(double value) {
        FloatControl gainControl = (FloatControl) line.getControl(FloatControl.Type.MASTER_GAIN);
        value = (value <= 0.0) ? .0001 : Math.min(value, 1.0);
        float dB = (float)Utils.clamp(-80, 6, (Math.log(value)/Math.log(10)*20.0));
        gainControl.setValue(dB);
    }

    public void rewind() {
        pause();
        line.setMicrosecondPosition(this.start * 1_000_000);
    }
}
