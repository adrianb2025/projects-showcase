package game.entity.projectile;

import game.entity.Entity;
import game.entity.unit.Unit;
import game.entity.unit.mob.Mob;
import game.gfx.Bitmap;
import game.weapon.Weapon;

public class Bullet extends Entity {

    public double xo;
    public double yo;
    public double zo;
    public Mob owner;
    public Weapon weapon;
    public int dmg;
    public double xStart;
    public double yStart;
    public double zStart;

    public Bullet(Mob owner, Weapon weapon, double xa, double ya, double za, int dmg) { this(owner, weapon, xa, ya, za, dmg, 4.0); }

    public Bullet(Mob owner, Weapon weapon, double xa, double ya, double za, int dmg, double speed) {
        this.owner = owner;
        this.weapon = weapon;
        xo = x = owner.x;
        yo = y = owner.y;
        zo = z = owner.z + 5.0;
        xStart = x;
        yStart = y;
        zStart = z;
        zh = 1.0;
        yr = 1.0;
        xr = 1.0;
        this.xa = xa * speed;
        this.ya = ya * speed;
        this.za = za * speed;
        this.dmg = dmg;
        isCollideable = false;
    }

    
    public boolean blocks(Entity e) {
        if (e == owner) return false;
        if (e instanceof Bullet) return false;
        return !(e instanceof Mob) || ((Mob)e).team != owner.team;
    }

    
    public void tick() {
        xo = x;
        yo = y;
        zo = z;
        super.tick();
        attemptMove();
    }

    
    public void renderShadow(Bitmap screen, int xp, int yp) {
        double xp0 = (x - y) * 0.75;
        double yp0 = (y + x) * 0.375 - z;
        double xp1 = (xo - yo) * 0.75;
        double yp1 = (yo + xo) * 0.375 - zo;
        double xd = xp1 - xp0;
        double yd = yp1 - yp0;
        int steps = (int)(Math.sqrt(xd * xd + yd * yd) + 1.0);
        for (int i = 0; i < steps; ++i) {
            screen.setPixel((int)(xp0 + xd * i / steps), (int)(yp0 + yd * i / steps + 8.0 + z), 1);
        }
    }

    public void render(Bitmap screen, int xp, int yp) {
        double xp0 = (x - y) * 0.75;
        double yp0 = (y + x) * 0.375 - z;
        double xp1 = (xo - yo) * 0.75;
        double yp1 = (yo + xo) * 0.375 - zo;
        double xd = xp1 - xp0;
        double yd = yp1 - yp0;
        int steps = (int)(Math.sqrt(xd * xd + yd * yd) + 1.0);
        for (int i = 0; i < steps; ++i) {
            if (Math.random() * steps < i) continue;
            int br = 200 - i * 200 / steps;
            int col = 0xFFFF00FF | 65792 * br;
            screen.fill((int)(xp0 + xd * i / steps) - 1, (int)(yp0 + yd * i / steps - 24.0) - 1, (int)(xp0 + xd * i / steps) + 1, (int)(yp0 + yd * i / steps - 24.0) + 1, col);
        }
    }

    public void collide(Entity e, double xxa, double yya, double zza) {
        if (e != null) e.hitBy(this);
        remove();
    }
    
    public int getDamage(Unit unit) { return getDamage(unit.x, unit.y, unit.z); }

    public int getDamage(double xx, double yy, double zz) {
        double fraction;
        double xd = xStart - xx;
        double yd = yStart - yy;
        double zd = zStart - zz;
        double distanceTravelled = Math.sqrt(xd * xd + yd * yd + zd * zd) * 5.0;
        double dmg = this.dmg;
        if (distanceTravelled < weapon.nearDistance) dmg *= weapon.highRamp / 100.0;
        else if (distanceTravelled > weapon.farDistance) dmg *= weapon.lowRamp / 100.0;
        else if (distanceTravelled < weapon.midDistance) {
            fraction = 1.0 - (distanceTravelled - weapon.nearDistance) / (weapon.midDistance - weapon.nearDistance);
            dmg *= (weapon.highRamp * fraction + 100.0 * (1.0 - fraction)) / 100.0;
        } else {
            fraction = (distanceTravelled - weapon.midDistance) / (weapon.farDistance - weapon.midDistance);
            dmg *= (weapon.lowRamp * fraction + 100.0 * (1.0 - fraction)) / 100.0;
        }
        
        int d = (int)(dmg + Weapon.random.nextDouble());
        return d;
    }

}