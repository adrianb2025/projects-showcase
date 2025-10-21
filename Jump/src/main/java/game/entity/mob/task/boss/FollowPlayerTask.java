package game.entity.mob.task.boss;

public class FollowPlayerTask extends BossTask {

    public void tick() {
        boss.jumpTo(boss.xt > boss.level.player.xt ? -1 : 1, 0);
    }

    public boolean finished() { return true; }

}