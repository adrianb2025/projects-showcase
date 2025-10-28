package game.entity.mob.hostile;

import game.entity.Entity;
import game.entity.Team;
import game.entity.item.ItemEntity;
import game.entity.item.resource.Resource;
import game.entity.item.resource.ResourceItem;
import game.entity.mob.Mob;
import game.gfx.Screen;
import game.gfx.util.PaletteHelper;

import javax.swing.plaf.IconUIResource;

public class SlimeFactory extends Mob {

    private int spawnTime;

    public SlimeFactory() {
        xr = 6;
        yr = 4;
        health = 50;
        spawnTime = 300 + random.nextInt(300);
        team = Team.HOSTILE;
    }

    public void tick() {
        super.tick();

        if (hurtTime <= 0) spawnTime--;

        if (spawnTime <= 0) {
            spawnTime = 300 + random.nextInt(300);
            level.add(new Slime(x, y));
        }
    }

    public void render(Screen screen) {
        int xt = 0;
        int yt = 6;

        int xo = x - 8;
        int yo = y - 11;

        int col = PaletteHelper.getColor(-1, 10, 242 + level.getNumber() * 10, 555);
        if (hurtTime > 0) col = PaletteHelper.getColor(-1, 555, 555, 555);
        screen.render(2, xo, yo, xt * 8, yt * 8, 8, 8, col, 0);
    }

    public void touchedBy(Entity e) {
        if (e.getTeam() != team) e.hurt(this, 2 * (level.getNumber() + 1), dir);
        super.touchedBy(e);
    }

    public void die() {
        super.die();

        int count = random.nextInt((level.getNumber() + 1) * 2) + 1;
        for (int i = 0; i < count; i++) {
            level.add(new ItemEntity(new ResourceItem(Resource.bigCoin), x + random.nextInt(11) - 5, y + random.nextInt(11) - 5));
        }
    }

}