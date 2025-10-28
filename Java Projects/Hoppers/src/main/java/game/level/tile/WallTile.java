package game.level.tile;

import game.entity.Entity;
import game.entity.hopper.YellowHopper;
import game.level.Level;

public class WallTile extends Tile {

    protected WallTile(int tile) { super(tile); }

    public boolean blocks() { return true; }

    public void bumpInto(Level level, int x, int y, double xa, double ya, Entity e) {
        if (xa != 0) {
            if (e instanceof YellowHopper) {
                if (Math.abs(e.xa) > 6) {
                    level.destroyTile(x, y, e);
                    e.xa *= 0.005;
                }
            }
        }
    }
}