package game.gfx;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class Art {

    public static final BufferedImage[] sprites = loadAndCut("/sprites.png", 16);
    public static final BufferedImage[] particles = loadAndCut("/particles.png", 8);
    public static final BufferedImage[] font = loadAndCut("/font.png", 8);
    public static final BufferedImage logo = load("/logo.png");
    private static final String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ      0123456789.,!?'\"-+=/\\%()<>:;    abcdefghijklmnopqrstuvwxyz      ";

    public static final BufferedImage load(String path) {
        BufferedImage image = null;
        try {
            image = ImageIO.read(Art.class.getResourceAsStream(path));
        } catch (IOException e) {
            throw new RuntimeException("Failed to load " + path);
        }

        BufferedImage result = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = result.createGraphics();
        g.drawImage(image, 0, 0, null);
        g.dispose();
        return result;
    }

    public static final BufferedImage[] loadAndCut(String path, int size) {
        BufferedImage sheet = null;
        try {
            sheet = ImageIO.read(Art.class.getResourceAsStream(path));
        } catch (IOException e) {
            throw new RuntimeException("Failed to load " + path);
        }

        int sw = sheet.getWidth() / size;
        int sh = sheet.getHeight() / size;
        BufferedImage[] result = new BufferedImage[sw * sh];
        for (int y = 0; y < sh; y++) {
            for (int x = 0; x < sw; x++) {
                BufferedImage image = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
                Graphics2D g = image.createGraphics();
                g.drawImage(sheet, -x * size, -y * size, null);
                g.dispose();
                result[x + y * sw] = image;
            }
        }

        return result;
    }

    public static void drawString(String text, int x, int y, Graphics2D g) {
        for (int i = 0; i < text.length(); i++) {
            int c = chars.indexOf(text.charAt(i));
            g.drawImage(font[c], x + i * 8, y, Color.BLACK, null);
        }
    }

    public static void drawProgressBar(int x, int y, int w, int h, Graphics2D g, Color c0, Color c1, double p) {
        int w0 = (int)(w * p);
        int w1 = (int)(w * (1 - p));

        g.setColor(c0);
        g.fillRect(x, y, w0, h);

        g.setColor(c1);
        g.fillRect(x + w0, y, w1, h);
    }

}