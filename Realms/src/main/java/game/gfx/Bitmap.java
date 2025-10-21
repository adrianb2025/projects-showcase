package game.gfx;

import game.gfx.util.BitmapHelper;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;

public class Bitmap {

    private final int w;
    private final int h;
    private int[] pixels;
    private BufferedImage image;

    public Bitmap(int w, int h) {
        this.w = w;
        this.h = h;
        image = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
        pixels = ((DataBufferInt)image.getRaster().getDataBuffer()).getData();
    }

    public static GraphicsConfiguration getDefaultConfig() {
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice gd = ge.getDefaultScreenDevice();
        return gd.getDefaultConfiguration();
    }

    public BufferedImage tilt(BufferedImage image, double angle, GraphicsConfiguration gc) {
        int t = image.getColorModel().getTransparency();
        BufferedImage result = gc.createCompatibleImage(w, h, t);
        Graphics2D g = result.createGraphics();
        g.rotate(angle, w / 2, h / 2);
        g.drawRenderedImage(image, null);
        return result;
    }

    public void rotate(double theta) {
        image = tilt(image, Math.toRadians(theta), getDefaultConfig());
        pixels = ((DataBufferInt)image.getRaster().getDataBuffer()).getData();
    }

    public void draw(int scale, double angle, Bitmap b, int xOffs, int yOffs, int xo, int yo, int w, int h, int cols, int bits) {
        Bitmap temp = new Bitmap(w, h);
        BitmapHelper.copy(b, xo, yo, 0, 0, w, h, temp);
        temp.rotate(angle);
        BitmapHelper.scaleDraw(temp, scale, xOffs, yOffs, 0, 0, w, h, cols, bits, this);
    }

    public void drawPixel(int xOffs, int yOffs, int col) { BitmapHelper.drawPixel(xOffs, yOffs, col, this); }
    public void drawLine(int x0, int y0, int x1, int y1, int col) { BitmapHelper.drawLine(x0, y0, x1, y1, col, this); }
    public void drawTriangle(int x0, int y0, int x1, int y1, int x2, int y2, int col) { BitmapHelper.drawTriangle(x0, y0, x1, y1, x2, y2, col, this); }
    public void draw(Bitmap src, int xOffs, int yOffs, int xo, int yo, int w, int h, int cols, int bits) { BitmapHelper.scaleDraw(src, 1, xOffs, yOffs, xo, yo, w, h, cols, bits, this); }
    public void draw(Bitmap src, int scale, int xOffs, int yOffs, int xo, int yo, int w, int h, int cols, int bits) { BitmapHelper.scaleDraw(src, scale, xOffs, yOffs, xo, yo, w, h, cols, bits, this); }

    public int getWidth() { return w; }
    public int getHeight() { return h; }
    public int[] getPixels() { return pixels; }
    public BufferedImage getImage() { return image; }

}