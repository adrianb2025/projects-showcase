package game.entity.mob;

import game.entity.Chair;
import game.entity.Entity;
import game.entity.mob.task.Task;
import game.snd.Sound;

import java.awt.*;

public class Mob extends Entity {

    public boolean onGround;
    public int steppedOnTile = -1;
    public Task task;
    public int hurtTime;
    public int health = 10;
    public int maxHealth = 10;
    public Color c0 = new Color(0xA35B70);
    public Color c1 = Color.BLACK;

    public Mob(double x, double y) { super(x, y); }

    public void tick() {
        super.tick();

        onGround = false;
        int xto = xt;
        int yto = yt;
        x += xa;
        y += ya;
        z += za;

        xa *= friction;
        ya *= friction;
        za *= friction;

        if (z < 0) {
            za *= -bounce;
            xa *= 0.005;
            ya *= 0.005;
            z = 0;
            onGround = true;
            int xtn = (int)x >> 4;
            int ytn = (int)y >> 4;

            if (xto != xtn || yto != ytn) {
                xt = xtn;
                yt = ytn;

                steppedOn(xt - xto, yt - yto);
            }
        }

        if (task != null) task.tick();
        if (task == null || task.finished()) setTask(getNextTask());

        za += gravity;
        if (hurtTime > 0) hurtTime--;
    }

    public void setTask(Task task) {
        if (task == null) return;
        this.task = task;
        task.init(this);
    }

    public void hurt(Entity e, double xxa, double yya) {
        if (hurtTime > 0) return;

        Sound.hurt.play();
        hurtTime = 30;
        health--;
        if (health <= 0) die();
    }

    protected void touchedBy(Entity e, double xxa, double yya) {
        if (e instanceof Chair && Math.abs(yya) > 1) hurt(e, xxa, yya);
    }

    protected Task getNextTask() { return null; }
    public void steppedOn(int xta, int yta) {}

}