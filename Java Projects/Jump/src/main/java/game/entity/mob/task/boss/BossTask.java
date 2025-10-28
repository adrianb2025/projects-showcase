package game.entity.mob.task.boss;

import game.entity.Entity;
import game.entity.mob.Boss;
import game.entity.mob.task.Task;

public abstract class BossTask extends Task {

    public Boss boss;

    public void init(Entity owner) {
        super.init(owner);
        boss = (Boss) owner;
    }

    public void tick() {}
    public boolean finished() { return false; }

}