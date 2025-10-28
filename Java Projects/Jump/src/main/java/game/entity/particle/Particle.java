package game.entity.particle;

import game.entity.Entity;

import java.awt.*;

public abstract class Particle extends Entity {
    public int lifeTime;
    public int maxLifeTime;

    public Particle(double x, double y, double z) {
        super(x, y);
        this.z = z;

        do {
            xa = random.nextDouble() * 2.0 - 1.0;
            ya = random.nextDouble() * 2.0 - 1.0;
            za = random.nextDouble();
        } while (Math.sqrt(xa * xa + ya * ya + za * za) > 1);
        xa *= 2;
        ya *= 2;
        za += 1;

        friction = 0.97;
        bounce = 0.8;
        lifeTime = maxLifeTime = 300;
    }

    public void tick() {
        if (lifeTime-- <= 0) {
            die();
            return;
        }

        x += xa;
        y += ya;
        z += za;
        xa *= friction;
        ya *= friction;
        za *= friction;
        za += gravity;

        if (z < 0) {
            za *= -bounce;
            z = 0;
        }
    }

    public void render(Graphics2D g) {
        int x = (int)this.x;
        int y = (int)this.y;

        g.setColor(Color.GRAY);
        g.fillRect(x, y, 1, 1);

        g.setColor(color);
        g.fillRect(x, y - (int)z, 1, 1);
    }
}