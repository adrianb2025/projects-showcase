package game.level.tile.tree;

import game.gfx.Art;
import game.gfx.Bitmap;
import game.level.tile.GrassTile;

public class DeadTreeTile extends GrassTile {

    public DeadTreeTile(int id) {
        super(id);
        isFloor = false;
    }

    public void render(Bitmap screen, int xp, int yp) {
        super.render(screen, xp, yp);
        screen.draw(Art.tiles[1][6], xp - 96, yp);
        screen.draw(Art.tiles[2][6], xp, yp);
        screen.draw(Art.tiles[3][6], xp + 96, yp);
        screen.draw(Art.tiles[1][5], xp - 96, yp - 96);
        screen.draw(Art.tiles[2][5], xp, yp - 96);
        screen.draw(Art.tiles[3][5], xp + 96, yp - 96);
    }

}