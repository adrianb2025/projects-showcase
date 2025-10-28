package game.particle;

public class FireParticle extends Particle {

    public FireParticle(double x, double y, int time) {
        super(x, y);
        gravity = -0.05;
        sprite = 2;
        ignoreBlocks = true;
        lifeTime = time;
        xa = (random.nextDouble() * 2 - 1) * 0.2;
    }

    public FireParticle(double x, double y) {
        super(x, y);
        gravity = -0.05;
        sprite = 2;
        ignoreBlocks = true;
        lifeTime = random.nextInt(10) + 10;
        xa = (random.nextDouble() * 2 - 1) * 0.2;
    }

}