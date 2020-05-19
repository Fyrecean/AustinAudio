import java.io.File;

public class Testing {
    public static void main(String[] args) {
        FileManager fm = FileManager.getFileManager();
        fm.setBounds(0, 10_000, 2_000);
        fm.loadFile(new File("res/Intruxx.wav"));
        fm.saveLoop("writeTest");
        //fm.loadFile(new File("res/writeTest.wav"));
        //fm.setLooping(true);
        LoopManager.getLoopManager().play();
    }
}