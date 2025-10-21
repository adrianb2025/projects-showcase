package game.level.tile;

import game.gfx.Art;
import game.level.Level;

import java.awt.*;

public class SolidTile extends Tile {

    public SolidTile(int id) { super(id); }

    public void render(Graphics2D g, Level level, int xt, int yt) { g.drawImage(Art.sheet[9], xt << 4, yt << 4, null); }
    public boolean blocks() { return true; }

}