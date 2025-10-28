package game.entity.unit.mob.buster;

import game.Player;
import game.entity.unit.Team;
import game.entity.unit.mob.Mob;
import game.entity.unit.mob.Phantom;
import game.level.tile.Tile;
import game.weapon.HunterWeapon;

public class Hunter extends Mob {
    
    public Hunter(Player player) {
        super(player);
        charClass = 4;
        speed = 120;
        weapon = new HunterWeapon(this);
        team = Team.HUNTERS;
    }

    public void remove() {
        super.remove();
        int xx = (int)(x / 64.0);
        int yy = (int)(y / 64.0);
        level.tiles[xx + yy * level.w] = Tile.tombstone;
    }

    public boolean isLegalTarget(Mob u) { return u instanceof Phantom; }

}