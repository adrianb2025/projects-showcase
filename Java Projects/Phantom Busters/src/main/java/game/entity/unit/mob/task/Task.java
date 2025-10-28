package game.entity.unit.mob.task;

import game.entity.unit.mob.Mob;

public class Task {

    public Mob mob;

    public void init(Mob mob) { this.mob = mob; }

    public void tick() {}
    public boolean finished() { return true; }

}