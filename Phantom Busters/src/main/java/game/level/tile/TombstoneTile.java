package game.level.tile;

import game.gfx.Art;
import game.gfx.Bitmap;

public class TombstoneTile extends GrassTile {

    public TombstoneTile(int id) {
        super(id);
        isFloor = false;
    }

    public void render(Bitmap screen, int xp, int yp) {
        super.render(screen, xp, yp);
        screen.draw(Art.tiles[2][1], xp, yp);
    }

    public boolean blocks() { return true; }

}
