package game.weapon;

import game.entity.unit.Unit;

public abstract class Weapon {

    public final double minDist;
    public final int rechargeTime;
    public Unit owner;
    public int chargeTime;

    protected Weapon(double minDist, int rechargeTime) {
        this.minDist = minDist;
        this.rechargeTime = rechargeTime;
    }

    public void init(Unit owner) { this.owner = owner; }

    public void tick() {
        if (chargeTime > 0) chargeTime--;
    }

    public final void shoot(Unit target) {
        if (chargeTime <= 0) {
            chargeTime = rechargeTime;
            fire(target);
        }
    }

    public abstract void fire(Unit target);
    public abstract int calcDamage(Unit target);

}