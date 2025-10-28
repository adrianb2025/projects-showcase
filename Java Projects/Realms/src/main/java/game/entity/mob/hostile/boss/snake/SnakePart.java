package game.entity.mob.hostile.boss.snake;

import game.entity.Entity;
import game.entity.mob.Mob;
import game.entity.mob.hostile.boss.Boss;
import game.level.tile.Tile;

import java.util.List;

public class SnakePart extends Boss {

    protected double xa;
    protected double ya;
    protected int time;

    public void tick() {
        super.tick();
        if (xa >= 0.00001) x = (int) xa;
        if (ya >= 0.00001) y = (int) ya;

        time++;
        List<Entity> entities = level.getEntities(x, y, x, y, null);
        for (Entity e : entities) {
            doHurt(e);
        }

        if (health < maxHealth) level.getFireParticles().createExplosion(x, y, 0.5, -1.0, (maxHealth - health) / 30);
        if (health < maxHealth / 10) level.setTile(x >> 4, y >> 4, Tile.lava);
    }

    private void doHurt(Entity e) {
        if (e instanceof SnakePart) return;
        e.hurt(this, 2 * (level.getNumber() + 1), dir);
    }

    public void touchedBy(Entity e) {
        doHurt(e);
        super.touchedBy(e);
    }

    public void hurt(Mob mob, int dmg, int attackDir) {
        if (health < 50) time = 0;
        super.hurt(mob, dmg, attackDir);
    }

    public void setMovement(double xa, double ya, double rot) {}
    public boolean canRegenerate() { return true; }

}