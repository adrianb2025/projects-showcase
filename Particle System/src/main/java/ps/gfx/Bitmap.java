package ps.gfx;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.util.List;

public class Bitmap {

    public final int w;
    public final int h;
    public int[] pixels;
    private BufferedImage image;

    public Bitmap(int w, int h) {
        this.w = w;
        this.h = h;
        image = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
        pixels = ((DataBufferInt)image.getRaster().getDataBuffer()).getData();
    }

    public void setPixel(int x, int y, int col) {
        if (x < 0 || y < 0 || x >= w || y >= h) return;
        pixels[x + y * w] = col & 0xFFFFFF;
    }

    public int getPixel(int x, int y) {
        if (x >= 0 && y >= 0 && x < w && y < h) return pixels[x + y * w];
        return 0;
    }

    public void smooth(int iterations) {
        iterations = Math.max(1, iterations);
        for (int i = 0; i < iterations; i++) {
            for (int y = 1; y < h - 1; y++) {
                for (int x = 1; x < w - 1; x++) {
                    List<Pixel> pc = PixelCache.getPixels();

                    pc.add(new Pixel(pixels[x + y * w]));

                    pc.add(new Pixel(pixels[(x - 1) + y * w]));
                    pc.add(new Pixel(pixels[(x + 1) + y * w]));
                    pc.add(new Pixel(pixels[x + (y - 1) * w]));
                    pc.add(new Pixel(pixels[x + (y + 1) * w]));

                    pc.add(new Pixel(pixels[(x - 1) + (y - 1) * w]));
                    pc.add(new Pixel(pixels[(x - 1) + (y + 1) * w]));
                    pc.add(new Pixel(pixels[(x + 1) + (y - 1) * w]));
                    pc.add(new Pixel(pixels[(x + 1) + (y + 1) * w]));

                    pixels[x + y * w] = Pixel.getAvgPixels(pc).getColor();
                }
            }
        }
    }

    public BufferedImage getImage() { return image; }

}
