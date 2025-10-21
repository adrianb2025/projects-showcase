package game.entity.weapon;

import game.entity.Team;
import game.gfx.util.PaletteHelper;

public class SilverArrow extends Arrow {

    public SilverArrow(Team team, int x, int y, double vx, double vy, int dmg) {
        super(team, x, y, vx, vy, dmg);
        col = PaletteHelper.getColor(-1, 111, 222, 222);
    }

}
