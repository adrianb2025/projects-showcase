package game.gfx;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Art {

    public static final BufferedImage[] sheet = TextureUtils.loadAndCut("/sheet.png", 16, 16);
    public static final Bitmap[] levels = loadAndCut("/levels.png", 20, 15);

    public static final Bitmap[] loadAndCut(String path, int w, int h) {
        BufferedImage sheet;
        try {
            sheet = ImageIO.read(Art.class.getResourceAsStream(path));
        } catch (Exception e) {
            throw new IllegalStateException("Failed to load " + path);
        }

        int sw = sheet.getWidth() / w;
        int sh = sheet.getHeight() / h;
        Bitmap[] result = new Bitmap[sw * sh];
        for (int y = 0; y < sh; y++) {
            for (int x = 0; x < sw; x++) {
                BufferedImage image = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
                Graphics2D g = image.createGraphics();
                g.drawImage(sheet, -x * w, -y * h, null);
                g.dispose();
                result[x + y * sw] = new Bitmap(image);
            }
        }

        return result;
    }

}