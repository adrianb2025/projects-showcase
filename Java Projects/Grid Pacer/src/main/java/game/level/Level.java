package game.level;

import game.Game;
import game.GridPacer;
import game.entity.Entity;
import game.entity.Pacer;
import game.level.block.Block;
import game.level.block.EmptyBlock;
import game.level.block.FinishBlock;
import game.level.block.SolidBlock;
import game.screen.FinishScreen;
import game.screen.GameScreen;
import game.screen.MouseEventHandler;
import game.screen.Screen;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Level {

    public List<Entity> entities = new ArrayList<Entity>();
    public Block[] blocks;
    public final int w;
    public final int h;
    public Block outOfBounds = new SolidBlock();
    public final int xOffset;
    public final int yOffset;
    public final GameScreen gameScreen;
    public boolean started;
    public final int steps;
    public int pacerSpeed = 1;

    public Level(GameScreen gameScreen, int w, int h, int steps) {
        this.gameScreen = gameScreen;
        this.w = w;
        this.h = h;
        this.steps = steps;
        xOffset = GridPacer.WIDTH - (w << 4) >> 1;
        yOffset = GridPacer.HEIGHT - (h << 4) >> 1;
        blocks = new Block[w * h];
        for (int i = 0; i < w * h; i++) {
            blocks[i] = new EmptyBlock();
            if (i == w * h - 1) blocks[i] = new FinishBlock();
            blocks[i].init(this);
        }
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

        for (int i = 0; i < w * h; i++) {
            blocks[i].tick();
        }
    }

    public void render(Graphics2D g) {
        for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {
                blocks[x + y * w].render(g, x, y, xOffset, yOffset);
            }
        }

        for (Entity e : entities) {
            e.render(g, xOffset, yOffset);
        }
    }

    public void placeBlockAt(int x, int y) {
        if (x == 0 && y == 0) return;

        Block block = getBlock(x, y);
        if (block == outOfBounds) return;
        if (block instanceof FinishBlock) return;

        Block solid = new SolidBlock();
        if (block instanceof SolidBlock) solid = new EmptyBlock();

        setBlock(solid, x, y);
    }

    public int calcSteps() {
        int result = 0;
        for (int i = 0; i < blocks.length; i++) {
            result += blocks[i].steps;
        }
        return result;
    }

    public void steppedOn(int x, int y) {
        if (x < 0 || y < 0 || x >= w || y >= h) return;
        blocks[x + y * w].steppedOn();
    }

    public void steppedFrom(int x, int y) {
        if (x < 0 || y < 0 || x >= w || y >= h) return;
        blocks[x + y * w].steppedFrom();
    }

    public Block getBlock(int x, int y) {
        if (x < 0 || y < 0 || x >= w || y >= h) return outOfBounds;
        return blocks[x + y * w];
    }

    public void setBlock(Block block, int x, int y) {
        if (x < 0 || y < 0 || x >= w || y >= h) return;
        blocks[x + y * w] = block;
    }

    public void addEntity(Entity e) {
        entities.add(e);
        e.init(this);
    }

    public void startGame() {
        addEntity(new Pacer());
        started = true;
    }

    public void finish() {
        int totalSteps = calcSteps();
        if (steps < totalSteps) {
            gameScreen.setScreen(new FinishScreen(gameScreen, "Completed: " + totalSteps, new MouseEventHandler() {
                public void onClick(Screen screen) {
                    screen.setScreen(new GameScreen());
                }
            }));
        } else {
            gameScreen.setScreen(new FinishScreen(gameScreen, "Incomplete: " + totalSteps, new MouseEventHandler() {
                public void onClick(Screen screen) {
                    entities.clear();
                    started = false;

                    for (int i = 0; i < blocks.length; i++) {
                        blocks[i].steps = 0;
                    }

                    screen.setScreen(gameScreen);
                }
            }));
        }
    }

}