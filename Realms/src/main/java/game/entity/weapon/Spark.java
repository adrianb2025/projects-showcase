package game.entity.weapon;

import game.entity.Entity;
import game.entity.mob.Mob;
import game.entity.mob.hostile.boss.AirMage;
import game.gfx.Screen;
import game.gfx.util.PaletteHelper;

import java.util.List;

public class Spark extends Entity {

    private int lifeTime;
    private double xa;
    private double ya;
    private double xx;
    private double yy;
    private int time;
    private Mob owner;

    public Spark(Mob owner, double xa, double ya) {
        this.owner = owner;
        xx = x = owner.getX();
        yy = y = owner.getY();
        xr = 0;
        yr = 0;
        this.xa = xa;
        this.ya = ya;

        lifeTime = 600 + random.nextInt(30);
    }

    public void tick() {
        time++;
        if (time >= lifeTime) {
            removed = true;
            return;
        }

        xx += xa;
        yy += ya;
        x = (int) xx;
        y = (int) yy;
        List<Entity> toHit = level.getEntities(x, y, x, y, null);
        for (Entity e : toHit) {
            if (e instanceof Mob && (!(e instanceof AirMage) || e.getTeam() != owner.getTeam())) e.hurt(owner, level.getNumber() + 1, ((Mob) e).getDir() ^ 1);
        }
    }

    public void render(Screen screen) {
        if (time >= lifeTime - 120 && time / 6 % 2 == 0) return;

        int xt = 8;
        int yt = 13;

        screen.render(x - 4, y - 4 - 2, xt * 8, yt * 8, PaletteHelper.getColor(-1, 555, 555, 555), random.nextInt(4));
        screen.render(x - 4, y + 4 - 2, xt * 8, yt * 8, PaletteHelper.getColor(-1, 0, 0, 0), random.nextInt(4));
    }
}