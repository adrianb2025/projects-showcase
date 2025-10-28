package game.entity.mob;

import game.entity.CharacterStats;
import game.entity.Entity;
import game.entity.Team;
import game.entity.mob.hostile.boss.Boss;
import game.entity.particle.BloodParticle;
import game.entity.weapon.Arrow;
import game.entity.particle.FlowTextParticle;
import game.gfx.Font;
import game.level.Level;
import game.level.tile.Tile;
import game.snd.Sound;

public class Mob extends Entity {

    private static final int DEFAULT_KNOCKBACK = 6;
    protected long tickTime;
    protected int hurtTime;
    protected int walkDist;
    protected int dir;
    protected int health = 10;
    protected int maxHealth;
    protected int xKnockback;
    protected int yKnockback;
    protected int viewRadius = 4;
    protected Mob target;
    protected int groundSlowPeriod = 50;
    protected int bloodCol = 0xCC1100;

    protected CharacterStats defaultStats = new CharacterStats(0, 10, 0, 0, 0);
    protected CharacterStats currentStats = new CharacterStats(0, 10, 0, 0, 0);
    protected CharacterStats compareStats = new CharacterStats(0, 10, 0, 0, 0);

    public void init(Level level) {
        super.init(level);
        health *= (level.getNumber() + 1);
        maxHealth = health;
    }

    public void tick() {
        tickTime++;
        if (!ignoreBlocks() && level.getTile(x >> 4, y >> 4) == Tile.lava && !(this instanceof Boss)) hurt(this, 4, dir ^ 1);
        if (health <= 0) die();
        if (hurtTime > 0) hurtTime--;

        if (canRegenerate() && health < currentStats.getEndurance() && tickTime % 420 == 0) {
            int hh = health;
            health = Math.min(currentStats.getEndurance(), health + currentStats.getRegeneration() + currentStats.getEndurance() / 10);
            level.add(new FlowTextParticle("+" + (health - hh), x, y, Font.GREEN_COL));
        }

        if (!compareStats.match(currentStats.mergeStats(new CharacterStats(0, 0, 0, 0, groundSlowPeriod)))) {
            updateStats();
            compareStats = currentStats.mergeStats(new CharacterStats(0, 0, 0, 0, groundSlowPeriod));
        }
    }

    public boolean move(int xa, int ya) {
        if (xKnockback < 0) {
            move2(-1, 0);
            xKnockback++;
        }

        if (xKnockback > 0) {
            move2(1, 0);
            xKnockback--;
        }

        if (yKnockback < 0) {
            move2(0, -1);
            yKnockback++;
        }

        if (yKnockback > 0) {
            move2(0, 1);
            yKnockback--;
        }

        if (hurtTime > 0) return true;

        if (xa != 0 || ya != 0) {
            walkDist++;
            if (xa < 0) dir = 2;
            if (xa > 0) dir = 3;
            if (ya < 0) dir = 1;
            if (ya > 0) dir = 0;
        }

        if (tickTime % (groundSlowPeriod + currentStats.getSlowPeriod()) == 0) return true;

        return super.move(xa, ya);
    }

    public boolean findStartPos(Level level) {
        int x = random.nextInt(level.getWidth());
        int y = random.nextInt(level.getHeight());

        int xx = x * 16 + 8;
        int yy = y * 16 + 8;


        if (level.getPlayer() != null) {
            int xd = level.getPlayer().getX() - xx;
            int yd = level.getPlayer().getY() - yy;
            if (xd * xd + yd * yd < 6400) return false;
        }

        this.x = xx;
        this.y = yy;

        if (!ignoreBlocks()) {
            int r = level.getMonsterDensity() * 16;
            if (level.getEntities(xx - r, yy - r, xx + r, yy + r, Team.HOSTILE).size() > 0) return false;
            if (!level.getTile(x, y).mayPass(level, x, y, this)) return false;
        }

        return true;
    }

    public void touchedBy(Entity e) {
        if (e instanceof Arrow) {
            Arrow arrow = (Arrow) e;
            if (team != arrow.getTeam()) {
                hurt(this, arrow.getDamage(), dir ^ 1);
                arrow.setRemoved(true);
                Sound.monsterHurt.play();
            }
        }
    }

    public void hurt(Mob mob, int dmg, int attackDir) {
        if (mob instanceof Player) Sound.playerHurt.play();
        doHurt(dmg, attackDir);
    }

    protected void doHurt(int dmg, int attackDir) {
        if (hurtTime > 0) return;

        for (int i = 0; i < Math.min(dmg, 10); i++) {
            level.add(new BloodParticle(x, y));
        }

        dmg = Math.max(0, dmg - currentStats.getDefense());
        level.add(new FlowTextParticle("" + dmg, x, y, Font.RED_COL));
        health -= dmg;


        if (attackDir == 0) yKnockback = +DEFAULT_KNOCKBACK;
        if (attackDir == 1) yKnockback = -DEFAULT_KNOCKBACK;
        if (attackDir == 2) xKnockback = -DEFAULT_KNOCKBACK;
        if (attackDir == 3) xKnockback = +DEFAULT_KNOCKBACK;

        hurtTime = 10;
    }

    public void setSlowPeriod(int slowPeriod) { groundSlowPeriod = slowPeriod; }
    public int getSlowPeriod() { return groundSlowPeriod + currentStats.getSlowPeriod(); };
    public void die() { removed = true; }
    public int getDir() { return dir; }
    public int getHealth() { return health; }
    public boolean canRegenerate() { return false; }
    public CharacterStats getCurrentStats() { return currentStats; }
    public void updateStats() {}

}