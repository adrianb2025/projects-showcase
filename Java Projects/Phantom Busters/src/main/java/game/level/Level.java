package game.level;

import game.Player;
import game.entity.Entity;
import game.entity.unit.mob.Phantom;
import game.gfx.Art;
import game.gfx.Bitmap;
import game.gfx.Sprite;
import game.level.blockmap.BlockMap;
import game.level.tile.Tile;
import game.particle.Particle;

import java.util.*;

public class Level {

    public Random random = new Random();
    public int w;
    public int h;
    public Tile[] tiles;
    public List<Entity> entities = new ArrayList<Entity>();
    public List<Particle> particles = new ArrayList<Particle>();
    public List<Sprite> mapSprites = new ArrayList<Sprite>();
    public BlockMap blockMap;
    public double maxHeight = 512;
    public int tickTime;
    boolean[] visibleTmpMap;
    private ShadowMask sm = new ShadowMask();
    public Player player;
    private Comparator<Sprite> spriteComparator = new Comparator<Sprite>() {
        public int compare(Sprite s0, Sprite s1) {
            if (s0.x + s0.y > s1.x + s1.y) return 1;
            if (s0.x + s0.y < s1.x + s1.y) return -1;

            if (s0.z > s1.z) return 1;
            if (s0.z < s1.z) return -1;
            if (s0.y > s1.y) return 1;
            if (s0.y < s1.y) return -1;
            if (s0.x > s1.x) return 1;
            if (s0.x < s1.x) return -1;

            return 1;
        }
    };

    public Level(int num) {
        loadLevel(num);
        player = new Player(this);
        spawnMobs();
    }

    public void loadLevel(int num) {
        Bitmap bmp = Art.load("/levels/" + num + ".png");
        w = bmp.w + 8;
        h = bmp.h + 8;
        tiles = new Tile[w * h];
        visibleTmpMap = new boolean[w * h];
        blockMap = new BlockMap(w, h, 64);
        for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {
                int xx = x - 4;
                int yy = y - 4;

                if (xx < 0 || yy < 0 || xx >= bmp.w || yy >= bmp.h) {
                    tiles[x + y * w] = Tile.wall;
                    continue;
                }

                int col = bmp.pixels[xx + yy * bmp.w];
                if (col == 0xFF000000) {
                    tiles[x + y * w] = Tile.wall;
                    continue;
                }

                if (col == 0xFF808080) {
                    int tree = random.nextInt(3);
                    switch (tree) {
                        case 0: {
                            tiles[x + y * w] = Tile.tree;
                            break;
                        }
                        case 1: {
                            tiles[x + y * w] = Tile.deadTree;
                            break;
                        }
                        case 2: {
                            tiles[x + y * w] = Tile.greatTree;
                        }
                    }
                    continue;
                }
                
                tiles[x + y * w] = Tile.grass;
            }
        }
    }

    public void spawnMobs() {
        for (int i = 0; i < 30; i++) {
            Phantom mob = new Phantom(player);
            add(mob);
            mob.findPos();
        }
    }

    public void add(Entity e) {
        entities.add(e);
        blockMap.add(e);
        e.init(this);
    }

    public void add(Particle p) {
        particles.add(p);
        p.init(this);
    }

    public void tick() {
        for (int i = 0; i < this.entities.size(); i++) {
            Entity e = this.entities.get(i);
            if (!e.removed) e.tick();

            if (e.removed) {
                blockMap.remove(e);
                entities.remove(i--);
                continue;
            }
            
            blockMap.update(e);
        }

        for (int i = 0; i < this.particles.size(); i++) {
            Particle p = this.particles.get(i);
            if (!p.removed) {
                p.tick();
                continue;
            }
            
            particles.remove(i--);
        }
     
        tickTime++;
    }

    public void renderBg(Bitmap screen, int xScroll, int yScroll, int[] visMap) {
        int w = screen.w / 96 + 9;
        int h = screen.h / 24 + 9;
        int x0 = xScroll / 96 - 3;
        int y0 = yScroll / 24 - 4;
        int x1 = x0 + w;
        int y1 = y0 + h;
        mapSprites.clear();
        for (int y = y0; y < y1; y++) {
            for (int x = x0; x < x1; x++) {
                int xt = x + (y >> 1) + (y & 1);
                int yt = (y >> 1) - x;
                if (xt <= 0 || yt <= 0 || xt >= this.w - 3 || yt >= this.h - 3) continue;
                boolean visible = false;
                int pp = xt - 1 + (yt - 1) * this.w;
                if ((visMap[pp] | visMap[pp + 1] | visMap[pp + 2] | visMap[pp + 3] | visMap[pp + 4]) > 0) visible = true;
                if ((visMap[pp += this.w] | visMap[pp + 1] | visMap[pp + 2] | visMap[pp + 3] | visMap[pp + 4]) > 0)  visible = true;
                if ((visMap[pp += this.w] | visMap[pp + 1] | visMap[pp + 2] | visMap[pp + 3] | visMap[pp + 4]) > 0) visible = true;
                if ((visMap[pp += this.w] | visMap[pp + 1] | visMap[pp + 2] | visMap[pp + 3] | visMap[pp + 4]) > 0) visible = true;
                if (!visible) continue;
                
                Tile tile = tiles[xt + yt * this.w];
                MapSprite mapSprite = new MapSprite(tile, xt * 16 * 4, yt * 16 * 4);
                if (!tile.isFloor) {
                    mapSprites.add(mapSprite);
                    continue;
                }

                mapSprite.render(screen);
            }
        }
    }

    public void renderShadows(Bitmap shadows, int xScroll, int yScroll, int[] visMap) {
        for (int yt = 1; yt < h - 4; yt++) {
            for (int xt = 1; xt < w - 4; xt++) {
                boolean visible = false;
                int pp = xt - 1 + (yt - 1) * w;
                if ((visMap[pp] | visMap[pp + 1] | visMap[pp + 2] | visMap[pp + 3]) > 2) visible = true;
                if (!visible && (visMap[pp += w] | visMap[pp + 1] | visMap[pp + 2] | visMap[pp + 3]) > 2) visible = true;
                if (!visible && (visMap[pp += w] | visMap[pp + 1] | visMap[pp + 2] | visMap[pp + 3]) > 2) visible = true;
                visibleTmpMap[xt + yt * w] = visible;
            }
        }

        int xx0 = xScroll;
        int yy0 = yScroll;
        int r = 32;

        for (Entity entity : getEntityScreenSpace(xx0 - r, yy0 - r, xx0 + r, yy0 + r)) {
            if (!visibleTmpMap[(int)(entity.x / 64.0) + (int)(entity.y / 64.0) * w]) continue;
            entity.renderShadow(shadows);
        }

        for (Particle particle : getParticleScreenSpace(xx0 - r, yy0 - r, xx0 + r, yy0 + r)) {
            if (!visibleTmpMap[(int)(particle.x / 64.0) + (int)(particle.y / 64.0) * w]) continue;
            particle.renderShadow(shadows);
        }
    }

    public void renderSprites(Bitmap screen, int xScroll, int yScroll, int[] visMap) {
        for (int yt = 1; yt < h - 4; yt++) {
            for (int xt = 1; xt < w - 4; xt++) {
                boolean visible = false;
                int pp = xt - 1 + (yt - 1) * w;
                if ((visMap[pp] | visMap[pp + 1] | visMap[pp + 2] | visMap[pp + 3]) > 2) visible = true;
                if (!visible && (visMap[pp += w] | visMap[pp + 1] | visMap[pp + 2] | visMap[pp + 3]) > 2) visible = true;
                if (!visible && (visMap[pp += w] | visMap[pp + 1] | visMap[pp + 2] | visMap[pp + 3]) > 2) visible = true;

                visibleTmpMap[xt + yt * w] = visible;
            }
        }
        
        TreeSet<Sprite> sortedSprites = new TreeSet<Sprite>(spriteComparator);
        int xx0 = xScroll;
        int yy0 = yScroll;
        int r = 32;
        
        for (Entity entity : getEntityScreenSpace(xx0 - r, yy0 - r, xx0 + r, yy0 + r)) {
            if (!visibleTmpMap[(int)(entity.x / 64.0) + (int)(entity.y / 64.0) * w]) continue;
            sortedSprites.add(entity);
        }
        
        for (Particle particle : getParticleScreenSpace(xx0 - r, yy0 - r, xx0 + r, yy0 + r)) {
            if (!visibleTmpMap[(int)(particle.x / 64.0) + (int)(particle.y / 64.0) * w]) continue;
            sortedSprites.add(particle);
        }
        
        sortedSprites.addAll(mapSprites);
        for (Sprite sprite : sortedSprites) {
            sprite.render(screen);
        }
    }

    public void renderInvis(Bitmap bm, int xScroll, int yScroll, int[] visMap) {
        int w = bm.w / 96 + 16;
        int h = bm.h / 24 + 20;
        int x0 = xScroll / 96 - 8;
        int y0 = yScroll / 24 - 12;
        int x1 = x0 + w;
        int y1 = y0 + h;
        for (int y = y0; y < y1; y++) {
            for (int x = x0; x < x1; x++) {
                int xt = x + (y >> 1) + (y & 1) + 1;
                int yt = (y >> 1) - x;
                
                if (xt < 0) xt = 0;
                if (yt < 0) yt = 0;
                if (xt > this.w - 2) xt = this.w - 2;
                if (yt > this.h - 2) yt = this.h - 2;
                
                if (xt < 0 || yt < 0 || xt >= this.w - 1 || yt >= this.h - 1) continue;
               
                int slot1 = 0;
                if (visMap[xt + yt * this.w] > 0) slot1++;
                if (visMap[xt + 1 + yt * this.w] > 0) slot1 += 2;
                if (visMap[xt + (yt + 1) * this.w] > 0) slot1 += 4;
                if (visMap[xt + 1 + (yt + 1) * this.w] > 0) slot1 += 8;
                
                
                int slot2 = 0;
                if (visMap[xt + yt * this.w] > 2) slot2++;
                if (visMap[xt + 1 + yt * this.w] > 2) slot2 += 2;
                if (visMap[xt + (yt + 1) * this.w] > 2) slot2 += 4;
                if (visMap[xt + 1 + (yt + 1) * this.w] > 2) slot2 += 8;
                
                if (slot1 > 0) {
                    if (slot2 == 15) continue;
                    bm.fogBlend(sm.masks[slot2 + 16], x * 96 + (y & 1) * 48, y * 24 - 24);
                }
                
                bm.blend(sm.masks[slot1], x * 96 + (y & 1) * 48, y * 24 - 24);
            }
        }
    }

    public Tile getTile(int x, int y) {
        if (x < 0 || y < 0 || x >= w || y >= h) return Tile.wall;
        return tiles[x + y * w];
    }

    public boolean blocks(double x0, double y0, double z0, double x1, double y1, double z1) {
        int xx0 = (int) Math.floor(x0 / 64);
        int yy0 = (int) Math.floor(y0 / 64);
        int xx1 = (int) Math.floor(x1 / 64);
        int yy1 = (int) Math.floor(y1 / 64);

        if (xx0 < 0) xx0 = 0;
        if (yy0 < 0) yy0 = 0;
        if (xx1 >= w) xx1 = w - 1;
        if (yy1 >= h) yy1 = h - 1;

        for (int y = yy0; y <= yy1; y++) {
            for (int x = xx0; x <= xx1; x++) {
                if (!tiles[x + y * w].blocks()) continue;
                return true;
            }
        }

        return false;
    }

    public List<Entity> getEntityScreenSpace(double x0, double y0, double x1, double y1) {
        List<Entity> result = EntityListCache.get();
        for (Entity e : entities) {
            if (!e.intersectsScreenSpace(x0, y0, x1, y1)) continue;
            result.add(e);
        }

        return result;
    }

    public List<Particle> getParticleScreenSpace(double x0, double y0, double x1, double y1) {
        List<Particle> result = ParticleListCache.get();
        for (Particle p : particles) {
            if (!p.intersectsScreenSpace(x0, y0, x1, y1)) continue;
            result.add(p);
        }

        return result;
    }

    public List<Entity> getEntities(double x0, double y0, double z0, double x1, double y1, double z1) { return blockMap.getEntities(x0, y0, z0, x1, y1, z1); }

}