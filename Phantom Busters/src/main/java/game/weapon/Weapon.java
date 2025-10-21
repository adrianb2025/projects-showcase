package game.weapon;

import game.entity.unit.mob.Mob;

import java.util.Random;

public class Weapon {

    public static final Random random = new Random();
    public Mob owner;
    public int maxAmmoLoaded;
    public int maxAmmoCarried;
    public int ammoLoaded;
    public int ammoCarried;
    public double shootDelayTime;
    public double startReloadDelayTime;
    public double reloadDelayTime;
    public double shootDelay;
    public double reloadDelay;
    public boolean wasReloading;
    public boolean aimAtGround;
    public double maxRange = 150.0;
    public double aimLead = 0.03;
    public double nearDistance = 0.0;
    public double midDistance = 512.0;
    public double farDistance = 1024.0;
    public double highRamp = 175.0;
    public double lowRamp = 50.0;

    public Weapon(Mob owner) { this.owner = owner; }

    public void tick() {
        if (shootDelay > 0.0) shootDelay -= 1 / 60.0;
        wasReloading = reloadDelay > 0.0;
        if (reloadDelay > 0.0) reloadDelay -= 1 / 60.0;
    }

    public void shoot(double xa, double ya, double za) {
        ammoLoaded--;
    }

    public boolean canUse() { return shootDelay <= 0.0 && reloadDelay <= 0.0; }

    public void reload() {
        if (ammoLoaded < maxAmmoLoaded && ammoCarried > 0) {
            ammoLoaded++;
            ammoCarried--;
            reloadDelay = !wasReloading ? startReloadDelayTime : reloadDelayTime;
        }
    }

    public boolean canPickupAmmo() {
        if (maxAmmoCarried == 0) return ammoLoaded < maxAmmoLoaded;
        return ammoCarried < maxAmmoCarried;
    }

    public void takeAmmo(int ammo) {
        if (maxAmmoCarried == 0) {
            ammoLoaded += ammo;
            if (ammoLoaded > maxAmmoLoaded) ammoLoaded = maxAmmoLoaded;
        } else {
            ammoCarried += ammo;
            if (ammoCarried > maxAmmoCarried) ammoCarried = maxAmmoCarried;
        }
    }

    public int getAmmoCapacity() {
        if (maxAmmoCarried == 0) return maxAmmoLoaded;
        return maxAmmoCarried;
    }

}