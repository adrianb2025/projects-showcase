package game.weapon;

import game.entity.unit.mob.Mob;

public class GrabberWeapon extends Weapon {

    public GrabberWeapon(Mob owner) {
        super(owner);
        maxAmmoLoaded = 0;
        ammoLoaded = 0;
        maxAmmoCarried = 0;
        ammoCarried = 0;
        shootDelayTime = 0.0;
        startReloadDelayTime = 0.0;
        reloadDelay = 0.0;
        maxRange = 220.0;
        aimLead = 2.0;
        highRamp = 100.0;
        lowRamp = 100.0;
    }

    public void shoot(double xa, double ya, double za) {}

}