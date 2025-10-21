package game.level;

import game.Game;
import game.entity.Entity;
import game.entity.Team;
import game.entity.Warper;
import game.entity.item.ItemEntity;
import game.entity.item.equipment.EquipmentItem;
import game.entity.mob.Guard;
import game.entity.mob.Mob;
import game.entity.mob.Player;
import game.entity.mob.hostile.Bird;
import game.entity.mob.hostile.SlimeFactory;
import game.entity.mob.hostile.boss.AirMage;
import game.entity.mob.hostile.boss.Boss;
import game.entity.mob.hostile.boss.snake.Snake;
import game.entity.mob.hostile.boss.snake.SnakeNeck;
import game.entity.mob.hostile.boss.snake.SnakePart;
import game.entity.particle.ParticleSystem;
import game.entity.tree.*;
import game.gfx.Bitmap;
import game.gfx.CreditsScreen;
import game.gfx.Screen;
import game.gfx.sprite.SpriteCollector;
import game.gfx.util.BitmapHelper;
import game.level.tile.Tile;

import java.util.*;

public class Level {

    private final static int GRASS_TILE = 0xFFFFFFFF;
    private final static int WATER_TILE = 0xFF0000FF;
    private final static int DEEP_WATER_TILE = 0xFF0000CC;
    private final static int SAND_TILE = 0xFFFFFF00;
    private final static int LAVA_TILE = 0xFFFF7F00;
    private final static int ROAD_TILE = 0xFF6B6B6B;
    private final static int SWAMP_TILE = 0xFF90FF00;
    private final static int ROCK_TILE = 0xFFF79090;

    private final static int HOLE_TILE = 0xFF606060;
    private final static int APPLE_TREE = 0xFF00FF00;
    private final static int FIR_TREE = 0xFF00CC00;
    private final static int TREE_STUMP = 0xFF008800;
    private final static int PLAYER_SPAWN = 0xFFFF0000;
    private final static int SHRUBBERY = 0xFF005500;

    private int number;

    private Random random = new Random();

    private int w;
    private int h;

    private Game game;

    private Player player;

    private byte[] tiles;

    private Fog fog;
    private int monsterDensity = 6;
    private List<Entity>[] entitiesInTiles;

    private int mobCount = 0;
    private int bossCount = 0;

    private List<Entity> entities = new ArrayList<Entity>();

    private int respX = 0;
    private int respY = 0;
    private List<Entity> rowSprites = new ArrayList<Entity>();
    private Comparator<Entity> spriteSorter = new Comparator<Entity>() {
        public int compare(Entity e0, Entity e1) {
            if (e1.getY() < e0.getY()) return 1;
            if (e1.getY() > e0.getY()) return -1;
            return 0;
        }
    };

    public Level(int level, Game game) {
        this.game = game;
        number = level;

        loadTiles(level);
        loadObjects(level);
        trySpawn();
    }

    private void loadTiles(int level) {
        Bitmap bmp = BitmapHelper.loadBitmapFromResources("/maps/" + level + ".png");

        w = bmp.getWidth();
        h = bmp.getHeight();

        fog = new Fog(w, h, true);

        tiles = new byte[w * h];

        entitiesInTiles = new ArrayList[w * h];
        for (int i = 0; i < w * h; i++) {
            entitiesInTiles[i] = new ArrayList<Entity>();
        }

        for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {
                int val = bmp.getPixels()[x + y * w];
                int xx = (x << 4) + 8;
                int yy = (y << 4) + 8;
                switch (val) {
                    case GRASS_TILE: {
                        setTile(x, y, Tile.grass);
                        break;
                    }

                    case WATER_TILE: {
                        setTile(x, y, Tile.water);
                        break;
                    }

                    case DEEP_WATER_TILE: {
                        setTile(x, y, Tile.deepWater);
                        break;
                    }

                    case SAND_TILE: {
                        setTile(x, y, Tile.sand);
                        break;
                    }

                    case ROAD_TILE: {
                        setTile(x, y, Tile.road);
                        break;
                    }

                    case SWAMP_TILE: {
                        setTile(x, y, Tile.swamp);
                        break;
                    }

                    case HOLE_TILE: {
                        setTile(x, y, Tile.hole);
                        break;
                    }

                    case ROCK_TILE: {
                        setTile(x, y, Tile.rock);
                        break;
                    }

                    case APPLE_TREE: {
                        add(new AppleTree(xx, yy));
                        break;
                    }

                    case FIR_TREE: {
                        int r = random.nextInt(100);
                        if (r < 72) break;
                        if (r < 75) {
                            add(new TreeStump(xx, yy, game.getSpriteCollector()));
                            break;
                        }
                        if (random.nextBoolean()) {
                            add(new FirTree(xx, yy, game.getSpriteCollector()));
                        } else {
                            add(new PineTree(xx, yy, game.getSpriteCollector()));
                        }
                        break;
                    }
                    case TREE_STUMP: {
                        add(new TreeStump(xx, yy, game.getSpriteCollector()));
                        break;
                    }
                    case SHRUBBERY: {
                        add(new Shrubbery(xx, yy, game.getSpriteCollector()));
                        break;
                    }

                    case LAVA_TILE: {
                        setTile(x, y, Tile.lava);
                        break;
                    }
                }
            }
        }
    }

    private void loadObjects(int level) {
        Bitmap bmp = BitmapHelper.loadBitmapFromResources("/maps/" + level + "O.png");
        for (int y = 0; y < bmp.getHeight(); y++) {
            for (int x = 0; x < bmp.getWidth(); x++) {
                int value = bmp.getPixels()[x + y * bmp.getWidth()];
                int xx = (x << 4) + 8;
                int yy = (y << 4) + 8;

                if (value == 0xFFFFFFFF) continue;

                if (value == 0xFF20FFFF) {
                    SnakePart prev = new Snake(xx, yy);
                    add(prev);
                    for (int a = 0; a < 16; a++) {
                        prev = new SnakeNeck(xx, yy, prev);
                        add(prev);
                    }

                    continue;
                } else if (value == 0xFF21FFFF) {
                    add(new AirMage(xx, yy));
                    continue;
                }

                switch (((value >> 16) & 0xFF)) {
                    case 0xFF: {
                        respX = xx;
                        respY = yy;

                        if (level != 0) add(new Warper(respX + 30, respY - 30, false));

                        if (level > 3) {
                            for (int aa = 0; aa < level - 3; aa++) {
                                add(new Guard(respX + random.nextInt(61) - 30, respY + random.nextInt(61) - 30));
                            }
                        }

                        break;
                    }
                }
            }
        }
    }

    public void trySpawn() {
        for (int i = 0; i < 50; i++) {
            Mob mob = new SlimeFactory();
            if (mob.findStartPos(this)) add(mob);
        }

        for (int i = 0; i < 10; i++) {
            Mob mob = new Bird();
            if (mob.findStartPos(this)) add(mob);
        }
    }

    public void tick() {
        for (int i = 0; i < w * h / 50; i++) {
            int xt = random.nextInt(w);
            int yt = random.nextInt(h);
            getTile(xt, yt).tick(this, xt, yt);
        }

        for (int i = 0; i < entities.size(); i++) {
            Entity entity = entities.get(i);
            int xto = entity.getX() >> 4;
            int yto = entity.getY() >> 4;

            entity.tick();

            if (entity.isRemoved()) {
                if (entity instanceof Mob) mobCount--;
                if (entity instanceof Boss) bossCount--;

                if (bossCount == 0) {

                    for (int aa = 0; aa < Boss.drops[number].length; aa++) {
                        add(new ItemEntity(new EquipmentItem(Boss.drops[number][aa]), entity.getX() + random.nextInt(31) - 15, entity.getY() + random.nextInt(31) - 15));
                    }

                    int offsetX = 0;
                    int offsetY = 0;
                    int xx;
                    int yy;
                    while (true) {
                        xx = (player.getX() >> 4) + offsetX;
                        yy = (player.getY() >> 4) + offsetY;

                        offsetX += random.nextInt(2) * 2 - 1;
                        offsetY += random.nextInt(2) * 2 - 1;

                        if (xx == player.getX() >> 4 && yy == player.getY() >> 4) continue;
                        if (getTile(xx, yy) == Tile.lava) continue;

                        break;
                    }

                    add(new Warper(xx << 4, yy << 4, true));

                    if (number == 6 - 1) CreditsScreen.getInstance().show();

                    bossCount = 100;

                }

                entities.remove(i--);
                removeEntity(xto, yto, entity);
            } else {
                int xt = entity.getX() >> 4;
                int yt = entity.getY() >> 4;

                if (xto != xt || yto != yt) {
                    removeEntity(xto, yto, entity);
                    insertEntity(xt, yt, entity);
                    if (entity instanceof Player) fog.clearFog(xt, yt, ((Player) entity).getClearFogRadius());
                }
            }
        }

        game.getFireParticles().tick();
    }

    public void renderBackground(Screen screen, int xScroll, int yScroll) {
        int xo = xScroll >> 4;
        int yo = yScroll >> 4;
        int w = (screen.getViewPort().getWidth() + 16 - 1) >> 4;
        int h = (screen.getViewPort().getHeight() + 16 - 1) >> 4;
        screen.setOffset(xScroll, yScroll);
        for (int y = yo; y <= h + yo; y++) {
            for (int x = xo; x <= w + xo; x++) {
                getTile(x, y).render(screen, this, x, y);
            }
        }

        screen.setOffset(0, 0);
    }

    public void renderSprites(Screen screen, int xScroll, int yScroll) {
        int offScreen = 4;
        int xo = Math.max((xScroll >> 4), 0);
        int yo = Math.max((yScroll >> 4), 0);
        int w = ((screen.getViewPort().getWidth() + 16 - 1) >> 4) + offScreen;
        int h = ((screen.getViewPort().getHeight() + 16 - 1) >> 4) + offScreen;

        screen.setOffset(xScroll, yScroll);
        for (int y = yo - offScreen; y <= h + yo; y++) {
            for (int x = xo - offScreen; x <= w + xo; x++) {
                if (x < 0 || y < 0 || x >= this.w || y >= this.h) continue;
                rowSprites.addAll(entitiesInTiles[x + y * this.w]);
            }

            if (rowSprites.size() > 0) sortAndRender(screen, rowSprites);
            rowSprites.clear();
        }

        game.getFireParticles().render(screen);
        screen.setOffset(0, 0);
    }

    public void renderFog(Screen screen, int xScroll, int yScroll) {
        int xo = xScroll >> 4;
        int yo = yScroll >> 4;
        int w = (screen.getViewPort().getWidth() + 16 - 1) >> 4;
        int h = (screen.getViewPort().getHeight() + 16 - 1) >> 4;
        screen.setOffset(xScroll, yScroll);
        for (int y = yo; y <= h + yo; y++) {
            for (int x = xo; x <= w + xo; x++) {
                fog.render(screen, x, y);
            }
        }

        screen.setOffset(0, 0);
    }

    private void sortAndRender(Screen screen, List<Entity> list) {
        Collections.sort(list, spriteSorter);
        for (Entity entity : list) {
            entity.render(screen);
        }
    }

    public void add(Entity entity) {
        if (entity instanceof Player) {
            player = (Player) entity;
            player.setX(respX);
            player.setY(respY);
            fog.clearFog(player.getX() >> 4, player.getY() >> 4, player.getClearFogRadius());
        }

        entity.setRemoved(false);
        entities.add(entity);
        entity.init(this);

        if (entity instanceof Mob) mobCount++;
        if (entity instanceof Boss) bossCount++;

        insertEntity(entity.getX() >> 4, entity.getY() >> 4, entity);
    }

    public void remove(Entity e) {
        entities.remove(e);
        int xto = e.getX() >> 4;
        int yto = e.getY() >> 4;
        removeEntity(xto, yto, e);
    }


    private void insertEntity(int x, int y, Entity entity) {
        if (x < 0 || y < 0 || x >= w || y >= h) return;
        entitiesInTiles[x + y * w].add(entity);
    }

    private void removeEntity(int x, int y, Entity entity) {
        if (x < 0 || y < 0 || x >= w || y >= h) return;
        entitiesInTiles[x + y * w].remove(entity);
    }

    public List<Entity> getEntities(int x0, int y0, int x1, int y1, Team team) {
        List<Entity> result = new ArrayList<Entity>();
        int xt0 = (x0 >> 4) - 1;
        int yt0 = (y0 >> 4) - 1;
        int xt1 = (x1 >> 4) + 1;
        int yt1 = (y1 >> 4) + 1;
        for (int y = yt0; y <= yt1; y++) {
            for (int x = xt0; x <= xt1; x++) {
                if (x < 0 || y < 0 || x >= w || y >= h) continue;
                List<Entity> entities = entitiesInTiles[x + y * w];
                for (Entity e : entities) {

                    boolean isTeam = team == null || team == e.getTeam();

                    if (isTeam) {
                        if (e.intersects(x0, y0, x1, y1)) result.add(e);
                    }
                }
            }
        }

        return result;
    }

    public Tile getTile(int x, int y) {
        if (x < 0 || y < 0 || x >= w || y >= h) return Tile.rock;
        return Tile.tiles[tiles[x + y * w]];
    }

    public void setTile(int x, int y, Tile tile) {
        if (x < 0 || y < 0 || x >= w || y >= h) return;
        tiles[x + y * w] = tile.getID();
    }

    public int getWidth() { return w; }
    public int getHeight() { return h; }
    public Player getPlayer() { return player; }
    public int getMonsterDensity() { return Math.max(0, monsterDensity - number); }
    public int getNumber() { return number; }
    public ParticleSystem getFireParticles() { return game.getFireParticles(); }
    public SpriteCollector getSpriteCollector() { return game.getSpriteCollector(); }
    public Game getGame() { return game; }
    public Fog getFog() { return fog; }

}