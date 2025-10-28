package game.particle;

public class RockParticle extends Particle {

    public RockParticle(double x, double y) {
        super(x, y);
        do {
            xa = random.nextDouble() * 2 - 1;
            ya = random.nextDouble() * 2 - 1;
        } while (xa * xa + ya * ya > 1);
        double dd = Math.sqrt(xa * xa + ya * ya);
        sprite = 0;
        double speed = random.nextDouble() * 2 + 0.5;
        xa = xa / dd * speed;
        ya = ya / dd * speed;
    }

}