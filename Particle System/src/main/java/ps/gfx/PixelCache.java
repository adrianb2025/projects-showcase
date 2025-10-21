package ps.gfx;

import java.util.ArrayList;
import java.util.List;

public class PixelCache {

    private static final List<Pixel> pixels = new ArrayList<Pixel>();

    public static List<Pixel> getPixels() {
        pixels.clear();
        return pixels;
    }

}