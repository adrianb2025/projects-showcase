package game.entity.tree;

import game.entity.item.Item;
import game.entity.item.ItemEntity;
import game.entity.item.resource.Resource;
import game.entity.item.resource.ResourceItem;
import game.entity.mob.Player;
import game.gfx.Screen;
import game.gfx.sprite.SpriteWrapper;
import game.gfx.util.BitmapHelper;
import game.gfx.util.PaletteHelper;

public class AppleTree extends Tree {

    private boolean apple;
    private long time = System.currentTimeMillis() + 45000;

    public AppleTree(int x, int y) { super(x, y, 16, 12); }

    public void tick() {
        super.tick();

        if (time > System.currentTimeMillis() || apple) return;
        apple = true;
        wrapSprite(true);
    }

    private void wrapSprite(boolean drawAura) {
        level.getSpriteCollector().resetWrappers();
        level.getSpriteCollector().resetWrappers();
        level.getSpriteCollector().addWrapper(new SpriteWrapper(17 * 8,  4 * 8, 4 * 8, 4 * 8, PaletteHelper.getColor(20, 40, 30, -1)));
        level.getSpriteCollector().addWrapper(new SpriteWrapper(21 * 8,  4 * 8, 4 * 8, 4 * 8, PaletteHelper.getColor(10, 10, 20, -1)));
        level.getSpriteCollector().addWrapper(new SpriteWrapper(25 * 8,  4 * 8, 4 * 8, 4 * 8, PaletteHelper.getColor(100, 210, 320, -1)));
        if (apple) level.getSpriteCollector().addWrapper(new SpriteWrapper(17 * 8, 0 * 8, 4 * 8, 4 * 8, PaletteHelper.getColor(310, 400, 510, -1)));
        sprite = level.getSpriteCollector().mergeWrappers("tree" + (apple ? "_apple" : ""), 2, 0, (drawAura) ? 0x01000000 : 0);
    }

    public void render(Screen screen) {
        wrapSprite(false);

        int xt = (x - xr * 2) - screen.getXOffset();
        int yt = (y - yr * 2 - 24) - screen.getYOffset();

        BitmapHelper.drawNormal(sprite, xt, yt, screen.getViewPort(), 0xFF00FF);
    }

    public boolean interact(Item item, Player player, int dir) {
        if (!apple) return false;

        time = System.currentTimeMillis() + 120000;
        apple = false;
        wrapSprite(true);

        for (int i = 0; i < random.nextInt(3) + 4; i++) {
            level.add(new ItemEntity(new ResourceItem(Resource.apple), x + random.nextInt(31) - 15, y + random.nextInt(31)));
        }

        return false;
    }
}