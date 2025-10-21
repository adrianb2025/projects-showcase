package game.entity.particle;

public class FlowTextParticle extends TextParticle {

    public FlowTextParticle(String text, int x, int y, int col) {
        super(text, x, y, col);
        zz = 0;
        time = 35;
    }

    public void tick() {
        super.tick();
        y -= 2;
    }

}