package game.entity.hopper.task;

import game.entity.hopper.Hopper;

public class IdleTask extends Task {

    public IdleTask(Hopper owner) { super(owner); }

    public void tick() {}
    public boolean finished() { return true; }

}