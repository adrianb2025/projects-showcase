package game.particle;

import game.gfx.Renderable;
import game.level.Level;

import java.awt.*;
import java.util.Random;

public abstract class Particle implements Renderable {

    public static final Random random = new Random();
    public double x;
    public double y;
    public double z;
    public double xa;
    public double ya;
    public double za;
    public int lifeTime;
    public int maxLifeTime;
    public Level level;
    public double gravity = 0.04;
    public double bounce = 0.7;
    public boolean removed;

    public Particle(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;

        do {
            xa = random.nextDouble() * 2.0 - 1.0;
            ya = random.nextDouble() * 2.0 - 1.0;
            za = random.nextDouble() - 1.0;
        } while (xa * xa + ya * ya + za * za > 1);
        lifeTime = maxLifeTime = random.nextInt(60) + 140;
    }

    public void init(Level level) { this.level = level; }

    public void tick() {
        if (lifeTime-- <= 0) {
            die();
            return;
        }

        x += xa;
        y += ya;
        z += za;
        xa *= 0.97;
        ya *= 0.97;
        za *= 0.97;

        if (z < 0) {
            z = 0;
            xa *= 0.77;
            ya *= 0.77;
            za *= -bounce;
        }

        za += gravity;
    }

    public void render(Graphics2D g) {
        int xx = (int)x;
        int yy = (int)y;
        g.setColor(Color.BLACK);
        g.fillRect(xx, yy, 1, 1);

        g.setColor(new Color(0xFF00FF));
        g.fillRect(xx, yy - (int)z, 1, 1);
    }

    public void die() { removed = true; }

}