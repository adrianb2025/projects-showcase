package game.entity.mob.hostile;

import game.entity.Entity;
import game.entity.Team;
import game.entity.item.ItemEntity;
import game.entity.item.resource.Resource;
import game.entity.item.resource.ResourceItem;
import game.entity.mob.Mob;
import game.gfx.Screen;
import game.gfx.util.PaletteHelper;

import java.util.List;

public class Slime extends Mob {

    private int xa;
    private int ya;
    private int jumpTime;

    public Slime() {}

    public Slime(int x, int y) {
        this.x = x;
        this.y = y;
        viewRadius = 6;
        team = Team.HOSTILE;
    }

    public void tick() {
        super.tick();
        int speed = 1;
        if (!move(xa * speed, ya * speed) || random.nextInt(40) == 0) {
            if (jumpTime <= -10) {
                xa = (random.nextInt(3) - 1);
                ya = (random.nextInt(3) - 1);

                int vr = viewRadius << 4;

                if (target == null) {
                    List<Entity> entities = level.getEntities(x - vr, y - vr, x + vr, y + vr, Team.PLAYER);
                    for (Entity e : entities) {
                        if (e instanceof Mob) {
                            target = (Mob) e;
                            break;
                        }
                    }
                }

                if (target != null) {
                    int xd = level.getPlayer().getX() - x;
                    int yd = level.getPlayer().getY() - y;

                    if (xd < 0) xa = -1;
                    if (xd > 0) xa = +1;
                    if (yd < 0) ya = -1;
                    if (yd > 0) ya = +1;

                    if (xd * xd + yd * yd > vr * vr || target.isRemoved()) target = null;
                }

                if (xa != 0 || ya != 0) jumpTime = 10;
            }
        }

        if (jumpTime-- <= 0) {
            xa = 0;
            ya = 0;
        }
    }

    public void render(Screen screen) {
        int xt = 0;
        int yt = 6;

        int xo = x - 4;
        int yo = y - 4;
        if (jumpTime > 0) {
            xt++;
            yo -= 4;
        }

        int col = PaletteHelper.getColor(-1, 10, 242 + level.getNumber() * 10, 555);
        if (hurtTime > 0) col = PaletteHelper.getColor(-1, 555, 555, 555);
        screen.render(xo, yo, xt * 8, yt * 8, 8, 8, col, 0);
    }

    public void touchedBy(Entity e) {
        if (e.getTeam() != team) e.hurt(this, level.getNumber() + 1, dir);
        super.touchedBy(e);
    }

    public void die() {
        super.die();

        int count = random.nextInt((level.getNumber() + 1) * 2) + 1;
        for (int i = 0; i < count; i++) {
            level.add(new ItemEntity(new ResourceItem(Resource.coin), x + random.nextInt(11) - 5, y + random.nextInt(11) - 5));
        }

        if (random.nextInt(20) == 0) level.add(new ItemEntity(new ResourceItem(Resource.health), x + random.nextInt(11) - 5, y + random.nextInt(11) - 5));
    }

}
