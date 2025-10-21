package game.entity.weapon;

import game.entity.Team;
import game.gfx.util.PaletteHelper;

public class FireArrow extends Arrow {

    public FireArrow(Team team, int x, int y, double vx, double vy, int dmg) {
        super(team, x, y, vx, vy, dmg);
        col = PaletteHelper.getColor(-1, 300, 500, 200);
        speed = 5;
    }

    public void tick() {
        super.tick();
        level.getFireParticles().createExplosion(x, y, 0.5, -0.6, 4);
    }
}
