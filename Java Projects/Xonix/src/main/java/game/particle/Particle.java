package game.particle;

import game.level.Level;

import java.awt.*;
import java.util.Random;
import static game.Constants.*;

public class Particle {
    public static Random random = new Random();
    public int xt;
    public int yt;
    public double x;
    public double y;
    public double z;
    public double xa;
    public double ya;
    public double za;
    public int sz = 2;
    public Color color = Color.WHITE;
    public Level level;
    public double bounceX = 0.8;
    public double bounceY = 0.8;
    public double bounceZ = 0.4;
    public double gravity = 0.264;
    public double friction = 0.98;
    public int lifeTime;
    public boolean removed;

    public Particle(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;

        do {
            xa = random.nextDouble() * 2.0 - 1.0;
            ya = random.nextDouble() * 2.0 - 1.0;
            za = random.nextDouble() * 3;
        } while (xa * xa + ya * ya > 1.0);

        xa *= 1;
        ya *= 1;
        za += 0.5;

        lifeTime = random.nextInt(30) + 15;
    }

    public void init(Level level) {
        this.level = level;
    }

    public void tick() {
        if (--lifeTime == 0) removed = true;

        int xxa = (int) Math.abs(xa * 1000);
        for (int i = xxa; i > 0; i--) {
            if (attemptMove(xa * (i / (double) xxa), 0, 0)) {
                x += xa * (i / (double) xxa);
                break;
            } else {
                xa *= -bounceX;
            }
        }

        int yya = (int) Math.abs(ya * 1000);
        for (int i = yya; i > 0; i--) {
            if (attemptMove(0, ya * (i / (double) yya), 0)) {
                y += ya * (i / (double) yya);
                break;
            } else {
                ya *= -bounceY;
            }
        }

        int zza = (int) Math.abs(za * 1000);
        for (int i = zza; i > 0; i--) {
            if (attemptMove(0, 0, za * (i / (double) zza))) {
                z += za * (i / (double) zza);
                break;
            } else {
                sz = 1;
                xa *= 0.77;
                ya *= 0.77;
                za *= -bounceZ;
            }
        }

        xa *= friction;
        ya *= friction;
        za *= friction;
        za -= gravity;

        xt = (int) (x / CELL_SIZE);
        yt = (int) (y / CELL_SIZE);
    }

    public boolean attemptMove(double xxa, double yya, double zza) {
        int xtn = (int) ((x + xxa) / CELL_SIZE);
        int ytn = (int) ((y + yya) / CELL_SIZE);
        double zn = z + zza;

        if (xtn < 0 || ytn < 0 || xtn >= level.w || ytn >= level.h) return false;

        int tile = level.getTile(xtn, ytn);
        int height = TILE_HEIGHTS[tile];

        if (zn < height) {
            //z = height;
            return false;
        }

        return zn > height || tile == NOTHING;
    }

    public void render(Graphics2D g) {
        int x = (int) this.x - sz / 2;
        int y = (int) this.y - sz / 2;

        g.setColor(Color.BLACK);
        g.fillRect(x, y, sz, sz);

        g.setColor(Color.BLACK);
        g.fillRect(x, y - (int) z, sz, sz);

        g.setColor(color);
        g.fillRect(x - 1, y - (int) z - 1, sz, sz);
    }
}
