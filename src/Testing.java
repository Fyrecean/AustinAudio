import java.io.File;

public class Testing {
    public static void main(String[] args) throws InterruptedException {
        FileManager fm = FileManager.getFileManager();
        fm.setBounds(15_000, 30_000, 5_000);
        fm.loadFile(new File("res/Intruxx.wav"));
        fm.saveLoop("intruxxTest");
        fm.loadFile(new File("res/Phobia.wav"));
        fm.saveLoop("phobiaTest");
        fm.stop();
    }
}