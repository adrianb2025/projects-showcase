package game.entity.unit;

import game.entity.Entity;
import game.entity.unit.task.Task;
import game.gfx.Art;
import game.level.tile.Tile;
import game.particle.DustParticle;
import game.weapon.Weapon;

import java.awt.*;
import java.awt.geom.AffineTransform;

public abstract class Unit extends Entity {

    private int[] steps = new int[] { 0, 1, 0, 2 };
    private Task task = Task.idle;
    protected double moveSpeed = 1.4;
    protected double turnSpeed = 0.2;
    protected double movement;
    protected int sprite;
    public Weapon weapon = null;
    protected int health = 10;
    protected int hurtTime;
    protected double bounce = 0.02;
    protected int swimTime;
    protected boolean swimming;

    public Unit(double x, double y, int r) { super(x, y, r); }

    public void tick() {
        if (hurtTime > 0) hurtTime--;

        attemptMove();
        swimming = level.getTile((int)(x + 8 + 0.5) >> 4, (int)(y + 8 + 0.5) >> 4) == Tile.water;
        swimTime = swimming ? ++swimTime : 0;

        xa *= 0.77;
        ya *= 0.77;
        za *= 0.77;
        za -= level.gravity;

        if (task != null) {
            task.tick();
            if (task.finished()) setTask(getNextTask());
        }

        if (weapon != null) weapon.tick();
    }

    public void moveForward() {
        double speed = moveSpeed;
        if (swimming) speed *= 0.3;

        xa += Math.cos(rot) * speed * 0.03;
        ya += Math.sin(rot) * speed * 0.03;
    }

    public void jump() {
        if (swimming) return;
        if (z > 0.001) return;
        za = 2;
    }

    public boolean turnTowards(double angle) {
        while (angle < -Math.PI) {
            angle += Math.PI * 2;
        }

        while (angle > Math.PI) {
            angle -= Math.PI * 2;
        }

        while (rot < -Math.PI) {
            rot += Math.PI * 2;
        }

        while (rot > Math.PI) {
            rot -= Math.PI * 2;
        }

        double angleDiff = angle - rot;

        while (angleDiff < -Math.PI) {
            angleDiff += Math.PI * 2;
        }

        while (angleDiff > Math.PI) {
            angleDiff -= Math.PI * 2;
        }

        double near = 0.1;
        boolean wasAimed = angleDiff * angleDiff < near * near;

        if (angleDiff < -turnSpeed) angleDiff = -turnSpeed;
        if (angleDiff > turnSpeed) angleDiff = +turnSpeed;

        rot += angleDiff;
        return wasAimed;
    }

    public final void attemptMove() {
        int xStep = (int)(Math.abs(xa * 100) + 1);
        double xxa = xa;
        for (int i = xStep; i > 0; i--) {
            double xn = xxa * i / xStep;
            if (isFree(xn, 0, 0)) {
                x += xn;
                movement += Math.abs(xn);
                break;
            }

            if (xxa != xa) continue;
            xa *= -bounce;
        }

        int yStep = (int)(Math.abs(ya * 100) + 1);
        double yya = ya;
        for (int i = yStep; i > 0; i--) {
            double yn = yya * i / yStep;
            if (isFree(0, yn, 0)) {
                y += yn;
                movement += Math.abs(yn);
                break;
            }

            if (yya != ya) continue;
            ya *= -bounce;
        }

        int zStep = (int)(Math.abs(za * 100) + 1);
        double zza = za;
        for (int i = zStep; i > 0; i--) {
            double zn = zza * i / zStep;
            if (isFree(0, 0, zn)) {
                z += zn;
                break;
            }

            if (zza != za) continue;
            za *= -bounce;
        }

    }

    private boolean isFree(double xxa, double yya, double zza) {
        double zn = z + zza;
        if (zn < 0) z = 0;
        return true;
    }

    public void setWeapon(Weapon weapon) {
        this.weapon = weapon;
        weapon.init(this);
    }

    public void setTask(Task task) {
        this.task = task;
        this.task.init(this);
    }

    public Task getNextTask() { return Task.idle; }

    public void render(Graphics2D g) {
        if (hurtTime > 0 && hurtTime / 4 % 2 == 0) return;

        int xx = (int)(x - 8);
        int yy = (int)(y - 16);

        g.setColor(Color.GRAY);
        g.fillRect((int)(x - 1), (int)(y - 1), 3, 1);
        int dir = (int)(-Math.floor(rot * 4 / (Math.PI * 2) - 0.5)) & 3;

        AffineTransform at = g.getTransform();

        if (dir == 3) {
            g.translate(x, y);
            g.scale(-1, 1);
            g.translate(-x, -y);
            dir = 1;
        }

        int frame = dir * 3 + steps[(int)(movement / 4) % steps.length];
        int h = swimming ? 8 : 16;
        int yOffs = swimming ? 8 : 0;

        if (swimming) {
            g.setColor(new Color(1f, 1f, 1f, 0.7f));
            int rr = 4;
            g.drawOval(xx + rr / 2 + 1 - (int)Math.abs(Math.sin(swimTime * 0.1) * rr * 0.5), yy + yOffs + h / 2, 8 + (int)Math.abs(Math.sin(swimTime * 0.1) * rr), 5);
        }

        g.drawImage(Art.sprites[frame + sprite * 16], xx, yy - (int)z + yOffs, xx + 16, yy - (int)z + h + yOffs, 0, 0, 16, h, null);

        g.setTransform(at);
    }

    public void hurt(Unit owner) {
        if (hurtTime > 0) return;

        int dmg = owner.weapon.calcDamage(this);
        health -= dmg;
        hurtTime = 30;

        if (health <= 0) die();
    }

    public void die() {
        for (int i = 0; i < 10; i++) {
            level.addParticle(new DustParticle(x, y, z));
        }

        super.die();
    }

}