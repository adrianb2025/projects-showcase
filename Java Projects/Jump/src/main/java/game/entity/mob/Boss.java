package game.entity.mob;

import game.entity.Chair;
import game.entity.Entity;
import game.entity.mob.task.IdleTask;
import game.entity.mob.task.Task;
import game.entity.mob.task.boss.FollowPlayerTask;
import game.entity.mob.task.boss.RandomMoveTask;
import game.gfx.Art;
import game.level.tile.Tile;
import game.snd.Sound;

import java.awt.*;

public class Boss extends Mob {

    private int spinTime;
    private int maxSpinTime = 120;
    private int recharge = 0;
    public Chair chair = null;
    public boolean bossDialogue = true;
    int[] moveAnim = { 0, 2, 0, 3 };
    int[] idleAnim = { 0, 1 };
    int[] spinAnim = { 0, 4, 6, 5 };

    public Boss() { super(0, 0); }

    public void tick() {
        super.tick();
        tickTime++;

        if (spinTime > 0) spinTime--;
        if (recharge > 0) recharge--;


        if (spinTime > 0 && chair != null) {
            chair.xa = random.nextDouble() * 2.0 - 1.0;
            chair.ya = 2;
            chair.za = 1;
            level.addEntity(chair);
            chair = null;
            recharge = 30;
        }

        if (level.player.xt == xt && chair != null) {
            chair.xa = 0;
            chair.ya = 10;
            chair.za = 1;
            level.addEntity(chair);
            chair = null;
            recharge = 30;
            Sound.shoot.play();
        }
    }

    public void jumpTo(int xxa, int yya) {
        if (xxa == 0 && yya == 0) return;
        if (!canJump(xxa, yya)) return;

        xa += xxa;
        ya += yya;
        za += 1;

        onGround = false;
    }

    public void render(Graphics2D g) {
        if (this.hurtTime > 0 && this.hurtTime / 4 % 2 == 0) return;

        int x = (int)(this.x - 8);
        int y = (int)(this.y - 12);
        int animFrame = tickTime / 15;
        int[] anim = idleAnim;
        if (task instanceof RandomMoveTask) anim = moveAnim;

        if (spinTime > 0) {
            double tt = spinTime / (double) maxSpinTime;
            animFrame = tickTime / (int)(2 + 13 * tt);
            anim = spinAnim;
        }

        int t = anim[animFrame % anim.length] + 56;
        g.drawImage(Art.sprites[8], x, y + 4, null);
        g.drawImage(Art.sprites[t], x, y - (int)z, null);
        if (this.chair != null) {
            chair.x = x + 8;
            chair.y = y + 6 + (double)(tickTime / 15 % 3 - 1);
            chair.z = 6;
            chair.render(g);
        }
    }

    public boolean canJump(int xxa, int yya) {
        int next = level.getTile(xt + xxa, yt + yya);
        return onGround && !Tile.tiles[next].blocks(this);
    }

    public void hurt(Entity e, double xxa, double yya) {
        if (hurtTime > 0) return;

        Sound.hurt.play();
        hurtTime = 30;
        spinTime = maxSpinTime;
        task = null;
        health--;
    }

    protected Task getNextTask() {
        if (bossDialogue) return null;
        if (spinTime > 0) return null;

        if (health == 1) {
            level.happyEnding();
            return null;
        }

        Task result = null;

        result = random.nextInt(10) <= 5 ? new FollowPlayerTask() : new IdleTask(30);
        if (recharge == 0 && chair == null && random.nextInt(100) < 20) chair = new Chair(this);
        return result;
    }

}