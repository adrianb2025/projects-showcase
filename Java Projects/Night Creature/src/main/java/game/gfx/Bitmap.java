package game.gfx;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;

public class Bitmap {

    public int w;
    public int h;
    public int[] pixels;
    public BufferedImage image;

    public Bitmap(int w, int h) {
        this.w = w;
        this.h = h;
        image = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        pixels = ((DataBufferInt) image.getRaster().getDataBuffer()).getData();
    }

}