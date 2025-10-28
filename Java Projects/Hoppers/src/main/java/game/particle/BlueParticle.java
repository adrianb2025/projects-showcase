package game.particle;

public class BlueParticle extends Particle {

    public BlueParticle(double x, double y) {
        super(x, y);
        gravity = -0.08;
        sprite = 1;
        ignoreBlocks = true;
        lifeTime = random.nextInt(10) + 30;
    }

}