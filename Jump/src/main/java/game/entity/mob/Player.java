package game.entity.mob;

import game.InputHandler;
import game.entity.Chair;
import game.entity.Entity;
import game.entity.particle.FireParticle;
import game.entity.particle.SplashParticle;
import game.gfx.Art;
import game.level.Level;
import game.level.tile.Tile;
import game.snd.Sound;

import java.awt.*;

public class Player extends Mob {

    public Entity carriedBy;
    public int distanceAchieved;
    private int dieTimer;
    private int shakeTime;
    private double jittering;
    private Chair chair = null;

    public Player(double x, double y) {
        super(x, y);
        bounce = 0;
    }

    public void handleInput(InputHandler input) {
        if (input.up.clicked && !jumpTo(0, -1)) {
            if (chair != null) {
                chair.xa = 0;
                chair.za = 1;
                chair.ya = -10;
                chair.removed = false;
                level.addEntity(chair);
                chair = null;
                Sound.shoot.play();
            } else interact(0, -2);
        }

        if (input.left.clicked) jumpTo(-1, 0);
        if (input.right.clicked) jumpTo(1, 0);
        if (input.down.clicked) interact(0, 1);
    }

    public void render(Graphics2D g) {
        if (hurtTime > 0 && hurtTime / 4 % 2 == 0) return;

        int x = (int)(this.x - 8);
        int y = (int)(this.y - 12);
        if (shakeTime > 0) x = (int)(x + jittering);

        int t = onGround ? 0 : distanceAchieved % 2 + 1;
        if (xa > 0.1) t = 4;
        if (xa < -0.1) t = 3;

        int sh = 16;

        if (steppedOnTile == Tile.water.id && onGround && carriedBy == null || dieTimer > 0) {
            sh = 8;
            y += 8;
            g.drawImage(Art.sprites[6 + (tickTime / 15 % 2 == 0 ? 1 : 0) + 0], x, y - 3, null);
        } else g.drawImage(Art.sprites[8], x, y + 4, null);

        g.drawImage(Art.sprites[t], x, y - (int)z, x + 16, y - (int)z + sh, 0, 0, 16, sh, null);

        if (chair != null) {
            chair.x = x + 8;
            chair.y = y + 6 + (double)(tickTime / 15 % 3 - 1);
            chair.z = 6 + z;
            chair.render(g);
        }
    }

    public void interact(int xxa, int yya) {
        if (chair != null) return;

        int x0 = (int)(x - r + xxa * 16);
        int y0 = (int)(y - r + yya * 16);
        int x1 = (int)(x + r + xxa * 16);
        int y1 = (int)(y + r + yya * 16);

        chair = (Chair) level.getEntity(this, x0, y0, x1, y1, new Level.EntityFilter() {
            public boolean accept(Entity e) {
                return e instanceof Chair;
            }
        });

        if (chair != null) {
            chair.owner = this;
            chair.lifeTime = chair.maxLifeTime;
            chair.removed = true;
            Sound.pickup.play();
        }
    }

    public boolean jumpTo(int xxa, int yya) {
        if (!canJump(xxa, yya)) return false;

        if (carriedBy != null) carriedBy = null;

        xa += xxa;
        ya += yya;
        za += 1;

        if (ya != 0) distanceAchieved++;
        Sound.jump.play();

        onGround = false;
        return true;
    }

    public void tick() {
        super.tick();

        if (carriedBy != null) {
            x = carriedBy.x;
            y = carriedBy.y;
            z = carriedBy.z;
        }

        if (dieTimer > 0) {
            dieTimer--;

            for (int i = 0; i < 4; i++) {
                double xo = (random.nextDouble() * 2.0 - 1.0) * 2;
                double yo = (random.nextDouble() * 2.0 - 1.0);

                level.addParticle(new FireParticle(x + xo, y + yo, z + 2));
            }

            if (dieTimer <= 0) {
                removed = true;
                level.gameOver();
            }
        }

        if (shakeTime > 0) {
            shakeTime--;
            jittering = (random.nextDouble() * 2.0 - 1.0) * 2;
        }

    }

    public boolean canJump(int xxa, int yya) {
        if (!level.canMoveForward && yya != 0) return false;
        if (!level.dialogueText.isEmpty()) return false;

        int next = level.getTile(xt + xxa, yt + yya);
        return onGround && next != -1 && !Tile.tiles[next].blocks(this) && shakeTime == 0;
    }

    public void steppedOn(int xta, int yta) {
        steppedOnTile = level.getTile(xt, yt);

        Entity e = level.getEntity(this, xt, yt, new Level.EntityFilter() {
            public boolean accept(Entity e) {
                return e instanceof Leaf;
            }
        });

        x = (xt << 4) + 8;
        y = (yt << 4) + 8;

        if (e != null) {
            carriedBy = e;
            xa = 0;
            ya = 0;
            za = 0;
            x = carriedBy.x;
            y = carriedBy.y;
            z = carriedBy.z;
        }

        if (e == null && steppedOnTile == Tile.water.id) {
            for (int i = 0; i < 32; i++) {
                level.addParticle(new SplashParticle(x, y, z));
            }

            shakeTime = 50;
            Sound.water.play();
        }
    }

    public void die() {
        if (dieTimer > 0) return;
        dieTimer = 120;
    }
}