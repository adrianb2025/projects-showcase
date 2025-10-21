package game.level;

import game.Game;
import game.InputHandler;
import game.JumpComponent;
import game.entity.Entity;
import game.entity.mob.Boss;
import game.entity.mob.Cow;
import game.entity.mob.Leaf;
import game.entity.mob.Player;
import game.entity.particle.Particle;
import game.gfx.Art;
import game.level.event.*;
import game.level.event.Event;
import game.level.tile.Tile;
import game.screen.GameOverScreen;
import game.screen.GameScreen;
import game.screen.HappyEndingScreen;
import game.snd.Sound;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.util.*;
import java.util.List;

public class Level {

    public static final Random random = new Random();
    public List<Entity> entities = new ArrayList<Entity>();
    public List<Particle> particles = new ArrayList<Particle>();
    public List<Entity> renderList = new ArrayList<Entity>();
    public final int w;
    public final int h;
    public int[] tiles;
    public int tickTime;
    public int dialogueTime;
    public int maxDialogueTime;
    public int dialogueDelay;
    public boolean dialogueSkipped;
    public String dialogueText = "";
    public String dialogueOwner = "";
    public int prevDialogueChar = -1;
    public int yScroll;
    public Player player;
    public Boss boss;
    public final GameScreen gameScreen;
    public int linesGenerated;
    public int shakeTime;
    public boolean showTiles = true;
    public boolean canMoveForward = true;
    public Event event;
    public Queue<Event> events = new LinkedList<Event>();
    private int prevDir = 1;

    public Level(GameScreen gameScreen, Player player, Boss boss, int w, int h) {
        this.gameScreen = gameScreen;
        this.player = player;
        this.boss = boss;
        this.w = w;
        this.h = h;
        tiles = new int[w * h];
        events.add(new IdleEvent(5));
        events.add(new LavaEvent(35));
        events.add(new IdleEvent(5));
        events.add(new CowRainEvent(30));
        events.add(new IdleEvent(15));
        events.add(new BossEvent(20, boss));
    }

    public void generate(int from, int distance) {
        for (int y = 0; y < distance; ++y) {
            int yp = h - y - linesGenerated - 1 - from;
            int tile = random.nextInt(10) <= 5 ? Tile.water.id : Tile.grass.id;
            prevDir = prevDir > 0 ? -1 : 1;
            for (int x = 0; x < w; ++x) {
                int index = x + yp * w;

                int desiredTile = tile;

                if (desiredTile == Tile.grass.id && random.nextDouble() < 0.1)  desiredTile = Tile.rock.id;
                if (desiredTile == Tile.water.id && random.nextDouble() < 0.5) addEntity(new Leaf((x << 4) + 8, (yp << 4) + 8, prevDir));


                tiles[index] = desiredTile;
            }
        }

        linesGenerated += distance;
    }

    public void addEntity(Entity e) {
        entities.add(e);
        e.init(this);

        if (e instanceof Boss) System.out.println("BOSSS ADDEDDDD");
    }

    public void addParticle(Particle p) {
        particles.add(p);
        p.init(this);
    }

    public void setEvent(Event event) {
        if (event == null) return;
        this.event = event;
        event.init(this, player);
    }

    public void gameOver() { gameScreen.setScreen(new GameOverScreen(gameScreen)); }
    public void happyEnding() { gameScreen.setScreen(new HappyEndingScreen(gameScreen)); }


    public void shake(int shakeTime) {
        this.shakeTime = shakeTime;
        Sound.cow.play();
    }

    public void tick(InputHandler input) {
        tickTime++;

        if (dialogueTime > 0) dialogueTime--;

        if (dialogueDelay > 0) {
            dialogueDelay--;
            if (dialogueDelay <= 0) {
                dialogueSkipped = true;
                dialogueText = "";
            }
        }

        if (input.isAnyKeyClicked()) {
            if (dialogueTime > 0) dialogueTime = 0;
            else if (dialogueDelay <= 0) dialogueDelay = 30;
        }

        int charTo = (int)((1.0 - dialogueTime / (double)maxDialogueTime) * dialogueText.length());
        if (prevDialogueChar != charTo) {
            prevDialogueChar = charTo;
            Sound.trrrrr.play();
        }


        for (int i = 0; i < particles.size(); i++) {
            Particle p = particles.get(i);

            if (!p.removed) {
                p.tick();
                continue;
            }

            particles.remove(i--);
        }

        if (event != null) event.tick();
        if (event == null || event.finished()) setEvent(events.poll());

        int offs = 120;
        if (event instanceof BossEvent) offs = 200;

        yScroll = (int) player.y - offs;

        if (shakeTime > 0) {
            shakeTime--;
            double xa = Math.cos(shakeTime / 15.0 * Math.PI * 2) * 2;
            double ya = Math.sin(shakeTime / 15.0 * Math.PI * 2) * 2;
            yScroll = (int)(yScroll + xa * ya);
        }

        for (int i = 0; i < entities.size(); i++) {
            Entity e = entities.get(i);

            if (!e.removed) {
                e.tick();
                continue;
            }

            entities.remove(i--);
        }
    }

    public void render(Graphics2D g) {
        if (showTiles) {
            int y0 = yScroll >> 4;
            int y1 = y0 + h + 1;

            if (y0 < 0) y0 = 0;
            if (y1 > h) y1 = h;

            for (int y = y0; y < y1; y++) {
                for (int x = 0; x < w; x++) {
                    int tile = getTile(x, y);
                    if (tile == -1) continue;
                    Tile.tiles[tile].render(this, g, x, y, yScroll);
                }
            }

        }

        renderList.clear();
        renderList.addAll(entities);
        renderList.addAll(particles);

        Collections.sort(renderList);

        AffineTransform at = g.getTransform();

        g.translate(0, -yScroll);

        for (Entity e : renderList) {
            if (e.removed) continue;
            e.render(g);
        }

        g.setTransform(at);

    }

    public void spawnCow(int z) {
        int spawnX;
        int spawnY;
        int t0;
        int t1;
        Cow cow = new Cow(0, 0);
        int attempts = 100;
        Entity e0 = null;
        Entity e1 = null;

        do {
            spawnX = random.nextInt(w - 1);
            spawnY = player.yt - random.nextInt(10);
            t0 = getTile(spawnX, spawnY);
            t1 = getTile(spawnX + 1, spawnY);
            e0 = getEntity(cow, spawnX, spawnY, null);
            e1 = getEntity(cow, spawnX + 1, spawnY, null);
        } while (attempts-- > 0 && (t0 == -1 || t1 == -1 || !Tile.tiles[t0].canSpawn(cow) || !Tile.tiles[t1].canSpawn(cow) || e0 != null || e1 != null));

        if (attempts > 0) {
            cow.x = spawnX << 4;
            cow.y = spawnY << 4;
            cow.z = z;
            addEntity(cow);
        }

    }

    public Entity getEntity(Entity owner, int x0, int y0, int x1, int y1, EntityFilter filter) {
        for (Entity e : entities) {
            if (e == owner || !e.intersects(x0, y0, x1, y1) || filter != null && !filter.accept(e)) continue;
            return e;
        }

        return null;
    }

    public Entity getEntity(Entity owner, int xt, int yt, EntityFilter filter) {
        for (Entity e : entities) {
            if (e == owner || e.xt != xt || e.yt != yt || filter != null && !filter.accept(e)) continue;
            return e;
        }

        return null;
    }

    public void startDialogue(String owner, String text, int duration) {
        dialogueTime = maxDialogueTime = duration;
        dialogueDelay = 0;
        dialogueText = text;
        dialogueOwner = owner;
        dialogueSkipped = false;
    }

    public boolean canStartDialogue() { return dialogueSkipped; }

    public void renderUI(Graphics2D g) {
        if (event instanceof BossEvent) {
            Art.drawProgressBar(10, 226, 140, 4, g, player.c0, player.c1, player.health / (double) player.maxHealth);
            Art.drawProgressBar(10, 10, 140, 4, g, player.c0, player.c1, boss.health / (double) boss.maxHealth);
        }

        if (!dialogueText.isEmpty() && prevDialogueChar != -1) {
            String text = dialogueText.substring(0, prevDialogueChar);
            Art.drawString(text, 15, 115, g);
            text = dialogueOwner + ":";
            Art.drawString(text, 10, 105, g);
        }
    }

    public int getTile(int x, int y) {
        if (x < 0 || y < 0 || x >= w || y >= h) return Tile.rock.id;
        return tiles[x + y * w];
    }

    public void setTile(int x, int y, int id) {
        if (x < 0 || y < 0 || x >= w || y >= h) return;
        tiles[x + y * w] = id;
    }

    public static interface EntityFilter {
        public boolean accept(Entity e);
    }

}