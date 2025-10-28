package game.level.tile.tree;

import game.gfx.Art;
import game.gfx.Bitmap;
import game.level.tile.GrassTile;

public class GreatTreeTile extends GrassTile {

    public GreatTreeTile(int id) {
        super(id);
        isFloor = false;
    }

    public void render(Bitmap screen, int xp, int yp) {
        super.render(screen, xp, yp);

        screen.draw(Art.tiles[4][4], xp - 96, yp - 192);
        screen.draw(Art.tiles[5][4], xp, yp - 192);
        screen.draw(Art.tiles[6][4], xp + 96, yp - 192);

        screen.draw(Art.tiles[4][5], xp - 96, yp - 96);
        screen.draw(Art.tiles[5][5], xp, yp - 96);
        screen.draw(Art.tiles[6][5], xp + 96, yp - 96);

        screen.draw(Art.tiles[4][6], xp - 96, yp);
        screen.draw(Art.tiles[5][6], xp, yp);
        screen.draw(Art.tiles[6][6], xp + 96, yp);
    }

    public boolean blocks() { return true; }
}