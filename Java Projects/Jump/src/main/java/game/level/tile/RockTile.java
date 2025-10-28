package game.level.tile;

import game.entity.Entity;
import game.gfx.Art;
import game.level.Level;

import java.awt.*;

public class RockTile extends GrassTile {

    public RockTile(int id) { super(id); }

    public void render(Level level, Graphics2D g, int x, int y, int yScroll) {
        super.render(level, g, x, y, yScroll);

        int t = 32;
        g.drawImage(Art.sprites[t], x * 16, y * 16 - yScroll, null);
    }

    public boolean blocks(Entity e) { return true; }
    public boolean canSpawn(Entity e) { return false; }

}