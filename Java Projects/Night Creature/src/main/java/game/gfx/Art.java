package game.gfx;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class Art {

    public static final BufferedImage[] levels = loadAndCut("/levels.png", 32);
    public static final BufferedImage[] sprites = loadAndCut("/sprites.png", 16);
    public static final BufferedImage[] particles = loadAndCut("/particles.png", 8);
    public static final BufferedImage[] waterToGrass = TileGenerator.gen(TileGenerator.water, TileGenerator.grass, 16);
    public static final BufferedImage[] sandToGrass = TileGenerator.gen(TileGenerator.sand, TileGenerator.grass, 16);

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

}