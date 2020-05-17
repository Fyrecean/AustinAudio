import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

public class AudioPlayer extends Thread {
    private CanLoop caller;
    private Clip line;
    private static Mixer mixer;
    private int start, end;
    private int fadeLength;
    private boolean fadingOut;
    private boolean startFadeout;
    private boolean playing;
    private boolean started;

    private final int timeConversion = 1_000;

    public AudioPlayer(CanLoop caller, File file, int start, int end, int fadeDuration) {
        this.caller = caller;
        this.end = end;
        this.start = start;
        this.fadeLength = fadeDuration;
        if (mixer == null)
            setMixer("Headset Earphone (Razer Kraken 7.1 V2)"); //TODO: Allow for changing audio device
        makeClip(file);
    }

    public AudioPlayer(CanLoop caller, File file, int fadeDuration) {
        this(caller, file, 0, -1, fadeDuration);
    }

    public void makeClip(File file) {
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
            line.setMicrosecondPosition(start * timeConversion);
        } catch (IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }

    public void run() {
        while (!started) {
            Thread.yield();
        }
        setVolume(0);
        play();
        fadeIn(fadeLength);
        setVolume(1);
        long endTime;
        if (end > 0) {
            endTime = (end - fadeLength) * timeConversion;
        } else {
            endTime = line.getMicrosecondLength() - (timeConversion * fadeLength);
        }
        while (line.isOpen() && !isInterrupted()) {
            if (startFadeout) {
                fadeOut(fadeLength);
                setVolume(0);
                break;
            }
            if (line.getMicrosecondPosition() >= endTime) {
                caller.triggerLoop();
                fadeOut(fadeLength);
                setVolume(0);
                break;
            }
        }
        line.stop();
        line.close();
    }

    private void fadeIn(int duration) {
        long fadeIncrement = duration / 10;
        double currentFade = 0;
        while (currentFade < duration && !fadingOut && !isInterrupted()) {
            currentFade += fadeIncrement;
            double volume = Utils.invlerp(0, fadeLength, currentFade);
            setVolume(volume);
            try {
                Thread.sleep(fadeIncrement);
            } catch (InterruptedException ex) {
                interrupt();
            }
        }
    }

    private void fadeOut(int duration) {
        if (!fadingOut) {
            fadingOut = true;
            double startingGain = getVolume01();
            long fadeIncrement = duration / 10;
            double currentFade = duration;
            while (currentFade > 0 && !isInterrupted()) {
                currentFade -= fadeIncrement;
                setVolume((1 - Utils.invlerp(fadeLength, 0, currentFade)) * startingGain);
                try {
                    Thread.sleep(fadeIncrement);
                } catch (InterruptedException ex) {
                    interrupt();
                }
            }
            fadingOut = false;
        }
    }

    public void triggerFade() {
        startFadeout = true;
    }

    public void play() {
        if (!started) {
            started = true;
        } else {
            if (!playing) {
                line.start();
                playing = true;
            }
        }
    }

    public void pause() {
        if (playing) {
            line.stop();
            playing = false;
        }
    }

    public void setVolume(double value) {
        FloatControl gainControl = (FloatControl) line.getControl(FloatControl.Type.MASTER_GAIN);
        value = (value <= 0.0) ? .0001 : Math.min(value, 1.0);
        float dB = (float)Utils.clamp(-80, 6, (Math.log(value)/Math.log(10)*20.0));
        gainControl.setValue(dB);
    }

    public double getVolume01() {
        float startingGain = ((FloatControl)line.getControl(FloatControl.Type.MASTER_GAIN)).getValue();
        return Math.pow(10.0, startingGain/20);
    }

    public static void setMixer(String deviceName) {
        for (Mixer.Info info : AudioSystem.getMixerInfo()) {
            if (info.getName().equals(deviceName)) {
                mixer = AudioSystem.getMixer(info);
            }
        }
    }
}