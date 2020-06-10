package austinaudio;

public class Utils {
    static double lerp(double from, double to, double current) {
        return from + current * (to - from);
    }

    static double invlerp(double from, double to, double current) {
        return (current - from) / (to - from);
    }

    static double clamp(double min, double max, double value) {
        if (value < min) {
            return min;
        } else return Math.min(value, max);
    }
}
