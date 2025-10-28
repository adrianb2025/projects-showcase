package game.entity.particle;

import game.util.MathUtil;

import java.awt.*;

public class FireParticle extends Particle {

    private int c0 = 0xA35B70;
    private int c1 = 0xE8B7AC;

    public FireParticle(double x, double y, double z) {
        super(x, y, z);
        lifeTime = maxLifeTime = random.nextInt(40) + 20;
        xa *= 0.05;
        ya *= 0.05;
        za = 0;
        gravity *= -0.2;
    }

    public void tick() {
        super.tick();
        double p = lifeTime / (double) maxLifeTime;
        int c = MathUtil.lerpRGB(c0, c1, p);
        color = new Color(c);
    }

    public void die() {
        if (random.nextInt(10) < 1) level.addParticle(new SmokeParticle(x, y, z));

        super.die();
    }
}
