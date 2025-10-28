package game.entity.unit.mob.task;

import game.entity.unit.mob.Mob;

public class MoveTask extends Task {

    public double x;
    public double y;

    public MoveTask(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public void init(Mob mob) {
        super.init(mob);
        mob.pathFinder.startFindingPath(getTravelCosts(), (int)(mob.x / 64.0), (int)(mob.y / 64.0), (int)(x / 64.0), (int)(y / 64.0));
    }

    public void tick() {
        if (mob.pathFinder.isPathing) mob.pathFinder.continueFindingPath(50);

        if (mob.pathFinder.pathP > 1) {
            int target = mob.pathFinder.path[mob.pathFinder.pathP - 2];
            int xt = target % mob.level.w * 64 + 32;
            int yt = target / mob.level.h * 64 + 32;


            if (mob.distanceTo(xt, yt) > 24.0) {
                if (mob.turnTowards(mob.angleTo(xt, yt))) mob.moveForward();
            } else if(--mob.pathFinder.pathP > 1) {
                mob.pathFinder.startFindingPath(getTravelCosts(), (int)(mob.x / 64.0), (int)(mob.y / 64.0), (int)(x / 64.0), (int)(y / 64.0));
            }
        } else if (mob.distanceTo(x, y) > 4.0 && mob.turnTowards(mob.angleTo(x, y))) {
            mob.moveForward();
        }
    }

    public int[] getTravelCosts() {
        int w = mob.level.w;
        int h = mob.level.h;
        int[] costs = new int[w * h];
        for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {
                if (!mob.player.canSee(x, y)) {
                    costs[x + y * w] = 10;
                    continue;
                }

                double r = 24;
                costs[x + y * w] = mob.level.getTile(x, y).blocks() ? 0 : 5;
            }
        }

        return costs;
    }

    public boolean finished() { return mob.distanceTo(x, y) < 24.0; }

}