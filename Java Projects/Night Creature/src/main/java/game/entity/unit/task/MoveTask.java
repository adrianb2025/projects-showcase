package game.entity.unit.task;

public class MoveTask extends Task {

    private double xt;
    private double yt;

    public MoveTask(double xt, double yt) {
        this.xt = xt;
        this.yt = yt;
    }

    public void updateTarget(double xt, double yt) {
        this.xt = xt;
        this.yt = yt;
    }

    public void tick() {
        if (owner.turnTowards(owner.angleTo(xt, yt))) owner.moveForward();
    }

    public boolean finished() { return owner.distanceToSqr(xt, yt) < 4; }

}