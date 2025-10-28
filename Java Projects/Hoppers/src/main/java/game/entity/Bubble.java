package game.entity;

import game.gfx.Art;
import game.particle.WhiteParticle;
import game.snd.Sound;

import java.awt.*;
import java.awt.geom.AffineTransform;

public class Bubble extends Entity {

    public int creationTime;
    public int maxCreationTime;
    public double maxScale;
    public double minScale;
    public int lifeTime;
    public int maxLifeTime;

    public Bubble(double x, double y) {
        super(x, y);
        xa = 0;
        ya = 0;
        creationTime = maxCreationTime = 120;
        lifeTime = maxLifeTime = 60 * 15;
        sprite = 4;
        zSort = 1;
        minScale = scale = 0.3;
        maxScale = 1;
        yr = 2;
        ignoreBlocks = true;
    }

    public void tick() {
        scale = (minScale + (maxScale - minScale) * (maxCreationTime - creationTime) / maxCreationTime);

        if (created()) {
            if (lifeTime-- <= 0) die();
            scale = (minScale + (maxScale - minScale) * lifeTime / maxLifeTime);
        }

        xo = x;
        yo = y;
        tickTime++;

        attemptMove();

        xa *= 0.7;
        ya *= 0.9;
    }

    public void render(Graphics2D g, double delta) {
        if (sprite >= 0) {
            double xx = (xo + (x - xo) * delta);
            double yy = (yo + (y - yo) * delta);

            AffineTransform at = g.getTransform();

            double breath = Math.sin(tickTime * 0.1) * 0.05;

            g.translate(xx, yy);
            g.scale(scale + breath, scale + breath);
            g.translate(-xx, -yy);

            g.drawImage(Art.sprites[sprite], (int)(xx - xr * scale), (int)(yy - yr * scale) - 32, null);

            g.setTransform(at);
        }
    }

    public void die() {
        Sound.bubbleDie.play();
        for (int i = 0; i < 5; i++) {
            level.addParticle(new WhiteParticle(x + random.nextInt(16) - 8, y + random.nextInt(16) - 8, random.nextDouble() * 0.04, 0.006));
        }

        remove();
    }

    public void blow() {
        if (creationTime > 0) creationTime--;
    }

    public boolean created() { return creationTime == 0; }
    protected boolean blocks(Entity e) { return created(); }
    protected void touchedBy(Entity e) {}

}