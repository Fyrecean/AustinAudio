import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;

public class Testing {
    public static void main(String[] args) throws InterruptedException {
        Mixer device = getDefaultDevice();
        Clip line1 = play("res/Phobia.wav", device);
        if (line1 == null) {
            return;
        }
        Clip line2 = play("res/Intruxx.wav", device);
        if (line2 == null) {
            return;
        }
        FloatControl gain1 = (FloatControl) line1.getControl(FloatControl.Type.MASTER_GAIN);
        FloatControl gain2 = (FloatControl) line2.getControl(FloatControl.Type.MASTER_GAIN);
        gain1.setValue(6f);
        gain2.setValue(-20f);
        do {
            Thread.sleep(50);
            float gain1v = gain1.getValue() - .01f;
            float gain2v = gain2.getValue() + .02f;
            gain1.setValue(gain1v);
            gain2.setValue(gain2v);
        } while (line1.isActive() && line2.isActive());
    }

    public static Mixer getDefaultDevice() {
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

    public static Clip play(String path, Mixer device) {
        AudioFormat format;
        AudioInputStream stream;
        try {
            File audioFile = new File(path);
            AudioFileFormat fileFormat = AudioSystem.getAudioFileFormat(audioFile);
            format = fileFormat.getFormat();
            stream = AudioSystem.getAudioInputStream(audioFile);
        } catch (IOException | UnsupportedAudioFileException e) {
            System.err.println("File not found");
            return null;
        }
        Clip.Info info = new DataLine.Info(Clip.class, format);

        if (!AudioSystem.isLineSupported(info)) {
            System.err.println("Line not supported");
            return null;
        }
        try {
            Clip line = (Clip) device.getLine(info);
            line.addLineListener(event -> {
                if (event.getType() == LineEvent.Type.STOP) {
                    System.out.println("Closing");
                    line.close();
                }
            });
            line.open(stream);
            line.start();
            return line;
        } catch (LineUnavailableException | IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}