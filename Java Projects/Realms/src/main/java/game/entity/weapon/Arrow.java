package game.entity.weapon;

import game.entity.Entity;
import game.entity.Team;
import game.entity.mob.Mob;
import game.gfx.Screen;
import game.gfx.util.PaletteHelper;

public class Arrow extends Entity {

    private double vx;
    private double vy;
    private int dmg;
    private int lifeTime;
    private Team team;
    private double beta;
    protected int col;
    protected int speed;

    public Arrow(Team team, int x, int y, double vx, double vy, int dmg) {
        this.team = team;
        this.x = x;
        this.y = y;
        this.vx = vx;
        this.vy = vy;
        this.dmg = dmg;
        lifeTime = 35;
        beta = Math.atan2(vy, vx);
        col = PaletteHelper.getColor(-1, 333, 111, 222);
        speed = 4;
    }

    public void tick() {
        double xa = speed;
        double ya = Math.sin(random.nextGaussian());
        double xxa = xa * Math.cos(beta) - ya * Math.sin(beta);
        double yya = ya * Math.cos(beta) + xa * Math.sin(beta);

        if (!move((int) Math.floor(xxa), (int) Math.floor(yya))) removed = true;

        lifeTime--;
        if (lifeTime < 0) removed = true;
    }

    public void render(Screen screen) {
        int col = PaletteHelper.getColor(-1, 555, 333, 111);
        screen.render(Math.toDegrees(beta) + 90, x - xr, y - yr, 0 * 8, 8 * 8, col, 0);
    }

    public boolean ignoreBlocks() { return true; }
    public int getDamage() { return dmg; }
    public Team getTeam() { return team; }

}