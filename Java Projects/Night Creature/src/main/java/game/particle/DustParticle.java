package game.particle;

import game.gfx.Art;

import java.awt.*;

public class DustParticle extends Particle {

    private int sprite;

    public DustParticle(double x, double y, double z) {
        super(x, y, z);
        xa *= 0.5;
        ya *= 0.5;
        gravity = 0.01;
        sprite = random.nextInt(2);
    }

    public void render(Graphics2D g) {
        int xx = (int)(x - 4);
        int yy = (int)(y - 4);
        g.drawImage(Art.particles[sprite], xx, yy - (int)z, null);
    }

}