package game.entity.unit.mob.task;

import game.entity.unit.mob.Mob;

public class AttackTask extends MoveTask {

    public Mob target;
    public double maxRange;

    public AttackTask(Mob target) { this(target, 0); }

    public AttackTask(Mob target, double maxRange) {
        super(target.x, target.y);
        this.target = target;
        this.maxRange = maxRange;
    }

    public void tick() {
        if (!hasTarget()) return;

        double weaponRange = mob.weapon.maxRange;
        double distanceToSql = mob.distanceToSqr(target);

        if (distanceToSql < weaponRange * weaponRange) mob.updateWeapon(target);
        else {
            x = target.x;
            y = target.y;
            super.tick();
        }

        if (maxRange > 0 && distanceToSql > maxRange * maxRange) target = null;
    }

    public boolean hasTarget() { return target != null && target.isAlive(); }
    public boolean finished() { return !hasTarget(); }

}