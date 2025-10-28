package game.gfx;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class Art {

    public static final BufferedImage[] sprites = loadAndCut("/sprites.png", 32, 32);
    public static final BufferedImage[] levels = loadAndCut("/levels.png", 10, 7);
    public static final BufferedImage[] tiles = loadAndCut("/tiles.png", 32, 32);
    public static final BufferedImage[] particles = loadAndCut("/particles.png", 8, 8);
    public static final BufferedImage[] gui = loadAndCut("/gui.png", 8, 8);
    public static final BufferedImage[] title = loadAndCut("/title.png", 320, 480);

    public static final BufferedImage[] loadAndCut(String path, int w, int h) {
        BufferedImage sheet = null;
        try {
            sheet = ImageIO.read(Art.class.getResourceAsStream(path));
        } catch (IOException e) {
            throw new RuntimeException("Failed to load " + path);
        }

        int sw = sheet.getWidth() / w;
        int sh = sheet.getHeight() / h;
        BufferedImage[] result = new BufferedImage[sw * sh];
        for (int y = 0; y < sh; y++) {
            for (int x = 0; x < sw; x++) {
                BufferedImage image = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
                Graphics g = image.getGraphics();
                g.drawImage(sheet, -x * w, -y * h, null);
                g.dispose();
                result[x + y * sw] = image;
            }
        }

        return result;
    }

}