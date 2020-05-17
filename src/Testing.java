import java.io.File;

public class Testing {
    public static void main(String[] args) {
        FileManager fm = FileManager.getFileManager();
        fm.setEndTime(118_000);
        fm.setStartTime(110_000);
        fm.setFadeDuration(2_000);
        fm.loadFile(new File("res/Intruxx.wav"));
        fm.saveLoop("writeTest");
        fm.setStartTime(0);
        fm.setEndTime(-1);
        fm.loadFile(new File("res/writeTest.wav"));
        fm.play();
    }
}