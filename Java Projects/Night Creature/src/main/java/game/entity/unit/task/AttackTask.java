package game.entity.unit.task;

import game.entity.unit.Unit;

public class AttackTask extends FollowTask {

    public AttackTask(Unit target, int time, double minDist) { super(target, time, minDist); }

    public void idle() {
        if (owner.turnTowards(owner.angleTo(target))) owner.weapon.shoot(target);
    }

    public boolean finished() { return owner.weapon == null || super.finished() || target.removed; }

}