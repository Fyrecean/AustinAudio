import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;

public class Player extends Thread {
    private Clip line;
    private Player thread;

    public Player() { }

    private void setClip(Clip line) {
        this.line = line;
    }

    public void play(File file) {
        thread = new Player();
        if (!thread.isAlive()) {
            Mixer device = getDefaultDevice();
            if (device == null) {
                System.err.println("Device not found");
                return;
            }
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
                line = (Clip) device.getLine(info);
                line.open(stream);
            } catch (IOException | LineUnavailableException e) {
                e.printStackTrace();
                return;
            }
            thread.setClip(line);
            thread.start();
        }
    }

    public void pause() {
        if (thread.isAlive()) {
            thread.interrupt();
        }
    }

    public void run() {
        line.start();
        try {
            Thread.sleep(20);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        do {
            Thread.yield();
        } while (line.isActive() && !isInterrupted());
        line.stop();
        line.close();
    }

    private Mixer getDefaultDevice() {
        for (Mixer.Info info : AudioSystem.getMixerInfo()) {
            if (info.getName().equals("Headset Earphone (Razer Kraken 7.1 V2)")) {
                Mixer mixer = AudioSystem.getMixer(info);
                System.out.println(info);
                System.out.println(Arrays.toString(mixer.getSourceLineInfo()));
                return mixer;
            }
        }
        return null;
    }
}
