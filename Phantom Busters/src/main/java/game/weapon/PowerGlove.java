package game.weapon;

import game.entity.projectile.Claw;
import game.entity.unit.mob.Mob;

public class PowerGlove extends Weapon {

    public PowerGlove(Mob owner) {
        super(owner);
        maxAmmoLoaded = 0;
        ammoLoaded = 0;
        maxAmmoCarried = 0;
        ammoCarried = 0;
        shootDelayTime = 1.0;
        startReloadDelayTime = 0.0;
        reloadDelay = 0.0;
        maxRange = 60.0;
        aimLead = 2.0;
        highRamp = 100.0;
        lowRamp = 100.0;
    }

    public void shoot(double xa, double ya, double za) {
        super.shoot(xa, ya, za);
        owner.level.add(new Claw(owner, this, xa, ya, za, 25));
        shootDelay = shootDelayTime;
    }

}