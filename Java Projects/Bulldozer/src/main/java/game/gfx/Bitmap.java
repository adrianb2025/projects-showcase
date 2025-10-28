package game.gfx;

import java.awt.image.BufferedImage;
import java.awt.image.DataBuffer;
import java.awt.image.DataBufferInt;

public class Bitmap {

    public int w;
    public int h;
    public int[] pixels;

    public Bitmap(BufferedImage image) {
        w = image.getWidth();
        h = image.getHeight();
        pixels = ((DataBufferInt)image.getRaster().getDataBuffer()).getData();
    }

}