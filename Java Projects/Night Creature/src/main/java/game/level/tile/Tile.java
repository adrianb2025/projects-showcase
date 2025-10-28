package game.level.tile;

import game.level.Level;

import java.awt.*;

public abstract class Tile {

    public static final Tile[] tiles = new Tile[255];
    public static final Tile grass = new GrassTile(0);
    public static final Tile water = new WaterTile(1);
    public static final Tile sand = new SandTile(2);
    public final byte id;

    public Tile(int id) {
        if (tiles[id] != null) throw new IllegalStateException("Tile ID must be unique(ID: " + id + " already in use)");
        this.id = (byte)id;
        tiles[id] = this;
    }

    public abstract void render(Graphics2D g, Level level, int xt, int yt, int xScroll, int yScroll);

}