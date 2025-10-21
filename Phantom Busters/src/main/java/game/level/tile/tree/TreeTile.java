package game.level.tile.tree;

import game.gfx.Art;
import game.gfx.Bitmap;
import game.level.tile.GrassTile;

public class TreeTile extends GrassTile {

    public TreeTile(int id) {
        super(id);
        isFloor = false;
    }

    public void render(Bitmap screen, int xp, int yp) {
        super.render(screen, xp, yp);
        screen.draw(Art.tiles[0][5], xp, yp - 96);
        screen.draw(Art.tiles[0][6], xp, yp);
    }

    public boolean blocks() { return true; }

}