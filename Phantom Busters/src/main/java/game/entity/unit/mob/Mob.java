package game.entity.unit.mob;

import game.Player;
import game.ai.PathFinder;
import game.entity.Entity;
import game.entity.unit.Unit;
import game.entity.unit.mob.task.IdleTask;
import game.entity.unit.mob.task.Task;
import game.gfx.Art;
import game.gfx.Bitmap;
import game.level.Level;
import game.particle.MeatDebris;
import game.weapon.HunterWeapon;
import game.weapon.Weapon;

import java.util.List;

public class Mob extends Unit {

    public int charClass;
    public int shootTime = 0;
    public Weapon weapon = new HunterWeapon(this);
    public PathFinder pathFinder;
    public Task task = new IdleTask();

    public Mob(Player player) {
        super(player);
    }

    public void init(Level level) {
        super.init(level);
        pathFinder = new PathFinder(level.w, level.h);
    }

    public void shootAt(Mob target) {
        double lead = Math.sqrt(target.distanceToSqr(this)) * weapon.aimLead / 5.0;
        double xd = target.x + target.xa * lead - x;
        double yd = target.y + target.ya * lead - y;
        double zd = target.z + target.za * lead - z;
        if (weapon.aimAtGround) zd = 0.0 - (z + 5.0);
        double dd = Math.sqrt(xd * xd + yd * yd + zd * zd);
        weapon.shoot(xd /= dd, yd /= dd, zd /= dd);
        dir = Math.atan2(yd, xd);
        shootTime = 20;
    }

    public void tick() {
        if (deadTime > 0) {
            if (visRange > deadTime / 4) visRange = deadTime / 4;
            deadTime--;
            if (deadTime == 0) remove();
            return;
        }

        super.tick();

        if (tickTime % 60 == 0 && health < maxHealth) health++;
        if (shootTime > 0) shootTime--;
        if (selectInterval > 0) selectInterval--;

        if (hurtTime > 0) hurtTime--;

        if (health <= 0) {
            die();
            return;
        }

        weapon.tick();
        updateWeapon(null);

        xa *= isOnGround() ? 0.91 : 0.99;
        ya *= isOnGround() ? 0.91 : 0.99;
        za -= 0.08;
        task.tick();

        if (task.finished()) setTask(getNextTask());
        attemptMove();
        if (xa * xa + ya * ya < 0.02) walkStep = 0;


        double r = 3.0;
        for (Entity e : level.getEntities(x - r, y - r, z - r, x + r, y + r, z + r)) {
            Mob u = (Mob) e;
            if (!(e instanceof Mob) || e == this || !u.isAlive()) continue;
            double xd = u.x - x;
            double yd = u.y - y;
            double dd = Math.sqrt(xd * xd + yd * yd);

            if (xd * xd + yd * yd < 0.01) {
                xd = 0.01;
                yd = 0.0;
            }

            if (!((dd) < r * r)) continue;

            xd = xd / dd / dd * 0.5;
            yd = yd / dd / dd * 0.5;

            knockBack(-xd, -yd, 0.0);
            u.knockBack(xd, yd, 0.0);
        }
    }

    public void setTask(Task task) {
        this.task = task;
        task.init(this);
    }

    public Task getNextTask() { return new IdleTask(); }

    public void die() {
        for (int i = 0; i < 8; i++) {
            level.add(new MeatDebris(x, y, z + (double)i));
        }
        
        deadTime = 180;
    }

    public void updateWeapon(Mob target) {
        if (weapon.canUse()) {
            if ((weapon.maxAmmoLoaded == 0 || weapon.ammoLoaded > 0) && target != null) shootAt(target);
            else weapon.reload();
        }
    }

    public Mob findTarget(double r) {
        List<Entity> es = level.getEntities(x - r, y - r, z - r, x + r, y + r, z + r);
        Mob closest = null;
        for (int i = 0; i < es.size(); i++) {
            Entity e = es.get(i);
            Mob u = (Mob) e;
            if (!(e instanceof Mob) || e == this || !u.isAlive() || !(u.distanceToSqr(this) < r * r) || !isLegalTarget(u)) continue;
            
            if (closest == null) {
                closest = u;
                continue;
            }
            
            if (!(e.distanceToSqr(this) < closest.distanceToSqr(this))) continue;
            closest = u;
            
        }
        
        return closest;
    }

    public boolean isLegalTarget(Mob u) { return false; }

    public void attract(Entity e, double maxDist) {
        super.attract(e, maxDist);
        task = new IdleTask();
    }

    public void touchedBy(Entity e, double xxa, double yya, double zza) {
        if (xxa != 0.0 || yya != 0.0) knockBack(xxa, yya, zza);
        super.touchedBy(e, xxa, yya, zza);
    }

    public void collide(Entity e, double xxa, double yya, double zza) {
        if (xxa != 0.0 || yya != 0.0) dir = random.nextDouble() * (Math.PI * 2) - 1.0;
        super.collide(e, xxa, yya, zza);
    }

    public void renderShadow(Bitmap screen, int xp, int yp) {
        if (deadTime > 0) return;
        screen.fill(xp - (int)(xr * 0.9), yp + (int)z, xp + (int)(xr * 0.9), yp + 4 + (int)z, 1);
    }

    public void render(Bitmap screen, int xp, int yp) {
        if (deadTime > 0) return;
        
        int frame = 0;
        if (shootTime == 0) {
            int dirFrame = (int)Math.floor(-dir * 4.0 / (Math.PI * 2) - 3.0) & 3;

            if (dirFrame == 0) frame = 0;
            if (dirFrame == 1) frame = 3;
            if (dirFrame == 2) frame = 6;

            if (dirFrame == 3) {
                frame = 3;
                screen.xFlip = true;
            }

            int walkFrame = walkStep / 4 & 3;

            if (frame == 3) {
                if (walkFrame == 1) frame++;
                if (walkFrame == 2) frame += 2;
                if (walkFrame == 3) frame++;
            } else {
                if (walkFrame == 1) frame++;
                if (walkFrame == 3) frame += 2;
            }

        } else {
            int dirFrame = (int)(-Math.floor(dir * 8.0 / (Math.PI * 2) - 0.5)) & 7;
            frame = dirFrame + 9;
            if (dirFrame > 4) {
                frame = 12 - (dirFrame - 5);
                screen.xFlip = true;
            }
        }
        if (hurtTime > 0 && hurtTime / 2 % 2 == 1) {
            if (health > maxHealth / 5) screen.blendDraw(Art.chars[frame][charClass], xp - 32, yp - 60, 0xFFFFFFFF);
            else screen.fogBlend(Art.chars[frame][charClass], xp - 32, yp - 60);
        } else if (selectInterval > 0 && selectInterval / 2 % 2 == 1) screen.blendDraw(Art.chars[frame][charClass], xp - 32, yp - 60, 0xFFFF0000);
        else screen.draw(Art.chars[frame][charClass], xp - 32, yp - 60);
        
        screen.xFlip = false;
        int dmg = (maxHealth - health) * 40 / maxHealth;
        
        screen.fill(xp - 20, yp + 12, xp + 16, yp + 12, 0xFFFF0000);
        screen.fill(xp - 20, yp + 12, xp + 16 - dmg, yp + 12, 0xFF00FF00);
        
        if (weapon.maxAmmoLoaded > 0) {
            int ammo = (weapon.maxAmmoLoaded - weapon.ammoLoaded) * 40 / weapon.maxAmmoLoaded;
            screen.fill(xp - 20, yp + 16, xp + 16, yp + 16, 0xFF202020);
            screen.fill(xp - 20, yp + 16, xp + 16 - ammo, yp + 16, 0xFFB0B0B0);
        }
    }

}