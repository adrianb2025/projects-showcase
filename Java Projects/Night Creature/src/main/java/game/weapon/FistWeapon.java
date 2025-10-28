package game.weapon;

import game.entity.unit.Unit;

public class FistWeapon extends Weapon {

    public FistWeapon() { super(4, 30); }

    public void fire(Unit target) { target.hurt(owner); }
    public int calcDamage(Unit target) { return 1; }

}