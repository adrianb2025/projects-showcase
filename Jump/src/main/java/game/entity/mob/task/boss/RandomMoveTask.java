package game.entity.mob.task.boss;

import game.entity.mob.task.Task;

public class RandomMoveTask extends BossTask {

    public void tick() { boss.jumpTo(random.nextInt(3) - 1, 0); }
    public boolean finished() { return boss.onGround; }

}