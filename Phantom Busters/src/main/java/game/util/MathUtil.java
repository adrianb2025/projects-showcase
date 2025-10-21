package game.util;

public class MathUtil {

    public static double normalize(double angle) {
        while (angle < -Math.PI) angle += Math.PI * 2;
        while (angle > Math.PI) angle -= Math.PI * 2;
        return angle;
    }

}