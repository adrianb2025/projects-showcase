package game.entity.projectile;

import game.entity.unit.mob.Mob;
import game.gfx.Bitmap;
import game.weapon.Weapon;

public class Claw extends Bullet {

    public Claw(Mob owner, Weapon weapon, double xa, double ya, double za, int dmg) { super(owner, weapon, xa, ya, za, dmg); }

    public void renderShadow(Bitmap screen, int xp, int yp) {}
    public void render(Bitmap screen, int xp, int yp) {}

}