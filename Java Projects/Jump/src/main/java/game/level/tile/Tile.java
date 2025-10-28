package game.level.tile;

import game.entity.Entity;
import game.level.Level;

import java.awt.*;
import java.util.Random;

public abstract class Tile {

    public static final Random random = new Random();
    public static final Tile[] tiles = new Tile[255];
    public static final Tile grass = new GrassTile(0);
    public static final Tile water = new WaterTile(1);
    public static final Tile rock = new RockTile(2);
    public static final Tile lava = new LavaTile(3);
    public static final Tile damaged = new DamagedTile(4);
    public static final Tile solidGrass = new SolidGrassTile(5);
    public int id;

    public Tile(int id) {
        if (tiles[id] != null) throw new IllegalStateException("Tile ID must be unique(Tile ID " + id + " already in use)");

        this.id = id;
        tiles[id] = this;
    }

    public abstract void render(Level level, Graphics2D g, int x, int y, int yScroll);

    public boolean blocks(Entity e) { return false; }
    public boolean canSpawn(Entity e) { return false; }

}