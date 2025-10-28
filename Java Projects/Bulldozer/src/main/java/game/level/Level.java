package game.level;

import game.entity.*;
import game.gfx.Art;
import game.gfx.Bitmap;
import game.level.tile.Tile;
import game.screen.GameScreen;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Level {

    public static final Random random = new Random();
    private List<Entity> entities = new ArrayList<Entity>();
    public int w;
    public int h;
    public int[] tiles;
    public final int level;
    public Bunny player;
    public List<Entity>[] entitiesInTile;
    public GameScreen gameScreen;

    public Level(int level, GameScreen gameScreen) {
        this.level = level;
        this.gameScreen = gameScreen;
        loadLevel(level);
    }

    public void add(Entity e) {
        entities.add(e);
        e.init(this);
        insertEntity(e, e.x, e.y);

        if (e instanceof Bunny) player = (Bunny) e;
    }

    private void loadLevel(int level) {
        Bitmap bmp = Art.levels[level % 5];
        w = bmp.w;
        h = bmp.h;
        tiles = new int[w * h];
        entitiesInTile = new ArrayList[w * h];


        int[] data = bmp.pixels;
        for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {
                entitiesInTile[x + y * w] = new ArrayList<Entity>();
                int col = data[x + y * w];
                Tile tile = Tile.grass;
                if (col == 0xFF007F00) tile = Tile.grass;
                else if (col == 0xFF000000) tile = Tile.hole;
                else if (col == 0xFF7F7F7F) tile = Tile.solid;
                else if (col == 0xFF0000FF) {
                    tile = Tile.grass;
                    add(new Bunny(x, y));
                } else if (col == 0xFFFF00FF) {
                    tile = Tile.grass;
                    add(new Carrot(x, y));
                }

                tiles[x + y * w] = tile.id;
            }
        }
    }

    public void tick() {
        for (int i = 0; i < entities.size(); i++) {
            Entity e = entities.get(i);

            int xto = e.x;
            int yto = e.y;

            if (!e.removed) e.tick();

            if (e.removed) {
                entities.remove(i--);
                removeEntity(e, xto, yto);
            } else {
                int xtn = e.x;
                int ytn = e.y;

                if (xto != xtn || yto != ytn) {
                    removeEntity(e, xto, yto);
                    insertEntity(e, xtn, ytn);
                }
            }
        }
    }

    public void render(Graphics2D g) {
        for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {
                getTile(x, y).render(g, this, x, y);
            }
        }

        for (Entity e : entities) {
            e.render(g);
        }
    }

    public Tile getTile(int x, int y) {
        if (x < 0 || y < 0 || x >= w || y >= h) return Tile.solid;
        return Tile.tiles[tiles[x + y * w]];
    }

    public void setTile(Tile tile, int x, int y) {
        if (x < 0 || y < 0 || x >= w || y >= h) return;
        tiles[x + y * w] = tile.id;
    }

    public void insertEntity(Entity e, int x, int y) {
        entitiesInTile[x + y * w].add(e);
    }

    public void removeEntity(Entity e, int x, int y) {
        entitiesInTile[x + y * w].remove(e);
    }

    public List<Entity> getEntities(int x, int y) {
        List<Entity> result = EntityListCache.get();
        result.addAll(entitiesInTile[x + y * w]);
        return result;
    }

    public void checkCompletion() {
        for (int i = 0; i < w * h; i++) {
            if (tiles[i] == Tile.hole.id) return;
        }

        gameScreen.nextLevel();
    }

}