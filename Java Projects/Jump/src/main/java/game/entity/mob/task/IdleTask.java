package game.entity.mob.task;

import java.awt.*;

public class IdleTask extends Task {

    public int duration;

    public IdleTask(int duration) { this.duration = duration; }

    public void tick() { duration--; }
    public boolean finished() { return duration <= 0; }

}