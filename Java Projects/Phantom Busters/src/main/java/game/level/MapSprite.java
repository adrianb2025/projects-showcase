package game.level;

import game.gfx.Bitmap;
import game.gfx.Sprite;
import game.level.tile.Tile;

public class MapSprite extends Sprite {

    public Tile tile;

    public MapSprite(Tile tile, double x, double y) {
        this.tile = tile;
        this.x = x + 32;
        this.y = y + 32;
    }

    public void render(Bitmap screen, int xp, int yp) { tile.render(screen, xp - 48, yp - 72); }

}