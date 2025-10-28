package game.level;

import game.entity.Entity;
import game.entity.Tree;
import game.gfx.Art;
import game.gfx.Renderable;
import game.level.tile.Tile;
import game.particle.Particle;
import game.screen.GameScreen;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Level {

    private final List<Entity> entities = new ArrayList<Entity>();
    private final List<Particle> particles = new ArrayList<Particle>();
    private final List<Renderable> renderList = new ArrayList<Renderable>();
    public final int w;
    public final int h;
    public final int x;
    public final int y;
    private final int[] tiles;
    public int xOffset;
    public int yOffset;
    public double gravity = 0.06;
    public GameScreen gameScreen;
    public boolean night;

    private Level(GameScreen gameScreen, int w, int h, int x, int y) {
        this.gameScreen = gameScreen;
        this.w = w;
        this.h = h;
        this.x = x;
        this.y = y;
        tiles = new int[w * h];
    }

    public static Level loadLevel(GameScreen gameScreen, int xx, int yy) {
        BufferedImage level = Art.levels[xx + yy * 8];
        int size = 32;
        int[] pixels = new int[size * size];
        level.getRGB(0, 0, size, size, pixels, 0, size);
        Level result = new Level(gameScreen, size, size, xx, yy);
        for (int y = 0; y < size; y++) {
            Tile prev = Tile.grass;
            for (int x = 0; x < size; x++) {
                int c = x + y * size;

                int col = pixels[c];
                int xp = (x << 4) + 8;
                int yp = (y << 4) + 8;

                Tile tile = Tile.water;
                if (col == 0xFF00FF00) tile = Tile.grass;
                else if (col == 0xFFFFFF00) tile = Tile.sand;
                else if (col == 0xFF007700) {
                    tile = prev;
                    result.addEntity(new Tree(xp, yp, 3));
                }

                result.tiles[c] = tile.id;
                prev = tile;
            }
        }

        return result;
    }

    public void addEntity(Entity e) {
        entities.add(e);
        e.init(this);
    }

    public void addParticle(Particle p) {
        particles.add(p);
        p.init(this);
    }

    public void tick() {
        for (int i = 0; i < entities.size(); i++) {
            Entity e = entities.get(i);

            if (!e.removed) {
                e.tick();
                continue;
            }

            entities.remove(i--);
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

    public void render(Graphics2D g) {
        int x0 = xOffset >> 4;
        int y0 = yOffset >> 4;
        int x1 = x0 + 12 + 2;
        int y1 = y0 + 7 + 2;

        if (x0 < 0) x0 = 0;
        if (y0 < 0) y0 = 0;
        if (x1 > w - 1) x1 = w - 1;
        if (y1 > h - 1) y1 = h - 1;

        for (int y = y0; y < y1; y++) {
            for (int x = x0; x < x1; x++) {
                getTile(x, y).render(g, this, x << 4, y << 4, xOffset, yOffset);
            }
        }

        AffineTransform at = g.getTransform();

        g.translate(-xOffset, -yOffset);

        renderList.clear();
        renderList.addAll(entities);
        renderList.addAll(particles);
        Collections.sort(renderList, new Comparator<Renderable>() {
            public int compare(Renderable r0, Renderable r1) {
                double y0 = 0.0;
                double y1 = 0.0;

                if (r0 instanceof Entity) y0 = ((Entity) r0).y;
                else if (r0 instanceof Particle) y0 = ((Particle) r0).y;

                if (r1 instanceof Entity) y1 = ((Entity) r1).y;
                else if (r1 instanceof Particle) y1 = ((Particle) r1).y;

                return Double.compare(y0, y1);
            }
        });

        for (int i = 0; i < renderList.size(); i++) {
            renderList.get(i).render(g);
        }

        g.setTransform(at);

    }

    public void setOffset(int xOffset, int yOffset) {
        this.xOffset = xOffset;
        this.yOffset = yOffset;
    }

    public <T extends Entity> Entity getNearbyEntity(double x, double y, double r, Class<T> clazz, EntityFilter<T> filter) {
        Entity result = null;
        double minDist = -1;
        for (int i = 0; i < entities.size(); i++) {
            Entity e = entities.get(i);
            double dist = e.distanceToSqr(x, y);
            if (!clazz.isInstance(e) || filter != null && !filter.accept(e) || !(dist < (e.r * e.r) + r * r) || minDist != -1 && !(dist < minDist)) continue;
            minDist = dist;
            result = e;
        }

        return result;
    }

    public <T extends Entity> List<Entity> getEntities(int x, int y, int r, Class<T> clazz) {
        List<Entity> result = EntityListCache.get();
        for (int i = 0; i < entities.size(); i++) {
            Entity e = entities.get(i);
            if (!clazz.isInstance(e) || !e.intersectsRing(x, y, r)) continue;
            result.add(e);
        }

        return result;
    }

    public void updateTime(int dayFactor) { night = dayFactor < 210; }
    public void addScore(int score) { gameScreen.addScore(score); }

    public interface EntityFilter<T> {
        public boolean accept(Entity e);
    }

    public Tile getTile(int x, int y) {
        if (x < 0 || y < 0 || x >= w || y >= h) return Tile.water;
        return Tile.tiles[tiles[x + y * w]];
    }

}