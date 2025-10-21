package game.entity.particle;

import java.awt.*;

public class SplashParticle extends Particle {

    public SplashParticle(double x, double y, double z) {
        super(x, y, z);
        bounce = 0;
        gravity *= 0.5;
        xa *= 0.25;
        ya *= 0.25;
        za *= 0.5;
        color = new Color(0xD6F1CD);
        lifeTime = random.nextInt(30) + 15;
    }

}