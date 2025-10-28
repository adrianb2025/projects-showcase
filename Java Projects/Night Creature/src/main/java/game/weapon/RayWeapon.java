package game.weapon;

import game.entity.Bullet;
import game.entity.unit.Unit;

public class RayWeapon extends Weapon {

    public RayWeapon() { super(100, 15); }

    public void fire(Unit target) {
        double xd = target.x - owner.x;
        double yd = target.y - owner.y;
        double l = Math.sqrt(xd * xd + yd * yd);

        if (l > 0.001) {
            xd /= l;
            yd /= l;
        }

        target.level.addEntity(new Bullet(owner, owner.x, owner.y - 8, xd * 10, yd * 10));
    }

    public int calcDamage(Unit target) { return (int) (Math.random() * 3 + 25); }

}
