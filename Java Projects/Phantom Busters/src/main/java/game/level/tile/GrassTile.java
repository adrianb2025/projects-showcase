package game.level.tile;

import game.gfx.Art;
import game.gfx.Bitmap;

import java.util.Random;

public class GrassTile extends Tile {

    private static final Random random = new Random(200);

    public GrassTile(int id) { super(id); }

    public void render(Bitmap screen, int xp, int yp) {
        random.setSeed(xp * 100 + yp * 100000000);
        screen.draw(Art.tiles[random.nextInt(2)][3], xp, yp);
    }
}