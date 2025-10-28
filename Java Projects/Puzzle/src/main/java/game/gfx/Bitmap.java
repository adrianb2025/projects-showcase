package game.gfx;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;

public class Bitmap {

    public final int w;
    public final int h;
    public int[] pixels;

    public Bitmap(int w, int h) {
        this.w = w;
        this.h = h;
        pixels = new int[w * h];
    }

    public Bitmap(int w, int h, int[] pixels) {
        this.w = w;
        this.h = h;
        this.pixels = pixels;
    }

    public Bitmap(BufferedImage image) {
        w = image.getWidth();
        h = image.getHeight();
        pixels = ((DataBufferInt)image.getRaster().getDataBuffer()).getData();
    }

    public void draw(Bitmap b, int xp, int yp) {
        int x0 = xp;
        int y0 = yp;
        int x1 = xp + b.w;
        int y1 = yp + b.h;

        if (x0 < 0) x0 = 0;
        if (y0 < 0) y0 = 0;
        if (x1 > w) x1 = w;
        if (y1 > h) y1 = h;

        for (int y = y0; y < y1; y++) {
            int sp = (y - y0) * b.w;
            int dp = y * w;
            for (int x = x0; x < x1; x++) {
                int c = b.pixels[sp++];
                if (c >= 0) continue;
                pixels[dp + x] = c;
            }
        }
    }

    public void draw(Bitmap b, int xp, int yp, int xOffs, int yOffs, int ww, int hh) {
        int x0 = xp;
        int y0 = yp;
        int x1 = xp + ww;
        int y1 = yp + hh;

        if (x0 < 0) x0 = 0;
        if (y0 < 0) y0 = 0;
        if (x1 > w) x1 = w;
        if (y1 > h) y1 = h;

        for (int y = y0; y < y1; y++) {
            int sp = (y - y0 + yOffs) * b.w + xOffs;
            int dp = y * w;
            for (int x = x0; x < x1; x++) {
                int c = b.pixels[sp++];
                if (c >= 0) continue;
                pixels[dp + x] = c;
            }
        }

    }

    public void setPixel(int xp, int yp, int col) {
        if (xp < 0 || yp < 0 || xp >= w || yp >= h) return;
        pixels[xp + yp * w] = 0xFF000000 | col;
    }

}