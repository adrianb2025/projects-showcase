package game.entity.unit.task;

import game.entity.unit.Unit;

public class FollowTask extends Task {

    protected int time;
    protected Unit target;
    protected double minDist;

    public FollowTask(Unit target, int time, double minDist) {
        this.target = target;
        this.time = time;
        this.minDist = minDist;
    }

    public void tick() {
        time--;
        if (owner.distanceToSqr(target) > minDist * minDist) {
            if (owner.turnTowards(owner.angleTo(target))) owner.moveForward();
        } else idle();
    }

    public boolean finished() { return time <= 0; }

    public void idle() {}

}