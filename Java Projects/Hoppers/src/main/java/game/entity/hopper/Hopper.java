package game.entity.hopper;

import game.InputHandler;
import game.entity.Bubble;
import game.entity.Entity;
import game.entity.hopper.task.IdleTask;
import game.entity.hopper.task.PortalTask;
import game.entity.hopper.task.Task;
import game.gfx.Art;
import game.level.tile.LavaTile;
import game.particle.WhiteParticle;
import game.snd.Sound;

import java.awt.*;
import java.awt.geom.AffineTransform;

public abstract class Hopper extends Entity {

    private Task task;
    private int frames;
    private IdleTask idleTask = new IdleTask(this);
    public double moveSpeed;
    public double acceleration;
    public boolean finished;
    public int selectedTime;
    public boolean ignoresLava;

    public int skillDelay;
    public int skillTime;

    public Hopper(double x, double y) {
        super(x, y);
        dir = random.nextInt(2);
        setTask(idleTask);
        moveSpeed = 0.7;
    }

    public void tick() {
        super.tick();

        if (!ignoresLava && level.getTile((int)x >> 5, ((int)(y - yr) >> 5) + 1) instanceof LavaTile) {
            hurt(-2.8, 0.8, 1);
        }

        double xd = xo - x;

        if (Math.abs(xd) > 3) level.addParticle(new WhiteParticle(xo, yo + yr, 0.1, -0.02));

        if (selectedTime > 0) selectedTime--;

        if (task != null && !task.finished()) task.tick();
        if (task == null || task.finished()) setTask(idleTask);

        if (entityUnder instanceof Bubble) {
            ((Bubble) entityUnder).created();
        }
    }

    public void render(Graphics2D g, double delta) {
        if (selectedTime > 0 && selectedTime / 4 % 2 == 0) return;
        super.render(g, delta);

        int xx = (int)(xo + (x - xo) * delta);
        int yy = (int)(yo + (y - yo) * delta);

        boolean xFlip = dir == 0;

        AffineTransform at = g.getTransform();

        g.translate(xx, yy);
        g.scale(xFlip ? -1 : 1, 1);
        g.translate(-xx, -yy);

        g.drawImage(Art.sprites[(frames / 10 % 3) + 2 * 4], (int)(xx - xr), (int)(yy - yr + ysOffs), null);

        g.setTransform(at);

    }

    public void tick(InputHandler input) {
        if (idle()) {
            double speed = moveSpeed + acceleration;
            boolean moving = false;

            if (skillTime < skillDelay) skillTime++;

            if (input.left.down) {
                xa -= speed;
                dir = 0;
                moving = true;
            }

            if (input.right.down) {
                xa += speed;
                dir = 1;
                moving = true;
            }

            if (input.up.down && entityUnder instanceof Bubble) entityUnder.ya -= 0.5;
            if (input.down.down && entityUnder instanceof Bubble) entityUnder.ya += 0.06;

            if (input.use.down) {
                if (skillTime >= skillDelay && useSkill()) skillTime = 0;
            }

            if (moving) frames += Math.round(speed + 0.5);

            if (input.jump.clicked && onGround) {
                ya -= 3 + Math.abs(xa) * 0.2;
                Sound.jump.play();
            }

        }
    }

    public void setTask(Task task) {
        if (task != null) task.destroy();
        this.task = task;
    }

    public void select() { selectedTime = 30; }
    public boolean idle() { return task == null || task.finished(); }
    public abstract boolean useSkill();
    public void outOfbounds() {
        if (ya > 0) die();
    }

    public void portaling(Entity e) { setTask(new PortalTask(this, level)); }

}