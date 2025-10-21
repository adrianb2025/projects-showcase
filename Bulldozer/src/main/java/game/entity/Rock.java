package game.entity;

import game.gfx.Art;
import game.level.tile.Tile;

import java.awt.*;

@Deprecated
public class Rock extends Entity {

    public Rock(int x, int y) { super(x, y); }

    public boolean blocks(Entity e) { return true; }
    public void render(Graphics2D g) { g.drawImage(Art.sheet[6], x << 4, y << 4, null); }

    public void steppedOn(Tile tile, int x, int y) {
        if (tile == Tile.hole) {
            level.setTile(Tile.carrotFill, x, y);
            level.checkCompletion();
            removed = true;
        }
    }

}