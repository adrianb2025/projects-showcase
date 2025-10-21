package game.entity.item.resource;

import game.entity.item.Item;
import game.entity.item.ItemEntity;
import game.entity.mob.Player;
import game.gfx.Font;
import game.gfx.Screen;
import game.gfx.gui.GuiManager;
import game.level.Level;
import game.level.tile.Tile;

public class ResourceItem extends Item {

    private Resource resource;
    private int count = 1;

    public ResourceItem(Resource resource) { this.resource = resource; }

    public ResourceItem(Resource resource, int count) {
        this.resource = resource;
        this.count = count;
    }

    public void renderIcon(Screen screen, int x, int y) {
        screen.render(x, y, resource.getXSprite() * 8, resource.getYSprite() * 8, resource.getColor(), 0);
    }

    public void renderInventory(Screen screen, int x, int y, int textX, int textY) {
        screen.render(x, y, resource.getXSprite() * 8, resource.getYSprite() * 8, resource.getColor(), 0);
        Font.draw("" + count, screen, (count > 10 ? 8 : 0) + textX, textY, GuiManager.FONT_COL);
    }

    public boolean interactOn(Tile tile, Level level, int xt, int yt, Player player, int attackDir) {
        if (resource.interactOn(tile, level, xt, yt, player, attackDir)) {
            count--;
            return true;
        }

        return false;
    }


    public void onTake(ItemEntity e) {}
    public int getColor() { return resource.getColor(); }
    public int getCount() { return count; }
    public int getXSprite() { return resource.getXSprite(); }
    public int getYSprite() { return resource.getYSprite(); }
    public String getName() { return resource.getName(); }
    public boolean isDepleted() { return count <= 0; }
    public Resource getResource() { return resource; }
    public void addCount(int amt) { count += amt; }

}