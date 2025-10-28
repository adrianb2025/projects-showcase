package game.level.tile;

import game.InputHandler;
import game.entity.Entity;
import game.entity.item.Item;
import game.entity.mob.Mob;
import game.entity.mob.Player;
import game.gfx.Screen;
import game.gfx.util.PaletteHelper;
import game.level.Level;

import java.util.Random;

public class Tile {

    protected static final Random random = new Random();
    public static final Tile[] tiles = new Tile[256];
    public static final Tile grass = new GrassTile(0);
    public static final Tile rock = new RockTile(1);
    public static final Tile lava = new LavaTile(2);
    public static final Tile sand = new SandTile(3);
    public static final Tile water = new WaterTile(4);
    public static final Tile hole = new HoleTile(5);
    public static final Tile road = new RoadTile(6);
    public static final Tile swamp = new SwampTile(7);
    public static final Tile deepWater = new DeepWaterTile(8);
    public static final int GRASS_TILE = 0;
    public static final int SAND_TILE = 3;
    public static final int WATER_TILE = 4;
    public static final int ROAD_TILE = 6;
    public static final int DEEP_WATER_TILE = 8;

    public int lavaMainColor = 500;
    public int grassMainColor = 131;
    public int dirtMainColor = 322;
    public int sandMainColor = 550;
    public int roadMainColor = 431;
    public int waterMainColor = 5;
    public int deepWaterMainColor = 2;
    public int holeMainColor = 111;
    public int swampMainColor = 240;

    public int holeCol = PaletteHelper.getColor(111, holeMainColor, 110, 110);
    public int grassCol = PaletteHelper.getColor(30, grassMainColor, 242, 333);
    public int lavaCol = PaletteHelper.getColor(500, lavaMainColor, 520, 550);
    public int sandCol = PaletteHelper.getColor(552, sandMainColor, 440, 440);
    public int roadCol = PaletteHelper.getColor(431, roadMainColor, roadMainColor - 110, 330);
    public int deepWaterCol = PaletteHelper.getColor(4, deepWaterMainColor, 114, 114);
    public int waterCol = PaletteHelper.getColor(5, waterMainColor, 115, 115);
    public int swampCol = PaletteHelper.getColor(-1, swampMainColor, swampMainColor - 110, -1);

    public boolean connectsToGrass;
    public boolean connectsToSand;
    public boolean connectsToLava;
    public boolean connectsToWater;
    public boolean connectsToSwamp;

    protected int slowPeriod = 10;
    protected boolean liquid;
    public static int tickCount;
    protected byte id;

    public Tile(int id) {
        if (tiles[id] != null) throw new RuntimeException("Duplicate Tile IDs");

        tiles[id] = this;
        this.id = (byte) id;
    }

    public void tick(Level level, int xt, int yt) {}
    public void render(Screen screen, Level level, int x, int y) {}
    public boolean mayPass(Level level, int xt, int yt, Entity e) { return true; }
    public boolean interact(Level level, int xt, int yt, Player player, Item item, int attackDir) { return false; }
    public void bumpInto(Level level, int xt, int yt, Entity e) {}
    public void steppedOn(Level level, int xt, int yt, Entity e) {
        if (e instanceof Mob && !e.ignoreBlocks()) ((Mob) e).setSlowPeriod(slowPeriod);
    }

    public boolean connectsToLiquid() { return connectsToWater || connectsToLava || connectsToSwamp; }
    public boolean isLiquid() { return liquid; }
    public byte getID() { return id; }

}