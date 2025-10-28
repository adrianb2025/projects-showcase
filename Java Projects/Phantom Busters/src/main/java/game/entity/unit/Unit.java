package game.entity.unit;

import game.Player;
import game.entity.Entity;
import game.entity.projectile.Bullet;
import game.particle.SplatDebris;
import game.util.MathUtil;

public class Unit extends Entity {

    public double speed = 100.0;
    public Team team;
    public int maxHealth = 125;
    public int health = 125;
    public int hurtTime = 0;
    public int selectInterval;
    public double dir = 0.0;
    public int walkStep = 0;
    public int visRange = 7;
    public Player player;
    public int deadTime;

    public Unit(Player player) { this.player = player; }

    public void moveForward() {
        double moveSpeed = 0.2 * speed / 100.0;
        if (!isOnGround()) moveSpeed *= 0.1;

        xa += Math.cos(dir) * moveSpeed;
        ya += Math.sin(dir) * moveSpeed;
        if (tickTime % 2 == 0) walkStep = (int)(walkStep + Math.round(speed / 100.0));
    }

    public void hurt(int dmg) {
        health -= dmg;
        hurtTime = 20;
    }

    public boolean isAlive() { return health > 0; }

    public void knockBack(double xxa, double yya, double zza) {
        xa += (xxa - xa) * 0.4;
        ya += (yya - ya) * 0.4;
        za += (zza - za) * 0.4;
    }

    public boolean isOnGround() { return z <= 1.0; }

    public void attract(Entity e, double maxDist) {
        double dist = Math.sqrt(distanceToSqr(e));
        if (dist > maxDist) {
            xa += (e.x - x) / dist * 0.2;
            ya += (e.y - y) / dist * 0.2;
            za += (e.z - z) / dist * 0.2;
        } else {
            xa -= (e.x - x) / dist * 0.2;
            ya -= (e.y - y) / dist * 0.2;
            za -= (e.z - z) / dist * 0.2;
        }
    }

    public void hitBy(Bullet bullet) {
        if (bullet.owner == this) return;

        hurt(bullet.getDamage(this));
        knockBack(bullet.xa * 0.25, bullet.ya * 0.25, bullet.za * 0.25);
        SplatDebris sd = new SplatDebris(x, y, z + 5.0);
        sd.xa -= bullet.xa * 0.1;
        sd.ya -= bullet.ya * 0.1;
        sd.za -= bullet.za * 0.1;
        level.add(sd);
        super.hitBy(bullet);
    }

    public void findPos() {
        int yd;
        int xd;
        while (level.getTile(xd = random.nextInt(level.w), yd = random.nextInt(level.h)).blocks()) {}
        x = xd * 16 * 4 + 32;
        y = yd * 16 * 4 + 32;
    }

    public boolean turnTowards(double angle) {
        angle = MathUtil.normalize(angle);
        dir = MathUtil.normalize(dir);
        double angleDiff = MathUtil.normalize(angle - dir);
        double turnSpeed = 0.2;
        double near = 1.0;
        boolean wasAimed = angleDiff * angleDiff < near * near;
        if (angleDiff < -turnSpeed) angleDiff = -turnSpeed;
        if (angleDiff > turnSpeed) angleDiff = turnSpeed;
        dir += angleDiff;
        return wasAimed;
    }

    public double distanceToScreenSpaceSqr(double x0, double y0) {
        double xx = (int) Math.floor((x - y) * 0.75);
        double yy = (int) Math.floor((y + x - 24.0) * 0.375);
        double xd = xx - x0;
        double yd = yy - y0;
        return xd * xd + yd * yd;
    }

}