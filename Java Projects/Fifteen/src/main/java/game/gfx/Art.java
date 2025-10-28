package game.gfx;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class Art {

    public static final BufferedImage mask = load("/mask.png");
    public static final BufferedImage[] images = frame(loadAndCut("/15.png", 32, 32));

    public static BufferedImage load(String path) {
        BufferedImage image;
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

    public static BufferedImage[] loadAndCut(String path, int sx, int sy) {
        BufferedImage sheet;
        try {
            sheet = ImageIO.read(Art.class.getResourceAsStream(path));
        } catch (IOException e) {
            throw new RuntimeException("Failed to load " + path);
        }

        int w = sheet.getWidth() / sx;
        int h = sheet.getHeight() / sy;
        BufferedImage[] result = new BufferedImage[w * h];

        for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {
                BufferedImage image = new BufferedImage(sx, sy, BufferedImage.TYPE_INT_ARGB);
                Graphics2D g = image.createGraphics();
                g.drawImage(sheet, -x * sx, -y * sy, null);
                g.dispose();
                result[x + y * w] = image;
            }
        }

        return result;
    }

    public static BufferedImage[] frame(BufferedImage[] sources) {
        for (int i = 0; i < sources.length; i++) {
            BufferedImage image = sources[i];
            Graphics2D g = image.createGraphics();
            g.drawImage(mask, 0, 0, null);
            g.dispose();
        }

        return sources;
    }

}