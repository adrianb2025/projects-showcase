package game.particle;

public class WhiteParticle extends Particle{

    public WhiteParticle(double x, double y, double speed, double gravity) {
        super(x, y);
        sprite = 3;
        ignoreBlocks = true;
        this.gravity = gravity;
        lifeTime = random.nextInt(80) + 10;

        do {
            xa = random.nextDouble() * 2 - 1;
            ya = random.nextDouble() * 2 - 1;
        } while (xa * xa + ya * ya > 1);

        double dd = Math.sqrt(xa * xa + ya * ya);
        xa = xa / dd * speed;
        ya = ya / dd * speed;
    }

    public WhiteParticle(double x, double y) {
        super(x, y);
        gravity = -0.08;
        sprite = 3;
        ignoreBlocks = true;
        lifeTime = random.nextInt(80) + 10;
    }

}
