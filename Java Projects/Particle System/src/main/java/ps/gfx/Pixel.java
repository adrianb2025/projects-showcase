package ps.gfx;

import java.util.List;

public class Pixel {

    public int r;
    public int g;
    public int b;

    public Pixel(int r, int g, int b) {
        this.r = r;
        this.g = g;
        this.b = b;
    }

    public Pixel(int col) {
        r = (col >> 16) & 0xFF;
        g = (col >> 8) & 0xFF;
        b = col & 0xFF;
    }

    public int getColor() {
        return ((r & 0xFF) << 16) | ((g & 0xFF) << 8) | (b & 0xFF);
    }

    public static Pixel getAvgPixels(List<Pixel> pixels) {
        long rr = 0;
        long gg = 0;
        long bb = 0;

        for (Pixel pixel : pixels) {
            rr += pixel.r;
            gg += pixel.g;
            bb += pixel.b;
        }

        return new Pixel((int)rr / pixels.size(), (int)gg / pixels.size(), (int)bb / pixels.size());
    }

}