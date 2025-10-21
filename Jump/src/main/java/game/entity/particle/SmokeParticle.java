package game.entity.particle;

import game.gfx.Art;

import java.awt.*;

public class SmokeParticle extends Particle {

    public SmokeParticle(double x, double y, double z) {
        super(x, y, z);
        xa *= 0.05;
        ya *= 0.05;
        za = 0;
        gravity *= -0.2;
        lifeTime = maxLifeTime = random.nextInt(60) + 15;
    }

    public void render(Graphics2D g) {
        int x = (int)(this.x - 4);
        int y = (int)(this.y - 4);
        int t = (int)((1.0 - lifeTime / (double) maxLifeTime) * 2.9);
        g.drawImage(Art.particles[t], x, y, null);
    }

}