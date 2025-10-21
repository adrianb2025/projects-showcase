package game.level.tile;

import game.gfx.Art;
import game.gfx.Bitmap;

public class WallTile extends Tile {

    public WallTile(int id) {
        super(id);
        isFloor = false;
    }

    public void render(Bitmap screen, int xp, int yp) { screen.draw(Art.tiles[0][4], xp, yp); }

    public boolean blocks() { return true; }

}