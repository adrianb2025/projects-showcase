package game.entity.mob;

import game.entity.CharacterStats;
import game.entity.Entity;
import game.entity.Team;
import game.entity.weapon.ArrowType;
import game.entity.weapon.Weapon;
import game.gfx.util.PaletteHelper;

import java.util.List;

public class Guard extends NPC {

    public Guard(int x, int y) {
        super(x, y);
        currentStats = currentStats.mergeStats(new CharacterStats(3, 20, 10, 10, 10, 20));
        int scol = (random.nextInt(7) - 3) * 10;
        col = PaletteHelper.getColor(-1, 100, 335 + col, 532);
    }

    public void tick() {
        super.tick();

        if (level.getPlayer() != null && randomWalkTime == 0) {
            int xd = level.getPlayer().getX() - x;
            int yd = level.getPlayer().getY() - y;
            if (xd * xd + yd * yd > 6400) {
                xa = 0;
                ya = 0;
                if (xd < 0) xa = -1;
                if (xd > 0) xa = +1;
                if (yd < 0) ya = -1;
                if (yd > 0) ya = +1;
            } else if (xd * xd + yd * yd < 600) {
                xa = 0;
                ya = 0;
            }
        }

        int speed = (tickTime % 4) == 0 ? 0 : 1;

        if (!move(xa * speed, ya * speed) || random.nextInt(100) == 0) {
            randomWalkTime = 30;
            xa = (random.nextInt(3) - 1);
            ya = (random.nextInt(3) - 1);
        }

        if (randomWalkTime > 0) randomWalkTime--;

        if (canAttack()) {
            if (target == null || target.isRemoved()) {
                int rr = 80;

                List<Entity> entities = level.getEntities(x - rr, y - rr, x + rr, y + rr, Team.HOSTILE);
                for (Entity e : entities) {
                    if (e instanceof Mob) {
                        target = (Mob) e;
                        break;
                    }
                }
            } else {
                int xd = target.getX() - x;
                int yd = target.getY() - y;

                double m = Math.sqrt(xd * xd + yd * yd);

                if (m > 150) target = null;

                double vx = xd / m;
                double vy = yd / m;

                if (tickTime % currentStats.getAttackDelay() == 0) {
                    boolean isFireArrow = random.nextInt(5) == 0;
                    Weapon.fire(isFireArrow ? ArrowType.FIRE : ArrowType.BASIC, team, x, y, vx, vy, currentStats.getStrength() + (isFireArrow ? currentStats.getStrength() : 0), level);
                }

            }
        }

    }

}