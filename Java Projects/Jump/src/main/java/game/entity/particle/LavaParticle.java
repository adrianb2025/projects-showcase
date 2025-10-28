package game.entity.particle;

import game.util.MathUtil;

import java.awt.*;

public class LavaParticle extends Particle {

    private int c0 = 0xA35B70;
    private int c1 = 0xE8B7AC;

    public LavaParticle(double x, double y, double z) {
        super(x, y, z);
        lifeTime = maxLifeTime = random.nextInt(40) + 20;
        xa *= 0.1;
        ya *= 0.1;
    }

    public void tick() {
        super.tick();
        double p = lifeTime / (double) maxLifeTime;
        int c = MathUtil.lerpRGB(c0, c1, p);
        color = new Color(c);
    }
}