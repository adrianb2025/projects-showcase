package game.entity.item;

import game.entity.Entity;
import game.entity.mob.Player;
import game.gfx.Screen;
import game.level.Level;
import game.level.tile.Tile;


public class Item implements ListItem {

    public boolean interactOn(Tile tile, Level level, int xt, int yt, Player player, int attackDir) { return false; }
    public void renderInventory(Screen screen, int x, int y) {}
    public void renderIcon(Screen screen, int x, int y) {}
    public boolean isDepleted() { return false; }
    public int getColor() { return 0; }
    public int getXSprite() { return 0; }
    public int getYSprite() { return 0; }
    public int getScale() { return 1; }
    public int getMaxTime() { return 600; }
    public void onTake(ItemEntity e) {}
    public boolean interact(Player player, Entity e, int attackDir) { return false; }
    public boolean canAttack() { return false; }
    public int getAttackDamageBonus(Entity e) { return 0; }
    public String getName() { return ""; }
    public boolean matches(Item item) { return item.getClass() == getClass(); }


}