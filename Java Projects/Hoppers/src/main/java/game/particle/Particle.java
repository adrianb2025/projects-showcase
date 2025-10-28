package game.particle;

import game.gfx.Art;
import game.level.Level;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.util.Random;

public class Particle {

    protected static final Random random = new Random();
    protected Level level;

    public double scale = 1.0;
    public double x;
    public double y;
    public double xa;
    public double ya;
    public double rot;
    public double rotA;
    public boolean removed;
    public int sprite = -1;
    public double bounce = 0.8;
    public int tickTime = 0;
    public int lifeTime = 120;
    public double gravity = 0.09;
    public boolean ignoreBlocks;

    public Particle(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public void init(Level level) { this.level = level; }

    public void tick() {
        if (lifeTime-- <= 0) {
            remove();
            return;
        }

        tickTime++;

        attemptMove();

        rot += rotA;

        rotA += xa * 0.2;
        rotA *= 0.3;

        xa *= level.friction;
        ya *= level.friction;
        ya += gravity;
    }

    public void remove() { removed = true; }


    public void attemptMove() {
        int xSteps = (int) (Math.abs(xa * 1000) + 1);
        for (int i = xSteps; i >= 0; i--) {
            if (isFree(xa * i / xSteps, 0)) {
                x += xa * i / xSteps;
                break;
            } else xa *= -bounce;
        }
        int ySteps = (int) (Math.abs(ya * 1000) + 1);
        for (int i = ySteps; i >= 0; i--) {
            if (isFree(0, ya * i / ySteps)) {
                y += ya * i / ySteps;
                break;
            } else ya *= -bounce;
        }
    }

    private boolean isFree(double xa, double ya) {
        if (ignoreBlocks) return true;
        int xto0 = (int) (x - 4) >> 5;
        int yto0 = (int) (y - 4) >> 5;
        int xto1 = (int) (x + 4) >> 5;
        int yto1 = (int) (y + 4) >> 5;

        int xt0 = (int) ((x + xa) - 4) >> 5;
        int yt0 = (int) ((y + ya) - 4) >> 5;
        int xt1 = (int) ((x + xa) + 4) >> 5;
        int yt1 = (int) ((y + ya) + 4) >> 5;
        for (int yt = yt0; yt <= yt1; yt++) {
            for (int xt = xt0; xt <= xt1; xt++) {
                if (xt >= xto0 && xt <= xto1 && yt >= yto0 && yt <= yto1) continue;
                if (level.getTile(xt, yt).blocks()) return false;
            }
        }

        return true;
    }

    public void render(Graphics2D g) {
        if (sprite >= 0) {
            AffineTransform at = g.getTransform();

            g.translate(x, y);
            g.scale(scale, scale);
            g.rotate(rot);
            g.translate(-x, -y);

            g.drawImage(Art.particles[sprite], (int) (x - 4), (int) (y - 4), null);


            g.setTransform(at);
        }
    }

}