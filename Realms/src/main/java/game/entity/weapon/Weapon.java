package game.entity.weapon;

import game.entity.Team;
import game.entity.item.Item;
import game.level.Level;

public class Weapon extends Item {

    public static void fire(ArrowType arrowType, Team team, int x, int y, double vx, double vy, int dmgBonus, Level level) {
        switch (arrowType) {
            case FIRE:
                level.add(new FireArrow(team, x, y, vx, vy, dmgBonus));
                break;
            case SILVER:
                level.add(new SilverArrow(team, x, y, vx, vy, dmgBonus));
                break;
            case BASIC:
                level.add(new BasicArrow(team, x, y, vx, vy, dmgBonus));
                break;
        }
    }

}