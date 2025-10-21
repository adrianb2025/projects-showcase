package game.entity.tree;

import game.entity.item.Item;
import game.entity.item.ItemEntity;
import game.entity.item.resource.Resource;
import game.entity.item.resource.ResourceItem;
import game.entity.mob.Player;
import game.gfx.sprite.SpriteCollector;
import game.gfx.sprite.SpriteWrapper;
import game.gfx.util.PaletteHelper;

public class Shrubbery extends Tree {

    private boolean berry;
    private boolean type = random.nextBoolean();
    private long tick = System.currentTimeMillis() + 60000;
    private long tick2;
    private SpriteCollector spriteCollector;

    public Shrubbery(int x, int y, SpriteCollector spriteCollector) {
        super(x, y, 4, 1);
        this.spriteCollector = spriteCollector;
        wrapSprite(berry);
    }

    private void wrapSprite(boolean flag) {
        spriteCollector.resetWrappers();
        spriteCollector.addWrapper(new SpriteWrapper(((type) ? 21 : 25) * 8, 0, 8 << 1, 8 << 1, PaletteHelper.getColor(20, 30, 40, -1)));
        spriteCollector.addWrapper(new SpriteWrapper(((type) ? 23 : 27) * 8, 0, 8 << 1, 8 << 1, PaletteHelper.getColor(10, (flag ? 15 : -1), 10, -1)));
        sprite = spriteCollector.mergeWrappers("shrubbery_" + (type ? "0" : "1") + (flag ? "_berry" : ""), 1, 0, 0);
    }

    public void tick() {
        super.tick();

        if (tick > System.currentTimeMillis() || berry) return;

        wrapSprite((berry = !berry));
    }

    public boolean interact(Item item, Player player, int dir) {
        if (!berry) return false;

        wrapSprite((berry = !berry));
        tick = System.currentTimeMillis() + 120000;

        for (int i = 0; i < random.nextInt(3) + 4; i++) {
            level.getPlayer().touchedBy(new ItemEntity(new ResourceItem(Resource.berry), 0, 0));
        }

        return false;
    }

}