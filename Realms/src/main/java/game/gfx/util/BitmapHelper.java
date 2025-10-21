package game.gfx.util;

import game.gfx.Bitmap;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;

public class BitmapHelper {

    public static final int BIT_MIRROR_X = 0x01;
    public static final int BIT_MIRROR_Y = 0x02;

    public static void fill(Bitmap b, int col) {
        for (int i = 0; i < b.getWidth() * b.getHeight(); i++) {
            b.getPixels()[i] = col;
        }
    }

    public static final Bitmap loadTextureFromResources(String path) {
        try {
            BufferedImage image = ImageIO.read(BitmapHelper.class.getResource(path));

            int w = image.getWidth();
            int h = image.getHeight();

            Bitmap result = new Bitmap(w, h);
            image.getRGB(0, 0, w, h, result.getPixels(), 0, w);

            for (int i = 0; i < result.getPixels().length; i++) {
                result.getPixels()[i] = (result.getPixels()[i] & 0xFF) / 64;
            }

            return result;

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static final Bitmap loadBitmapFromResources(String path) {
        try {
            BufferedImage image = ImageIO.read(BitmapHelper.class.getResource(path));

            int w = image.getWidth();
            int h = image.getHeight();

            Bitmap result = new Bitmap(w, h);
            image.getRGB(0, 0, w, h, result.getPixels(), 0, w);

            return result;

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void copy(Bitmap src, int xOffs, int yOffs, int xo, int yo, int w, int h, Bitmap dst) {
        xo = Math.max(0, Math.min(xo, dst.getWidth() - 1));
        yo = Math.max(0, Math.min(yo, dst.getHeight() - 1));
        w = Math.min(dst.getWidth() - 1, xo + w);
        h = Math.min(dst.getHeight() - 1, yo + h);
        xOffs = Math.min(src.getWidth() - w, xOffs - xo);
        yOffs = Math.min(src.getHeight() - h, yOffs - yo);
        for (int y = yo; y < h; y++) {
            for (int x = xo; x < w; x++) {
                dst.getPixels()[x + y * dst.getWidth()] = src.getPixels()[xOffs + x + (yOffs + y) * src.getWidth()];
            }
        }
    }

    public static void copy(Bitmap src, int xOffs, int yOffs, int xo, int yo, int w, int h, Bitmap dst, int alpha) {
        xo = Math.max(0, Math.min(xo, dst.getWidth() - 1));
        yo = Math.max(0, Math.min(yo, dst.getHeight() - 1));
        w = Math.min(dst.getWidth() - 1, xo + w);
        h = Math.min(dst.getHeight() - 1, yo + h);
        xOffs = Math.min(src.getWidth() - w, xOffs - xo);
        yOffs = Math.min(src.getHeight() - h, yOffs - yo);
        for (int y = yo; y < h; y++) {
            for (int x = xo; x < w; x++) {
                int col = src.getPixels()[xOffs + x + (yOffs + y) * src.getWidth()];
                if (col == alpha) continue;
                dst.getPixels()[x + y * dst.getWidth()] = col;
            }
        }
    }

    public static void drawAura(Bitmap b, int alpha, int col) {
        int w = b.getWidth();
        int h = b.getHeight();

        for (int x = 1; x < w - 1; x++) {
            for (int y = 1; y < h - 1; y++) {
                if (b.getPixels()[x + y * w] != alpha && b.getPixels()[x + y * w] != col) {
                    if (b.getPixels()[x + (y - 1) * w] == alpha) b.getPixels()[x + (y - 1) * w] = col;
                    if (b.getPixels()[(x - 1) + y * w] == alpha) b.getPixels()[(x - 1) + y * w] = col;
                    if (b.getPixels()[x + (y + 1) * w] == alpha) b.getPixels()[x + (y + 1) * w] = col;
                    if (b.getPixels()[(x + 1) + y * w] == alpha) b.getPixels()[(x + 1) + y * w] = col;
                }
            }
        }
    }

    public static void drawShadow(Bitmap b, int alpha, int col) {
        int w = b.getWidth();
        int h = b.getHeight();

        for (int x = 0; x < w - 1; x++) {
            for (int y = 0; y < h - 1; y++) {
                if (b.getPixels()[x + y * w] != alpha && b.getPixels()[x + y * w] != col) {
                    if (b.getPixels()[x + 1 + (y + 1) * w] == alpha) b.getPixels()[x + 1 + (y + 1) * w] = col;
                }
            }
        }
    }

    public static void drawPixel(int xOffs, int yOffs, int col, Bitmap dst) {
        if (xOffs >= 0 && yOffs >= 0 && xOffs < dst.getWidth() - 1 && yOffs < dst.getHeight() - 1) {
            dst.getPixels()[xOffs + yOffs * dst.getWidth()] = col;
        }
    }

    public static void drawTriangle(int x0, int y0, int x1, int y1, int x2, int y2, int col, Bitmap dst) {
        Polygon polygon = new Polygon(new int[] { x0, x1, x2 }, new int[] { y0, y1, y2 }, 3);
        Graphics2D g = dst.getImage().createGraphics();
        g.setColor(new Color(col));
        g.fillPolygon(polygon);
    }

    public static void drawLine(int x0, int y0, int x1, int y1, int col, Bitmap dst) {
        Graphics2D g = dst.getImage().createGraphics();
        g.setColor(new Color(col));
        g.drawLine(x0, y0, x1, y1);
    }

    public static void drawHalfTile(Bitmap src, int xOffs, int yOffs, int xo, int yo, int cols, int bits, Bitmap dst) {
        scaleDraw(src, 1, xOffs, yOffs, xo, yo, 8, 8, cols, bits, dst);
    }

    public static void scaleDraw(Bitmap src, int scale, int xOffs, int yOffs, int xo, int yo, int w, int h, int colors, int bits, Bitmap dst) {
        boolean mirrorX = (bits & BIT_MIRROR_X) > 0;
        boolean mirrorY = (bits & BIT_MIRROR_Y) > 0;

        for (int y = 0; y < h * scale; y++) {
            int yp = y + yOffs;
            if (yp < 0 || yp >= dst.getHeight()) continue;
            int ys = y;
            if (mirrorY) ys = (h * scale - 1) - y;

            for (int x = 0; x < w * scale; x++) {
                int xp = x + xOffs;
                if (xp < 0 || xp >= dst.getWidth()) continue;

                int xs = x;
                if (mirrorX) xs = (w * scale - 1) - x;

                int color = (colors >> (src.getPixels()[(xs / scale + xo) + (ys / scale + yo) * src.getWidth()] * 8)) & 255;
                if (color < 255) {
                    dst.getPixels()[xp + yp * dst.getWidth()] = color;
                }
            }
        }
    }

    public static void drawNormal(Bitmap src, int xOffs, int yOffs, Bitmap dst, int alpha) {
        for (int y = 0; y < src.getHeight(); y++) {
            int yPix = y + yOffs;
            if (yPix < 0 || yPix >= dst.getHeight()) continue;

            for (int x = 0; x < src.getWidth(); x++) {
                int xPix = x + xOffs;
                if (xPix < 0 || xPix >= dst.getWidth()) continue;

                int color = src.getPixels()[x + y * src.getWidth()];

                if (color == alpha) continue;
                dst.getPixels()[xPix + yPix * dst.getWidth()] = color;
            }
        }
    }

}