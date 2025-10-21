package game.level.tile;

import game.entity.Entity;
import game.level.Level;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.util.Random;

public abstract class Tile {

    public static final Tile[] tiles = new Tile[255];
    public static final Random random = new Random();
    public static final Tile grass = new GrassTile(0);
    public static final Tile solid = new SolidTile(1);
    public static final Tile hole = new HoleTile(2);
    public static final Tile carrotFill = new CarrotFillTile(3);
    public int id;

    public Tile(int id) {
        if (tiles[id] != null) throw new RuntimeException("Tile ID must be unique");
        tiles[id] = this;
        this.id = id;
    }

    public abstract void render(Graphics2D g, Level level, int xt, int yt);
    public boolean blocks() { return false; }

}