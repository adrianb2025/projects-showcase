package game.gfx;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;

public class TextureUtils {

    public static final BufferedImage load(String path) {
        BufferedImage image;
        try {
            image = ImageIO.read(Art.class.getResourceAsStream(path));
        } catch (Exception e) {
            throw new IllegalStateException("Failed to load " + path);
        }

        BufferedImage result = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = result.createGraphics();
        g.drawImage(image, 0, 0, null);
        g.dispose();
        return result;
    }

    public static final BufferedImage[] loadAndCut(String path, int w, int h) {
        BufferedImage sheet;
        try {
            sheet = ImageIO.read(Art.class.getResourceAsStream(path));
        } catch (Exception e) {
            throw new IllegalStateException("Failed to load " + path);
        }

        int sw = sheet.getWidth() / w;
        int sh = sheet.getHeight() / h;
        BufferedImage[] result = new BufferedImage[sw * sh];
        for (int y = 0; y < sh; y++) {
            for (int x = 0; x < sw; x++) {
                BufferedImage image = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
                Graphics2D g = image.createGraphics();
                g.drawImage(sheet, -x * w, -y * h, null);
                g.dispose();
                result[x + y * sw] = image;
            }
        }

        return result;
    }
}