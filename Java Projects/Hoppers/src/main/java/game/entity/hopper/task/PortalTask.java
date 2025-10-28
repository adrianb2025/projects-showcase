package game.entity.hopper.task;

import game.entity.hopper.Hopper;
import game.level.Level;
import game.particle.WhiteParticle;
import game.snd.Sound;

public class PortalTask extends Task {

    private int tickTime;
    private Level level;

    public PortalTask(Hopper owner, Level level) {
        super(owner);
        this.level = level;
    }

    public void tick() {
        tickTime++;

        for (int i = 0; i < 3; i++) {
            level.addParticle(new WhiteParticle(owner.x + random.nextInt(16) - 8, owner.y + owner.yr));
        }
    }

    public boolean finished() {
        if (tickTime >= 60) {
            owner.finished = true;
            owner.remove();
            Sound.portaled.play();
            return true;
        }

        return false;
    }

}