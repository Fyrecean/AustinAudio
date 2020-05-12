import java.io.Serializable;
import java.nio.file.Path;

public class Loop implements Serializable {
    private String name;
    private Path file;
    private Loop next;
    private Loop prev;
    private int start;
    private int end;

    public Loop(String name, Path file, int start, int end) {
        this.name = name;
        this.file = file;
        this.start = start;
        this.end = end;
    }

    protected void setNext(Loop newNext) {
        next = newNext;
    }

    protected void setPrev(Loop newPrev) {
        prev = newPrev;
    }

    public String getName() {
        return name;
    }

    protected Path getFileURL() {
        return file;
    }

    protected Loop getNext() {
        return next;
    }

    protected Loop getPrev() {
        return prev;
    }

    protected int getStart() {
        return start;
    }

    protected int getEnd() {
        return end;
    }

    public int getDuration() {
        return end - start;
    }
}
