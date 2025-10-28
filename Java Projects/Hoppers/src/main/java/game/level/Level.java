package game.level;

import game.entity.Entity;
import game.entity.hopper.GrayHopper;
import game.entity.hopper.Hopper;
import game.entity.hopper.OrangeHopper;
import game.entity.hopper.YellowHopper;
import game.gfx.Art;
import game.level.tile.Tile;
import game.particle.Particle;
import game.particle.RockParticle;

import java.awt.*;
import java.util.*;
import java.util.List;

public class Level {

    public static final Random random = new Random();
    public double gravity;
    public double friction;
    public List<Entity> entities = new ArrayList<Entity>();
    public List<Hopper> hoppers = new ArrayList<Hopper>();
    public List<Particle> particles = new ArrayList<Particle>();
    public List<Entity>[] entityMap;
    public final int w = 10;
    public final int h = 7;
    public int num;
    public Tile[] tiles;

    public Level(int num) {
        if (num >= Art.levels.length) num = 0;

        this.num = num;
        tiles = new Tile[w * h];
        entityMap = new ArrayList[w * h];
        friction = 0.99;
        gravity = 0.09;
        loadLevel();
    }

    public void loadLevel() {
        for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {
                entityMap[x + y * w] = new ArrayList<Entity>();

                tiles[x + y * w] = Tile.empty;

                int col = Art.levels[num].getRGB(x, y);
                if (col == 0xFF267F00) tiles[x + y * w] = Tile.ground;
                else if (col == 0xFF808080) tiles[x + y * w] = Tile.wall;
                else if (col == 0xFFFF0000) tiles[x + y * w] = Tile.lava;
                else if (col == 0xFF00FFFF) tiles[x + y * w] = Tile.portal;
                else if (col == 0xFFFFFF00) {
                    YellowHopper hopper = new YellowHopper((x << 5) + 16, (y << 5) + 16);
                    addEntity(hopper);
                } else if (col == 0xFF404040) {
                    GrayHopper hopper = new GrayHopper((x << 5) + 16, (y << 5) + 16);
                    addEntity(hopper);
                } else if (col == 0xFFFF6A00) {
                    OrangeHopper hopper = new OrangeHopper((x << 5) + 16, (y << 5) + 16);
                    addEntity(hopper);
                }
            }
        }
    }

    public void addEntity(Entity e) {
        entities.add(e);
        e.init(this);

        e.xSlot = (int)(e.x + e.xr) / 32;
        e.ySlot = (int)(e.y + e.yr) / 32;

        if (e.xSlot >= 0 && e.ySlot >= 0 && e.xSlot < w && e.ySlot < h) entityMap[e.xSlot + e.ySlot * w].add(e);
        if (e instanceof Hopper) hoppers.add((Hopper) e);
    }

    public void addParticle(Particle p) {
        particles.add(p);
        p.init(this);
    }

    public void tick() {
        for (int i = 0; i < this.w * this.h; i++) {
            tiles[i].tick(this, i % this.w, i / this.w);
        }

        for (int i = 0; i < entities.size(); i++) {
            Entity e = entities.get(i);

            int xOld = e.xSlot;
            int yOld = e.ySlot;

            if (!e.removed) e.tick();

            e.xSlot = (int)(e.x + e.xr) >> 5;
            e.ySlot = (int)(e.y + e.yr) >> 5;

            boolean checkOld = xOld >= 0 && yOld >= 0 && xOld < w && yOld < h;

            if (e.removed) {
                if (checkOld) entityMap[e.xSlot + e.ySlot * w].remove(e);
                entities.remove(i--);
            } else {
                if (e.xSlot != xOld || e.ySlot != yOld) {
                    if (checkOld) entityMap[xOld + yOld * w].remove(e);
                    if (e.xSlot >= 0 && e.ySlot >= 0 && e.xSlot < w && e.ySlot < h) entityMap[e.xSlot + e.ySlot * w].add(e);
                    else e.outOfBounds();
                }
            }
        }

        for (int i = 0; i < particles.size(); i++) {
            Particle p = particles.get(i);

            if (!p.removed) {
                p.tick();
                continue;
            }

            particles.remove(i--);
        }
    }

    public void render(Graphics2D g, double delta) {
        for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {
                Tile tile = getTile(x, y);
                tile.render(g, x << 5, y << 5);
            }
        }

        Collections.sort(this.entities, new Comparator<Entity>() {
            public int compare(Entity e0, Entity e1) {
                if (e0.zSort < e1.zSort) return -1;
                if (e0.zSort > e1.zSort) return 1;
                if ((int) e0.y < (int) e1.y) return -1;
                if ((int) e0.y > (int) e1.y) return +1;
                if ((int) e0.x < (int) e1.x) return -1;
                if ((int) e0.x > (int) e1.x) return +1;
                return 0;
            }
        });

        for (Entity e : entities) {
            e.render(g, delta);
        }

        for (Particle p : particles) {
            p.render(g);
        }
    }

    public List<Entity> getEntities(int x0, int y0, int x1, int y1) {
        List<Entity> result = new ArrayList<Entity>();
        int xt0 = (x0 >> 5) - 1;
        int yt0 = (y0 >> 5) - 1;
        int xt1 = (x1 >> 5) + 1;
        int yt1 = (y1 >> 5) + 1;

        for (int y = yt0; y <= yt1; y++) {
            for (int x = xt0; x <= xt1; x++) {
                if (x < 0 || y < 0 || x >= w || y >= h) continue;

                List<Entity> entities = entityMap[x + y * w];
                for (int i = 0; i < entities.size(); i++) {
                    Entity e = entities.get(i);
                    if (e.intersects(x0, y0, x1, y1)) result.add(e);
                }
            }
        }

        return result;
    }

    public Tile getTile(int x, int y) {
        if (y >= h) return Tile.empty;
        if (x >= 0 && y >= 0 && x < w) return tiles[x + y * w];
        return Tile.wall;
    }

    public void setTile(Tile tile, int x, int y) {
        if (x >= 0 && y >= 0 && x < w && y < h) {
            tiles[x + y * w] = tile;
        }
    }

    public void destroyTile(int xt, int yt, Entity e) {
        setTile(Tile.empty, xt, yt);
        for (int i = 0; i < 5; i++) {
            addParticle(new RockParticle((xt << 5) + 16 + random.nextInt(16) - 8, (yt << 5) + 16 + random.nextInt(16) - 8));
        }
    }

}