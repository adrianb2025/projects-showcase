package game.level.tile;

import game.gfx.Art;
import game.level.Level;

import java.awt.*;

public class DamagedTile extends LavaTile {

    public DamagedTile(int id) {
        super(id);
        sparks = false;
    }

    public void render(Level level, Graphics2D g, int x, int y, int yScroll) {
        super.render(level, g, x, y, yScroll);
        random.setSeed(1231234597L + x * 12871523L + y * 8762232177L);
        int t = random.nextInt(2) + 24;
        g.drawImage(Art.sprites[t], x * 16, y * 16 - yScroll, null);
    }

}