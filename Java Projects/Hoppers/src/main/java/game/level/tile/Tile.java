package game.level.tile;

import game.entity.Entity;
import game.gfx.Art;
import game.level.Level;

import java.awt.*;
import java.util.Random;

public class Tile {

    protected static final Random random = new Random();
    public static final Tile ground = new GroundTile(0);
    public static final Tile wall = new WallTile(1);
    public static final Tile lava = new LavaTile(2);
    public static final Tile empty = new AirTile(-1);
    public static final Tile portal = new PortalTile(-1);

    protected int tile;

    protected Tile(int tile) { this.tile = tile; }

    public void tick(Level level, int xt, int yt) {}

    public void render(Graphics2D g, int x, int y) {
        if (tile >= 0) g.drawImage(Art.tiles[tile], x, y, null);
    }

    public boolean blocks() { return false; }
    public void bumpInto(Level level, int x, int y, double xa, double ya, Entity e) {}

}