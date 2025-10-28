package game.gfx;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class Art {

    public static final Bitmap[][] tiles = loadCutAndScale("/tiles.png", 24, 4);
    public static final Bitmap[][] chars = loadCutAndScale("/chars.png", 16, 4);
    public static final Bitmap[][] particles = loadCutAndScale("/particles.png", 8, 4);
    public static final Bitmap[][] cursors = loadAndCut("/cursors.png", 16);

    public static Bitmap load(String path) {
        BufferedImage image;
        try {
            image = ImageIO.read(Art.class.getResourceAsStream(path));
        } catch (IOException e) {
            throw new RuntimeException("Failed to load " + path);
        }

        BufferedImage result = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics g = result.getGraphics();
        g.drawImage(image, 0, 0, null);
        g.dispose();
        return new Bitmap(result);
    }

    public static final Bitmap[][] loadAndCut(String path, int size) { return loadCutAndScale(path, size, 1); }

    public static final Bitmap[][] loadCutAndScale(String path, int size, int scale) {
        BufferedImage sheet;
        try {
            sheet = ImageIO.read(Art.class.getResourceAsStream(path));
        } catch (IOException e) {
            throw new RuntimeException("Failed to load " + path);
        }

        int sw = sheet.getWidth() / size;
        int sh = sheet.getHeight() / size;
        Bitmap[][] result = new Bitmap[sw][sh];
        for (int y = 0; y < sh; y++) {
            for (int x = 0; x < sw; x++) {
                BufferedImage image = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
                Graphics g = image.getGraphics();
                g.drawImage(sheet, -x * size, -y * size, null);
                g.dispose();
                result[x][y] = DarkenArt.darken(new Bitmap(Art.scale(image, scale)));
            }
        }

        return result;
    }

    public static BufferedImage scale(BufferedImage src, int scale) {
        if (scale <= 1) return src;

        int w = src.getWidth() * scale;
        int h = src.getHeight() * scale;
        BufferedImage result = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        Graphics g = result.getGraphics();
        g.drawImage(src.getScaledInstance(w, h, 16), 0, 0, null);
        g.dispose();
        return result;
    }

}