package game.weapon;

import game.entity.projectile.Bullet;
import game.entity.unit.mob.Mob;

public class HunterWeapon extends Weapon {

    public HunterWeapon(Mob owner) {
        super(owner);
        maxAmmoLoaded = 0;
        ammoLoaded = 0;
        maxAmmoCarried = 0;
        ammoCarried = 0;
        maxRange = 350.0;
        shootDelayTime = 0.5;
        startReloadDelayTime = 1.0;
        reloadDelayTime = 0.5;
        highRamp = 100.0;
        lowRamp = 100.0;
    }

    public void shoot(double xa, double ya, double za) {
        super.shoot(xa, ya, za);
        double spread = 0.001;
        for (int i = 0; i < 1; ++i) {
            double xxa = xa + (random.nextDouble() - 0.5) * spread;
            double yya = ya + (random.nextDouble() - 0.5) * spread;
            double zza = za + (random.nextDouble() - 0.5) * spread * 0.5;
            owner.level.add(new Bullet(owner, this, xxa, yya, zza, 50, 64.0));
        }
        
        shootDelay = shootDelayTime;
    }

}