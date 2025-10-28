package game.entity;

import game.level.Level;
import game.particle.Particle;

import java.awt.*;
import static game.Constants.*;

public class Enemy extends Entity {

    private boolean canDestroy;

    public Enemy(int x, int y) {
        super(x, y);
        color = Color.RED;

        xa = random.nextBoolean() ? -1 : 1;
        ya = random.nextBoolean() ? -1 : 1;
        canDestroy = random.nextBoolean();
        color = canDestroy ? Color.RED : Color.PINK;
    }

    public void tick() {
        if (attemptMove(xa, 0)) {
            xt += xa;
        }

        if (attemptMove(0, ya)) {
            yt += ya;
        }
    }

    public static void spawnRandom(Level level) {
        int x;
        int y;

        do {
            x = random.nextInt(level.w);
            y = random.nextInt(level.h);
        } while (level.getTile(x, y)  != NOTHING);

        Enemy e = new Enemy(x, y);
        level.add(e);
    }

    public boolean attemptMove(int xxa, int yya) {
        int xn = xt + xxa;
        int yn = yt + yya;
        if (xn < 0 || yn < 0 || xn >= level.w || yn >= level.h) return false;

        int tile = level.getTile(xn, yn);

        if (tile != NOTHING) {
            bumpInto(xn, yn, xxa, yya, tile);
        }

        return tile == NOTHING;
    }

    private void bumpInto(int xtn, int ytn, int xxa, int yya, int tile) {
        if (xxa != 0) xa *= -1;
        if (yya != 0) ya *= -1;
        if (tile == PATH) level.resetGame();

        if (tile == GROUND && canDestroy) {
            level.setTile(xtn, ytn, NOTHING);
            for (int i = 0; i < 2; i++) {
                double x = xtn * CELL_SIZE + CELL_SIZE * 0.5 + (random.nextDouble() * 2.0 - 1.0) * 0.5;
                double y = ytn * CELL_SIZE + CELL_SIZE / 2;

                Particle p = new Particle(x, y, TILE_HEIGHTS[tile] * 2);
                p.xa *= 0.25;
                p.ya *= 0.25;
                p.xa += -xxa * 1.1;
                p.ya += -yya * 1.1;
                level.add(p);
            }
        }
    }
}